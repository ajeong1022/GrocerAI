package com.example.grocerai;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectedRecipeAdapter extends RecyclerView.Adapter {

    private ArrayList<Bitmap> images;
    private SelectedRecipeCountChangedListener listener;

    public SelectedRecipeAdapter(ArrayList<Bitmap> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_selected_recipe, parent, false);
        return new SelectedRecipeViewHolder(relativeLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Bitmap selectedRecipe = images.get(position);
        SelectedRecipeViewHolder selectedRecipeHolder = (SelectedRecipeViewHolder) holder;
        selectedRecipeHolder.recipeImageView.setImageBitmap(selectedRecipe);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void addItem(Bitmap b) {
        images.add(b);
    }

    public void setSelectedRecipeCountChangedListener(SelectedRecipeCountChangedListener listener) {
        this.listener = listener;
    }

    public void onSelectedRecipeCountChanged() {
        listener.OnSelectedRecipeCountChanged();
    }

    private class SelectedRecipeViewHolder extends RecyclerView.ViewHolder {

        private ImageView recipeImageView;
        private ImageView removeButton;

        SelectedRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImageView = itemView.findViewById(R.id.iv_selected_recipe);
            removeButton = itemView.findViewById(R.id.iv_remove_recipe);
            recipeImageView.setBackground(recipeImageView.getContext().getDrawable(R.drawable.background_rounding));
            recipeImageView.setClipToOutline(true);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    images.remove(((BitmapDrawable) recipeImageView.getDrawable()).getBitmap());
                    SelectedRecipeAdapter.this.notifyDataSetChanged();
                    listener.OnSelectedRecipeCountChanged();
                }
            });
        }
    }
}
