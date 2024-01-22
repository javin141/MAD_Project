package com.sp.mallreview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent iSplash = new Intent(SplashActivity.this, MallReview.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(iSplash);
                finish();
            }
        },3000);
    }
}