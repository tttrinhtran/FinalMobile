package com.example.finalproject.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.Constants;
import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.FriendsScreen;
import com.example.finalproject.R;
import com.example.finalproject.RegisterScreen.Hobbies.HobbiesAdapter;
import com.example.finalproject.User;
import com.example.finalproject.databinding.ActivityUserBioBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserBio extends AppCompatActivity {

    User user;
    ActivityUserBioBinding binding;
    ArrayList<String> hobiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        user = (User) getIntent().getSerializableExtra("USER_OBJECT");

        setDataBio(user);
        setHobies();
        onButtonClicked();
    }

    private void onButtonClicked() {
        binding.ItemSwipeBackArrow.setOnClickListener( view -> onBackPressed());
    }

    private void setDataBio(User user) {

        binding.ItemSwipeLastName.setText(user.get_UserLastname());
        binding.ItemSwipeFirstName.setText(user.get_UserFirstname());
        binding.ItemSwipeAge.setText(user.get_UserAge());
        binding.ItemSwipeSchool.setText(user.get_UserSchool());
        binding.ItemSwipeBiography.setText(user.get_UserBio());
        binding.ItemSwipeSpecialization.setText(user.get_UserSpecialization());

        FirebaseCloudStorageManager firebaseCloudStorageManager = new FirebaseCloudStorageManager();
        firebaseCloudStorageManager.FetchingImageFromFirebase(user, binding.ItemSwipeImage);
        binding.ItemSwipeImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

    }

    private void setHobies()
    {
        hobiesList=user.get_UserHobbies();
        RecyclerView recyclerView=findViewById(R.id.bioHobbiesRecyclerview);
         hobbiesAdapter adapter= new hobbiesAdapter(hobiesList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

    }

}