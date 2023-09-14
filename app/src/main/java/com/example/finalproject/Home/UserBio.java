package com.example.finalproject.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.User;

import org.w3c.dom.Text;

public class UserBio extends AppCompatActivity {

    TextView _lastName;
    TextView _firstName;
    TextView _age;
    TextView _school;
    TextView _biography;
    TextView _specilization;
    ImageView _nopeButton;
    ImageView _matchButton;
    Intent i = getIntent();
    User user = (User) i.getSerializableExtra("USER_OBJECT");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bio);

        fetchUI();
        setDataBio();
        onButtonClicked();
    }

    private void onButtonClicked() {
        _nopeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _nopeButton.setImageResource(R.drawable.nope_icon_fill);
                Intent i = new Intent(UserBio.this, HomeScreen.class);
                User cur = user;
                i.putExtra("USER_OBJECT_BIO", cur);
                startActivity(i);
            }
        });
    }

    private void setDataBio() {

        _lastName.setText(user.get_UserLastname());
        _firstName.setText(user.get_UserFirstname());
        _age.setText(user.get_UserAge());
        _school.setText(user.get_UserSchool());
        _biography.setText(user.get_UserBio());
        _specilization.setText(user.get_UserSpecialization());
    }

    private void fetchUI() {
        _lastName = (TextView) findViewById(R.id.ItemSwipeLastName);
        _firstName = (TextView) findViewById(R.id.ItemSwipeFirstName);
        _age = (TextView) findViewById(R.id.ItemSwipeAge);
        _school = (TextView) findViewById(R.id.ItemSwipeSchool);
        _biography = (TextView) findViewById(R.id.ItemSwipeBiography);
        _specilization = (TextView) findViewById(R.id.ItemSwipeSpecialization);
        _nopeButton = (ImageView) findViewById(R.id.ItemSwipeNopeButton);
        _matchButton = (ImageView) findViewById(R.id.ItemSwipeMatchButton);
    }
}