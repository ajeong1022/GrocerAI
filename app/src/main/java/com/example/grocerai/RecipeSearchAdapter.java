package com.example.grocerai;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerai.RetroFit.RecipeSearchResult.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeSearchAdapter extends RecyclerView.Adapter {

    private ArrayList<Recipe> searchResults;
    private SelectedRecipeAdapter selectedRecipeAdapter;

    public RecipeSearchAdapter(ArrayList<Recipe> searchResults, SelectedRecipeAdapter selectedRecipeAdapter) {
        this.searchResults = searchResults;
        this.selectedRecipeAdapter = selectedRecipeAdapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe_search, parent, false);
        return new RecipeSearchViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Recipe recipe = searchResults.get(position);
        RecipeSearchViewHolder viewHolder = (RecipeSearchViewHolder) holder;
        ImageView imageView = viewHolder.imageView;
        Picasso
                .get()
                .load(recipe.getImageURL())
                .placeholder(R.drawable.groceries)
                .into(imageView);
        viewHolder.titleView.setText(recipe.getTitle());
        TextView urlView = viewHolder.urlView;
        urlView.setMovementMethod(LinkMovementMethod.getInstance());
        urlView.setText(Html.fromHtml(urlView.getContext().getString(R.string.recipe_search_url,recipe.getURL())));
        //TODO: Implement list processing to show ingredients in appropriate format.
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    private class RecipeSearchViewHolder extends RecyclerView.ViewHolder {

        private TextView titleView;
        private TextView urlView;
        private TextView ingreidentView;
        private ImageView imageView;

        public RecipeSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.tv_recipe_search_title);
            urlView = itemView.findViewById(R.id.tv_recipe_search_url);
            ingreidentView = itemView.findViewById(R.id.tv_recipe_search_ingredients);
            imageView = itemView.findViewById(R.id.iv_recipe_search_image);
            imageView.setBackground(imageView.getContext().getDrawable(R.drawable.background_rounding));
            imageView.setClipToOutline(true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    selectedRecipeAdapter.addItem(image);
                    selectedRecipeAdapter.notifyDataSetChanged();
                    selectedRecipeAdapter.onSelectedRecipeCountChanged();
                }
            });
        }
    }

}
