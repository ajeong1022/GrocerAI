package com.example.grocerai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    private ListView mSearchResultView;
    private ArrayAdapter<String> mAdapter;
    private FloatingActionButton mFab;
    private ArrayList<String> searchResults;
    private TextView mSelectedRecipesView;
    private ArrayList<String> selectedRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_selection);
        mSearchBar = findViewById(R.id.recipe_searchBar);
        mSearchBar.setOnSearchActionListener(this);
        mSearchResultView = findViewById(R.id.lv_recipe_search_result);
        searchResults = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchResults);
        mSearchResultView.setAdapter(mAdapter);
        mSelectedRecipesView = findViewById(R.id.tv_selected_recipes);
        mSearchResultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                if (!mSelectedRecipesView.getText().equals("")) mSelectedRecipesView.append(",");
                mSelectedRecipesView.append(textView.getText());
                selectedRecipes.add(textView.getText().toString());
            }
        });
        selectedRecipes = new ArrayList<>();
        mFab = findViewById(R.id.fab_confirm_selection);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeSelectionActivity.this, GroceryListActivity.class);
                //Process recipe ingredient compression here
                //Dummy data
                ArrayList<String> ingredients = new ArrayList<>();
                for (String recipe : selectedRecipes) ingredients.add("Ingredient for " + recipe);
                intent.putStringArrayListExtra("recipes", ingredients);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        //Don't do anything when the search state changes for now.
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

        searchResults.clear();
        searchResults.add("Recipe 1");
        searchResults.add("Recipe 2");
        searchResults.add("Recipe 3");
        mAdapter.notifyDataSetChanged();

//        String title = mSearchBar.getText();
//        EdamamService service = EdamamClient.getClient().create(EdamamService.class);
//        Call<RecipeSearchResult> call = service.searchRecipeByTitle(title, EDAMAM_APP_ID, EDAMAM_APP_KEY);
//        call.enqueue(new Callback<RecipeSearchResult>() {
//            @Override
//            public void onResponse(Call<RecipeSearchResult> call, Response<RecipeSearchResult> response) {
//                Log.d(TAG, String.valueOf(response.code()));
//
//                RecipeSearchResult result = response.body();
//                if (result != null) {
//                    List<Recipe> recipes = result.extractRecipes();
//                    searchResults.clear();
//                    for (Recipe r : recipes) searchResults.add(r.getTitle());
//                    mAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RecipeSearchResult> call, Throwable t) {
//                Log.e(TAG, "Error while searching for recipes: " + t.toString());
//            }
//        });

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        //Don't do anything for now because there are no buttons in the search bar.
    }

}
