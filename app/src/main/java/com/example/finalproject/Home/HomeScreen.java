package com.example.finalproject.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.R;
import com.example.finalproject.SwipeAdapter;
import com.example.finalproject.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.library.Koloda;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    private SwipeAdapter adapter;
    private List<Integer> list;
    private User user;
    private ArrayList<String> activeFriend;
    Koloda koloda;

    private TextView titleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getUser();
        swipe();
        setUp();

    }

    private void swipe()
    {
        koloda = findViewById(R.id.HomeScreenSwipeItem);
        list = new ArrayList<>();
        adapter = new SwipeAdapter(this, list);
        koloda.setAdapter(adapter);
    }
    private void getActiveFriend()
    {
        activeFriend=user.getFriend();


    }
    void setUp()
    {
        titleText=(TextView) findViewById(R.id.HomeScreenTitleText);
        String temp="Hello! "+user.get_UserName();
        titleText.setText(temp);

    }
    private void getUser()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("myUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonString = sharedPreferences.getString("myUserString", null);
        Type type = new TypeToken<User>() {}.getType();
        user = gson.fromJson(jsonString, type);
    }


}