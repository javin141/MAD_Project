package com.sp.mad_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Loginpage extends AppCompatActivity {

    private TextInputEditText usernameEditText, emailEditText, passwordEditText;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        usernameEditText = findViewById(R.id.input_username);
        emailEditText = findViewById(R.id.input_email);
        passwordEditText = findViewById(R.id.input_password);

        Button createAccountButton = findViewById(R.id.createAccountButton);
        Button loginButton = findViewById(R.id.loginButton);

        createAccountButton.setOnClickListener(view -> createAccount());
        loginButton.setOnClickListener(view -> login());
    }

    private void createAccount() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
            showToast("Please fill in all fields");
            return;
        }

        AstraHelper ah = new AstraHelper();
        ah.insertVolleyLogin(this, username, email, password);
        showToast("Account created successfully");
    }

    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showToast("Please fill in both email and password");
            return;
        }
        AstraHelper ah = new AstraHelper();
        // Use the asynchronous method to check login
        ah.getByIDVolleyLogin(this, email, new AstraHelper.OnLoginResultListener() {
            @Override
            public void onSuccess(AstraHelper.UserCredentials credentials) {
                // Check if the entered password matches the retrieved password
                if (password.equals(credentials.getPassword())) {
                    // Passwords match, update preferences and show toast
                    updatePreferencesAndShowToast(credentials.getUsername(), email);
                } else {
                    // Passwords do not match
                    showToast("Invalid password. Please check and try again.");
                }
            }

            @Override
            public void onUserNotFound() {
                // User not found
                showToast("Invalid email. Please check and try again.");
            }

            @Override
            public void onError() {
                // Handle error
                showToast("An error occurred. Please try again later.");
            }
        });
    }

    private void updatePreferencesAndShowToast(String username, String email) {
        // Update preferences with the logged-in status, username, and email
        AppPreferences.setUserLoggedIn(this, true);
        AppPreferences.setUsername(this, username);
        AppPreferences.setUserEmail(this, email);

        // Show a success toast
        showToast("Login successful! Welcome, " + username);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

