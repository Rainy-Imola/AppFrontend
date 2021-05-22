package com.example.JTrace.board_fragment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.JTrace.R;
import com.example.JTrace.model.comment;

public class commentViewHolder extends RecyclerView.ViewHolder {
    private comment comment;
    private TextView authorView,contentView;
    public commentViewHolder(@NonNull View itemView) {
        super(itemView);
        authorView=itemView.findViewById(R.id.author_view);
        contentView=itemView.findViewById(R.id.comment_content_textview);
    }
    public void bind(comment c){
        this.comment=c;
        authorView.setText(this.comment.getAuthor());
        contentView.setText(this.comment.getContent());
    }
}
