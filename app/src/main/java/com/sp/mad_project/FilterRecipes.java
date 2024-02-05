package com.sp.mad_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.savedstate.SavedStateRegistryOwner;


public class FilterRecipes extends AppCompatActivity {

    private RadioButton radioMain, radioSide, radioSnacks, radioDesserts, radioDrinks, radioOther;
    private Button filterbutton;
    private RadioButton Prep_less, Prep_more;
    private RadioButton Calorie_less, Calorie_more;
    private EditText preptime, calories;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_recipes);
        radioMain = findViewById(R.id.radioMain);
        radioSide = findViewById(R.id.radioSide);
        radioSnacks = findViewById(R.id.radioSnacks);
        radioDesserts = findViewById(R.id.radioDesserts);
        radioDrinks = findViewById(R.id.radioDrinks);
        radioOther = findViewById(R.id.radioOther);
        preptime = findViewById(R.id.prepTimeEditText);
        Prep_less = findViewById(R.id.radioPrepTimeLessThan);
        Prep_more = findViewById(R.id.radioPrepTimeMoreThan);
        calories = findViewById(R.id.caloriesEditText);
        Calorie_more = findViewById(R.id.radioCalorieMoreThan);
        Calorie_less = findViewById(R.id.radioCalorieLessThan);
        Button filterbutton = findViewById(R.id.saveFiltersButton);
        filterbutton.setOnClickListener(view -> savefilters());

    }
    private void savefilters() {
       setRecipeTypeRadioButton();
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
}
