package com.example.finalproject.Section;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.ArrayList;

public class sectionListAdap  extends RecyclerView.Adapter<sectionListAdap.MyViewHolder> {
    ArrayList<SectionModel> sectionList;
    Context context;
    sectionListInterface sectionListInterface;

    public sectionListAdap(ArrayList<SectionModel> sectionList, Context context, sectionListInterface sectionListInterface)
    {
        this.sectionList=sectionList;
        this.context=context;
        this.sectionListInterface=sectionListInterface;

    }

    @NonNull
    @Override
    public sectionListAdap.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_section_list,parent,false);
        return new sectionListAdap.MyViewHolder(view, sectionListInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull sectionListAdap.MyViewHolder holder, int position) {
        holder.tvDate.setText(sectionList.get(position).get_SectionDate());
        holder.tvHour.setText(sectionList.get(position).get_SectionHour());
        holder.tvName.setText(sectionList.get(position).get_SectionName());

    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvHour;
        TextView tvDate;

//        ShapeableImageView img;
        public MyViewHolder(@NonNull View itemView, com.example.finalproject.Section.sectionListInterface sectionListInterface) {
            super(itemView);
            tvName=itemView.findViewById(R.id.SectionNameItemRecycler);
            tvHour=itemView.findViewById(R.id.SectionTimeItemRecycler);
            tvDate=itemView.findViewById(R.id.SectionDateItemRecycler);
        }
    }
}
