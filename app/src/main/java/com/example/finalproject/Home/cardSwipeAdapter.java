package com.example.finalproject.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.User;

import java.util.ArrayList;

public class cardSwipeAdapter extends RecyclerView.Adapter<cardSwipeAdapter.MyViewHolder> {

    private ArrayList<User> swipeList;

    Context context;
    public cardSwipeAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.swipeList = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_swipe, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.setData(swipeList.get(position));

    }

    @Override
    public int getItemCount() {
        if(swipeList != null)
            return swipeList.size() ;
        else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
//        ShapeableImageView img;
        TextView tvSchool;
        TextView tvName;
        TextView tvAge;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            img=itemView.findViewById(R.id.SwipeItemImage);
            tvName= itemView.findViewById(R.id.HomeScreenUserName);
            tvAge= itemView.findViewById(R.id.HomeScreenUserAge);
            tvSchool= itemView.findViewById(R.id.HomeScreenUserSchool);


        }
        void setData(User data) {

            tvName.setText(data.get_UserName());
            tvAge.setText(data.get_UserAge());
            tvSchool.setText(data.get_UserSchool());
        }
    }
}
