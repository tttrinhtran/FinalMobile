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
import com.example.finalproject.databinding.ItemChatListBinding;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> users;
    private final UserListener userListener;

    public UserAdapter( List<User> users, UserListener userListener ) {
        this.users = users;
        this.userListener = userListener;
        for( User user : users ) Log.d( "Testing", "UserAdapter: " + user._UserName );
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
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() { return users.size(); }

    class UserViewHolder extends RecyclerView.ViewHolder {

        ItemChatListBinding binding;

        UserViewHolder( ItemChatListBinding itemChatListBinding ) {
            super(itemChatListBinding.getRoot());
            binding = itemChatListBinding;
        }

        void setUserData( User user ) {
//            biding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.ItemChatFriendName.setText(user._UserName);
            // Just for testing
            binding.ItemChatContent.setText(user._UserEmail);
            binding.getRoot().setOnClickListener( v -> userListener.onUserClicker(user));
            Log.d( "Adapter", "setUserData: " + user._UserName );
        }
    }

    private Bitmap getUserImage(String encodedImage ) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }
}
