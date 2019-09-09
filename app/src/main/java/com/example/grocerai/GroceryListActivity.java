package com.example.grocerai;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerai.adapter.GroceryListAdapter;
import com.example.grocerai.retrofit.Ingredient;
import com.example.grocerai.retrofit.RecipeSearchResult.Recipe;

import java.util.ArrayList;

public class GroceryListActivity extends AppCompatActivity {

    private static final int SCAN_ITEM_REQUEST = 0;

    private Toolbar mToolBar;
    private RecyclerView mIngredientsView;
    private ArrayList<Recipe> recipes;
    private GroceryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        recipes = getIntent().getParcelableArrayListExtra("recipes");

        mToolBar = findViewById(R.id.toolbar_grocery_list);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mIngredientsView = findViewById(R.id.rv_grocery_list);
        mIngredientsView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroceryListAdapter(generateGroceryList(recipes));
        mIngredientsView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mIngredientsView.addItemDecoration(itemDecoration);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SCAN_ITEM_REQUEST) {
            if (resultCode == RESULT_OK)
                Toast.makeText(this, data.getStringExtra("message"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_checkout) {
            int unchecked = adapter.getUnchecked();
            if (unchecked > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
                builder.setMessage(getString(R.string.checkout_dialog_body, unchecked))
                        .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NavUtils.navigateUpFromSameTask(GroceryListActivity.this);
                            }
                        })
                        .setNegativeButton("Resume shopping", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }

    private ArrayList<Ingredient> generateGroceryList(ArrayList<Recipe> recipes) {
        ArrayList<Ingredient> groceryList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            for (String ingredient : recipe.getIngredients())
                groceryList.add(new Ingredient(ingredient, false));
        }
        return groceryList;
    }

}
