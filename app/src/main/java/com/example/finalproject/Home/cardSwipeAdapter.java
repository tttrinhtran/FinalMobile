package com.example.finalproject.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.Listeners.CardListener;
import com.example.finalproject.R;
import com.example.finalproject.User;
import com.example.finalproject.databinding.ItemSwipeBinding;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class cardSwipeAdapter extends RecyclerView.Adapter<cardSwipeAdapter.MyViewHolder>{

    private ArrayList<User> swipeList;
    private CardListener listener;

    Context context;
    public cardSwipeAdapter( ArrayList<User> list, CardListener listener ) {
        this.swipeList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemSwipeBinding itemSwipeBinding = ItemSwipeBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new MyViewHolder(itemSwipeBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.setData(swipeList.get(position));

    }

    @Override
    public int getItemCount() {
        return swipeList.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemSwipeBinding binding;

        MyViewHolder( ItemSwipeBinding itemSwipeBinding ) {
            super(itemSwipeBinding.getRoot());
            binding = itemSwipeBinding;
        }
        void setData(User data) {

            binding.HomeScreenUserName.setText(data.get_UserLastname() + " " + data.get_UserFirstname());
            binding.HomeScreenUserAge.setText(data.get_UserAge());
            binding.HomeScreenUserSchool.setText(data.get_UserSchool());
            binding.HomeScreenUserHobbies.setText(data.hobbieArrayListToString());
            binding.getRoot().setOnClickListener( view -> listener.onCardClicker(data) );

            FirebaseCloudStorageManager firebaseCloudStorageManager = new FirebaseCloudStorageManager();
            binding.SwipeItemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            firebaseCloudStorageManager.FetchingImageFromFirebase( data, binding.SwipeItemImage );
        }
    }


}
