package com.example.finalproject.Message.Chatbot

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.TextView.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.Constants
import com.example.finalproject.FirebaseFirestoreController
import com.example.finalproject.Position
import com.example.finalproject.R
import com.example.finalproject.SharedPreferenceManager
import com.example.finalproject.User
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.Executors


class ChatbotActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    lateinit var sender: User
    lateinit var chatbot: User

    // creating variables on below line.
    lateinit var sendButton: FrameLayout
    lateinit var displayScreen: RecyclerView
    lateinit var queryEdt: EditText
    lateinit var chatbotName:TextView

    lateinit var messageArray: ArrayList<String>
    lateinit var chatAdapter: ChatbotAdapter

    var url = "https://api.openai.com/v1/completions"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // initializing variables on below line.
        sendButton = findViewById(R.id.layoutSend)
        displayScreen = findViewById(R.id.chatRecyclerView)
        queryEdt = findViewById(R.id.inputMessage)
        chatbotName = findViewById(R.id.ChatUsername)

        chatbotName.setText("chatbot")

        val userSharedPreference = SharedPreferenceManager<User>(User::class.java,this)
        sender = userSharedPreference.retrieveSerializableObjectFromSharedPreference(Constants.KEY_SHARED_PREFERENCE_USERS)

        chatbot = User()
        chatbot._UserName = "chatbot"

        messageArray = ArrayList()

        chatAdapter = ChatbotAdapter(
            this,
            messageArray
        )
        displayScreen.adapter = chatAdapter
        displayScreen.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        displayScreen.visibility = VISIBLE

        // adding editor action listener for edit text on below line.
        sendButton.setOnClickListener(View.OnClickListener { v ->
                // setting response tv on below line.
                // responseTV.text = "Please wait.."
                // validating text
                if (queryEdt.text.toString().length > 0) {
                    messageArray.add(queryEdt.text.toString())
                    chatAdapter.notifyDataSetChanged()
                    val quest = queryEdt.text.toString()
                    queryEdt.setText("")
                    // calling get response to get the response.
                    getResponse(quest)
                } else {
                    Toast.makeText(this, "Please enter your query.", Toast.LENGTH_SHORT).show()
                }
        })
    }

    private fun getResponse(query: String) {

        val firestoreController = FirebaseFirestoreController<String>(String::class.java)
        val chatgptListAPI = firestoreController.retrieveAllDocumentsIDOfaCollection("ChatgptAPI")
        val chatgptAPI = chatgptListAPI[0]
        val chatgptUrl = "https://api.openai.com/v1/chat/completions"

        val requestBody = """
            {
                "model": "gpt-3.5-turbo",
                "messages": [{"role": "user", "content": "$query"}],
                "max_tokens": 500,
                "temperature": 0
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(chatgptUrl)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $chatgptAPI")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                if(body != null){
                    Log.d("ChatGPT", body)
                }
                var jsonObject = JSONObject(body)
                val jsonArray = jsonObject.getJSONArray("choices")
                val result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content")

                val handler = Handler(Looper.getMainLooper())
                val executorService = Executors.newSingleThreadExecutor()

                executorService.execute(Runnable {
                    messageArray.add(result)
                    handler.post(Runnable {
                        chatAdapter.notifyDataSetChanged()
                    })
                })
            }
        })
    }
}