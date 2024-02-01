package com.sp.mad_project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Initialize MediaPlayer with the audio resource
        mediaPlayer = MediaPlayer.create(this, R.raw.splash); // Replace with your audio file

        // Start playing the audio
        mediaPlayer.start();

        // 3-second delay before navigating to the next page
        new Handler().postDelayed(() -> {
            // Stop the audio when navigating to the next page
            mediaPlayer.stop();
            mediaPlayer.release();

            startActivity(new Intent(Splash_Screen.this, Homepage.class));
            finish(); // Close the splash screen activity
        }, 3000); // 3 seconds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
