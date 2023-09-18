package com.example.finalproject.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Constants;
import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;

public class ProfileScreenSchoolModification extends AppCompatActivity {
    private User user;

    private EditText _newSchool;
    private ImageView _backButton;
    private TextView _confirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen_school_modification);
        _newSchool = findViewById(R.id.ProfileScreenSchoolModificationNewSchoolEditText);
        _backButton = findViewById(R.id.ProfileScreenSchoolModificationBackButton);
        _confirmButton = findViewById(R.id.ProfileScreenSchoolModificationConfirmButton);

        SharedPreferenceManager<User> userSharedPreferenceManager = new SharedPreferenceManager<>(User.class, ProfileScreenSchoolModification.this);
        user = userSharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(Constants.KEY_SHARED_PREFERENCE_USERS);

        _backButton.setOnClickListener(v -> finish());

        _confirmButton.setOnClickListener(v -> {
            String newSchool = _newSchool.getText().toString();
            if(!newSchool.isEmpty()) {
                user.set_UserSchool(newSchool);

                userSharedPreferenceManager.storeSerializableObjectToSharedPreference(user, Constants.KEY_SHARED_PREFERENCE_USERS);

                FirebaseFirestoreController<User> firestoreController = new FirebaseFirestoreController<>(User.class);
                firestoreController.updateDocumentField(Constants.KEY_COLLECTION_USERS, user.get_UserName(), "_UserSchool", newSchool);

            } else Toast.makeText(this, "Fill the new School", Toast.LENGTH_SHORT).show();
        });

    }
}