package com.example.easytalk.board_fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.easytalk.R;

public class MessageDetailActivity extends AppCompatActivity {

    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        Intent intent=getIntent();
        String content=intent.getStringExtra("content");
        String author=intent.getStringExtra("author");
        String date=intent.getStringExtra("date");
        mTextView=getWindow().getDecorView().findViewById(R.id.ContentDetail);
        String tmp=author+"于"+date+"说："+content;
        mTextView.setText(tmp);
    }
}