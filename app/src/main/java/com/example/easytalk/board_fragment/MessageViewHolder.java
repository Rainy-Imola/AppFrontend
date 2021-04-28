package com.example.easytalk.board_fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytalk.R;

public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView mTextView;
    public MessageViewHolder(@NonNull View itemView){
        super(itemView);
        mTextView=itemView.findViewById(R.id.OverallMessage);

    }
    public void bind(String author,String content){
        String tmp=author+" 说："+content;
        mTextView.setText(tmp);
    }


    @Override
    public void onClick(View v) {
        Log.d("debug","onClickCalled");
        //Intent intent=new Intent(v.getContext(),)
    }
}
