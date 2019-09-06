package com.example.grocerai.RetroFit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < hits.size(); i++) {
            JsonObject wrapper = hits.get(i).getAsJsonObject();
            JsonObject recipe = wrapper.getAsJsonObject("recipe");

            String title = recipe.get("label").getAsString();
            String imageURL = recipe.get("image").getAsString();
            String URL = recipe.get("url").getAsString();

            ArrayList<String> ingredients = new ArrayList<>();
            JsonArray ingredientLines = recipe.getAsJsonArray("ingredientLines");
            for (int j = 0; j < ingredientLines.size(); j++) {
                ingredients.add(ingredientLines.get(j).getAsString());
            }
            recipes.add(new Recipe(title, imageURL, URL, ingredients));
        }
        return recipes;
    }

    public class Recipe {
        private String title;
        private String imageURL;
        private String URL;
        private ArrayList<String> ingredients;

        public Recipe(String title, String imageURL, String URL, ArrayList<String> ingredients) {
            this.title = title;
            this.imageURL = imageURL;
            this.URL = URL;
            this.ingredients = ingredients;
        }

        public String getTitle() {
            return title;
        }

        public ArrayList<String> getIngredients() {
            return ingredients;
        }

        public String getImageURL() {
            return imageURL;
        }

        public String getURL() {
            return URL;
        }
    }
}
