package com.sp.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RecipeInfo extends AppCompatActivity {

    private LocalDBHelper localDBHelper;
    private long recipeId;
    private boolean isUpvoted = false;
    private TextView usernameTextView, ratingTextView, recipeNameTextView, caloriesTextView, typeTextView, prepTimeTextView, descriptionTextView;
    private ImageView recipeImageView;
    private Button upvoteButton, downvoteButton, editButton, timerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_info);

        localDBHelper = new LocalDBHelper(this);

        // Initialize UI elements
        usernameTextView = findViewById(R.id.recipeInfoUsername);
        ratingTextView = findViewById(R.id.recipeInfoRating);
        recipeNameTextView = findViewById(R.id.recipeInfoName);
        caloriesTextView = findViewById(R.id.recipeInfoCalories);
        typeTextView = findViewById(R.id.recipeInfoType);
        prepTimeTextView = findViewById(R.id.recipeInfoPrepTime);
        descriptionTextView = findViewById(R.id.recipeInfoDescription);
        recipeImageView = findViewById(R.id.recipeInfoImage);

        upvoteButton = findViewById(R.id.upvoteButton);
        editButton = findViewById(R.id.editButton);
        timerButton = findViewById(R.id.timerButton);

        // Get the recipeId from the intent
        Intent intent = getIntent();
        if (intent.hasExtra("recipe_ID")) {
            recipeId = intent.getLongExtra("recipe_ID", 0);

            // Fetch details from the database using recipeId
            Recipe recipe = localDBHelper.getRecipe(recipeId);

            if (recipe != null) {
                // Set the recipe details to the UI elements
                usernameTextView.setText("Username: " + Objects.requireNonNullElse(recipe.getUsername(), "N/A"));
                ratingTextView.setText("Rating: " + recipe.getRating());
                recipeNameTextView.setText("Recipe Name: " + Objects.requireNonNullElse(recipe.getName(), "N/A"));
                caloriesTextView.setText("Calories: " + Objects.requireNonNullElse(recipe.getCalories(), "N/A"));
                typeTextView.setText("Type: " + Objects.requireNonNullElse(recipe.getType(), "N/A"));
                prepTimeTextView.setText("Prep Time: " + Objects.requireNonNullElse(recipe.getPrepTime(), "N/A"));
                descriptionTextView.setText("Description: " + Objects.requireNonNullElse(recipe.getDescription(), "N/A"));

                // Set the image from the database
                byte[] imageBytes = recipe.getImage();
                if (imageBytes != null) {
                    recipeImageView.setImageBitmap(BitmapUtils.getImage(imageBytes));
                }

                // Set edit button click listener
                editButton.setOnClickListener(v -> {
                    // Create an intent to open AddRecipes activity
                    Intent editIntent = new Intent(RecipeInfo.this, AddRecipes.class);

                    // Pass recipe details to AddRecipes activity
                    editIntent.putExtra("recipeId", recipeId);
                    editIntent.putExtra("recipeName", recipe.getName());
                    editIntent.putExtra("calories", String.valueOf(recipe.getCalories()));
                    editIntent.putExtra("prepTime", recipe.getPrepTime());
                    editIntent.putExtra("description", recipe.getDescription());
                    editIntent.putExtra("type", recipe.getType());
                    editIntent.putExtra("imageBytes", recipe.getImage());

                    // Set the isEditing flag to true
                    editIntent.putExtra("isEditing", true);

                    // Start the AddRecipes activity
                    startActivity(editIntent);
                });

                // Set timer button click listener
                timerButton.setOnClickListener(v -> {
                     Intent intent2 = new Intent(RecipeInfo.this, CookingTimer.class);
                     startActivity(intent2);
                    Toast.makeText(RecipeInfo.this, "Opening Timer", Toast.LENGTH_SHORT).show();
                });

            } else {
                // Log an error and finish the activity if the recipe is null
                Log.e("RecipeInfo", "Recipe object is null");
                finish();
            }
        }
    }
}
