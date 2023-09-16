package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.finalproject.Home.HomeScreen;
import com.example.finalproject.Section.SectionScreen;
import com.example.finalproject.databinding.ActivityProfileScreenBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProfileScreen extends AppCompatActivity {

    private ActivityProfileScreenBinding binding;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListener();
        navBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferenceManager<User> currentInstance = new SharedPreferenceManager<>(User.class, this);
        currentInstance.storeSerializableObjectToSharedPreference(currentUser, Constants.KEY_SHARED_PREFERENCE_USERS);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(currentUser._UserName)
                .update(
                        "_UserMinAge", currentUser._UserMinAge,
                        "_UserMaxAge", currentUser._UserMaxAge,
                        "_UserDistancePref", currentUser._UserDistancePref
                );
    }

    private void init() {

        SharedPreferenceManager<User> currentInstance = new SharedPreferenceManager<>(User.class, this);
        currentUser = currentInstance.retrieveSerializableObjectFromSharedPreference( Constants.KEY_SHARED_PREFERENCE_USERS );

        binding.ProfileScreenPhoneText.setText(currentUser._UserPhone);
        binding.ProfileScreenEmailText.setText(currentUser._UserName);
        binding.ProfileScreenUsername.setText(currentUser._UserFirstname + " " + currentUser._UserLastname);

        binding.ProfileScreenDistanceSlider.setValue(currentUser._UserDistancePref);
        binding.ProfileScreenDistanceText.setText( (int)currentUser._UserDistancePref + " mi");
        binding.ProfileScreenAgeSlider.setValues( currentUser._UserMinAge, currentUser._UserMaxAge);
        binding.ProfileScreenAgeText.setText( (int)currentUser._UserMinAge + "-" + (int)currentUser._UserMaxAge);
    }

    private void setListener() {

        binding.ProfileScreenDistanceSlider.addOnChangeListener( (slider, value, fromUser) -> {
            currentUser._UserDistancePref = value;
            binding.ProfileScreenDistanceText.setText( (int)value + " mi");
        } );

        binding.ProfileScreenAgeSlider.addOnChangeListener( (rangeSlider, value, fromUser) -> {
            List<Float> values = rangeSlider.getValues();

            currentUser._UserMinAge = values.get(0);
            currentUser._UserMaxAge = values.get(1);

            binding.ProfileScreenAgeText.setText( (int)currentUser._UserMinAge + "-" + (int)currentUser._UserMaxAge);
        } );

    }

    private void navBar(){
        ImageView home;
        ImageView section;
        ImageView friend;
        ImageView profile;


        home = findViewById(R.id.NaviBarHomeIcon);
        section= findViewById(R.id.NaviBarSectionIcon);
        friend= findViewById(R.id.NaviBarFriendIcon);
        profile = findViewById(R.id.NaviBarProfile);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, HomeScreen.class);
                Bundle b = ActivityOptions.makeSceneTransitionAnimation(ProfileScreen.this).toBundle();
                startActivity(intent, b);
                finish();
            }
        });

        section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, SectionScreen.class);
                Bundle b = ActivityOptions.makeSceneTransitionAnimation(ProfileScreen.this).toBundle();
                startActivity(intent, b);
            }
        });


        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, FriendsScreen.class);
                Bundle b = ActivityOptions.makeSceneTransitionAnimation(ProfileScreen.this).toBundle();
                startActivity(intent, b);
            }
        });
    }
}