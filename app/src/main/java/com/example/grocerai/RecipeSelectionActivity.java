package com.example.grocerai;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerai.RetroFit.EdamamClient;
import com.example.grocerai.RetroFit.EdamamService;
import com.example.grocerai.RetroFit.RecipeSearchResult;
import com.example.grocerai.RetroFit.RecipeSearchResult.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeSelectionActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private static final String EDAMAM_APP_ID = "bdf8b248";
    private static final String EDAMAM_APP_KEY = "4b5ccb41c12f39c7405d636d4f20af6e";

    private static final String TAG = "RecipeSelectionActivity";

    private MaterialSearchBar mSearchBar;
    private RecyclerView mSearchResultView;
    private RecyclerView mSelectedRecipesView;
    private LinearLayout mSearchEmptyView;
    private LinearLayout mSelectedEmptyView;
    private FloatingActionButton mFab;
    private RecipeSearchAdapter recipeSearchAdapter;
    private SelectedRecipeAdapter selectedRecipeAdapter;
    private ArrayList<Recipe> searchResults;
    private ArrayList<Bitmap> selectedRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_selection);
        mSearchBar = findViewById(R.id.recipe_searchBar);
        mSearchBar.setOnSearchActionListener(this);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this);
        horizontalLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        selectedRecipes = new ArrayList<>();
        selectedRecipeAdapter = new SelectedRecipeAdapter(selectedRecipes);
        mSelectedRecipesView = findViewById(R.id.rv_selected_recipe);
        mSelectedRecipesView.setLayoutManager(horizontalLayoutManager);
        mSelectedRecipesView.setAdapter(selectedRecipeAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchResults = new ArrayList<>();
        recipeSearchAdapter = new RecipeSearchAdapter(searchResults, selectedRecipeAdapter);
        mSearchResultView = findViewById(R.id.rv_recipe_search_result);
        mSearchResultView.setLayoutManager(layoutManager);
        mSearchResultView.setAdapter(recipeSearchAdapter);

        mSearchEmptyView = findViewById(R.id.ll_empty_search_result_view);
        mSelectedEmptyView = findViewById(R.id.ll_empty_search_selected_view);
        selectedRecipeAdapter.setSelectedRecipeCountChangedListener(new SelectedRecipeCountChangedListener() {
            @Override
            public void OnSelectedRecipeCountChanged() {
                if (selectedRecipeAdapter.getItemCount() == 0 ) mSelectedEmptyView.setVisibility(View.VISIBLE);
                else mSelectedEmptyView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        //Don't do anything when the search state changes for now.
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        String title = mSearchBar.getText();
        EdamamService service = EdamamClient.getClient().create(EdamamService.class);
        Call<RecipeSearchResult> call = service.searchRecipeByTitle(title, EDAMAM_APP_ID, EDAMAM_APP_KEY);
        call.enqueue(new Callback<RecipeSearchResult>() {
            @Override
            public void onResponse(Call<RecipeSearchResult> call, Response<RecipeSearchResult> response) {
                Log.d(TAG, String.valueOf(response.code()));

                RecipeSearchResult result = response.body();
                if (result != null) {
                    List<Recipe> recipes = result.extractRecipes();
                    searchResults.clear();
                    searchResults.addAll(recipes);
                    recipeSearchAdapter.notifyDataSetChanged();
                    mSearchEmptyView.setVisibility(View.GONE);
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
        //Don't do anything for now because there are no buttons in the search bar.
    }

}
