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

public class ProfileScreenMajorModification extends AppCompatActivity {

    private User user;

    private EditText _newMajor;
    private ImageView _backButton;
    private TextView _confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen_major_modification);

        _newMajor = findViewById(R.id.ProfileScreenMajorModificationNewMajorEditText);
        _backButton = findViewById(R.id.ProfileScreenMajorModificationBackButton);
        _confirmButton = findViewById(R.id.ProfileScreenMajorModificationConfirmButton);

        SharedPreferenceManager<User> userSharedPreferenceManager = new SharedPreferenceManager<>(User.class, ProfileScreenMajorModification.this);
        user = userSharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(Constants.KEY_SHARED_PREFERENCE_USERS);

        _backButton.setOnClickListener(v -> finish());

        _confirmButton.setOnClickListener(v -> {
            String newMajor = _newMajor.getText().toString();
            if(!newMajor.isEmpty()) {
                user.set_UserSpecialization(newMajor);

                userSharedPreferenceManager.storeSerializableObjectToSharedPreference(user, Constants.KEY_SHARED_PREFERENCE_USERS);

                FirebaseFirestoreController<User> firestoreController = new FirebaseFirestoreController<>(User.class);
                firestoreController.updateDocumentField(Constants.KEY_COLLECTION_USERS, user.get_UserName(), "_UserSpecialization", newMajor);

            } else Toast.makeText(this, "Fill the new Major", Toast.LENGTH_SHORT).show();
        });
    }
}