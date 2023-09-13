package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Home.HomeScreen;
import com.example.finalproject.Listeners.UserListener;
import com.example.finalproject.Message.ChatActivity;
import com.example.finalproject.databinding.ActivityFriendsScreenBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FriendsScreen extends AppCompatActivity implements UserListener {

    private ActivityFriendsScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendsScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        getUser();
        navBar();
    }

    private void setListener() {

    }

    private void getUser() {

        SharedPreferenceManager<User> currentInstance = new SharedPreferenceManager<>(User.class, this);
        User currentUser = currentInstance.retrieveSerializableObjectFromSharedPreference( Constants.KEY_SHARED_PREFERENCE_USERS );

        List<String> friendList = currentUser.get_UserFriend();
        if( friendList.isEmpty() ) showErrorMessage();
        else {
            UserAdapter userAdapter = new UserAdapter( friendList, this );
            binding.FriendScreenChat.setAdapter(userAdapter);
            binding.FriendScreenChat.setVisibility(View.VISIBLE);
        }
    }

    private void showErrorMessage() {
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

    @Override
    public void onUserClicker(User user) {
        Intent intent = new Intent( getApplicationContext(), ChatActivity.class );
        intent.putExtra( Constants.RECEIVED_USER, user );
        startActivity(intent);
        // finish();
    }
}