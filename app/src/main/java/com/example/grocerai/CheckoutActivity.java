package com.example.grocerai;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private ListView mCheckoutSummaryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        mCheckoutSummaryView = findViewById(R.id.lv_checkout_summary);
        ArrayList<String> dummy = new ArrayList<>();
        for (int i = 0; i < 10; i++) dummy.add("Checkout item " + i);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dummy);
        mCheckoutSummaryView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_finish_shopping) NavUtils.navigateUpFromSameTask(this);
        return true;
    }
}
