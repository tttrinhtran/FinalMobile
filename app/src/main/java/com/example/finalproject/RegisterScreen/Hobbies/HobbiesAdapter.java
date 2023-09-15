package com.example.finalproject.RegisterScreen.Hobbies;

import static com.example.finalproject.R.drawable.round_solid_btn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.io.Serializable;
import java.util.ArrayList;


public class HobbiesAdapter extends RecyclerView.Adapter<HobbiesAdapter.HobbieViewHolder> implements HobbiesSelectListenerInterface, Serializable {

    private Context context;
    private ArrayList<String> hobbies;
    private HobbiesSelectListenerInterface listenerInterface;


    public HobbiesAdapter(Context context, ArrayList<String> hobbies, HobbiesSelectListenerInterface listenerInterface) {
        this.context = context;
        this.hobbies = hobbies;
        this.listenerInterface = listenerInterface;
    }

    @Override
    public void onHobbiesClicked(int hobbieAdapter) {
    }


    public static class HobbieViewHolder extends RecyclerView.ViewHolder {
        TextView hobbiesText;
        CardView cardViewList;
        LinearLayout cardLayout;
        boolean choose;
        public HobbieViewHolder(@NonNull View itemView, HobbiesSelectListenerInterface hobbiesSelectListenerInterface) {
            super(itemView);

            hobbiesText = itemView.findViewById(R.id.HobbiesCardviewText);
            cardViewList = itemView.findViewById(R.id.HobbiesCardviewContainer);
            cardLayout = itemView.findViewById(R.id.HobbiesCardLayout);
            choose = false;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(hobbiesSelectListenerInterface != null){
                        int pos = getAbsoluteAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            hobbiesSelectListenerInterface.onHobbiesClicked(pos);
//                            hobbiesText.setBackgroundColor(R.drawable.btn_animation);
                            if(choose == true){
                                cardViewList.setBackgroundResource(R.drawable.round_outline_btn);
                                choose = false;
                            }
                            else if(choose == false){
                                cardViewList.setBackgroundResource(R.drawable.border_full);
                                choose = true;
                            }
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public HobbiesAdapter.HobbieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hobbies_cardview, parent, false);
        return new HobbieViewHolder(view, listenerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HobbieViewHolder holder, int position) {
        String hobbie = hobbies.get(position);
        holder.hobbiesText.setText(hobbie);
    }

    @Override
    public int getItemCount() {
        return hobbies.size();
    }
}
