package com.example.finalproject.Profile;

import static com.example.finalproject.Constants.KEY_COLLECTION_USERS;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;

public class ProfileScreenPhoneNumberModification extends AppCompatActivity {

    private User user;
    private FirebaseFirestoreController<User> userFirebaseFirestoreController;
    private SharedPreferenceManager<User> userSharedPreferenceManager;

    private ImageView _ProfileSettingPhoneNumberBackButton;
    private EditText _ProfileSettingNewPhoneNumberEditText;
    private TextView _ProfileSettingPhoneNumberConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen_phone_number_modification);

        ProfilePhoneNumberScreen_UIElementsFetching();

        userFirebaseFirestoreController = new FirebaseFirestoreController<>(User.class);
        userSharedPreferenceManager = new SharedPreferenceManager<>(User.class,ProfileScreenPhoneNumberModification.this);

        user = userSharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);

        _ProfileSettingPhoneNumberBackButton.setOnClickListener(view -> finish());

        _ProfileSettingPhoneNumberConfirmButton.setOnClickListener(view -> {
            String newphone = _ProfileSettingNewPhoneNumberEditText.getText().toString();

            if (!newphone.isEmpty()){
                user.set_UserPhone(newphone);

                // Update SharedPreference
                userSharedPreferenceManager.storeSerializableObjectToSharedPreference(user, KEY_SHARED_PREFERENCE_USERS);

                // Update to firestore
                userFirebaseFirestoreController.updateDocumentField(KEY_COLLECTION_USERS, user.get_UserName(), "_UserPhone", newphone);

                finish();
            } else Toast.makeText(ProfileScreenPhoneNumberModification.this, "Fill the new phone number", Toast.LENGTH_SHORT).show();
        });
    }

    private void ProfilePhoneNumberScreen_UIElementsFetching() {
        _ProfileSettingPhoneNumberBackButton = findViewById(R.id.ProfileScreenPhoneModificationBackButton);
        _ProfileSettingNewPhoneNumberEditText = findViewById(R.id.ProfileScreenPhoneModificationNewPhoneEditText);
        _ProfileSettingPhoneNumberConfirmButton = findViewById(R.id.ProfileScreenPhoneModificationConfirmButton);
    }
}