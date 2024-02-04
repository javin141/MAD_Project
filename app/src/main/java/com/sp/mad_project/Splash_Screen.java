package com.sp.mad_project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

public class Splash_Screen extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        TextView titleTextView = findViewById(R.id.titleTextView);

        // Translation and rotation animation for titleTextView
        ObjectAnimator translationY = ObjectAnimator.ofFloat(titleTextView, "translationY", -200f, 0f);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(titleTextView, "rotation", 0f, 360f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationY, rotation);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(2000); // Adjust duration as needed

        // Initialize MediaPlayer with the audio resource
        mediaPlayer = MediaPlayer.create(this, R.raw.splash); // Replace with your audio file

        // Start playing the audio
        mediaPlayer.start();

        // Start the combined translation and rotation animation
        animatorSet.start();

        // 3-second delay before navigating to the next page
        new Handler().postDelayed(() -> {
            // Stop the audio when navigating to the next page
            mediaPlayer.stop();
            mediaPlayer.release();

            startActivity(new Intent(Splash_Screen.this, Homepage.class));
            finish(); // Close the splash screen activity
        }, 4000); // 3 seconds
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
