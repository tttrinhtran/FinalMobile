package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Home.HomeScreen;
import com.example.finalproject.databinding.ActivityFriendsScreenBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FriendsScreen extends AppCompatActivity {

    private ActivityFriendsScreenBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendsScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext(), "chatAppPreference");
        setListener();
        getUser();
        navBar();
    }

    private void setListener() {

    }

    private void getUser() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_FRIENDS)
                .get()
                .addOnCompleteListener(task -> {
                   loading(false);
                   // String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                   String currentUserId = "0777428999";
                   if( task.isSuccessful() && task.getResult() != null ) {
                       List<User> friends = new ArrayList<>();
                       for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult() ) {
                           if( currentUserId.equals(queryDocumentSnapshot.getId())) continue;
                           User user = new User(
                /*                   queryDocumentSnapshot.getString("_UserName"),
                                   queryDocumentSnapshot.getString("_UserPassword"),
                                   queryDocumentSnapshot.getString("_UserFirstname"),
                                   queryDocumentSnapshot.getString("_UserLastname"),
                                   queryDocumentSnapshot.getString("_UserDoB"),
                                   queryDocumentSnapshot.getString("_UserJoinDate"),
                                   queryDocumentSnapshot.getString("_UserEmail"),
                                   queryDocumentSnapshot.getString("_UserPhone"),
                                   queryDocumentSnapshot.getBoolean("_UserActive"),
                                   queryDocumentSnapshot.getString("_UserBio"),
                                   queryDocumentSnapshot.getString("_UserHobbies"),
                                   queryDocumentSnapshot.getString("_UserSchool"),
                                   queryDocumentSnapshot.getString("_UserSpecialization"),*/
                           );
                           friends.add(user);
                       }

                       if( friends.size() > 0 ) {
                           // Phải sửa á
                           UserAdapter userAdapter = new UserAdapter( friends, null );
                           binding.FriendScreenChat.setAdapter(userAdapter);
                           binding.FriendScreenChat.setVisibility(View.VISIBLE);
                       }
                       else showErrorMessage();
                   }
                   else showErrorMessage();
                });
    }

    private void showErrorMessage() {
    }

    private void loading( Boolean isLoading ) {
    }

    private void navBar(){
        ImageView home;
        ImageView section;
        ImageView friend;
        ImageView profile;


        home = findViewById(R.id.NaviBarHomeIcon);
        section= findViewById(R.id.NaviBarSectionIcon);
        friend= findViewById(R.id.NaviBarFriendIcon); friend.setImageResource(R.drawable.friend_icon_fill);
        profile = findViewById(R.id.NaviBarProfile);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}