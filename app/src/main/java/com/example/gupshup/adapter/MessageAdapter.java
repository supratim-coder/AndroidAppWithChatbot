package com.example.gupshup.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gupshup.R;
import com.example.gupshup.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{

    List<Message> messageList;
    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null);
        MyViewHolder myViewHolder = new MyViewHolder(chatView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message = messageList.get(position);
        if(message.getSentBy().equals(Message.SENT_BY_ME)){
            holder.gptLeftChatView.setVisibility(View.GONE);
            holder.gptRightChatView.setVisibility(View.VISIBLE);
            holder.gptRightTextView.setText(message.getMessage());
        }else{
            holder.gptRightChatView.setVisibility(View.GONE);
            holder.gptLeftChatView.setVisibility(View.VISIBLE);
            holder.gptLeftTextView.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() { 
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout gptLeftChatView,gptRightChatView;
        TextView gptLeftTextView,gptRightTextView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            gptLeftChatView = itemView.findViewById(R.id.gpt_left_chat_layout);
            gptRightChatView = itemView.findViewById(R.id.gpt_right_chat_layout);
            gptLeftTextView = itemView.findViewById(R.id.gpt_left_chat_textview);
            gptRightTextView = itemView.findViewById(R.id.gpt_right_chat_textview);
        }
    }
}
