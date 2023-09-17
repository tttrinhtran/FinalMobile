package com.example.finalproject.Setting;

import static com.example.finalproject.Constants.KEY_COLLECTION_USERS;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.Profile.ImageSettingBottomSheetFragment;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;

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
    private Button _SettingPersonalInfoConfirmButton;
    private ImageView _SettingPersonalInforBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen_modify_personal_information);

        SettingPersonalInformation_UIElementsFetching();

        firebaseCloudStorageManager = new FirebaseCloudStorageManager();
        userSharedPreferenceManager = new SharedPreferenceManager<>(User.class, SettingScreenModifyPersonalInformation.this);

        UserPreparation();

        OriginalDataFetching();


        _SettingPersonalInfoAvatar.setOnClickListener(view -> {
            ImageSettingBottomSheetFragment settingScreenPersonalInformationBottomSheetFragment = new ImageSettingBottomSheetFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            settingScreenPersonalInformationBottomSheetFragment.setArguments(bundle);
            settingScreenPersonalInformationBottomSheetFragment.show(getSupportFragmentManager(), "settingScreenPersonalInformationBottomSheetFragment");
        });

        _SettingPersonalInfoConfirmButton.setOnClickListener(v -> {

            UserSetting();

            FirebaseFirestoreController<User> firebaseFirestoreController = new FirebaseFirestoreController<>(User.class);
            firebaseFirestoreController.addToFirestore(KEY_COLLECTION_USERS, user.get_UserName(), user);

            userSharedPreferenceManager.storeSerializableObjectToSharedPreference(user, KEY_SHARED_PREFERENCE_USERS);

            Bitmap avatarBitmap = ((BitmapDrawable)_SettingPersonalInfoAvatar.getDrawable()).getBitmap();

            if (avatarBitmap != null) {
                firebaseCloudStorageManager.uploadImageToFirebase(avatarBitmap,user.get_UserName());
            }

            finish();
        });

        _SettingPersonalInforBackButton.setOnClickListener(v -> finish());
    }

    private void UserPreparation() {
        user = userSharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);

        _SettingPersonalInfoAvatar.setScaleType(ImageView.ScaleType.FIT_CENTER);
        firebaseCloudStorageManager.FetchingImageFromFirebase(user, _SettingPersonalInfoAvatar);
    }

    private void UserSetting() {
        user.set_UserNickName(_SettingPersonalInfoNickname.getText().toString());
        user.set_UserFirstname(_SettingPersonalInfoFirstname.getText().toString());
        user.set_UserLastname(_SettingPersonalInfoLastname.getText().toString());
        user.set_UserSchool(_SettingPersonalInfoSchool.getText().toString());
        user.set_UserSpecialization(_SettingPersonalInfoSpecialization.getText().toString());
        user.set_UserPhone(_SettingPersonalInfoPhone.getText().toString());
    }

    private void OriginalDataFetching() {
        _SettingPersonalInfoNickname.setText(user.get_UserNickName());
        _SettingPersonalInfoFirstname.setText(user.get_UserFirstname());
        _SettingPersonalInfoLastname.setText(user.get_UserLastname());
        _SettingPersonalInfoSchool.setText(user.get_UserSchool());
        _SettingPersonalInfoSpecialization.setText(user.get_UserSpecialization());
        _SettingPersonalInfoPhone.setText(user.get_UserPhone());
    }


    private void SettingPersonalInformation_UIElementsFetching() {
        _SettingPersonalInfoAvatar = findViewById(R.id.SettingPersonalInformationScreenAvatarImageView);
        _SettingPersonalInfoFirstname = findViewById(R.id.SettingPersonalInformationScreenFirstNameEditText);
        _SettingPersonalInfoLastname = findViewById(R.id.SettingPersonalInformationScreenLastNameEditText);
        _SettingPersonalInfoSchool = findViewById(R.id.SettingPersonalInformationScreenSchoolEditText);
        _SettingPersonalInfoSpecialization = findViewById(R.id.SettingPersonalInformationScreenSpecializationEditText);
        _SettingPersonalInfoPhone = findViewById(R.id.SettingPersonalInformationScreenPhoneEditText);
        _SettingPersonalInfoNickname = findViewById(R.id.SettingPersonalInformationScreenNickNameEditText);
        _SettingPersonalInfoConfirmButton = findViewById(R.id.SettingPersonalInformationConfirmButton);
        _SettingPersonalInforBackButton = findViewById(R.id.SettingPersonalInformationBackButton);
    }
}