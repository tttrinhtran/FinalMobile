package com.example.finalproject.Message;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Constants;
import com.example.finalproject.PreferenceManager;
import com.example.finalproject.User;
import com.example.finalproject.databinding.ActivityChatBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private User receiverUser;
    private List<ChatMessage> chatMessageList;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        loadReceiverDetails();
        init();
        listenMessages();
    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessageList,
                // This line should be replace later
                getBitmapFromEncodedString("receiverUser.image"),
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiverUser.get_UserName());
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        binding.inputMessage.setText(null);
    }

    private void  listenMessages() {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.get_UserName() )
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverUser.get_UserName())
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = ( value, error ) -> {
        if( error != null ) return;
        if( value != null ) {
            int count = chatMessageList.size();
            for(DocumentChange documentChange : value.getDocumentChanges() ) {

                if( documentChange.getType() == DocumentChange.Type.ADDED ) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderID = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverID = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessageList.add(chatMessage);
                }
            }
            Collections.sort(chatMessageList, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if( count == 0 ) chatAdapter.notifyDataSetChanged();
            else {
                chatAdapter.notifyItemRangeInserted(chatMessageList.size(), chatMessageList.size());
                binding.chatRecyclerView.smoothScrollToPosition( chatMessageList.size() - 1  );
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
    };

    private Bitmap getBitmapFromEncodedString( String encodedImage ) {
        byte[] bytes = Base64.decode( encodedImage, Base64.DEFAULT );
        return BitmapFactory.decodeByteArray( bytes, 0, bytes.length );
    }

    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.get_UserName());

    }

    private void setListener() {
        binding.imageBack.setOnClickListener( v -> onBackPressed() );
        binding.layoutSend.setOnClickListener( v -> sendMessage() );
    }

    private String getReadableDateTime( Date date ) {
        return new SimpleDateFormat( "MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
}