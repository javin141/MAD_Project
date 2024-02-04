package com.sp.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class RecipeList extends AppCompatActivity {
    private LocalDBHelper localDBHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);

        localDBHelper = new LocalDBHelper(this); // Initialize LocalDBHelper

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRecipes);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Assuming 2 columns

        // Fetch and display recipes
        fetchAndDisplayRecipes();

        FloatingActionButton fabAddOutlet = findViewById(R.id.fabAddRecipe);
        fabAddOutlet.setOnClickListener(view -> {
            Intent intent = new Intent(RecipeList.this, AddRecipes.class);
            startActivity(intent);
        });

        // Button navigation logic
        findViewById(R.id.btnAbout).setOnClickListener(view -> {
            startActivity(new Intent(RecipeList.this, HelpActivity.class));
        });

        findViewById(R.id.btnHome).setOnClickListener(view -> {
            // Handle the "Home" button click
            startActivity(new Intent(RecipeList.this, Homepage.class));
        });

        findViewById(R.id.btnExit).setOnClickListener(view -> {
            // Handle the "Exit" button click
            finishAffinity(); // Close the entire app
        });

        // Setup RecyclerView
        setupRecyclerView();
    }

    // Method to fetch and display recipes
    public void fetchAndDisplayRecipes() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            // If there is internet, fetch data from AstraDB and update SQLite database
            AstraHelper.getAllRecipesByVolley(this);

            // Introduce a delay of 3 seconds before retrieving from SQLite database
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Retrieve recipes from SQLite database
                    List<Recipe> recipes = localDBHelper.getAllRecipes();
                    // Update the RecyclerView or UI with the recipes
                    updateUI(recipes);
                }
            }, 2500);
        } else {
            // If there is no internet, retrieve recipes from SQLite database and display them
            List<Recipe> recipes = localDBHelper.getAllRecipes();
            // Update the RecyclerView or UI with the recipes
            updateUI(recipes);
        }
    }

    private void updateUI(List<Recipe> recipes) {
        RecipeAdapter recipeAdapter = new RecipeAdapter(recipes);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRecipes);
        recyclerView.setAdapter(recipeAdapter);

        recipeAdapter.setOnItemClickListener(recipeId -> {
            // Handle item click, open RecipeInfo activity, etc.
            Intent intent = new Intent(RecipeList.this, RecipeInfo.class);
            intent.putExtra("recipe_ID", recipeId);
            startActivity(intent);
        });
    }

    // Setup RecyclerView
    private void setupRecyclerView() {
        // Additional RecyclerView setup logic can go here if needed
    }
}