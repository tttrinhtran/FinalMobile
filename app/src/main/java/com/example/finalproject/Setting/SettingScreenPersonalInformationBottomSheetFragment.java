package com.example.finalproject.Setting;

import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;

public class SettingScreenPersonalInformationBottomSheetFragment extends BottomSheetDialogFragment {

    private FirebaseCloudStorageManager firebaseCloudStorageManager;
    private SharedPreferenceManager<User> sharedPreferenceManager;

    private User user;
    private final int REQUEST_IMAGE_CAPTURE_CAMERA = 1;
    private final int REQUEST_IMAGE_CAPTURE_GALLARY = 2;


    private Button _SettingPersonalInformationBottomSheetLibraryButton;

    Button _SettingPersonalInformationBottomSheetCameraButton;

    private Button _SettingPersonalInformationBottomSheetCancelButton;

    private ImageView _SettingPersonalInformationAvatarRender;

    private Bitmap newAvatarBitmap;


    private ActivityResultLauncher<Intent> RetrieveImageFromGallary = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        try {
                            newAvatarBitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().getContentResolver(),
                                    selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_setting_screen_personal_information_bottom_sheet_fragment, container, false);

        _SettingPersonalInformationBottomSheetCameraButton = view.findViewById(R.id.SettingPersonalInformationBottomSheetFragmentCameraButton);
        _SettingPersonalInformationBottomSheetLibraryButton = view.findViewById(R.id.SettingPersonalInformationBottomSheetFragmentLibraryButton);
        _SettingPersonalInformationBottomSheetCancelButton = view.findViewById(R.id.SettingPersonalInformationBottomSheetFragmentCancelButton);

        firebaseCloudStorageManager = new FirebaseCloudStorageManager();

        sharedPreferenceManager = new SharedPreferenceManager<>(User.class, requireActivity());
        user = sharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);

        assert Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || getArguments() != null;

        _SettingPersonalInformationAvatarRender = (ImageView) getArguments().getSerializable("user_avatar");

        _SettingPersonalInformationBottomSheetCameraButton.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                        Manifest.permission.CAMERA)) {} else {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_IMAGE_CAPTURE_CAMERA);
                    if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) GetImageByUsingCamera();
                }
            } else {
                GetImageByUsingCamera();
            }
        });

        _SettingPersonalInformationBottomSheetLibraryButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {} else {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_IMAGE_CAPTURE_GALLARY);
                    if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) GetImageFromGallary();
                }
            } else {
                GetImageFromGallary();
            }
        });

        _SettingPersonalInformationBottomSheetCancelButton.setOnClickListener(v -> finish_fragment());

        return view;
    }

    private void GetImageByUsingCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE_CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE_CAMERA){
            ChangeAvatar();
        }
    }

    private void GetImageFromGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        RetrieveImageFromGallary.launch(intent);

        if(newAvatarBitmap != null){
            ChangeAvatar();
        }
    }

    private void ChangeAvatar() {
        firebaseCloudStorageManager.uploadImageToFirebase(newAvatarBitmap, user.get_UserName());
        _SettingPersonalInformationAvatarRender.setImageBitmap(newAvatarBitmap);
        finish_fragment();
    }

    private void finish_fragment() {
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        fm.popBackStack ("settingScreenPersonalInformationBottomSheetFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}