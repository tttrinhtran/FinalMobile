package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class LoginScreen extends AppCompatActivity {

    FirebaseFirestore database;
    EditText _username;
    EditText _password;
    TextView _register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        LoadingUIElements();
        database = FirebaseFirestore.getInstance();
    }

    private void LoadingUIElements() {
        _username = (EditText) findViewById(R.id.LoginUsername);
        _password = (EditText) findViewById(R.id.LoginPassword);
        _register = (TextView) findViewById(R.id.LoginRegister);
    }


}