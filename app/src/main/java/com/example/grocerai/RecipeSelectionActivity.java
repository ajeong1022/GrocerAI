package com.example.grocerai;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.grocerai.adapter.RecipeSearchAdapter;
import com.example.grocerai.adapter.SelectedRecipeAdapter;
import com.example.grocerai.adapter.SelectedRecipeCountChangedListener;
import com.example.grocerai.retrofit.EdamamClient;
import com.example.grocerai.retrofit.EdamamService;
import com.example.grocerai.retrofit.RecipeSearchResult;
import com.example.grocerai.retrofit.RecipeSearchResult.Recipe;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.jetbrains.annotations.NotNull;

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
    private ProgressBar mSearchProgressBar;
    private Button mConfirmSelectionButton;
    private RecipeSearchAdapter recipeSearchAdapter;
    private SelectedRecipeAdapter selectedRecipeAdapter;
    private ArrayList<Recipe> searchResults;
    private ArrayList<Pair<Recipe, Bitmap>> selectedRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_selection);
        mSearchBar = findViewById(R.id.recipe_searchBar);
        mSearchBar.setOnSearchActionListener(this);

        //Setup for the selected recipes list.
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this);
        horizontalLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        selectedRecipes = new ArrayList<>();
        selectedRecipeAdapter = new SelectedRecipeAdapter(selectedRecipes);
        mSelectedRecipesView = findViewById(R.id.rv_selected_recipe);
        mSelectedRecipesView.setLayoutManager(horizontalLayoutManager);
        mSelectedRecipesView.setAdapter(selectedRecipeAdapter);

        //Setup for the search result list.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchResults = new ArrayList<>();
        recipeSearchAdapter = new RecipeSearchAdapter(searchResults, selectedRecipeAdapter);
        mSearchResultView = findViewById(R.id.rv_recipe_search_result);
        mSearchResultView.setLayoutManager(layoutManager);
        mSearchResultView.setAdapter(recipeSearchAdapter);

        //Remove blink effect when applying sliding animation to expandable RV item.
        ((SimpleItemAnimator) mSearchResultView.getItemAnimator()).setSupportsChangeAnimations(false);

        mSearchEmptyView = findViewById(R.id.ll_empty_search_result_view);
        mSelectedEmptyView = findViewById(R.id.ll_empty_search_selected_view);
        selectedRecipeAdapter.setSelectedRecipeCountChangedListener(new SelectedRecipeCountChangedListener() {
            @Override
            public void OnSelectedRecipeCountChanged() {
                mSelectedEmptyView.setVisibility(selectedRecipes.size() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        mSearchProgressBar = findViewById(R.id.pb_recipe_search);

        mConfirmSelectionButton = findViewById(R.id.bt_confirm_selected_recipes);
        mConfirmSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedRecipes.size() == 0) {
                    Toast.makeText(RecipeSelectionActivity.this, "You must select at least one recipe.", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Recipe> toSend = new ArrayList<>();
                    for (Pair<Recipe, Bitmap> pair : RecipeSelectionActivity.this.selectedRecipes) {
                        toSend.add(pair.first);
                    }
                    Intent intent = new Intent(RecipeSelectionActivity.this, GroceryListActivity.class);
                    intent.putParcelableArrayListExtra("recipes", toSend);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        //Don't do anything when the search state (whether the search bar is focused) changes for now.
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        String title = mSearchBar.getText();
        EdamamService service = EdamamClient.getClient().create(EdamamService.class);
        Call<RecipeSearchResult> call = service.searchRecipeByTitle(title, EDAMAM_APP_ID, EDAMAM_APP_KEY);
        call.enqueue(new Callback<RecipeSearchResult>() {
            @Override
            public void onResponse(@NotNull Call<RecipeSearchResult> call, @NotNull Response<RecipeSearchResult> response) {
                Log.d(TAG, String.valueOf(response.code()));

                RecipeSearchResult result = response.body();
                if (result != null) {
                    List<Recipe> recipes = result.extractRecipes();
                    searchResults.clear();
                    searchResults.addAll(recipes);
                    recipeSearchAdapter.notifyDataSetChanged();
                    mSearchProgressBar.setVisibility(View.GONE);
                    if (searchResults.size() == 0) mSearchEmptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<RecipeSearchResult> call, @NotNull Throwable t) {
                Log.e(TAG, "Error while searching for recipes: " + t.toString());
            }
        });

        mSearchEmptyView.setVisibility(View.GONE);
        mSearchProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        //Don't do anything for now because there are no buttons in the search bar.
    }

}
