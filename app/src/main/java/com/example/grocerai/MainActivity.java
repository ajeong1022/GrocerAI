 package com.example.grocerai;

 import android.content.Intent;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.Button;

 import androidx.appcompat.app.AppCompatActivity;

 public class MainActivity extends AppCompatActivity {

    private Button mStartShoppingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartShoppingButton = findViewById(R.id.bt_begin_shopping);

        mStartShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecipeSelectionActivity.class));
            }
        });

    }
 }
