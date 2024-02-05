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
       //setRecipeTypeRadioButton();
        LocalDBHelper.fh.setRecipetype(getSelectedRecipeType());
        calories = findViewById(R.id.caloriesEditText);
        LocalDBHelper.fh.setCalorieamt(String.valueOf(calories));
        preptime = findViewById(R.id.prepTimeEditText);
        LocalDBHelper.fh.setPreptime(String.valueOf(preptime));
        LocalDBHelper.fh.setCaloriemoreorless(getSelectedparametercalories());
        LocalDBHelper.fh.setPreptimemoreorless(getSelectedparameterpreptime());
        finish();
        RecipeList refresh = new RecipeList();
        refresh.fetchAndDisplayRecipes();
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
            return "*";
        }
    }

    private String getSelectedparametercalories() {
        if (Calorie_less.isChecked()){
            return "<";
        } else if (Calorie_more.isChecked()){
            return ">";
        } else { return "*";}

    }

    private String getSelectedparameterpreptime() {
        if (Prep_less.isChecked()){
            return "<";
        } else if (Prep_more.isChecked()){
            return ">";
        } else { return "*";}

    }
}

