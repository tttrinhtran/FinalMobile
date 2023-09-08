package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Home.HomeScreen;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_screen) ;
        Intent intent = new Intent(MainActivity.this, LoginScreen.class);
        startActivity(intent);
    }
}
