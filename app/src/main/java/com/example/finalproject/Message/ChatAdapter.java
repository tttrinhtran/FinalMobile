package com.example.finalproject.Message;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.User;
import com.example.finalproject.databinding.ItemContainerReceivedMessageBinding;
import com.example.finalproject.databinding.ItemContainerSentMessageBinding;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessage;
    private final User receiverUser;
    private final String senderID;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<ChatMessage> chatMessage, User receiverUser, String senderID) {
        this.chatMessage = chatMessage;
        this.receiverUser = receiverUser;
        this.senderID = senderID;
    }

//    public ChatAdapter(List<ChatMessage> chatMessage, String senderID) {
//        this.chatMessage = chatMessage;
//        this.receiverProfileImage = null;
//        this.senderID = senderID;
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( viewType == VIEW_TYPE_SENT ) {
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from((parent.getContext())),
                            parent,
                            false
                    )
            );
        }
        else {
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from((parent.getContext())),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if( getItemViewType(position) == VIEW_TYPE_SENT ) ((SentMessageViewHolder) holder).setData(chatMessage.get(position));
        else ((ReceivedMessageViewHolder) holder).setData(chatMessage.get(position), receiverUser );
    }

    @Override
    public int getItemCount() {
        return chatMessage.size();
    }

    @Override
    public int getItemViewType(int position) {
        if( chatMessage.get(position).senderId.equals(senderID)) return VIEW_TYPE_SENT;
        return VIEW_TYPE_RECEIVED;
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding ) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData( ChatMessage chatMessage ) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            binding.textDateTime.setVisibility(View.GONE);

            binding.getRoot().setOnClickListener( v -> {
                Log.d( "ChatAdapter", "setData: " + binding.textDateTime.getText() + " " + binding.textDateTime.getVisibility() );
                if( binding.textDateTime.getVisibility() == View.VISIBLE ) binding.textDateTime.setVisibility(View.GONE);
                else binding.textDateTime.setVisibility(View.VISIBLE);
            });
        }

    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private final  ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        void setData(ChatMessage chatMessage, User receiverUser ) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            // binding.imageProfile.setImageBitmap(receiverProfileImage);
            binding.textDateTime.setVisibility(View.GONE);

            FirebaseCloudStorageManager firebaseCloudStorageManager = new FirebaseCloudStorageManager();
            binding.imageProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
            firebaseCloudStorageManager.FetchingImageFromFirebase( receiverUser, binding.imageProfile );

            binding.getRoot().setOnClickListener( v -> {
                Log.d( "ChatAdapter", "setData: " + binding.textDateTime.getText() + " " + binding.textDateTime.getVisibility() );
                if( binding.textDateTime.getVisibility() == View.VISIBLE ) binding.textDateTime.setVisibility(View.GONE);
                else binding.textDateTime.setVisibility(View.VISIBLE);
            });
        }
    }
}
