package com.example.finalproject;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finalproject.Setting.SettingScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class FirebaseAuthentication {
    boolean isCompleted = false;
    boolean isSuccess = false;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Context context;

    public FirebaseAuthentication(Context context) {
        this.context = context;
    }

    public static final String TAG = "FirebaseAuthentication";

    public void UserRegister(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            emailVerification();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText((Activity)context, "Register failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean UserSignUp(String email, String password){
        isCompleted = false;
        isSuccess = false;

        boolean complete_check = false;

        mAuth.signInWithEmailAndPassword(email, password)
                 .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             // Sign in success, update UI with the signed-in user's information
                             Log.d(TAG, "signInWithEmail:success");
                             isSuccess = true;
                             isCompleted = true;
                         } else {
                             isSuccess = false;
                             isCompleted = true;
                         }
                     }
                 });

        while(!complete_check){
            complete_check = isComplete();
        }

        return isSuccess;
    }

    private boolean isSuccessfull(){
        return isSuccess;
    }

    private boolean isComplete(){
        return isCompleted;
    }

    public FirebaseUser getFirebaseCurrentUser(){
        return mAuth.getCurrentUser();
    }

    public void emailVerification(){
        FirebaseUser user = getFirebaseCurrentUser();

        if(user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                            }
                        }
                    });
        }
    }

    public void removeUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });
    }

    public String getEmail(){
        FirebaseUser user = getFirebaseCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            return user.getEmail();
        } else return null;
    }

    public void changePassword(String email, String old_password, String new_pass){
        FirebaseUser user = getFirebaseCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(email, old_password);

        isCompleted = false;
        isSuccess = false;
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(new_pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                        isSuccess = true;

                                        Intent intent = new Intent(context, SettingScreen.class);
                                        startActivity(context, intent,null);
                                    } else {
                                        Toast.makeText(context, "Password update failed", Toast.LENGTH_SHORT).show();
                                    }
                                    isCompleted = true;

                                }
                            });
                        } else {
                            Toast.makeText(context, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                            isCompleted = true;
                        }
                    }
                });
    }

    public void SignOut (){
        mAuth.signOut();
    }
}
