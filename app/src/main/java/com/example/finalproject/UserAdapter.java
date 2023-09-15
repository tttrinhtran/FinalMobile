package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Listeners.UserListener;
import com.example.finalproject.Message.ChatMessage;
import com.example.finalproject.databinding.ItemChatListBinding;

import java.util.List;
import java.util.Objects;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<ChatMessage> conversations;
    private final User currentUser;
    private final UserListener userListener;

    public UserAdapter( List<ChatMessage> conversations, User currentUser, UserListener userListener ) {
        this.conversations = conversations;
        this.currentUser = currentUser;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ) {
        ItemChatListBinding itemChatListBinding = ItemChatListBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemChatListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position ) {

        ChatMessage currentConversation = conversations.get(position);
        Log.d("Hoktro", "onBindViewHolder: " + position);
        FirebaseFirestoreController<User> currentInstance = new FirebaseFirestoreController<>(User.class);
        User receiverUser;
        int state = 0;
        if(!Objects.equals(currentUser.get_UserName(), currentConversation.senderId)) state = 1;

        if(Objects.equals(currentConversation.senderId, currentUser.get_UserName())) receiverUser = currentInstance.retrieveObjectsFirestoreByID(Constants.KEY_COLLECTION_USERS, currentConversation.receiverId);
        else receiverUser = currentInstance.retrieveObjectsFirestoreByID(Constants.KEY_COLLECTION_USERS, currentConversation.senderId);

        holder.setUserData( receiverUser, currentConversation.message, state );
    }

    @Override
    public int getItemCount() {
        // Log.d("Hoktro", "getItemCount: " + conversations.size());
        return conversations.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        ItemChatListBinding binding;

        UserViewHolder( ItemChatListBinding itemChatListBinding ) {
            super(itemChatListBinding.getRoot());
            binding = itemChatListBinding;
        }

        void setUserData( User user, String lastMessage, int state ) {
//            biding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.ItemChatFriendName.setText(user._UserFirstname + " " + user._UserLastname);
            if( state == 0 ) lastMessage = "You: " + lastMessage;
            binding.ItemChatContent.setText(lastMessage);
            binding.getRoot().setOnClickListener( v -> userListener.onUserClicker(user));
            Log.d( "Adapter", "setUserData: " + user._UserName );
        }
    }

    private Bitmap getUserImage(String encodedImage ) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }
}
