package com.example.gupshup.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gupshup.ChatActivity;
import com.example.gupshup.R;
import com.example.gupshup.model.ChatMessageModel;
import com.example.gupshup.model.UserModel;
import com.example.gupshup.utils.AndroidUtil;
import com.example.gupshup.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder> {

    Context context;
    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder userModelViewHolder, int i, @NonNull ChatMessageModel model) {

        if(model.getSenderId().equals(FirebaseUtil.currentUserId())){
            userModelViewHolder.leftChatLayout.setVisibility(View.GONE);
            userModelViewHolder.rightChatLayout.setVisibility(View.VISIBLE);
            userModelViewHolder.rightChatTextview.setText(model.getMessage());


            //userModelViewHolder.textSendTime.setText(FirebaseUtil.timestampToString(model.getTimestamp()));
        }else{
            userModelViewHolder.rightChatLayout.setVisibility(View.GONE);
            userModelViewHolder.leftChatLayout.setVisibility(View.VISIBLE);
            userModelViewHolder.leftChatTextview.setText(model.getMessage());

            //userModelViewHolder.textReceiveTime.setText(FirebaseUtil.timestampToString(model.getTimestamp()));
        }
    }


    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row,parent,false);
        return new ChatModelViewHolder(view);
    }

    class ChatModelViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftChatLayout,rightChatLayout;
        TextView leftChatTextview,rightChatTextview;
        TextView textSendTime,textReceiveTime;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
            //textSendTime = itemView.findViewById(R.id.text_Send_Time);
            //textReceiveTime = itemView.findViewById(R.id.text_Receive_Time);
        }
    }
}
