package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.finalproject.databinding.ActivityFriendsScreenBinding;
import com.example.finalproject.databinding.FriendBioBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class FriendBio extends AppCompatActivity {

    private FriendBioBinding binding;
    private User currentUser;
    private User currentFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FriendBioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadUser();
        attachInfo();
        setListener();

    }

    private void loadUser() {
        // Get friend from intent
        currentFriend = (User) getIntent().getSerializableExtra(Constants.FRIEND_USER);
        FirebaseFirestoreController<User> instance = new FirebaseFirestoreController<>(User.class);
        currentFriend = instance.retrieveObjectsFirestoreByID( Constants.KEY_COLLECTION_USERS, currentFriend._UserName );
        
        // Get current user from shared preference
        SharedPreferenceManager<User> currentInstance = new SharedPreferenceManager<>(User.class, this);
        currentUser = currentInstance.retrieveSerializableObjectFromSharedPreference( Constants.KEY_SHARED_PREFERENCE_USERS );
    }

    private void attachInfo() {

        binding.FriendBioName.setText( currentFriend._UserFirstname + " " + currentFriend._UserLastname );
        binding.FriendBioAge.setText("Age: " + calculateAge(currentFriend._UserDoB));
        binding.FriendBioBiography.setText(currentFriend._UserBio);
        binding.FriendBioSpecialization.setText(currentFriend._UserSpecialization);

        FirebaseCloudStorageManager firebaseCloudStorageManager = new FirebaseCloudStorageManager();
        binding.FriendAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        firebaseCloudStorageManager.FetchingImageFromFirebase( currentFriend, binding.FriendAvatar );
    }

    private int calculateAge( String DoB ) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate dob = LocalDate.parse( DoB, formatter);
        LocalDate currentDate = LocalDate.now();

        return Period.between(dob, currentDate).getYears();
    }

    private void setListener() {

        binding.FrienBioBackArrow.setOnClickListener( view -> onBackPressed() );
        binding.UnfriendLayout.setOnClickListener( view -> {

            // Update local
            currentUser._UserFriend.remove( currentFriend._UserName );
            currentFriend._UserFriend.remove( currentUser._UserName );

            // Update share preference
            SharedPreferenceManager<User> currentInstance = new SharedPreferenceManager<>(User.class, this);
            currentInstance.storeSerializableObjectToSharedPreference(currentUser, Constants.KEY_SHARED_PREFERENCE_USERS);

            // Update firebase
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .document(currentUser._UserName)
                    .update( "_UserFriend", currentUser._UserFriend );
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .document(currentFriend._UserName)
                    .update("_UserFriend", currentFriend._UserFriend );

            // Delete conversation
            String deleteId;
            if( currentUser._UserName.compareTo(currentFriend._UserName) < 0 ) deleteId = currentUser._UserName + "+" + currentFriend._UserName;
            else deleteId = currentFriend._UserName + "+" + currentUser._UserName;
            database.collection(Constants.KEY_COLLECTION_CONVERSATION)
                    .document(deleteId)
                    .delete();

            // Return to Friend Screen
            Intent intent = new Intent(this, FriendsScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

    }
}