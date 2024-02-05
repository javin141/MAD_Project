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
<<<<<<< Updated upstream
       //setRecipeTypeRadioButton();
=======
        filterhandler fh = new filterhandler();
        fh.setRecipetype(getSelectedRecipeType());
        calories = findViewById(R.id.caloriesEditText);
        fh.setCalorieamt(String.valueOf(calories));
        preptime = findViewById(R.id.prepTimeEditText);
        fh.setPreptime(String.valueOf(preptime));
        fh.setCaloriemoreorless(getSelectedparametercalories(());
        fh.setPreptimemoreorless(getSelectedparameterpreptime());
>>>>>>> Stashed changes
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

    private String getSelectedparametercalories() {
        if (Calorie_less.isChecked()){
            return "less than";
        } else if (Calorie_more.isChecked()){
            return "more than";
        } else { return "unchecked";}

    }

    private String getSelectedparameterpreptime() {
        if (Prep_less.isChecked()){
            return "less than";
        } else if (Prep_more.isChecked()){
            return "more than";
        } else { return "unchecked";}

    }
}

