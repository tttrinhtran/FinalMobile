package com.example.finalproject.Profile;

import static com.example.finalproject.Constants.KEY_COLLECTION_USERS;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;

public class ProfileScreenNameModification extends AppCompatActivity {

    private FirebaseFirestoreController<User> userFirebaseFirestoreController;
    private SharedPreferenceManager<User> userSharedPreferenceManager;

    private User user;

    private EditText _ProfileScreenNameModificationLastname;
    private EditText _ProfileScreenNameModificationFirstname;
    private EditText _ProfileScreenNameModificationNickname;
    private ImageButton _ProfileScreenNameModificationBackButton;
    private Button _ProfileScreenNameModificationConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen_name_modification);

        ProfileSettingName_UIElementsFetching();

        userFirebaseFirestoreController = new FirebaseFirestoreController<>(User.class);
        userSharedPreferenceManager = new SharedPreferenceManager<>(User.class,ProfileScreenNameModification.this);

        user = userSharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);

        _ProfileScreenNameModificationBackButton.setOnClickListener(view -> finish());

        _ProfileScreenNameModificationConfirmButton.setOnClickListener( view -> {
            String newFirstname = _ProfileScreenNameModificationFirstname.getText().toString();
            String newLastname = _ProfileScreenNameModificationLastname.getText().toString();
            String newNickname = _ProfileScreenNameModificationNickname.getText().toString();


            user.set_UserFirstname(newFirstname);
            user.set_UserLastname(newLastname);
            user.set_UserNickName(newNickname);

            userSharedPreferenceManager.storeSerializableObjectToSharedPreference(user, KEY_SHARED_PREFERENCE_USERS);

            userFirebaseFirestoreController.updateDocumentField(KEY_COLLECTION_USERS, user.get_UserName(), "_UserFirstname", newFirstname);
            userFirebaseFirestoreController.updateDocumentField(KEY_COLLECTION_USERS, user.get_UserName(), "_UserLastname", newLastname);
            userFirebaseFirestoreController.updateDocumentField(KEY_COLLECTION_USERS, user.get_UserName(), "_UserNickName", newNickname);

            finish();
        });
    }

    private void ProfileSettingName_UIElementsFetching() {
        _ProfileScreenNameModificationLastname = findViewById(R.id.ProfileScreenNameModificationNewLastnameEditText);
        _ProfileScreenNameModificationFirstname = findViewById(R.id.ProfileScreenNameModificationNewFirstnameEditText);
        _ProfileScreenNameModificationNickname = findViewById(R.id.ProfileScreenNameModificationNewNickNameEditText);
        _ProfileScreenNameModificationConfirmButton = findViewById(R.id.ProfileScreenNameModificationConfirmButton);
        _ProfileScreenNameModificationBackButton = findViewById(R.id.ProfileScreenNameModificationBackButton);
    }
}