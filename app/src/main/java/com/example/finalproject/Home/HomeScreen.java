package com.example.finalproject.Home;

import static com.example.finalproject.Constants.KEY_COLLECTION_USERS;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.Home.Active.ActiveListAdapter;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
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
     User user;
     ArrayList<User> activeFriend;

    Koloda koloda;
    FirebaseFirestoreController<User> userFirebaseController;
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
        userFirebaseController=new FirebaseFirestoreController<>(User.class);
       ArrayList<String> temp= user.get_UserFriend();
       for(int i=0; i< temp.size(); i++)
       {

           User tmp=userFirebaseController.retrieveObjectsFirestoreByID(KEY_COLLECTION_USERS, temp.get(i));
           if (activeFriend==null) {
               activeFriend=new ArrayList<>();
           }

               activeFriend.add(tmp);

       }



    }
    void setUp()
    {
        titleText=(TextView) findViewById(R.id.HomeScreenTitleText);
        String temp="Hello! " + user.get_UserName();
        titleText.setText(temp);
        getActiveFriend();
        RecyclerView recyclerView=findViewById(R.id.HomeScreenFriendRecyclerView);
        ActiveListAdapter adapter= new ActiveListAdapter(activeFriend, (Context) this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }
     void getUser()
    {
        SharedPreferenceManager<User> sharedPreferenceManager = new SharedPreferenceManager<>(User.class, this);
        user = sharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);
    }


}