package com.example.finalproject.Profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.finalproject.Constants;
import com.example.finalproject.FirebaseAuthentication;
import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.FriendsScreen;
import com.example.finalproject.Home.HomeScreen;
import com.example.finalproject.Home.hobbiesAdapter;
import com.example.finalproject.Login.LoginScreen;
import com.example.finalproject.R;
import com.example.finalproject.Section.SectionScreen;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;
import com.example.finalproject.databinding.ActivityProfileScreenBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class ProfileScreen extends AppCompatActivity {

    SharedPreferenceManager<User> currentInstance;
    private ActivityProfileScreenBinding binding;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        currentInstance = new SharedPreferenceManager<>(User.class, this);

        currentUser = currentInstance.retrieveSerializableObjectFromSharedPreference( Constants.KEY_SHARED_PREFERENCE_USERS );


        FirebaseCloudStorageManager firebaseCloudStorageManager = new FirebaseCloudStorageManager();
        firebaseCloudStorageManager.FetchingImageFromFirebase(currentUser, binding.ProfileScreenAvatar);


    }

    @Override
    protected void onResume() {
        super.onResume();
        currentInstance = new SharedPreferenceManager<>(User.class, this);

        init();

        setListener();
        navBar();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
        currentUser = currentInstance.retrieveSerializableObjectFromSharedPreference( Constants.KEY_SHARED_PREFERENCE_USERS );

        binding.ProfileScreenPhoneText.setText(currentUser.get_UserPhone());

        String passwordhide = new String(new char[currentUser.get_UserPassword().length()]).replace('\0', '*');
        binding.ProfileScreenPasswordText.setText(passwordhide);

        String nickname;
        if(currentUser.get_UserNickName() == null || currentUser.get_UserNickName().isEmpty()) nickname = "";
        else nickname = " (" + currentUser.get_UserNickName() + ")";
        binding.ProfileScreenUsername.setText(currentUser.get_UserFirstname() + " " + currentUser.get_UserLastname() + nickname);

        binding.ProfileScreenDistanceSlider.setValue(currentUser.get_UserDistancePref());
        binding.ProfileScreenDistanceText.setText( (int) currentUser.get_UserDistancePref() + " mi");
        binding.ProfileScreenAgeSlider.setValues(currentUser.get_UserMinAge(), currentUser.get_UserMaxAge());
        binding.ProfileScreenAgeText.setText( (int) currentUser.get_UserMinAge() + "-" + (int) currentUser.get_UserMaxAge());
        binding.ProfileScreenLocationInfo.setText( currentUser.get_UserAddress() );

        binding.ProfileScreenNameEdit.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileScreen.this, ProfileScreenNameModification.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);{
            if(requestCode != 1 && resultCode != -1 && data != null);{
            }
        }
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

        binding.ProfileScreenNameEdit.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileScreen.this, ProfileScreenNameModification.class);
            startActivity(intent);
        });

        binding.ProfileScreenAvatar.setOnClickListener(view -> {
            ImageSettingBottomSheetFragment settingScreenPersonalInformationBottomSheetFragment = new ImageSettingBottomSheetFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", currentUser.get_UserName());
            settingScreenPersonalInformationBottomSheetFragment.setArguments(bundle);
            settingScreenPersonalInformationBottomSheetFragment.show(getSupportFragmentManager(), "settingScreenPersonalInformationBottomSheetFragment");
        });

        binding.LogoutLayout.setOnClickListener(view -> {

            currentUser.set_UserActive(false);

            FirebaseAuthentication firebaseAuthentication = new FirebaseAuthentication(ProfileScreen.this);
            firebaseAuthentication.SignOut();

            Intent intent = new Intent(ProfileScreen.this, LoginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
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
        profile = findViewById(R.id.NaviBarProfile); profile.setImageResource(R.drawable.profile_icon_fill);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, SectionScreen.class);
                Bundle b = ActivityOptions.makeSceneTransitionAnimation(ProfileScreen.this).toBundle();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent, b);
                finish();
            }
        });


        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, FriendsScreen.class);
                Bundle b = ActivityOptions.makeSceneTransitionAnimation(ProfileScreen.this).toBundle();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent, b);
                finish();
            }
        });
    }
}