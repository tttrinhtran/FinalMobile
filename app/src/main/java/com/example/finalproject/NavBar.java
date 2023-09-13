package com.example.finalproject;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Home.HomeScreen;

public class NavBar extends AppCompatActivity {

    ImageView home;
    ImageView section;
    ImageView Friend;
    ImageView profile;
    public void onPress()
    {
        home= findViewById(R.id.NaviBarHomeIcon);
        section= findViewById(R.id.NaviBarSectionIcon);
        Friend= findViewById(R.id.NaviBarFriendIcon);
        profile = findViewById(R.id.NaviBarProfile);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NavBar.this, "Direction Right", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
