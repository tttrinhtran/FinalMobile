package com.example.finalproject.Message.Chatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.ArrayList;

public class ChatbotAdapter extends RecyclerView.Adapter<ChatbotAdapter.ChatbotViewHolder> {

    private Context context;
    private ArrayList<String> messages;


    public ChatbotAdapter(Context context, ArrayList<String> hobbies) {
        this.context = context;
        this.messages = hobbies;
    }

    public static class ChatbotViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        CardView cardViewList;
        boolean choose;
        public ChatbotViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.chatbotResponseMessage);
            cardViewList = itemView.findViewById(R.id.chatpotResponseCardview);
            choose = false;
        }
    }

    @NonNull
    @Override
    public ChatbotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatbot_response_cardview, parent, false);
        return new ChatbotViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatbotViewHolder holder, int position) {
        String mess = messages.get(position);
        holder.message.setText(mess);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
