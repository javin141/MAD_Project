package com.sp.mad_project;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddRecipes extends AppCompatActivity {

    private TextInputEditText etRecipeName, etCalories, etPrepTime, etDescription;
    private ImageView imageView;
    private RadioButton radioMain, radioSide, radioSnacks, radioDesserts, radioDrinks, radioOther;
    private LocalDBHelper localDBHelper;

    private static final int PICK_IMAGE = 1;
    private boolean isEditing = false;
    private long recipeId;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipes);

        etRecipeName = findViewById(R.id.recipeNameInput);
        etRecipeName.setEnabled(true);
        etCalories = findViewById(R.id.caloriesInput);
        etPrepTime = findViewById(R.id.prepTimeInput);
        etDescription = findViewById(R.id.descriptionInput);
        imageView = findViewById(R.id.imageInput);
        radioMain = findViewById(R.id.radioMain);
        radioSide = findViewById(R.id.radioSide);
        radioSnacks = findViewById(R.id.radioSnacks);
        radioDesserts = findViewById(R.id.radioDesserts);
        radioDrinks = findViewById(R.id.radioDrinks);
        radioOther = findViewById(R.id.radioOther);

        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        btnUploadImage.setOnClickListener(view -> pickImage());

        Button btnCaptureImage = findViewById(R.id.btnCaptureImage);
        btnCaptureImage.setOnClickListener(view -> captureImage());

        Button btnSaveRecipe = findViewById(R.id.saveButton);
        btnSaveRecipe.setOnClickListener(view -> saveRecipe());

        localDBHelper = new LocalDBHelper(this);

        // Check if it's an editing mode
        Intent intent = getIntent();
        if (intent.hasExtra("isEditing") && intent.getBooleanExtra("isEditing", false)) {
            isEditing = true;

            etRecipeName.setEnabled(false);

            // Retrieve and set existing recipe details
            recipeId = intent.getLongExtra("recipeId", 0);
            etRecipeName.setText(intent.getStringExtra("recipeName"));
            etCalories.setText(intent.getStringExtra("calories"));
            etPrepTime.setText(intent.getStringExtra("prepTime"));
            etDescription.setText(intent.getStringExtra("description"));
            setRecipeTypeRadioButton(intent.getStringExtra("type"));

            byte[] imageBytes = intent.getByteArrayExtra("imageBytes");
            if (imageBytes != null) {
                // Set the existing image to the ImageView
                Bitmap existingImageBitmap = BitmapUtils.getImage(imageBytes);
                imageView.setImageBitmap(existingImageBitmap);
            }
        }
    }

    private void setRecipeTypeRadioButton(String recipeType) {
        switch (recipeType) {
            case "Main Course":
                radioMain.setChecked(true);
                break;
            case "Side Dish":
                radioSide.setChecked(true);
                break;
            case "Snack":
                radioSnacks.setChecked(true);
                break;
            case "Dessert":
                radioDesserts.setChecked(true);
                break;
            case "Drink":
                radioDrinks.setChecked(true);
                break;
            case "Other":
                radioOther.setChecked(true);
                break;
        }
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                // Get the selected image URI
                Uri selectedImageUri = data.getData();

                // Compress the image
                Bitmap imageBitmap = BitmapUtils.compressImage(selectedImageUri, this);

                // Set the compressed image to the ImageView
                imageView.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            // Handle the captured image
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void saveRecipe() {
        String username;
        if (AppPreferences.isUserLoggedIn(this)) {
            // User is logged in, get the username from preferences
            username = AppPreferences.getUsername(this);
        } else {
            // User is not logged in, set default username
            username = "No Username";
        }
        String recipeName = Objects.requireNonNull(etRecipeName.getText()).toString().trim();
        String calories = Objects.requireNonNull(etCalories.getText()).toString().trim();
        String prepTime = Objects.requireNonNull(etPrepTime.getText()).toString().trim();
        String description = Objects.requireNonNull(etDescription.getText()).toString().trim();
        String type = getSelectedRecipeType();
        String rating = "0";

        // Check if imageView has a drawable
        Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Use a placeholder image if no image is selected
        if (imageBitmap == null) {
            imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_image);
        }

        // Use actual imageBytes from the image view
        byte[] imageBytes = BitmapUtils.getBytes(imageBitmap);

        // Set default values
        if (calories.isEmpty()) {
            calories = "0";
        }

        // Use AstraHelper to insert or update recipe in the Astra database
        AstraHelper astraHelper = new AstraHelper(); // Create an instance of AstraHelper
        if (isEditing) {
            // Editing an existing recipe
            astraHelper.updateVolley(this, username, recipeName, calories, imageBytes, type, prepTime, description, rating);
        } else {
            // Inserting a new recipe
            astraHelper.insertVolley(this, username, recipeName, calories, imageBytes, type, prepTime, description, rating);
        }

        Toast.makeText(this, "Recipe saved successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
        finish(); // Close the activity after saving
    }


    private String getSelectedRecipeType() {
        if (radioMain.isChecked()) {
            return "Main Course";
        } else if (radioSide.isChecked()) {
            return "Side Dish";
        } else if (radioSnacks.isChecked()) {
            return "Snack";
        } else if (radioDesserts.isChecked()) {
            return "Dessert";
        } else if (radioDrinks.isChecked()) {
            return "Drink";
        } else if (radioOther.isChecked()) {
            return "Other";
        } else {
            return "";
        }
    }

}
