package com.sp.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // 3-second delay before navigating to the next page
        new Handler().postDelayed(() -> {
            startActivity(new Intent(Splash_Screen.this, Homepage.class));
            finish(); // Close the splash screen activity
        }, 3000); // 3 seconds
    }
}