package com.example.finalproject.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.finalproject.R;
import com.example.finalproject.SwipeAdapter;
import com.yalantis.library.Koloda;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    private SwipeAdapter adapter;
    private List<Integer> list;
    Koloda koloda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        koloda = findViewById(R.id.HomeScreenSwipeItem);
        list = new ArrayList<>();
        adapter = new SwipeAdapter(this, list);
        koloda.setAdapter(adapter);
    }
}