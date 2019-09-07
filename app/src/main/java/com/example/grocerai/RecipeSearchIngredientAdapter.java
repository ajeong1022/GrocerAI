package com.example.grocerai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * RecyclerView Adapter for the TextViews that list the ingredients that go into each search result.
 */
public class RecipeSearchIngredientAdapter extends RecyclerView.Adapter {

    private ArrayList<String> ingredients;

    public RecipeSearchIngredientAdapter(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe_search_ingredient, parent, false);

        return new RecipeSearchIngredientViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecipeSearchIngredientViewHolder viewHolder = (RecipeSearchIngredientViewHolder) holder;
        viewHolder.ingredientView.setText(String.format("• %s", ingredients.get(position)));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    protected class RecipeSearchIngredientViewHolder extends RecyclerView.ViewHolder {

        protected TextView ingredientView;

        public RecipeSearchIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientView = (TextView) itemView;
        }
    }
}
