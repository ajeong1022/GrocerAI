package com.example.grocerai;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerai.RetroFit.RecipeSearchResult.Recipe;

import java.util.ArrayList;

public class SelectedRecipeAdapter extends RecyclerView.Adapter {

    private ArrayList<Pair<Recipe, Bitmap>> data;
    private SelectedRecipeCountChangedListener listener;
    private int lastAnimatedPosition = -1;

    public SelectedRecipeAdapter(ArrayList<Pair<Recipe, Bitmap>> data) {
        this.data = data;
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
        final Recipe recipe = data.get(position).first;
        final Bitmap image = data.get(position).second;
        final SelectedRecipeViewHolder selectedRecipeHolder = (SelectedRecipeViewHolder) holder;

        selectedRecipeHolder.recipeImageView.setImageBitmap(image);
        if (position > lastAnimatedPosition) {
            holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.fade_in));
            lastAnimatedPosition++;
        }

        selectedRecipeHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap b = ((BitmapDrawable) selectedRecipeHolder.recipeImageView.getDrawable()).getBitmap();
                data.remove(new Pair<Recipe, Bitmap>(recipe, image));
                SelectedRecipeAdapter.this.notifyDataSetChanged();
                listener.OnSelectedRecipeCountChanged();
                lastAnimatedPosition--;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(Recipe r, Bitmap b) {
        data.add(new Pair<Recipe, Bitmap>(r, b));
    }

    public boolean contains(Bitmap b) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (Pair<Recipe, Bitmap> pair : data) bitmaps.add(pair.second);
        return bitmaps.contains(b);
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
        }
    }
}
