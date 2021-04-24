package com.example.easytalk.board_fragment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytalk.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private TextView mTextView;
    public MessageViewHolder(@NonNull View itemView){
        super(itemView);
        mTextView=itemView.findViewById(R.id.messageContent);

    }
    public void bind(String content){
        mTextView.setText(content);
    }

}
