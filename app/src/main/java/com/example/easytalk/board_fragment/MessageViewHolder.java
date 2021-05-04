package com.example.easytalk.board_fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytalk.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView contentTextView,authorTextView,dateTextView;
    private String author,content,date;
    public MessageViewHolder(@NonNull View itemView){
        super(itemView);
        contentTextView=itemView.findViewById(R.id.msg_content);
        authorTextView=itemView.findViewById(R.id.msg_author);
        dateTextView=itemView.findViewById(R.id.msg_date);
        itemView.setOnClickListener(this);
    }
    public void bind(String author, String content, Date date){
        this.author=author;
        this.content=content;
        SimpleDateFormat format=new SimpleDateFormat( "yyyy-MM-dd HH-mm-ss");
        this.date=format.format(date);


        contentTextView.setText(this.content);
        authorTextView.setText("author:"+this.author);
        dateTextView.setText(this.date);
    }


    @Override
    public void onClick(View v) {
        Log.d("ClickEvent","onClick called");
        Intent intent=new Intent(v.getContext(),MessageDetailActivity.class);
        intent.putExtra("author",author);
        intent.putExtra("content",content);
        intent.putExtra("date",date);
        v.getContext().startActivity(intent);
    }
}
