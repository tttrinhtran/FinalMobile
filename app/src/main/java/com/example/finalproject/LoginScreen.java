package com.example.finalproject;

import static com.example.finalproject.Constants.FIRESTORE_LOCATION_KEY;
import static com.example.finalproject.Constants.KEY_COLLECTION_USERS;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;
import static com.example.finalproject.Constants.LOCATION_UPDATE_STATUS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Home.HomeScreen;
import com.example.finalproject.RegisterScreen.RegisterScreenSignup;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginScreen extends AppCompatActivity {

    FirebaseAuthentication firebaseAuthentication;
    FirebaseFirestoreController<User> userFirebaseController;
    User _LoginScreenUser;
    EditText _LoginScreenUsernameEditText;
    EditText _LoginScreenPasswordEditText;
    TextView _LoginScreenLoginButton;
    TextView _LoginScreenRegisterText;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_login_screen);

        LoginScreen_LoadingUIElements();

        // init Firebase controller for User class.
        userFirebaseController = new FirebaseFirestoreController<>(User.class);

        firebaseAuthentication = new FirebaseAuthentication(LoginScreen.this);

        // setting up Login button
        // Note: The following is written in Lambda format. In order words, view -> {LoginScreen_Datafetch();} is equivalent to
        // new View.OnClickListener(){...}
        _LoginScreenLoginButton.setOnClickListener(view -> {
            LoginScreen_Datafetch();
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
        progressBar = (ProgressBar) findViewById(R.id.LoginScreenProgressBar);
    }

    // This function used to check the user account. If it is exist then the data of corresponding user will be fetched
    // and stored in application during the app cycle. Otherwise, it reports log in failed.
    private void LoginScreen_Datafetch(){
        String userName = _LoginScreenUsernameEditText.getText().toString();
        String password = _LoginScreenPasswordEditText.getText().toString();

        // Retrieve corresponding user from firestore if exist
        if(userName.isEmpty() || password.isEmpty()){
            // change Toast into notification later.
            Toast.makeText(LoginScreen.this, "Please fulfill Username and Password.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            // taking the user's data based on username.
            progressBar.setVisibility(View.VISIBLE);
            verifyAccount(userName, password);
        }
    }

    private void verifyAccount(String userName, String password) {


        Handler handler = new Handler(Looper.getMainLooper());
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                boolean isLoginSuccessful =  firebaseAuthentication.UserSignUp(userName, password);

                if(isLoginSuccessful) {
                    _LoginScreenUser = userFirebaseController.retrieveObjectsFirestoreByID(KEY_COLLECTION_USERS, userName);
                    Intent intent = new Intent(LoginScreen.this, LocationUpdateActivity.class);
                    passOnUser();
                    startActivity(intent);
                } else {
                    Looper.prepare();
                    Toast.makeText(LoginScreen.this, "Username or Password is Incorrect", Toast.LENGTH_SHORT).show();
                }

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
    private void  passOnUser()
    {
        SharedPreferenceManager<User> sharedPreferences = new SharedPreferenceManager<>(User.class, LoginScreen.this);
        sharedPreferences.storeSerializableObjectToSharedPreference(_LoginScreenUser, KEY_SHARED_PREFERENCE_USERS);
    }
}