package com.sp.mad_project;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CookingTimer extends AppCompatActivity {

    private NumberPicker npMinutes, npSeconds;
    private Button btnStartTimer, btnStopTimer, btnBack;
    private TextView tvTimer;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cooking_timer);

        npMinutes = findViewById(R.id.npMinutes);
        npSeconds = findViewById(R.id.npSeconds);
        btnStartTimer = findViewById(R.id.btnStartTimer);
        btnStopTimer = findViewById(R.id.btnStopTimer);
        btnBack = findViewById(R.id.btnBack);
        tvTimer = findViewById(R.id.tvTimer);

        // Initialize NumberPickers
        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);
        npSeconds.setMinValue(0);
        npSeconds.setMaxValue(59);

        // Initialize MediaPlayer for beep sound
        mediaPlayer = MediaPlayer.create(this, R.raw.beep);
        mediaPlayer.setVolume(1.0f, 1.0f); // Set volume to maximum

        // Set button click listeners
        btnBack.setOnClickListener(v -> finish());
        btnStartTimer.setOnClickListener(v -> startTimer());
        btnStopTimer.setOnClickListener(v -> stopTimer());
    }

    private void startTimer() {
        if (!isTimerRunning) {
            int minutes = npMinutes.getValue();
            int seconds = npSeconds.getValue();

            long totalMillis = (minutes * 60 + seconds) * 1000;

            countDownTimer = new CountDownTimer(totalMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimerText(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    updateTimerText(0);
                    playBeepSound();
                    Toast.makeText(CookingTimer.this, "Timer finished!", Toast.LENGTH_SHORT).show();
                    isTimerRunning = false;
                    countDownTimer = null; // Set countDownTimer to null after finishing
                }
            }.start();

            isTimerRunning = true;
        }
    }

    private void stopTimer() {
       if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            tvTimer.setText("00:00");
            isTimerRunning = false;
            stopBeepSound();

    }

    private void updateTimerText(long millisUntilFinished) {
        int minutes = (int) (millisUntilFinished / 1000) / 60;
        int seconds = (int) (millisUntilFinished / 1000) % 60;

        String timerText = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText(timerText);
    }

    private void playBeepSound() {
        mediaPlayer.start();
    }

    private void playBeepSoundLoop() {
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void stopBeepSound() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0); // Reset to the beginning
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}

