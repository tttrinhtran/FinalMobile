package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.RegisterScreen.RegisterScreenSignup;

public class LoginScreen extends AppCompatActivity {

    FirebaseFirestoreController<User> userFirebaseFirestoreController;
    User _LoginScreenUser;
    EditText _LoginScreenUsernameEditText;
    EditText _LoginScreenPasswordEditText;
    TextView _LoginScreenLoginButton;
    TextView _LoginScreenRegisterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        LoginScreen_LoadingUIElements();

        // init Firebase controller for User class.
        userFirebaseFirestoreController = new FirebaseFirestoreController<>(User.class);

        // setting up Login button
        // Note: The following is written in Lambda format. In order words, view -> {LoginScreen_Datafetch();} is equivalent to
        // new View.OnClickListener(){...}
        _LoginScreenLoginButton.setOnClickListener(view -> {
            boolean check = LoginScreen_Datafetch();
            if(check){
                Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        // Setting register
        _LoginScreenRegisterText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginScreen.this, RegisterScreenSignup.class);
            startActivity(intent);
        });
    }

    private void LoginScreen_LoadingUIElements() {
        _LoginScreenUsernameEditText = (EditText) findViewById(R.id.LoginScreenUsernameEditText);
        _LoginScreenPasswordEditText = (EditText) findViewById(R.id.LoginScreenPasswordEditText);
        _LoginScreenLoginButton = (TextView) findViewById(R.id.LoginScreenButton);
        _LoginScreenRegisterText = (TextView) findViewById(R.id.LoginScreenRegisterEditText);
    }

    // This function used to check the user account. If it is exist then the data of corresponding user will be fetched
    // and stored in application during the app cycle. Otherwise, it reports log in failed.
    private boolean LoginScreen_Datafetch(){
        String userName = _LoginScreenUsernameEditText.getText().toString();
        String password = _LoginScreenPasswordEditText.getText().toString();

        // Retrieve corresponding user from firestore if exist
        if(userName.isEmpty() || password.isEmpty()){
            // change Toast into notification later.
            Toast.makeText(LoginScreen.this, "Please fulfill Username and Password.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            // taking the user's data based on username.
            _LoginScreenUser = userFirebaseFirestoreController.retrieveObjectsFirestoreByID(User.UserCollectionKey, userName);
        }

        return verifyAccount(password);
    }

    private boolean verifyAccount(String password) {
        // Checking if there is valid user in the database which matched with both username and password.
        if(_LoginScreenUser == null || !_LoginScreenUser.get_UserPassword().equals(password)){
            Toast.makeText(LoginScreen.this, "username or password is incorrect. Try again!", Toast.LENGTH_SHORT).show();
            _LoginScreenUser = null; // reset to null since we have taken the corresponding user data already, if password is incorrect
                                    // we have to reset it to null.
            return false;
        }else return true;
    }
}