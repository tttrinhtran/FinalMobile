package com.example.finalproject.Message;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Constants;
import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.FriendBio;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;
import com.example.finalproject.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private User senderUser;
    private User receiverUser;
    private List<ChatMessage> chatMessageList;
    private ChatAdapter chatAdapter;
    // private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private String conversationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        loadSenderDetails();
        loadReceiverDetails();
        setListener();
        init();
        listenMessages();
        checkText();
    }

    private void checkText() {
        // Inside onCreate method
        binding.inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed, but required by TextWatcher interface
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if there is text in the input message
                if (s.toString().trim().length() > 0) {
                    // Set the send button's background color to blue
                    binding.layoutSend.setBackgroundResource(R.drawable.background_chat_accept);
                } else {
                    // Set the send button's background color to its default color (if different)
                    binding.layoutSend.setBackgroundResource(R.drawable.background_chat_input);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void init() {
        // preferenceManager = new PreferenceManager(getApplicationContext(),Constants.CHAT_PREFERENCE);
        chatMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessageList,
                // This line should be replace later
                // getBitmapFromEncodedString("receiverUser.image"),
                receiverUser,
                senderUser.get_UserName()
                // preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, senderUser.get_UserName());
        message.put(Constants.KEY_RECEIVER_ID, receiverUser.get_UserName());
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);

        if( conversationId != null ) updateConversation(binding.inputMessage.getText().toString());
        else {
            HashMap<String, Object> conversationMap = new HashMap<>();
            conversationMap.put(Constants.KEY_SENDER_ID, senderUser.get_UserName());
            // Must be filled later
            conversationMap.put(Constants.SENDER_IMAGE, "null");
            conversationMap.put(Constants.KEY_RECEIVER_ID, receiverUser.get_UserName() );
            // Must be filled later
            conversationMap.put(Constants.RECEIVER_IMAGE, "null");
            conversationMap.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
            conversationMap.put(Constants.KEY_TIMESTAMP, new Date() );
            addConversation(conversationMap);
        }

        binding.inputMessage.setText(null);
    }

    private void  listenMessages() {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderUser.get_UserName())
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.get_UserName() )
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverUser.get_UserName())
                .whereEqualTo(Constants.KEY_RECEIVER_ID, senderUser.get_UserName())
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = ( value, error ) -> {
        if( error != null ) return;
        if( value != null ) {
            int count = chatMessageList.size();
            for(DocumentChange documentChange : value.getDocumentChanges() ) {

                if( documentChange.getType() == DocumentChange.Type.ADDED ) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.timestamp = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessageList.add(chatMessage);
                }
            }
            Collections.sort(chatMessageList, (obj1, obj2) -> obj1.timestamp.compareTo(obj2.timestamp));
            if( count == 0 ) chatAdapter.notifyDataSetChanged();
            else {
                chatAdapter.notifyItemRangeInserted(chatMessageList.size(), chatMessageList.size());
                binding.chatRecyclerView.smoothScrollToPosition( chatMessageList.size() - 1  );
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
//        binding.progressBar.setVisibility(View.GONE);
        if(conversationId == null) checkForConversation();
    };

    private Bitmap getBitmapFromEncodedString( String encodedImage ) {
        byte[] bytes = Base64.decode( encodedImage, Base64.DEFAULT );
        return BitmapFactory.decodeByteArray( bytes, 0, bytes.length );
    }

    private void loadSenderDetails() {
        SharedPreferenceManager<User> currentInstance = new SharedPreferenceManager<>(User.class, this);
        senderUser = currentInstance.retrieveSerializableObjectFromSharedPreference( Constants.KEY_SHARED_PREFERENCE_USERS );
    }

    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.RECEIVED_USER);
        binding.ChatUsername.setText(receiverUser.get_UserFirstname() + " " + receiverUser.get_UserLastname());

        FirebaseCloudStorageManager firebaseCloudStorageManager = new FirebaseCloudStorageManager();
        firebaseCloudStorageManager.FetchingImageFromFirebase(receiverUser, binding.ChatAvatar);
        binding.ChatAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    private void setListener() {
        binding.ChatBackArrow.setOnClickListener( v -> onBackPressed() );
        binding.layoutSend.setOnClickListener( v -> sendMessage());
        binding.ChatUserInfo.setOnClickListener( view -> {
            Intent intent = new Intent( getApplicationContext(), FriendBio.class );
            intent.putExtra( Constants.FRIEND_USER, receiverUser );
            startActivity(intent);
        });
    }

    private String getReadableDateTime( Date date ) {
        return new SimpleDateFormat( "MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void addConversation( HashMap<String, Object> conversation ) {

        String ID;
        if( senderUser.get_UserName().compareTo(receiverUser.get_UserName()) < 0 ) ID = senderUser.get_UserName() + "+" + receiverUser.get_UserName();
        else ID = receiverUser.get_UserName() + "+" + senderUser.get_UserName();

        database.collection(Constants.KEY_COLLECTION_CONVERSATION)
                .document(ID)
                .set(conversation)
                .addOnSuccessListener( documentReference -> conversationId = ID );
    }

    private void updateConversation( String message ) {
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSATION).document(conversationId);
        documentReference.update(
                Constants.KEY_SENDER_ID, senderUser.get_UserName(),
                Constants.KEY_RECEIVER_ID, receiverUser.get_UserName(),
                Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date()
        );
    }

    private void checkForConversation() {
        if( chatMessageList.size() != 0 ) {
            checkForConversationRemotely( senderUser.get_UserName(), receiverUser.get_UserName() );
            checkForConversationRemotely( receiverUser.get_UserName(), senderUser.get_UserName() );
        }
    }
    private void checkForConversationRemotely( String senderId, String receiverId ) {
        database.collection(Constants.KEY_COLLECTION_CONVERSATION)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversationOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversationOnCompleteListener = task -> {
        if( task.isSuccessful() && task.getResult() != null && task.getResult().getDocumentChanges().size() > 0 ) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversationId = documentSnapshot.getId();
        }
    };
}