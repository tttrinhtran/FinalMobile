package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.finalproject.Login.LoginScreen;

public class SplashWelcome extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

//        Intent i = new Intent(SplashWelcome.this, LoginScreen.class);
//        startActivity(i);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lottieAnimationView = findViewById(R.id.lottie);
                lottieAnimationView.animate().translationY(1400).setDuration(5000);
                Intent i = new Intent(SplashWelcome.this, MainActivity.class );
                startActivity(i);
                finish();
            }
        }, 5000 );
    }
}