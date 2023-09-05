package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Listeners.UserListener;
import com.example.finalproject.databinding.ItemContainerUserBinding;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> users;
    private final UserListener userListener;

    public UserAdapter( List<User> users, UserListener userListener ) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position ) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() { return users.size(); }

    class UserViewHolder extends RecyclerView.ViewHolder {

        ItemContainerUserBinding binding;

        UserViewHolder( ItemContainerUserBinding itemContainerUserBiding ) {
            super(itemContainerUserBiding.getRoot());
            binding = itemContainerUserBiding;
        }

        void setUserData( User user ) {
            binding.textName.setText(user._UserName);
            binding.textEmail.setText(user._UserEmail);
//            biding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.getRoot().setOnClickListener( v -> userListener.onUserClicker(user));
        }
    }

    private Bitmap getUserImage(String encodedImage ) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }
}
