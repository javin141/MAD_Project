package com.sp.mad_project;

import android.os.Bundle;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.savedstate.SavedStateRegistryOwner;


public class FilterRecipes extends AppCompatActivity {

    private RadioButton radioMain, radioSide, radioSnacks, radioDesserts, radioDrinks, radioOther;
    private Button = saveFilter
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_recipes);
        radioMain = findViewById(R.id.radioMain);
        radioSide = findViewById(R.id.radioSide);
        radioSnacks = findViewById(R.id.radioSnacks);
        radioDesserts = findViewById(R.id.radioDesserts);
        radioDrinks = findViewById(R.id.radioDrinks);
        radioOther = findViewById(R.id.radioOther);


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
