package com.example.grocerai;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grocerai.RetroFit.EdamamClient;
import com.example.grocerai.RetroFit.EdamamService;
import com.example.grocerai.RetroFit.RecipeSearchResult;
import com.example.grocerai.RetroFit.RecipeSearchResult.Recipe;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeSelectionActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private static final String EDAMAM_APP_ID = "bdf8b248";
    private static final String EDAMAM_APP_KEY = "4b5ccb41c12f39c7405d636d4f20af6e";

    private static final String TAG = "RecipeSelectionActivity";

    private MaterialSearchBar searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_selection);
        searchBar = findViewById(R.id.recipe_searchBar);
        searchBar.setOnSearchActionListener(this);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        //Don't do anything when the search state changes for now.
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        String title = searchBar.getText();
        EdamamService service = EdamamClient.getClient().create(EdamamService.class);
        Call<RecipeSearchResult> call = service.searchRecipeByTitle(title, EDAMAM_APP_ID, EDAMAM_APP_KEY);
        call.enqueue(new Callback<RecipeSearchResult>() {
            @Override
            public void onResponse(Call<RecipeSearchResult> call, Response<RecipeSearchResult> response) {
                Log.d(TAG, String.valueOf(response.code()));
                RecipeSearchResult result = response.body();
                List<Recipe> recipes = result.extractRecipes();

                //TODO: Replace logs with ListView population
                for (Recipe r : recipes) {
                    Log.d(TAG, r.getTitle());
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResult> call, Throwable t) {
                Log.e(TAG, "Error while searching for recipes: " + t.toString());
            }
        });

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        //Don't do anything for now because there are no buttons.
    }
}
