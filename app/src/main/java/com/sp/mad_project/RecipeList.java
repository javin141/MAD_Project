package com.sp.mad_project;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipeList extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRecipes);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Assuming 2 columns

        // Generate sample recipe data
        List<Recipe> recipeList = generateRecipeList();

        // Create and set a simple adapter directly without a custom adapter class
        recyclerView.setAdapter(new SimpleRecipeAdapter(recipeList));
    }

    // Method to test gen recipes
    private List<Recipe> generateRecipeList() {
        List<Recipe> recipes = new ArrayList<>();

        return recipes;
    }
}

