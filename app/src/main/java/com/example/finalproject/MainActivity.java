package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.finalproject.Section.SectionRoomScreen;
import com.yalantis.library.Koloda;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeAdater adapter;
    private List<Integer> list;
    Koloda koloda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, SectionRoomScreen.class);
        startActivity(intent);
    }
}
