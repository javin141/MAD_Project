package com.sp.mad_project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LocalDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipes_database";
    private static final int DATABASE_VERSION = 3; // Incremented by 1

    public static final String TABLE_NAME = "recipes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_PREP_TIME = "prep_time";
    public static final String COLUMN_TYPE = "type"; // Changed from COLUMN_INGREDIENTS to COLUMN_TYPE
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_USERNAME = "username"; // New column

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_CALORIES + " TEXT," +
                    COLUMN_PREP_TIME + " TEXT," +
                    COLUMN_TYPE + " TEXT," + // Changed from COLUMN_INGREDIENTS to COLUMN_TYPE
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_IMAGE + " BLOB," +
                    COLUMN_RATING + " TEXT," + // Changed from INTEGER to TEXT
                    COLUMN_USERNAME + " TEXT)";

    public LocalDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade logic goes here
    }

    public static long insertRecipe(String username, String name, String calories, byte[] image, String type, String prepTime, String description, String rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_CALORIES, calories);
        values.put(COLUMN_PREP_TIME, prepTime);
        values.put(COLUMN_TYPE, type); // Changed from COLUMN_INGREDIENTS to COLUMN_TYPE
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_RATING, rating);

        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();
        return newRowId;
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
                    recipe.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE))); // Changed from COLUMN_INGREDIENTS to COLUMN_TYPE
                    recipe.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                    recipe.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE)));
                    recipe.setRating(cursor.getString(cursor.getColumnIndex(COLUMN_RATING))); // Changed from INTEGER to TEXT
                    recipe.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));

                    recipes.add(recipe);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        db.close();
        return recipes;
    }
}



