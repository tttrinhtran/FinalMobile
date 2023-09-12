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
        home= findViewById(R.id.HomeScreenNaviBarHomeIcon);
        section= findViewById(R.id.HomeScreenNaviBarSectionIcon);
        Friend= findViewById(R.id.HomeScreenNaviBarFriendIcon);
        profile = findViewById(R.id.HomeScreenNaviBarHomeProfile);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NavBar.this, "Direction Right", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
