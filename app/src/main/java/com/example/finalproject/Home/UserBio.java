package com.example.finalproject.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.FirebaseCloudStorageManager;
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
    ImageView _backButton;
    ImageView _userAvatar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bio);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        Intent i = getIntent();
        User user = (User) i.getSerializableExtra("USER_OBJECT");

        fetchUI();
        setDataBio(user);
        onButtonClicked();
    }

    private void onButtonClicked() {
        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserBio.this, HomeScreen.class);
                startActivity(i);
            }
        });
    }

    private void setDataBio(User user) {
        _lastName.setText(user.get_UserLastname());
        _firstName.setText(user.get_UserFirstname());
        _age.setText(user.get_UserAge());
        _school.setText(user.get_UserSchool());
        _biography.setText(user.get_UserBio());
        _specilization.setText(user.get_UserSpecialization());

        FirebaseCloudStorageManager firebaseCloudStorageManager = new FirebaseCloudStorageManager();
        firebaseCloudStorageManager.FetchingImageFromFirebase(user, _userAvatar);
        _userAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    private void fetchUI() {
        _lastName = (TextView) findViewById(R.id.ItemSwipeLastName);
        _firstName = (TextView) findViewById(R.id.ItemSwipeFirstName);
        _age = (TextView) findViewById(R.id.ItemSwipeAge);
        _school = (TextView) findViewById(R.id.ItemSwipeSchool);
        _biography = (TextView) findViewById(R.id.ItemSwipeBiography);
        _specilization = (TextView) findViewById(R.id.ItemSwipeSpecialization);
        _backButton = (ImageView) findViewById(R.id.ItemSwipeBackArrow);
        _userAvatar = (ImageView) findViewById(R.id.ItemSwipeImage);
    }
}