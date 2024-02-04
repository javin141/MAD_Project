package com.sp.mad_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        // Find Recipes Button
        Button findRecipesButton = findViewById(R.id.findRecipesButton);
        Button cookingTimerButton = findViewById(R.id.cookingTimerButton);
        findRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, RecipeList.class);
                startActivity(intent);
            }
        });

        cookingTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, CookingTimer.class);
                startActivity(intent);
            }
        });

    }
}