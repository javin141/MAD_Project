package com.sp.mad_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Loginpage extends AppCompatActivity {
    private TextInputEditText username ,email , password;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        EditText username = findViewById(R.id.input_username);
        EditText password = findViewById(R.id.input_username);
        Button Submitbutton = findViewById(R.id.submitButton);
        Submitbutton.setOnClickListener(view -> Submit());

    }
    private void Submit() {
        String emailcheck = String.valueOf(findViewById(R.id.input_email));
        String passwdcheck = String.valueOf(findViewById(R.id.input_password));
        AstraHelper.getByIDVolleyLogin(this,emailcheck);
        passwdhandler pwh = new passwdhandler();
        String Realpasswd = pwh.getpasswd();
        if (Realpasswd == passwdcheck ) {

        } else {
            //toast something
        }




    }
}
