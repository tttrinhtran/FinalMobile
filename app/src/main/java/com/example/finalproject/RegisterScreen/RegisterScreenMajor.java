package com.example.finalproject.RegisterScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.User;

public class RegisterScreenMajor extends AppCompatActivity {

    User user;
    EditText _RegisterScreenMajorSchool;
    EditText _RegisterScreenMajorSpecialization;
    TextView _RegisterScreenMajorNextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen_major);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        user = (User) getIntent().getExtras().getSerializable("new_user");

        _RegisterScreenMajorSchool = (EditText) findViewById(R.id.RegisterScreenMajorSchoolEditText);
        _RegisterScreenMajorSpecialization = (EditText) findViewById(R.id.RegisterScreenMajorSpecializationEditText);
        _RegisterScreenMajorNextButton = (TextView) findViewById(R.id.RegisterScreenMajorNextButton);

        // "view -> {...}" is lambda format, it is similar to "new View.OnClickListener(){...}"
        _RegisterScreenMajorNextButton.setOnClickListener(view -> {
            boolean isSuccessfull =  CreateSchoolAndSpecializationForNewUser();
            if(isSuccessfull == true){
                Intent intent = new Intent(RegisterScreenMajor.this, RegisterScreenHobbies.class);
                intent.putExtra("new_user", user);
                startActivity(intent);
            }
        });
    }

    private boolean CreateSchoolAndSpecializationForNewUser() {
        String school = _RegisterScreenMajorSchool.getText().toString();
        String specialization = _RegisterScreenMajorSpecialization.getText().toString();

        if(school.isEmpty() || specialization.isEmpty()) {
            Toast.makeText(RegisterScreenMajor.this, "Please full fill the information.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            user.set_UserSchool(school);
            user.set_UserSpecialization(specialization);
            return true;
        }
    }


}