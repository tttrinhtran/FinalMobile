package com.example.finalproject.RegisterScreen;

import static com.example.finalproject.Constants.KEY_COLLECTION_USERS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.FirebaseAuthentication;
import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.Login.LoginScreen;
import com.example.finalproject.R;
import com.example.finalproject.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterScreenEmailVerification extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private FirebaseFirestoreController<User> UserDatabase;
    private User user;
    private FirebaseAuthentication firebaseAuthentication;

    private TextView _EmailSendNotification;
    private Button _EmailResendButton;
    private Button _EmailNextButton;


    private Timer timer;
    private Double time = 30.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen_email_verification);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        user = (User) getIntent().getSerializableExtra("new_user");

        _EmailSendNotification = (TextView) findViewById(R.id.RegisterScreenVerificationEmailSentNotificationTextView);
        _EmailResendButton = (Button) findViewById(R.id.RegisterScreenVerificationEmailResendButton);
        _EmailNextButton = (Button) findViewById(R.id.RegisterScreenVerificationRegisteredButton);

        firebaseAuthentication = new FirebaseAuthentication(RegisterScreenEmailVerification.this);
        UserDatabase = new FirebaseFirestoreController<>(User.class);

        timer = new Timer();

        _EmailResendButton.setOnClickListener(view -> {
            firebaseAuthentication.getFirebaseCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!firebaseAuthentication.getFirebaseCurrentUser().isEmailVerified()) {
                        firebaseAuthentication.emailVerification();
                        _EmailResendButton.setEnabled(false);
                        time = 30.0;
                        timer.schedule(new HandleResendButton(), 0);
                    }else Toast.makeText(RegisterScreenEmailVerification.this, "The account is verified already", Toast.LENGTH_SHORT).show();
                }
            });
        });

        _EmailNextButton.setOnClickListener(view -> {
            firebaseAuthentication.getFirebaseCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (firebaseAuthentication.getFirebaseCurrentUser().isEmailVerified()){
                        user.set_UserDistancePref(500);
                        user.set_UserMinAge(18);
                        user.set_UserMaxAge(30);

                        Date date = Calendar.getInstance().getTime();
                        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");
                        String strDate = dateFormat.format(date);

                        user.set_UserJoinDate(strDate);

                        user.set_UserWaitingList(new ArrayList<>());
                        user.set_UserFriend(new ArrayList<>());

                        UserDatabase.addToFirestore(KEY_COLLECTION_USERS, user.get_UserName(), user);
                        Toast.makeText(RegisterScreenEmailVerification.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterScreenEmailVerification.this, LoginScreen.class);
                        startActivity(intent);
                    }else Toast.makeText(RegisterScreenEmailVerification.this, "Verify email before continue", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuthentication.UserRegister(user.get_UserName(), user.get_UserPassword());
    }

    @Override
    protected void onPause() {

        firebaseAuthentication.getFirebaseCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!firebaseAuthentication.getFirebaseCurrentUser().isEmailVerified()) {
                    firebaseAuthentication.removeUser();
                }
            }
        });

        super.onPause();
    }

    class HandleResendButton extends TimerTask {

        @Override
        public void run() {
            for (int i = 0; i <= 30; i++) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _EmailSendNotification.setText(getTimerText());
                        if(time == 0) _EmailResendButton.setEnabled(true);
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