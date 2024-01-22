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

    // This method is used to generate sample recipe data, replace it with your actual data source
    private List<Recipe> generateRecipeList() {
        List<Recipe> recipes = new ArrayList<>();

        // Add sample recipes
        recipes.add(new Recipe("Recipe 1", "280 kcal / 10 mins", R.drawable.sample_food));
        recipes.add(new Recipe("Recipe 2", "350 kcal / 15 mins", R.drawable.sample_food));
        // Add more recipes as needed...

        return recipes;
    }
}

