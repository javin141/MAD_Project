package com.sp.mad_project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;


public class LocalDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipes_database";
    private static final int DATABASE_VERSION = 3;

    private Context context;
    private Set<Long> votedRecipeIds = new HashSet<>();
    private Map<Long, Boolean> voteStatusMap = new HashMap<>();

    public static final String TABLE_NAME = "recipes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_PREP_TIME = "prep_time";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_USERNAME = "username";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_CALORIES + " TEXT," +
                    COLUMN_PREP_TIME + " TEXT," +
                    COLUMN_TYPE + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_IMAGE + " BLOB," +
                    COLUMN_RATING + " TEXT," +
                    COLUMN_USERNAME + " TEXT)";

    public LocalDBHelper() {
        super(null, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public LocalDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertRecipe(String username, String name, String calories, byte[] image, String type, String prepTime, String description, String rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_CALORIES, calories);
        values.put(COLUMN_PREP_TIME, prepTime);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_RATING, rating);

        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            if (cursor.moveToFirst()) {
                do {
                    Recipe recipe = new Recipe();

                    recipe.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                    recipe.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                    recipe.setCalories(cursor.getString(cursor.getColumnIndex(COLUMN_CALORIES)));
                    recipe.setPrepTime(cursor.getString(cursor.getColumnIndex(COLUMN_PREP_TIME)));
                    recipe.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                    recipe.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                    recipe.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE)));
                    recipe.setRating(cursor.getString(cursor.getColumnIndex(COLUMN_RATING)));
                    recipe.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));

                    recipes.add(recipe);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return recipes;
    }

    @SuppressLint("Range")
    public Recipe getRecipe(long recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Recipe recipe = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(recipeId)});

            if (cursor != null && cursor.moveToFirst()) {
                recipe = new Recipe();

                recipe.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                recipe.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                recipe.setCalories(cursor.getString(cursor.getColumnIndex(COLUMN_CALORIES)));
                recipe.setPrepTime(cursor.getString(cursor.getColumnIndex(COLUMN_PREP_TIME)));
                recipe.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                recipe.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                recipe.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE)));
                recipe.setRating(cursor.getString(cursor.getColumnIndex(COLUMN_RATING)));
                recipe.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return recipe;
    }

    @SuppressLint("Range")
    public List<Recipe> Recipesfilter() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;


        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            if (cursor.moveToFirst()) {
                do {
                    Recipe recipe = new Recipe();

                    recipe.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                    recipe.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                    recipe.setCalories(cursor.getString(cursor.getColumnIndex(COLUMN_CALORIES)));
                    recipe.setPrepTime(cursor.getString(cursor.getColumnIndex(COLUMN_PREP_TIME)));
                    recipe.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                    recipe.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                    recipe.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE)));
                    recipe.setRating(cursor.getString(cursor.getColumnIndex(COLUMN_RATING)));
                    recipe.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));

                    recipes.add(recipe);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return recipes;
    }

    public void upvoteRecipe(long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING, COLUMN_RATING + " + 1");
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(recipeId)});
        db.close();
    }

    public void downvoteRecipe(long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING, COLUMN_RATING + " - 1");
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(recipeId)});
        db.close();
    }



    public void removeUpvote(long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING, COLUMN_RATING + " - 1");
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(recipeId)});
        db.close();
    }

    public boolean isRecipeUpvoted(long recipeId) {
        return votedRecipeIds.contains(recipeId) && voteStatusMap.get(recipeId);
    }

    public void removeDownvote(long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING, COLUMN_RATING + " + 1");
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(recipeId)});
        db.close();
    }

    public boolean isRecipeDownvoted(long recipeId) {
        return votedRecipeIds.contains(recipeId) && !voteStatusMap.get(recipeId);
    }

    @SuppressLint("Range")
    public int getRecipeRating(long recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_RATING + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(recipeId)});

        int rating = 0;
        if (cursor != null && cursor.moveToFirst()) {
            rating = cursor.getInt(cursor.getColumnIndex(COLUMN_RATING));
        }

        if (cursor != null) {
            cursor.close();
        }

        return rating;
    }

}

