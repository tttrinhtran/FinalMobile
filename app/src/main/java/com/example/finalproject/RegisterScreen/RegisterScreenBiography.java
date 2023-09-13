package com.example.finalproject.RegisterScreen;


import static com.example.finalproject.Constants.KEY_COLLECTION_USERS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.FirebaseAuthentication;
import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.LoginScreen;
import com.example.finalproject.R;
import com.example.finalproject.User;

public class RegisterScreenBiography extends AppCompatActivity {
    User user;
    EditText _RegisterScreenBiographyBio;
    Button _RegisterScreenBiographyFinishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen_biography);

        user = (User) getIntent().getExtras().getSerializable("new_user");

        RegisterScreenBiographyFetchingUIElements();

        _RegisterScreenBiographyFinishButton.setOnClickListener(view -> {
            boolean isSuccessful = CreateBiographyForNewUser();
            if(isSuccessful == true){
                Intent intent = new Intent(RegisterScreenBiography.this, RegisterScreenEmailVerification.class);
                intent.putExtra("new_user", user);
                startActivity(intent);
            }
        });
    }

    private boolean CreateBiographyForNewUser() {
        String bio = _RegisterScreenBiographyBio.getText().toString();
        if(bio.isEmpty()){
            Toast.makeText(RegisterScreenBiography.this, "Fill the Biography to continue", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            user.set_UserBio(bio);
            return true;
        }
    }

    private void RegisterScreenBiographyFetchingUIElements() {
        _RegisterScreenBiographyBio = (EditText) findViewById(R.id.RegisterScreenBiographyBioEditText);
        _RegisterScreenBiographyFinishButton = (Button) findViewById(R.id.RegisterScreenBiographyFinishButton);
    }
}