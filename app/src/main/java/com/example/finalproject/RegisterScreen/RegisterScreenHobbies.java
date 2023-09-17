package com.example.finalproject.RegisterScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.R;
import com.example.finalproject.RegisterScreen.Hobbies.HobbiesAdapter;
import com.example.finalproject.RegisterScreen.Hobbies.HobbiesSelectListenerInterface;
import com.example.finalproject.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegisterScreenHobbies extends AppCompatActivity implements HobbiesSelectListenerInterface {

    static final String HobbieFirestoreKey = "Hobbies"; // ID of collection on Firebase Firestore

    User user;

    FirebaseFirestoreController<String> HobbiesDatabase;
    RecyclerView _RegisterScreenHobbiesOptions;
    HobbiesAdapter _hobbiesAdapter;
    ArrayList<String> _hobbiesList;
    TextView _RegisterScreenHobbiesNextButton;
    ArrayList<String> temp_hobbiesList;

    private HashMap<String, Boolean> isChooosenHobby;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen_hobbies);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        temp_hobbiesList = new ArrayList<>();
        isChooosenHobby = new HashMap<>();

        user = (User) getIntent().getExtras().getSerializable("new_user");

        RegisterScreenHobbiesLoadingUIElements();

        FetchingHobbiesList();

        CreatingRecyclerViewForHobbiesOptions();

        // "view -> {...}" is lambda format instead of "new View.OnClickListener(){...}"
        _RegisterScreenHobbiesNextButton.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterScreenHobbies.this, RegisterScreenBiography.class);
            user.set_UserHobbies(temp_hobbiesList);
            intent.putExtra("new_user", user);
            startActivity(intent);
        });
    }

    // Inflate the UI elements with java file
    private void RegisterScreenHobbiesLoadingUIElements() {
        _RegisterScreenHobbiesOptions = (RecyclerView) findViewById(R.id.RegisterScreenHobbiesOptionsRecyclerView);
        _RegisterScreenHobbiesNextButton = (TextView) findViewById(R.id.RegisterScreenHobbiesNextButton);
    }

    private void FetchingHobbiesList() {
        _hobbiesList = new ArrayList<>();
        HobbiesDatabase = new FirebaseFirestoreController<>(String.class); // new Firebase controller for String type
        _hobbiesList = HobbiesDatabase.retrieveAllDocumentsIDOfaCollection(RegisterScreenHobbies.HobbieFirestoreKey); // Taking all popular hobbies (stored in firestore)

        for (String s: _hobbiesList){
            isChooosenHobby.put(s, false);
        }
    }

    private void CreatingRecyclerViewForHobbiesOptions() {
        _RegisterScreenHobbiesOptions.setHasFixedSize(true);
        _RegisterScreenHobbiesOptions.setLayoutManager(new GridLayoutManager(RegisterScreenHobbies.this, 2)); // The RecyclerView is rendered by GridLayout format with 5 columns
        _hobbiesAdapter = new HobbiesAdapter(RegisterScreenHobbies.this, _hobbiesList, this);
        _RegisterScreenHobbiesOptions.setAdapter(_hobbiesAdapter);
    }

    @Override
    public void onHobbiesClicked(int hobbieAdapter) {
        String user_hobbies = _hobbiesList.get(hobbieAdapter);
        if(Boolean.TRUE.equals(isChooosenHobby.get(user_hobbies))){
            temp_hobbiesList.remove(user_hobbies);
            isChooosenHobby.replace(user_hobbies, false);
        }else {
            temp_hobbiesList.add(user_hobbies);
            isChooosenHobby.replace(user_hobbies, true);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}