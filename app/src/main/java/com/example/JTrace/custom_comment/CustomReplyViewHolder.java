package com.example.JTrace.custom_comment;

import android.view.View;
import android.widget.TextView;

import com.example.JTrace.board_fragment.SampleCircleImageView;
import com.example.JTrace.widget.RoundImageView;
import com.jidcoo.android.widget.commentview.view.ViewHolder;
import com.example.JTrace.R;

public class CustomReplyViewHolder extends ViewHolder {
    public TextView userName, prizes, reply;
    public TextView date_reply;
    public RoundImageView ico;

    public CustomReplyViewHolder(View view) {
        super(view);
        userName = view.findViewById(R.id.reply_item_userName);
        prizes = view.findViewById(R.id.prizes);
        reply = view.findViewById(R.id.reply_item_content);
        date_reply = view.findViewById(R.id.reply_item_time);
        ico = view.findViewById(R.id.ico);
    }
}
