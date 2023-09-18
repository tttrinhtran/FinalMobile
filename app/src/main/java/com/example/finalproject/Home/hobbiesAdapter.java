package com.example.finalproject.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Home.Active.ActiveListAdapter;
import com.example.finalproject.R;

import java.util.ArrayList;

public class hobbiesAdapter extends RecyclerView.Adapter<hobbiesAdapter.MyViewHolder> {
    //this one not clickable
    ArrayList<String> hobbiesList;
    Context context;
    public hobbiesAdapter(ArrayList<String> hobbiesList, Context context)
    {
        this.hobbiesList=hobbiesList;
        this.context=context;
    }
    @NonNull
    @Override
    public hobbiesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hobbies_cardview, parent,false);
        return new hobbiesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull hobbiesAdapter.MyViewHolder holder, int position) {
        holder.text.setText(hobbiesList.get(position));


    }

    @Override
    public int getItemCount() {
        return hobbiesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.HobbiesCardviewText);
        }
    }
}
