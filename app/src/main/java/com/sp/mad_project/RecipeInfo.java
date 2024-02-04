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

import java.util.Map;
import java.util.Objects;
import java.util.HashMap;

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

        localDBHelper = new LocalDBHelper(this); // Initialize LocalDBHelper

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
        downvoteButton = findViewById(R.id.downvoteButton);
        editButton = findViewById(R.id.editButton);
        timerButton = findViewById(R.id.timerButton);

        // Get the recipeId from the intent
        Intent intent = getIntent();
        if (intent.hasExtra("recipeId")) {
            recipeId = intent.getLongExtra("recipeId", 0);

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

                // Set upvote and downvote buttons click listeners
                upvoteButton.setOnClickListener(v -> handleVote(true));
                downvoteButton.setOnClickListener(v -> handleVote(false));

                updateVoteStatus();

                // Set edit button click listener
                editButton.setOnClickListener(v -> {
                    // Navigate to AddRecipe activity with the recipe details for editing
                    Intent editIntent = new Intent(RecipeInfo.this, AddRecipes.class);
                    editIntent.putExtra("recipeId", recipe.getId());
                    startActivity(editIntent);
                });

                // Set timer button click listener
                timerButton.setOnClickListener(v -> {
                    // Handle cooking timer logic
                    Toast.makeText(RecipeInfo.this, "Cooking Timer functionality goes here", Toast.LENGTH_SHORT).show();
                });
            } else {
                // Log an error and finish the activity if the recipe is null
                Log.e("RecipeInfo", "Recipe object is null");
                finish();
            }
        }
    }

    private void handleVote(boolean isUpvote) {
        // Handle upvote or downvote logic
        int currentRating = localDBHelper.getRecipeRating(recipeId);

        // Check if the user has already voted
        boolean hasVoted = localDBHelper.hasVoted(recipeId);

        if (hasVoted) {
            // If the user has already voted, remove their previous vote
            localDBHelper.removeVote(recipeId);
        }

        if (isUpvote) {
            currentRating++;
        } else {
            currentRating--;
        }

        // Update the rating in the database
        localDBHelper.updateRating(recipeId, currentRating);

        // Update the UI
        isUpvoted = isUpvote;
        updateVoteButtonText(currentRating);
        Toast.makeText(this, "Vote submitted!", Toast.LENGTH_SHORT).show();
    }

    private void updateVoteButtonText(int currentRating) {
        // Update the text over the button based on the current rating and vote status
        String buttonText = isUpvoted ? "Upvote (" + currentRating + ")" : "Downvote (" + currentRating + ")";
        upvoteButton.setText(buttonText);
    }

    // Add the following method to update vote status when loading RecipeInfo
    private void updateVoteStatus() {
        int currentRating = localDBHelper.getRecipeRating(recipeId);
        isUpvoted = localDBHelper.hasVoted(recipeId);

        updateVoteButtonText(currentRating);
    }
}