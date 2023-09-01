package com.example.finalproject.RegisterScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.finalproject.FirebaseController;
import com.example.finalproject.LoginScreen;
import com.example.finalproject.R;
import com.example.finalproject.RegisterScreen.Hobbies.HobbiesAdapter;
import com.example.finalproject.RegisterScreen.Hobbies.HobbiesSelectListenerInterface;
import com.example.finalproject.User;

import java.util.ArrayList;

public class RegisterScreenHobbies extends AppCompatActivity implements HobbiesSelectListenerInterface {

    static final String HobbieFirestoreKey = "Hobbies"; // ID of collection on Firebase Firestore

    User user;

    FirebaseController<String> HobbiesDatabase;
    RecyclerView _RegisterScreenHobbiesOptions;
    HobbiesAdapter _hobbiesAdapter;
    ArrayList<String> _hobbiesList;
    Button _RegisterScreenHobbiesNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen_hobbies);

        user = (User) getIntent().getExtras().getSerializable("new_user");

        RegisterScreenHobbiesLoadingUIElements();

        FetchingHobbiesList();

        CreatingRecyclerViewForHobbiesOptions();

        // "view -> {...}" is lambda format instead of "new View.OnClickListener(){...}"
        _RegisterScreenHobbiesNextButton.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterScreenHobbies.this, RegisterScreenBiography.class);
            intent.putExtra("new_user", user);
            startActivity(intent);
        });
    }

    // Inflate the UI elements with java file
    private void RegisterScreenHobbiesLoadingUIElements() {
        _RegisterScreenHobbiesOptions = (RecyclerView) findViewById(R.id.RegisterScreenHobbiesOptionsRecyclerView);
        _RegisterScreenHobbiesNextButton = (Button) findViewById(R.id.RegisterScreenHobbiesNextButton);
    }

    private void FetchingHobbiesList() {
        _hobbiesList = new ArrayList<>();
        HobbiesDatabase = new FirebaseController<>(String.class); // new Firebase controller for String type
        _hobbiesList = HobbiesDatabase.retrieveAllDocumentsIDOfaCollection(RegisterScreenHobbies.HobbieFirestoreKey); // Taking all popular hobbies (stored in firestore)
    }

    private void CreatingRecyclerViewForHobbiesOptions() {
        _RegisterScreenHobbiesOptions.setHasFixedSize(true);
        _RegisterScreenHobbiesOptions.setLayoutManager(new GridLayoutManager(RegisterScreenHobbies.this, 3)); // The RecyclerView is rendered by GridLayout format with 5 columns
        _hobbiesAdapter = new HobbiesAdapter(RegisterScreenHobbies.this, _hobbiesList, this);
        _RegisterScreenHobbiesOptions.setAdapter(_hobbiesAdapter);
    }

    @Override
    public void onHobbiesClicked(int hobbieAdapter) {
        String user_hobbies = user.get_UserHobbies();
        if(user_hobbies == null) {
            user_hobbies = _hobbiesList.get(hobbieAdapter);
        }
        else user_hobbies = user_hobbies + ',' + _hobbiesList.get(hobbieAdapter);
        user.set_UserHobbies(user_hobbies);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}