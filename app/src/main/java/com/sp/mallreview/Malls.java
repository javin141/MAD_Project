package com.sp.mallreview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//SQlite DATABASE

public class Malls extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MallReview.db";
    private static final int SCHEMA_VERSION = 1;

    public Malls(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE malls_table ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " mallName TEXT, mallDate TEXT, mallDesc TEXT, mallScore TEXT, lat REAL, lon REAL);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getAll() {
        return (getReadableDatabase().rawQuery(
                "SELECT _id, mallName, mallDate, mallDesc, mallScore, lat, lon FROM malls_table ORDER BY mallName", null));
    }

    public Cursor getById(String id) {
        String[] args = {id};

        return (getReadableDatabase().rawQuery(
                "SELECT _id, mallName, mallDate, mallDesc, mallScore, lat, lon FROM malls_table WHERE _ID = ?", args));

    }


    public void insert(String mallName, String mallDate,
                       String mallDesc, String mallScore,
                       double lat, double lon) {
        ContentValues cv = new ContentValues();

        cv.put("mallName", mallName);
        cv.put("mallDate", mallDate);
        cv.put("mallDesc", mallDesc);
        cv.put("mallScore", mallScore);
        cv.put("lat", lat);
        cv.put("lon", lon);

        getWritableDatabase().insert("malls_table", "mallName", cv);

    }

    public void update(String id, String mallName, String mallDate, String mallDesc, String mallScore, double lat, double lon) {
        ContentValues cv = new ContentValues();
        String[] args = {id};
        cv.put("mallName", mallName);
        cv.put("mallDate", mallDate);
        cv.put("mallDesc", mallDesc);
        cv.put("mallScore", mallScore);
        cv.put("lat", lat);
        cv.put("lon", lon);

        getWritableDatabase().update("malls_table", cv, " _ID = ?", args);
    }

    public String getID(Cursor c) {
        return (c.getString(0));
    }

    public String getmallName(Cursor c) {
        return (c.getString(1));
    }

    public String getmallDate(Cursor c) {
        return (c.getString(2));
    }

    public String getmallDesc(Cursor c) {
        return (c.getString(3));
    }

    public String getmallScore(Cursor c) {
        return (c.getString(4));
    }

    public double getLatitude(Cursor c) {
        return (c.getDouble(5));
    }

    public double getLongitude(Cursor c) {
        return (c.getDouble(6));
    }
}