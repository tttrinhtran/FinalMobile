package com.example.finalproject.RegisterScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.FirebaseAuthentication;
import com.example.finalproject.Login.LoginScreen;
import com.example.finalproject.R;
import com.example.finalproject.User;

public class RegisterScreenSignup extends AppCompatActivity {

    private EditText _SignupUsername;
    private EditText _SignupPassword;
    private EditText _SignupConfirmPassword;
    private TextView _SignupNextButton;
    private TextView _SigninButton;

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

        _SigninButton.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterScreenSignup.this, LoginScreen.class);
            startActivity(intent);
        });
    }

    private void RegisterScreenSignupFetchUIElements() {
        _SignupUsername = (EditText) findViewById(R.id.RegisterScreenUsernameEditText);
        _SignupPassword = (EditText) findViewById(R.id.RegisterScreenPasswordEditText);
        _SignupConfirmPassword = (EditText) findViewById(R.id.RegisterScreenConfirmPasswordEditText);
        _SignupNextButton = (TextView) findViewById(R.id.RegisterScreenButton);
        _SigninButton = (TextView) findViewById(R.id.RegisterScreenSignin);
    }

    private boolean CreateNewUsernameAndPassword() {
        String username = _SignupUsername.getText().toString();
        String password = _SignupPassword.getText().toString();
        String confirm_password = _SignupConfirmPassword.getText().toString();

        FirebaseAuthentication firebaseAuthentication = new FirebaseAuthentication(RegisterScreenSignup.this);


        if (username.isEmpty() || password.isEmpty() || confirm_password.isEmpty()){
            Toast.makeText(RegisterScreenSignup.this, "Please full fill the information.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!EmailValidator.Companion.isValidEmail(username)) {
            Toast.makeText(RegisterScreenSignup.this, "Invalid Email Format", Toast.LENGTH_SHORT).show();
            return false;
        } else if (firebaseAuthentication.checkForDuplicatedEmail(username)) {
            Toast.makeText(RegisterScreenSignup.this, "Email is existed", Toast.LENGTH_SHORT).show();

        } else if (password.length() <= 6) {
            Toast.makeText(RegisterScreenSignup.this, "Password must more than 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            if (password.equals(confirm_password)){
                user.set_UserName(EmailValidator.Companion.emailNormalization(username));
                user.set_UserPassword(password);
                return true;
            }else{
                Toast.makeText(RegisterScreenSignup.this, "Confirm password is incorrect.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }
}