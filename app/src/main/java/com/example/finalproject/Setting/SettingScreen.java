package com.example.finalproject.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.finalproject.FirebaseAuthentication;
import com.example.finalproject.Login.LoginScreen;
import com.example.finalproject.R;

public class SettingScreen extends AppCompatActivity {

    private Button _SettingScreenChangePersonalInformationButton;
    private Button _SettingScreenChangeAccountButton;
    private Button _SettingScreenHelpAndSupportButton;
    private Button _SettingScreenSignOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);

        SettingScreen_UIElementsFetching();

        _SettingScreenChangePersonalInformationButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingScreen.this, SettingScreenModifyPersonalInformation.class);
            startActivity(intent);
        });

        _SettingScreenChangeAccountButton.setOnClickListener(view -> {
            Intent intent = new Intent(SettingScreen.this, SettingAccountScreen.class);
            startActivity(intent);
        });

        _SettingScreenSignOutButton.setOnClickListener(view -> {
            FirebaseAuthentication firebaseAuthentication = new FirebaseAuthentication(SettingScreen.this);
            firebaseAuthentication.SignOut();

            Intent intent = new Intent(SettingScreen.this, LoginScreen.class);
            startActivity(intent);
        });
    }

    private void SettingScreen_UIElementsFetching() {
        _SettingScreenChangePersonalInformationButton = (Button) findViewById(R.id.SettingScreenPersonalInformationButton);
        _SettingScreenChangeAccountButton = (Button) findViewById(R.id.SettingScreenAccountButton);
        _SettingScreenHelpAndSupportButton = (Button) findViewById(R.id.SettingScreenHelpAndSupportButton);
        _SettingScreenSignOutButton = (Button) findViewById(R.id.SettingScreenSignOutButton);
    }
}