package com.example.grocerai;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class GroceryListActivity extends AppCompatActivity {

    private static final int SCAN_ITEM_REQUEST = 0;

    private ListView mGroceryListView;
    private FloatingActionButton mFab;
    private ArrayList<String> recipes;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        mGroceryListView = findViewById(R.id.lv_grocery_list);
        mFab = findViewById(R.id.fab_scan_item);
        recipes = getIntent().getStringArrayListExtra("recipes");
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipes);
        mGroceryListView.setAdapter(mAdapter);
        mGroceryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroceryListActivity.this, CameraActivity.class);
                startActivityForResult(intent, SCAN_ITEM_REQUEST);
            }
        });

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
            startActivity(new Intent(this, CheckoutActivity.class));
        }
        return true;
    }
}
