package com.example.finalproject.RegisterScreen

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.finalproject.FirebaseCloudStorageManager
import com.example.finalproject.R
import com.example.finalproject.User
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class RegisterScreenAvatar : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 2000

    private lateinit var user: User
    private lateinit var image: ImageView
    private lateinit var captureButton :Button
    private lateinit var nextButton :Button
    private var imageBitmap: Bitmap? = null

    private lateinit var firebaseCloudStorageManager: FirebaseCloudStorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen_avatar)

        RegisterScreenAvatar_FetchingUIElements()

        user = intent.getSerializableExtra("new_user") as User

        firebaseCloudStorageManager = FirebaseCloudStorageManager()

        request_permission()

        captureButton.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

        nextButton.setOnClickListener{
            if (imageBitmap != null){
                firebaseCloudStorageManager.uploadImageToFirebase(imageBitmap!!, user._UserName)

                startActivity(Intent(this, RegisterScreenMajor::class.java).apply {
                    putExtra("new_user",user)
                })
            }
        }
    }

    private fun request_permission() {
        val requestSinglePermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                } else {
                    Toast.makeText(this, "Camera permission is denied", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        requestSinglePermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    private fun RegisterScreenAvatar_FetchingUIElements() {
        image = findViewById(R.id.RegisterScreenAvatarPreviewImageView)
        captureButton = findViewById(R.id.RegisterScreenAvatarCameraButton)
        nextButton = findViewById(R.id.RegisterScreenAvatarNextButton)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE && data != null){
            imageBitmap = data.extras!!.get("data") as Bitmap
            image.setImageBitmap(imageBitmap)
        }
    }
}