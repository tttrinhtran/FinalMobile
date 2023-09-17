package com.example.finalproject.RegisterScreen

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.finalproject.FirebaseCloudStorageManager
import com.example.finalproject.R
import com.example.finalproject.User

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

        captureButton.setOnClickListener{
            val RegisterScreenAvatarBottomSheetFragment =
                RegisterAvatarSettingBottomSheetFragment()
            val bundle = Bundle()
            bundle.putSerializable("user", user.get_UserName())
            RegisterScreenAvatarBottomSheetFragment.arguments = bundle
            RegisterScreenAvatarBottomSheetFragment.show(
                supportFragmentManager,
                "RegisterAvatarBottomSheetFragment"
            )
        }

        nextButton.setOnClickListener{
            if (image.drawable != null){
                imageBitmap = image.drawable.toBitmap()
                firebaseCloudStorageManager.uploadImageToFirebase(imageBitmap!!, user._UserName)

                startActivity(Intent(this, RegisterScreenMajor::class.java).apply {
                    putExtra("new_user",user)
                })
            }
        }
    }
    private fun RegisterScreenAvatar_FetchingUIElements() {
        image = findViewById(R.id.RegisterScreenAvatarPreviewImageView)
        captureButton = findViewById(R.id.RegisterScreenAvatarCameraButton)
        nextButton = findViewById(R.id.RegisterScreenAvatarNextButton)
    }
}