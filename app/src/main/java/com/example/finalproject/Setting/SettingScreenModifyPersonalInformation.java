package com.example.finalproject.Setting;

import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;

import java.io.Serializable;

public class SettingScreenModifyPersonalInformation extends AppCompatActivity {

    private FirebaseCloudStorageManager firebaseCloudStorageManager;

    private User user;

    private SharedPreferenceManager<User> userSharedPreferenceManager;

    private ImageView _SettingPersonalInfoAvatar;
    private EditText _SettingPersonalInfoNickname;
    private EditText _SettingPersonalInfoFirstname;
    private EditText _SettingPersonalInfoLastname;
    private EditText _SettingPersonalInfoSchool;
    private EditText _SettingPersonalInfoSpecialization;
    private EditText _SettingPersonalInfoPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen_modify_personal_information);

        SettingPersonalInformation_UIElementsFetching();

        firebaseCloudStorageManager = new FirebaseCloudStorageManager();
        userSharedPreferenceManager = new SharedPreferenceManager<>(User.class, SettingScreenModifyPersonalInformation.this);

        user = userSharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);

        _SettingPersonalInfoAvatar.setScaleType(ImageView.ScaleType.FIT_CENTER);
        firebaseCloudStorageManager.FetchingImageFromFirebase(user,_SettingPersonalInfoAvatar);

        _SettingPersonalInfoAvatar.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user_avatar", (Serializable) _SettingPersonalInfoAvatar);

            SettingScreenPersonalInformationBottomSheetFragment settingScreenPersonalInformationBottomSheetFragment = new SettingScreenPersonalInformationBottomSheetFragment();
        });
    }



    private void SettingPersonalInformation_UIElementsFetching() {
        _SettingPersonalInfoAvatar = findViewById(R.id.SettingPersonalInformationScreenAvatarImageView);
        _SettingPersonalInfoFirstname = findViewById(R.id.SettingPersonalInformationScreenFirstNameEditText);
        _SettingPersonalInfoLastname = findViewById(R.id.SettingPersonalInformationScreenLastNameEditText);
        _SettingPersonalInfoSchool = findViewById(R.id.SettingPersonalInformationScreenSchoolEditText);
        _SettingPersonalInfoSpecialization = findViewById(R.id.SettingPersonalInformationScreenSpecializationEditText);
        _SettingPersonalInfoPhone = findViewById(R.id.SettingPersonalInformationScreenPhoneEditText);
        _SettingPersonalInfoNickname = findViewById(R.id.SettingPersonalInformationScreenNickNameEditText);
    }
}