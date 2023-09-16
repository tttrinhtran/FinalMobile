package com.example.finalproject.Profile;

import static com.example.finalproject.Constants.FIRESTORE_LOCATION_KEY;
import static com.example.finalproject.Constants.KEY_COLLECTION_USERS;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;
import static com.example.finalproject.Constants.LOCATION_UPDATE_STATUS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.FirebaseAuthentication;
import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.Position;
import com.example.finalproject.R;
import com.example.finalproject.Setting.SettingAccountScreen;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileScreenPasswordModification extends AppCompatActivity {
    private SharedPreferenceManager<User> userSharedPreferenceManager;

    private EditText _ProfileScreenPasswordModificationOldPassword;
    private EditText _ProfileScreenPasswordModificationNewPassword;
    private EditText _ProfileScreenPasswordModificationNewPasswordConfirm;
    private Button _ProfileScreenPasswordModificationConfirm;
    private ProgressBar _ProfileScreenPasswordModificationProgressBar;
    private ImageButton _ProfileScreenPasswordModificationBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen_password_modification);

        ProfileScreenPasswordModification_UIElementsFetching();

        userSharedPreferenceManager = new SharedPreferenceManager<>(User.class, ProfileScreenPasswordModification.this);
        User user = userSharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);

        _ProfileScreenPasswordModificationBackButton.setOnClickListener(view -> {finish();});

        _ProfileScreenPasswordModificationConfirm.setOnClickListener(view -> {
            FirebaseAuthentication firebaseAuthentication = new FirebaseAuthentication(ProfileScreenPasswordModification.this);
            String old_password = _ProfileScreenPasswordModificationOldPassword.getText().toString();
            String new_password = _ProfileScreenPasswordModificationNewPassword.getText().toString();
            String new_password_confirm = _ProfileScreenPasswordModificationNewPasswordConfirm.getText().toString();

            if(!old_password.isEmpty() && !new_password.isEmpty() && !new_password_confirm.isEmpty()) {

                if(new_password.equals(new_password_confirm)) {
                    String email = user.get_UserName();

                    Handler handler = new Handler(Looper.getMainLooper());
                    ExecutorService executorService = Executors.newSingleThreadExecutor();

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {

                                    _ProfileScreenPasswordModificationProgressBar.setVisibility(View.VISIBLE);
                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                }
                            });

                            boolean isPasswordChanged = firebaseAuthentication.changePassword(email, old_password, new_password);
                            if(isPasswordChanged) {
                                // Update user SharedPreference
                                user.set_UserPassword(new_password);
                                userSharedPreferenceManager.storeSerializableObjectToSharedPreference(user,KEY_SHARED_PREFERENCE_USERS);

                                // Update user collection in firestore
                                FirebaseFirestoreController<User> userFirebaseFirestoreController = new FirebaseFirestoreController<>(User.class);
                                userFirebaseFirestoreController.updateDocumentField(KEY_COLLECTION_USERS,user.get_UserName(), "_UserPassword", new_password);

                                finish();
                            }

                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    _ProfileScreenPasswordModificationProgressBar.setVisibility(View.INVISIBLE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                }
                            });
                        }
                    });
                } else Toast.makeText(ProfileScreenPasswordModification.this, "Confirm new password and new password are not matched",Toast.LENGTH_SHORT).show();
            } else Toast.makeText(ProfileScreenPasswordModification.this, "Full fill Old Password and New password.",Toast.LENGTH_SHORT).show();
        });
    }

    private void ProfileScreenPasswordModification_UIElementsFetching() {
        _ProfileScreenPasswordModificationOldPassword = findViewById(R.id.ProfileScreenPasswordModificationOldPasswordEditText);
        _ProfileScreenPasswordModificationNewPassword = findViewById(R.id.ProfileScreenPasswordModificationNewPasswordEditText);
        _ProfileScreenPasswordModificationNewPasswordConfirm = findViewById(R.id.ProfileScreenPasswordModificationNewPasswordConfirmEditText);
        _ProfileScreenPasswordModificationConfirm = findViewById(R.id.ProfileScreenPasswordModificationConfirmButton);
        _ProfileScreenPasswordModificationProgressBar = findViewById(R.id.ProfileScreenPasswordModificationProgressBar);
        _ProfileScreenPasswordModificationBackButton = findViewById(R.id.ProfileScreenPasswordModificationBackButton);
    }
}