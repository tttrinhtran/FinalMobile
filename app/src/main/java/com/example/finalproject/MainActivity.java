package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.RegisterScreen.RegisterScreenAvatar;
import com.example.finalproject.openvcall.ui.SectionRoomVideoChatScreen;
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

        Intent intent = new Intent(MainActivity.this, LoginScreen.class);
        startActivity(intent);
    }
}
