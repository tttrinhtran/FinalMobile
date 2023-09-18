package com.example.finalproject.Profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.finalproject.Constants
import com.example.finalproject.FirebaseFirestoreController
import com.example.finalproject.R
import com.example.finalproject.SharedPreferenceManager
import com.example.finalproject.User

class ProfileScreenCity : AppCompatActivity() {

    lateinit var user: User

    lateinit var _backBtn: ImageView
    lateinit var _confirm: TextView
    lateinit var _newCity: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen_city)

        _backBtn = findViewById(R.id.ProfileScreenCityModificationBackButton)
        _confirm = findViewById(R.id.ProfileScreenCityModificationConfirmButton)
        _newCity = findViewById(R.id.ProfileScreenCityModificationNewCityEditText)

        val userSharedPreferenceManager = SharedPreferenceManager<User>(User::class.java,this)
        user = userSharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(Constants.KEY_SHARED_PREFERENCE_USERS)

        _backBtn.setOnClickListener(View.OnClickListener { finish() })
        _confirm.setOnClickListener(View.OnClickListener {
            val newCity = _newCity.text.toString()

            if(newCity.isNotEmpty()){
                user._UserCity = newCity

                userSharedPreferenceManager.storeSerializableObjectToSharedPreference(user, Constants.KEY_SHARED_PREFERENCE_USERS);

                val firebaseFirestoreController = FirebaseFirestoreController(User::class.java)
                firebaseFirestoreController.updateDocumentField(Constants.KEY_COLLECTION_USERS, user._UserName, "_UserCity", newCity)

            } else Toast.makeText(this, "Fill the new city", Toast.LENGTH_SHORT).show()

        })
    }
}