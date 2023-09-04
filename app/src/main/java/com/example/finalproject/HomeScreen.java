package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yalantis.library.Koloda;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    private SwipeAdater adapter;
    private List<Integer> list;
    Koloda koloda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        koloda = findViewById(R.id.HomeScreenSwipeItem);
        list = new ArrayList<>();
        adapter = new SwipeAdater(this, list);
        koloda.setAdapter(adapter);
    }
}