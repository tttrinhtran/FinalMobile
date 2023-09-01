package com.example.finalproject.RegisterScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.User;

public class RegisterScreenSignup extends AppCompatActivity {

    private EditText _SignupUsername;
    private EditText _SignupPassword;
    private EditText _SignupConfirmPassword;
    private TextView _SignupNextButton;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen_signup);

        RegisterScreenSignupFetchUIElements();

        user = new User();

        // "view -> {...}" is lambda format instead of "new View.OnClickListener(){...}"
        _SignupNextButton.setOnClickListener(view -> {
            boolean isSuccessful = CreateNewUsernameAndPassword();
            if(isSuccessful == true){
                Intent intent = new Intent(RegisterScreenSignup.this, RegisterScreenPersonalInformation.class);
                intent.putExtra("new_user", user);
                startActivity(intent);
            }
        });
    }

    private void RegisterScreenSignupFetchUIElements() {
        _SignupUsername = (EditText) findViewById(R.id.RegisterScreenUsernameEditText);
        _SignupPassword = (EditText) findViewById(R.id.RegisterScreenPasswordEditText);
        _SignupConfirmPassword = (EditText) findViewById(R.id.RegisterScreenConfirmPasswordEditText);
        _SignupNextButton = (TextView) findViewById(R.id.RegisterScreenButton);
    }

    private boolean CreateNewUsernameAndPassword() {
        String username = _SignupUsername.getText().toString();
        String password = _SignupPassword.getText().toString();
        String confirm_password = _SignupConfirmPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty() || confirm_password.isEmpty()){
            Toast.makeText(RegisterScreenSignup.this, "Please full fill the information.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if (password.equals(confirm_password)){
                user.set_UserName(username);
                user.set_UserPassword(password);
                return true;
            }else{
                Toast.makeText(RegisterScreenSignup.this, "Confirm password is incorrect.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }


}