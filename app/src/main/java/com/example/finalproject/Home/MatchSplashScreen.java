package com.example.finalproject.Home;

import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.FriendsScreen;
import com.example.finalproject.Login.LoginScreen;
import com.example.finalproject.Profile.ProfileScreen;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;
import com.example.finalproject.databinding.ActivityMatchSplashScreenBinding;

public class MatchSplashScreen extends AppCompatActivity {

    private User user1;
    private User user2;
    private ImageView _userAvt1;
    private ImageView _userAvt2;
    private TextView _matchFriends; private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_match_splash_screen);

        fetchUI();
        getData();
        setData();
        setListener();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i = new Intent(MatchSplashScreen.this, ChatActivity.class);
//                startActivity(i);
//                finish();
//            }
//        }, 3000 );
    }

    private void setData() {
        FirebaseCloudStorageManager firebaseCloudStorageManager = new FirebaseCloudStorageManager();
        firebaseCloudStorageManager.FetchingImageFromFirebase(user1, _userAvt1);
        firebaseCloudStorageManager.FetchingImageFromFirebase(user2, _userAvt2);

        _userAvt1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        _userAvt2.setScaleType(ImageView.ScaleType.CENTER_CROP);

        name = "You and " + user2.get_UserFirstname();
        _matchFriends.setText(name);
    }

    private void fetchUI() {
        _userAvt1 = (ImageView) findViewById(R.id.MatchScreenAvatar1);
        _userAvt2 = (ImageView) findViewById(R.id.MatchScreenAvatar3);
        _matchFriends = (TextView) findViewById(R.id.MatchFriendName);
    }

    private void getData() {
        Intent i = getIntent();
        user2 = (User) i.getSerializableExtra("USER_MATCH");
        SharedPreferenceManager<User> sharedPreferenceManager = new SharedPreferenceManager<>(User.class, this);
        user1 = sharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);
    }

    private void setListener() {
        LinearLayout SplashScreenLayout = findViewById(R.id.SplashScreenLayout);
        SplashScreenLayout.setOnClickListener( view -> {
            Intent intent = new Intent(MatchSplashScreen.this, FriendsScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

}