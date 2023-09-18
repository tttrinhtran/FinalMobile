package com.example.finalproject.RegisterScreen;

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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;


public class RegisterAvatarSettingBottomSheetFragment extends BottomSheetDialogFragment {

    private String user_name;
    private final int REQUEST_IMAGE_CAPTURE_CAMERA = 1;
    private final int REQUEST_IMAGE_CAPTURE_GALLARY = 2;

    private boolean isComplete = false;


    private Button _RegisterAvatarBottomSheetLibraryButton;

    Button _RegisterAvatarBottomSheetCameraButton;

    private Button _RegisterAvatarBottomSheetCancelButton;

    private ImageView _RegisterAvatarRender;

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

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.image_setting_bottom_sheet_fragment, container, false);

        View parent_view = getLayoutInflater().inflate(R.layout.activity_register_screen_avatar, null,false);

        _RegisterAvatarBottomSheetCameraButton = view.findViewById(R.id.SettingPersonalInformationBottomSheetFragmentCameraButton);
        _RegisterAvatarBottomSheetLibraryButton = view.findViewById(R.id.SettingPersonalInformationBottomSheetFragmentLibraryButton);
        _RegisterAvatarRender = parent_view.findViewById(R.id.RegisterScreenAvatarPreviewImageView);

        user_name = getArguments().getString("user");

        _RegisterAvatarBottomSheetCameraButton.setOnClickListener(v -> {

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

        _RegisterAvatarBottomSheetLibraryButton.setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
                GetImageFromGallary();
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        REQUEST_IMAGE_CAPTURE_GALLARY);
                if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
            ImageView avatarImageView = activity.findViewById(R.id.RegisterScreenAvatarPreviewImageView);
            if(newAvatarBitmap != null) {
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