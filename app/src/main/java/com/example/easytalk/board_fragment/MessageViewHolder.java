package com.example.easytalk.board_fragment;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytalk.R;
import com.example.easytalk.model.message;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView contentTextView,authorTextView,dateTextView;
    private SimpleDraweeView coverView;
    private message msg;
    private String author,content,date,cover;
    public MessageViewHolder(@NonNull View itemView){
        super(itemView);
        contentTextView=itemView.findViewById(R.id.msg_content);
        authorTextView=itemView.findViewById(R.id.msg_author);
        dateTextView=itemView.findViewById(R.id.msg_date);
        coverView=itemView.findViewById(R.id.sd_cover);
        itemView.setOnClickListener(this);
    }
    public void bind(message msg){
        this.author=msg.getAuthor();
        this.content=msg.getContent();
        SimpleDateFormat format=new SimpleDateFormat( "yyyy-MM-dd HH-mm-ss");
        this.date=format.format(msg.getCreatedAt());
        this.cover=msg.getImageUrl();
        this.msg=msg;

        contentTextView.setText(this.content);
        authorTextView.setText("author:"+this.author);
        dateTextView.setText(this.date);
        coverView.setImageURI(this.cover);
    }


    @Override
    public void onClick(View v) {
        Log.d("ClickEvent","onClick called");
        Intent intent=new Intent(v.getContext(),MessageDetailActivity.class);
        intent.putExtra("message", msg);
        v.getContext().startActivity(intent);
    }
}
