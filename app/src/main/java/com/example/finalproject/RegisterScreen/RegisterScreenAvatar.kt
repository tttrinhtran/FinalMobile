package com.example.finalproject.RegisterScreen

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var imageBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen_avatar)

        RegisterScreenAvatar_FetchingUIElements()

        user = intent.getSerializableExtra("new_user") as User

        captureButton.setOnClickListener{
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        }

        nextButton.setOnClickListener{
            if (imageBitmap != null){
                user._UserAvatarUrl = uploadImageToFirebase(imageBitmap, user._UserName)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE && data != null){
            imageBitmap = data.extras!!.get("data") as Bitmap
            image.setImageBitmap(imageBitmap)
        }
    }

    fun uploadImageToFirebase(image: Bitmap, imageName: String): String? {
        val storage = FirebaseStorage.getInstance()
        // Create a storage reference from our app
        val storageRef = storage.reference

        // Create a reference to "mountains.jpg"
        val mountainsRef = storageRef.child("$imageName.jpg")

        // Create a reference to 'images/mountains.jpg'
        val mountainImagesRef = storageRef.child("images/$imageName.jpg")

        // While the file names are the same, the references point to different files
        mountainsRef.name == mountainImagesRef.name // true
        mountainsRef.path == mountainImagesRef.path // false
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { }
        return mountainsRef.downloadUrl.toString()
    }
}