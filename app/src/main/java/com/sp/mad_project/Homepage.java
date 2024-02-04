package com.sp.mad_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

public class Homepage extends AppCompatActivity {

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navSelected);

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


    NavigationView.OnNavigationItemSelectedListener navSelected = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id== R.id.sidenav_exit) {
                finish();
            } else if (id == R.id.sidenav_about) {
                ;
            } else if (id == R.id.sidenav_help) {
                ;
            } else if (id == R.id.sidenav_login) {
                ;
            }
            return true;
        }
    };
}