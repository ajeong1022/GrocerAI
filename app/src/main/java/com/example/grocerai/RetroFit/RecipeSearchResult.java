package com.example.grocerai.RetroFit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecipeSearchResult {

    @SerializedName("q")
    private String queryText;
    @SerializedName("from")
    private int from;
    @SerializedName("to")
    private int to;
    @SerializedName("more")
    private boolean more;
    @SerializedName("count")
    private int count;
    @SerializedName("hits")
    private JsonArray hits;

    public String getQueryText() {
        return queryText;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public boolean isMore() {
        return more;
    }

    public int getCount() {
        return count;
    }

    public JsonArray getHits() {
        return hits;
    }

    public List<Recipe> extractRecipes() {
        JsonParser parser = new JsonParser();
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < hits.size(); i++) {
            JsonObject wrapper = hits.get(i).getAsJsonObject();
            JsonObject recipe = wrapper.getAsJsonObject("recipe");
            List<String> ingredients = new ArrayList<>();
            JsonArray ingredientLines = recipe.getAsJsonArray("ingredientLines");
            for (int j = 0; j < ingredientLines.size(); j++) {
                ingredients.add(ingredientLines.get(j).toString());
            }
            recipes.add(new Recipe(recipe.get("label").toString(), ingredients));
        }
        return recipes;
    }

    public class Recipe {
        private String title;
        private List<String> ingredients;

        public Recipe(String title, List<String> ingredients) {
            this.title = title;
            this.ingredients = ingredients;
        }

        public String getTitle() {
            return title;
        }

        public List<String> getIngredients() {
            return ingredients;
        }
    }
}
