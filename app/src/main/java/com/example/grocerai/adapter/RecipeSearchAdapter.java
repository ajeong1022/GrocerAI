package com.example.grocerai.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerai.R;
import com.example.grocerai.retrofit.RecipeSearchResult.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * RecyclerView Adapter for the CardViews that display each search result.
 */
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final Recipe recipe = searchResults.get(position);
        final RecipeSearchViewHolder viewHolder = (RecipeSearchViewHolder) holder;
        final ImageView imageView = viewHolder.imageView;

        Picasso
                .get()
                .load(recipe.getImageURL())
                .placeholder(R.drawable.groceries)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                if (!selectedRecipeAdapter.contains(image)) {
                    selectedRecipeAdapter.addItem(recipe, image);
                    selectedRecipeAdapter.notifyDataSetChanged();
                    selectedRecipeAdapter.onSelectedRecipeCountChanged();

                } else
                    Toast.makeText(imageView.getContext(), "You have already selected this dish", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.titleView.setText(recipe.getTitle());
        TextView urlView = viewHolder.urlView;
        urlView.setMovementMethod(LinkMovementMethod.getInstance());
        urlView.setText(Html.fromHtml(urlView.getContext().getString(R.string.recipe_search_url, recipe.getURL())));
        final RecyclerView ingredientsView = viewHolder.ingredientsView;
        ingredientsView.setAdapter(new RecipeSearchIngredientAdapter(recipe.getIngredients()));
        viewHolder.expandIngredientsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientsView.setVisibility(viewHolder.isExpanded ? View.GONE : View.VISIBLE);

                //This line enables the sliding animation.
                RecipeSearchAdapter.this.notifyItemChanged(position);

                //Rotate the expand icon.
                Animation anim = AnimationUtils.loadAnimation(viewHolder.expandIcon.getContext(),
                        viewHolder.isExpanded ? R.anim.collapse_ingredients_rotation : R.anim.expand_ingredients_rotation);
                anim.setFillAfter(true);
                viewHolder.expandIcon.startAnimation(anim);

                //Toggle the isExpanded field.
                viewHolder.isExpanded = !viewHolder.isExpanded;
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    private class RecipeSearchViewHolder extends RecyclerView.ViewHolder {

        private TextView titleView;
        private TextView urlView;
        private LinearLayout expandIngredientsView;
        private ImageView imageView;
        private ImageView expandIcon;
        private RecyclerView ingredientsView;
        private boolean isExpanded = false;

        public RecipeSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.tv_recipe_search_title);
            urlView = itemView.findViewById(R.id.tv_recipe_search_url);
            expandIngredientsView = itemView.findViewById(R.id.ll_recipe_search_ingredients);
            imageView = itemView.findViewById(R.id.iv_recipe_search_image);
            imageView.setBackground(imageView.getContext().getDrawable(R.drawable.background_rounding));
            imageView.setClipToOutline(true);

            expandIcon = itemView.findViewById(R.id.iv_expand_icon);

            ingredientsView = itemView.findViewById(R.id.rv_recipe_search_ingredient_list);
            ingredientsView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

        }
    }

}
