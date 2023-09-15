package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Home.HomeScreen;
import com.example.finalproject.Listeners.UserListener;
import com.example.finalproject.Message.ChatActivity;
import com.example.finalproject.Message.ChatMessage;
import com.example.finalproject.databinding.ActivityFriendsScreenBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FriendsScreen extends AppCompatActivity implements UserListener {

    private ActivityFriendsScreenBinding binding;
    private List<String> friendList;
    private List<ChatMessage> conversations;
    private User currentUser;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendsScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        getUser();
        listenConversation();
        navBar();
    }

    private void setListener() {

    }

    private void getUser() {

        SharedPreferenceManager<User> currentInstance = new SharedPreferenceManager<>(User.class, this);
        currentUser = currentInstance.retrieveSerializableObjectFromSharedPreference( Constants.KEY_SHARED_PREFERENCE_USERS );

        friendList = currentUser.get_UserFriend();
        conversations = new ArrayList<>();

         userAdapter = new UserAdapter( conversations, currentUser, this );
         binding.FriendScreenChat.setAdapter(userAdapter);
         binding.FriendScreenChat.setVisibility(View.VISIBLE);

    }

    private void showErrorMessage() {
    }

    private void navBar(){
        ImageView home;
        ImageView section;
        ImageView friend;
        ImageView profile;


        home = findViewById(R.id.NaviBarHomeIcon);
        section= findViewById(R.id.NaviBarSectionIcon);
        friend= findViewById(R.id.NaviBarFriendIcon); friend.setImageResource(R.drawable.friend_icon_fill);
        profile = findViewById(R.id.NaviBarProfile);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onUserClicker(User user) {
        Intent intent = new Intent( getApplicationContext(), ChatActivity.class );
        intent.putExtra( Constants.RECEIVED_USER, user );
        startActivity(intent);
        // finish();
    }

    private void listenConversation() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_CONVERSATION)
                .whereEqualTo(Constants.KEY_SENDER_ID, currentUser.get_UserName() )
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATION)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, currentUser.get_UserName() )
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = ( value, error ) -> {
        if( error != null || value == null ) return;
        for( DocumentChange documentChange : value.getDocumentChanges() ) {
            if( documentChange.getType() == DocumentChange.Type.ADDED ) {
                Log.d("Hoktro", "Added");
                String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.senderId = senderId;
                chatMessage.receiverId = receiverId;
                if( currentUser.get_UserName().equals(senderId) ) {
                    chatMessage.conversationImage = documentChange.getDocument().getString(Constants.RECEIVER_IMAGE);
                    chatMessage.conversationId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                }
                else {
                    chatMessage.conversationImage = documentChange.getDocument().getString(Constants.SENDER_IMAGE);
                    chatMessage.conversationId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                }
                chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                chatMessage.timestamp = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                conversations.add(chatMessage);
            }
            else if ( documentChange.getType() == DocumentChange.Type.MODIFIED ) {
                Log.d("Hoktro", "Modified");
                for( int i = 0; i < conversations.size(); ++i ) {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    if( conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId) ) {
                        conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                        conversations.get(i).timestamp = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                        break;
                    }
                }
            }
        }
        Collections.sort( conversations, ( obj1, obj2 ) -> obj2.timestamp.compareTo(obj1.timestamp) );
        userAdapter.notifyDataSetChanged();
        binding.FriendScreenChat.smoothScrollToPosition(0);
        binding.FriendScreenChat.setVisibility(View.VISIBLE);
    };

}