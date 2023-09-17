package com.example.finalproject.RegisterScreen;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.User;

public class RegisterScreenBiography extends AppCompatActivity {
    User user;
    EditText _RegisterScreenBiographyBio;
    Button _RegisterScreenBiographyFinishButton;
    TextView _RegisterScreenBiographyCharacterCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen_biography);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

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

        _RegisterScreenBiographyBio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                _RegisterScreenBiographyCharacterCounter.setText(_RegisterScreenBiographyBio.getText().toString().length() + "/100");
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
        _RegisterScreenBiographyCharacterCounter = (TextView) findViewById(R.id.RegisterScreenBiographyCharacterCounterTextView);
    }
}