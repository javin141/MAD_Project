package com.sp.mad_project;

public class Recipe {
    private long id;
    private String name;
    private String calories;
    private String prepTime;
    private String type; // Changed from ingredients to type
    private String description;
    private byte[] image;
    private String rating; // Changed from int to String
    private String username;

    // Constructors
    public Recipe(long id, String name, String calories, String prepTime, String type, String description, byte[] image, String rating, String username) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.prepTime = prepTime;
        this.type = type;
        this.description = description;
        this.image = image;
        this.rating = rating;
        this.username = username;
    }

    public Recipe() {
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCalories() {
        return calories;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImage() {
        return image;
    }

    public String getRating() {
        return rating;
    }

    public String getUsername() {
        return username;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}


