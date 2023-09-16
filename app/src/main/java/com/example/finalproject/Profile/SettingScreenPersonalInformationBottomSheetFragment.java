package com.example.finalproject.Profile;

import static com.example.finalproject.Constants.FIRESTORE_LOCATION_KEY;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;
import static com.example.finalproject.Constants.LOCATION_UPDATE_STATUS;

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
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.Position;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.Inflater;



public class SettingScreenPersonalInformationBottomSheetFragment extends BottomSheetDialogFragment {

    private String user_name;
    private final int REQUEST_IMAGE_CAPTURE_CAMERA = 1;
    private final int REQUEST_IMAGE_CAPTURE_GALLARY = 2;

    private boolean isComplete = false;


    private Button _SettingPersonalInformationBottomSheetLibraryButton;

    Button _SettingPersonalInformationBottomSheetCameraButton;

    private Button _SettingPersonalInformationBottomSheetCancelButton;

    private ImageView _SettingPersonalInformationAvatarRender;

    private Bitmap newAvatarBitmap = null;


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
                                StoreAndUpdateAvatar(newAvatarBitmap);

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

        View parent_view = getLayoutInflater().inflate(R.layout.activity_setting_screen_modify_personal_information, null,false);

        _SettingPersonalInformationBottomSheetCameraButton = view.findViewById(R.id.SettingPersonalInformationBottomSheetFragmentCameraButton);
        _SettingPersonalInformationBottomSheetLibraryButton = view.findViewById(R.id.SettingPersonalInformationBottomSheetFragmentLibraryButton);
        _SettingPersonalInformationBottomSheetCancelButton = view.findViewById(R.id.SettingPersonalInformationBottomSheetFragmentCancelButton);
        _SettingPersonalInformationAvatarRender = parent_view.findViewById(R.id.SettingPersonalInformationScreenAvatarImageView);

        user_name = getArguments().getString("user");

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
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
                GetImageFromGallary();
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        REQUEST_IMAGE_CAPTURE_GALLARY);
                if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    GetImageFromGallary();
                }
            }
        });
        return view;
    }

    private void GetImageByUsingCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE_CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE_CAMERA && data != null){
            Bundle extras = data.getExtras();
            newAvatarBitmap = (Bitmap) extras.get("data");

            StoreAndUpdateAvatar(newAvatarBitmap);

        }
    }

    private void GetImageFromGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        RetrieveImageFromGallary.launch(intent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void StoreAndUpdateAvatar(Bitmap newAvatarBitmap) {
        Activity activity = getActivity();
        if (activity != null) {
            ImageView avatarImageView = activity.findViewById(R.id.ProfileScreenAvatar);
            if(newAvatarBitmap != null) {
                // Get  user
                FirebaseCloudStorageManager firebaseCloudStorageManager = new FirebaseCloudStorageManager();
                firebaseCloudStorageManager.uploadImageToFirebase(newAvatarBitmap, user_name);

                avatarImageView.setImageBitmap(newAvatarBitmap);

                removeSelf();

            }
        }
    }

    private void removeSelf(){
        requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(this).commit();
    }
}