package com.example.finalproject.Setting;

import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalproject.FirebaseAuthentication;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;

public class SettingAccountScreen extends AppCompatActivity {

    private User user;

    private EditText _SettingAccountOldPasswordEditText;
    private EditText _SettingAccountNewPasswordEditText;
    private EditText _SettingAccountNewPasswordConfirmEditText;
    private Button _SettingAccountSubmitButton;
    private ImageButton _SettingAccountBackButton;
    private ProgressBar _SettingAccountProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account_screen);

        SettingAccount_UIElementsFetch();

        SharedPreferenceManager<User> userSharedPreferenceManager = new SharedPreferenceManager<>(User.class,SettingAccountScreen.this);
        user = userSharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);

        _SettingAccountSubmitButton.setOnClickListener(view -> {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            FirebaseAuthentication firebaseAuthentication = new FirebaseAuthentication(SettingAccountScreen.this);
            String old_password = _SettingAccountOldPasswordEditText.getText().toString();
            String new_password = _SettingAccountNewPasswordEditText.getText().toString();
            String new_password_confirm = _SettingAccountNewPasswordConfirmEditText.getText().toString();

            if(!old_password.isEmpty() && !new_password.isEmpty() && !new_password_confirm.isEmpty()) {

                if(new_password.equals(new_password_confirm)) {
                    _SettingAccountProgressBar.setVisibility(View.VISIBLE);
                    String email = user.get_UserName();

                    firebaseAuthentication.changePassword(email, old_password, new_password);
                } else Toast.makeText(SettingAccountScreen.this, "Confirm new password and new password are not matched",Toast.LENGTH_SHORT).show();
            } else Toast.makeText(SettingAccountScreen.this, "Full fill Old Password and New password.",Toast.LENGTH_SHORT).show();
        });

    }

    private void SettingAccount_UIElementsFetch() {
        _SettingAccountOldPasswordEditText = findViewById(R.id.SettingAccountScreenOldPasswordEditText);
        _SettingAccountNewPasswordEditText = findViewById(R.id.SettingAccountScreenNewPasswordEditText);
        _SettingAccountSubmitButton = findViewById(R.id.SettingAccountScreenSubmitButton);
        _SettingAccountBackButton = findViewById(R.id.SettingAccountScreenBackButton);
        _SettingAccountProgressBar = findViewById(R.id.SectionAccountScreenProgressBar);
        _SettingAccountNewPasswordConfirmEditText = findViewById(R.id.SettingAccountScreenNewPasswordComfirmEditText);
    }
}