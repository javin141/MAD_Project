package com.sp.mad_project;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class about extends AppCompatActivity {
    private TextView about,name,email;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = findViewById(R.id.textView5);
        email = findViewById(R.id.textView4);
        name.setText(AppPreferences.getUsername(this));
        email.setText(AppPreferences.getUserEmail(this));
    }
}
