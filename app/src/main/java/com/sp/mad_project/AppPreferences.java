package com.sp.mad_project;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String USER_LOGGED_IN = "user_logged_in";
    private static final String USER_EMAIL = "user_email";
    private static final String USERNAME = "username";

    public static void setUserLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(USER_LOGGED_IN, loggedIn);
        editor.apply();
    }

    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(USER_LOGGED_IN, false);
    }

    public static void setUserEmail(Context context, String email) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(USER_EMAIL, email);
        editor.apply();
    }

    public static String getUserEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(USER_EMAIL, "0");
    }

    public static void setUsername(Context context, String username) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(USERNAME, username);
        editor.apply();
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(USERNAME, "No Username");
    }
}