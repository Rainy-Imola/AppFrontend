package com.example.JTrace.custom_comment;

import android.view.View;
import android.widget.TextView;

import com.example.JTrace.board_fragment.SampleCircleImageView;
import com.example.JTrace.R;
import com.example.JTrace.widget.RoundImageView;

public class CustomCommentViewHolder {
    public TextView userName, prizes, comment, date_comment;
    public RoundImageView ico;

    public CustomCommentViewHolder(View view) {
        userName = view.findViewById(R.id.user);
        prizes = view.findViewById(R.id.prizes);
        comment = view.findViewById(R.id.data);
        ico = view.findViewById(R.id.ico);
        date_comment = view.findViewById(R.id.date_comment);
    }
}
