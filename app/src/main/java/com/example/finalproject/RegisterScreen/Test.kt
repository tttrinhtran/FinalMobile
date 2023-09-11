package com.example.finalproject.RegisterScreen

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import com.example.finalproject.User
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.File

class Test : AppCompatActivity() {
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
}