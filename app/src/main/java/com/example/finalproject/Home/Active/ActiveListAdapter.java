package com.example.finalproject.Home.Active;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.R;
import com.example.finalproject.User;

import java.util.ArrayList;

public class ActiveListAdapter extends RecyclerView.Adapter<ActiveListAdapter.MyViewHolder> {

    ArrayList<User> activeList;
    Context context;

    public ActiveListAdapter(ArrayList<User> activeList, Context context)
    {
        this.context=context;
        this.activeList=activeList;
    }
    @NonNull
    @Override
    public ActiveListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.homescreen_active_item, parent,false);
        return new ActiveListAdapter.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ActiveListAdapter.MyViewHolder holder, int position) {
        holder.name.setText(activeList.get(position).get_UserFirstname());

        // holder.name.setIma
        if(activeList.get(position).is_UserActive() == true) {
            holder.status.setCardBackgroundColor(ContextCompat.getColor(context, R.color.active_friends));
        }
        else
            holder.status.setCardBackgroundColor(ContextCompat.getColor(context, R.color.secondary_text));

        FirebaseCloudStorageManager firebaseCloudStorageManager = new FirebaseCloudStorageManager();
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        firebaseCloudStorageManager.FetchingImageFromFirebase(activeList.get(position), holder.img);

    }

    @Override
    public int getItemCount() {
        if(activeList != null)
            return activeList.size();
        else
            return 0;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView name;
        CardView status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.homeScreenActiveImg);
            name=itemView.findViewById(R.id.homeScreenActiveName);
            status=itemView.findViewById(R.id.homeScreenActiveStatus);

        }
    }
}
