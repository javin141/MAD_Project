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

import java.util.Objects;

public class RecipeInfo extends AppCompatActivity {

    private LocalDBHelper localDBHelper;
    private long recipeId;
    private boolean isUpvoted = false;
    private TextView usernameTextView, ratingTextView, recipeNameTextView, caloriesTextView, typeTextView, prepTimeTextView, descriptionTextView, musicStatusTextView;
    private ImageView recipeImageView, musicButton;
    private Button upvoteButton, downvoteButton, editButton, timerButton, ttsButton;
    private TextToSpeechManager ttsManager;
    private boolean isMusicPlaying = false;

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
        ImageView backButton = findViewById(R.id.backButton);

        ttsButton = findViewById(R.id.ttsButton);
        ttsManager = new TextToSpeechManager(this);
        musicButton = findViewById(R.id.musicButton);
        musicStatusTextView = findViewById(R.id.musicStatusTextView);


        // Get the recipeId from the intent
        Intent intent = getIntent();
        if (intent.hasExtra("recipe_ID")) {
            recipeId = intent.getLongExtra("recipe_ID", 0);

            // Fetch details from the database using recipeId
            Recipe recipe = localDBHelper.getRecipe(recipeId);

            if (recipe != null) {
                // Set the recipe details to the UI elements
                usernameTextView.setText("Username: " + Objects.requireNonNullElse(recipe.getUsername(), "N/A"));
                ratingTextView.setText("Upvotes: " + recipe.getRating());
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
                    // Get the current logged-in username
                    String currentUsername = AppPreferences.getUsername(RecipeInfo.this);
                    // Check if the current username matches the recipe's username

                    if (currentUsername.equals(recipe.getUsername())) {
                        // User is authorized to edit, proceed with opening AddRecipes activity
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
                    } else {
                        // User is not authorized to edit
                        Toast.makeText(RecipeInfo.this, "You are not authorized to edit this recipe", Toast.LENGTH_SHORT).show();
                    }
                });

                musicButton.setOnClickListener(v -> toggleMusic());

                // Set timer button click listener
                timerButton.setOnClickListener(v -> {
                     Intent intent2 = new Intent(RecipeInfo.this, CookingTimer.class);
                     startActivity(intent2);
                    Toast.makeText(RecipeInfo.this, "Opening Timer", Toast.LENGTH_SHORT).show();
                });

                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Call the method to navigate to RecipeList when the back button is clicked
                        navigateToRecipeList();
                    }
                });

                // Set up TTS button click listener
                ttsButton.setOnClickListener(v -> {
                    String description = Objects.requireNonNullElse(recipe.getDescription(), "Description not available");
                    ttsManager.toggleTextToSpeech(description);
                });

                upvoteButton.setOnClickListener(v -> {
                    // Check if the recipe is upvoted or downvoted
                    if (isUpvoted) {
                        // If upvoted, change the rating by -1 in LocalDBHelper
                        localDBHelper.updateRecipeRating(recipeId, -1);

                        // Set isUpvoted to false
                        isUpvoted = false;
                        // Display a toast or any other visual indication for downvote
                        Toast.makeText(RecipeInfo.this, "Downvoted", Toast.LENGTH_SHORT).show();
                    } else {
                        // If not upvoted, change the rating by +1 in LocalDBHelper
                        localDBHelper.updateRecipeRating(recipeId, 1);

                        // Set isUpvoted to true
                        isUpvoted = true;
                        // Display a toast or any other visual indication for upvote
                        Toast.makeText(RecipeInfo.this, "Upvoted", Toast.LENGTH_SHORT).show();
                    }

                    // Update the UI with the new rating
                    updateRatingUI();
                });

            } else {
                // Log an error and finish the activity if the recipe is null
                Log.e("RecipeInfo", "Recipe object is null");
                finish();
            }
        }
    }
    private void toggleMusic() {
        if (isMusicPlaying) {
            MusicManager.stopMusic();
            musicStatusTextView.setText("Music Off");
        } else {
            MusicManager.toggleMusic(this);
            musicStatusTextView.setText("Music On");
        }
        isMusicPlaying = !isMusicPlaying;
    }
    public void navigateToRecipeList() {
        // Create an intent to open the RecipeList activity
        Intent intent = new Intent(RecipeInfo.this, RecipeList.class);

        // Start the RecipeList activity
        startActivity(intent);

        // Finish the current activity
        finish();
    }
    private void updateRatingUI() {
        String currentRating = localDBHelper.getRecipeRatingById(recipeId);

        // Update the UI elements with the new rating
        if (currentRating != null) {
            ratingTextView.setText("Rating: " + currentRating);
        }
    }
    @Override
    protected void onDestroy() {
        // Release TextToSpeech resources
        if (ttsManager != null) {
            ttsManager.destroy();
        }
        super.onDestroy();
    }
}
