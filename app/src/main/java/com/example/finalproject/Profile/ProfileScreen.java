package com.example.finalproject.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.finalproject.Constants;
import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.FriendsScreen;
import com.example.finalproject.Home.HomeScreen;
import com.example.finalproject.R;
import com.example.finalproject.Section.SectionScreen;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;
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
                .document(currentUser.get_UserName())
                .update(
                        "_UserMinAge", currentUser.get_UserMinAge(),
                        "_UserMaxAge", currentUser.get_UserMaxAge(),
                        "_UserDistancePref", currentUser.get_UserDistancePref()
                );
    }

    private void init() {

        SharedPreferenceManager<User> currentInstance = new SharedPreferenceManager<>(User.class, this);
        currentUser = currentInstance.retrieveSerializableObjectFromSharedPreference( Constants.KEY_SHARED_PREFERENCE_USERS );

        binding.ProfileScreenPhoneText.setText(currentUser.get_UserPhone());

        String passwordhide = "";
        for (int i = 0; i < currentUser.get_UserPassword().toString().length(); i++){
            passwordhide += "*";
        }
        binding.ProfileScreenPasswordText.setText(passwordhide);

        binding.ProfileScreenUsername.setText(currentUser.get_UserFirstname() + " " + currentUser.get_UserLastname() + " (" + currentUser.get_UserNickName() + ")");

        binding.ProfileScreenDistanceSlider.setValue(currentUser.get_UserDistancePref());
        binding.ProfileScreenDistanceText.setText( (int) currentUser.get_UserDistancePref() + " mi");
        binding.ProfileScreenAgeSlider.setValues(currentUser.get_UserMinAge(), currentUser.get_UserMaxAge());
        binding.ProfileScreenAgeText.setText( (int) currentUser.get_UserMinAge() + "-" + (int) currentUser.get_UserMaxAge());

        FirebaseCloudStorageManager firebaseCloudStorageManager = new FirebaseCloudStorageManager();
        firebaseCloudStorageManager.FetchingImageFromFirebase(currentUser, binding.ProfileScreenAvatar);
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

        binding.ProfileScreenPasswordEditIcon.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileScreen.this, ProfileScreenPasswordModification.class);
            startActivity(intent);
        });

        binding.ProfileScreenPhoneEditIcon.setOnClickListener( view -> {
            Intent intent = new Intent(ProfileScreen.this, ProfileScreenPhoneNumberModification.class);
            startActivity(intent);
        });

        binding.ProfileScreenUsername.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileScreen.this, ProfileScreen.class);
            startActivity(intent);
        });

        binding.ProfileScreenAvatar.setOnClickListener(view -> {
            SettingScreenPersonalInformationBottomSheetFragment settingScreenPersonalInformationBottomSheetFragment = new SettingScreenPersonalInformationBottomSheetFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", currentUser);
            settingScreenPersonalInformationBottomSheetFragment.setArguments(bundle);
            settingScreenPersonalInformationBottomSheetFragment.show(getSupportFragmentManager(), "settingScreenPersonalInformationBottomSheetFragment");
        });
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