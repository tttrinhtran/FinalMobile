package com.example.finalproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File

class FirebaseCloudStorageManager : AppCompatActivity() {
    private lateinit var user: User
    private lateinit var imageview: ImageView
    private lateinit var but: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        user = intent.getSerializableExtra("new_user") as User
        imageview = findViewById(R.id.screen)
        but = findViewById(R.id.fetch)

        but.setOnClickListener { fetching(user, imageview) }
    }

    fun fetching (user_temp:User, imageview_temp : ImageView) {
        val storageRef = FirebaseStorage.getInstance().reference.child(user_temp._UserName+".jpg")

        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            imageview_temp.setImageBitmap(BitmapFactory.decodeFile(localFile.absolutePath))
        }
    }
    fun uploadImageToFirebase(image: Bitmap, imageName: String): String? {
        val storage = FirebaseStorage.getInstance()
        // Create a storage reference from our app
        val storageRef = storage.reference

        // Create a reference to "imageName.jpg"
        val mountainsRef = storageRef.child("$imageName.jpg")

        // Create a reference to 'images/imageName.jpg'
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