package com.example.finalproject.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.FirebaseAuthentication;
import com.example.finalproject.R;
import com.example.finalproject.RegisterScreen.RegisterScreenEmailVerification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Timer;
import java.util.TimerTask;

public class LoginScreenForgotPassword extends AppCompatActivity {

    private FirebaseAuthentication firebaseAuthentication;

    private Timer timer;

    private TextView _forgotPasswordSendEmail;
    private TextView _forgotPasswordTimeCounter;
    private ImageView _forgotPasswordBackToLogin;
    private EditText _forgotPasswordEmail;

    private double time = 30.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen_forgot_password);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        _forgotPasswordSendEmail = findViewById(R.id.ForgotPasswordConfirmButton);
        _forgotPasswordEmail = findViewById(R.id.ForgotPassworEditText);
        _forgotPasswordBackToLogin = findViewById(R.id.ForgotPasswordBackArrowIcon);
        _forgotPasswordTimeCounter = findViewById(R.id.LoginScreenForgotPasswordNotification);

        firebaseAuthentication = new FirebaseAuthentication(LoginScreenForgotPassword.this);

        timer = new Timer();

        _forgotPasswordSendEmail.setOnClickListener(view -> {
            String email = _forgotPasswordEmail.getText().toString();
            if(!email.isEmpty()) {
                firebaseAuthentication.forgotPassword(email);
                _forgotPasswordSendEmail.setHint("Resend Email");
                _forgotPasswordSendEmail.setEnabled(false);
                time = 30.0;
                timer.schedule(new HandleResendButton(), 0);
                } else Toast.makeText(LoginScreenForgotPassword.this, "fill your Email", Toast.LENGTH_SHORT).show();
        });

        _forgotPasswordBackToLogin.setOnClickListener(v -> finish());
    }

    class HandleResendButton extends TimerTask {

        @Override
        public void run() {
            for (int i = 0; i <= 30; i++) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _forgotPasswordTimeCounter.setText(getTimerText());
                        if(time == 0) _forgotPasswordSendEmail.setEnabled(true);
                    }
                });
                try {
                    onComplete();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            time = 30.0;
        }

        private String getTimerText(){
            int rounder = (int) Math.round(time);
            int second = ((rounder%86400)%3600)%60;

            return String.format("%02d", second);
        }

        private void onComplete() throws InterruptedException {
            Thread.sleep(1000);
            time --;
        }
    };
}