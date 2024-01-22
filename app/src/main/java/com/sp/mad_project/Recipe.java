package com.sp.mad_project; // Replace with your actual package name

public class Recipe {
    private String name;
    private String caloriesAndPrepTime;
    private int imageResource;

    public Recipe(String name, String caloriesAndPrepTime, int imageResource) {
        this.name = name;
        this.caloriesAndPrepTime = caloriesAndPrepTime;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getCaloriesAndPrepTime() {
        return caloriesAndPrepTime;
    }

    public int getImageResource() {
        return imageResource;
    }
}
