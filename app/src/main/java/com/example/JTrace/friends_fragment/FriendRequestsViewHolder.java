package com.example.JTrace.friends_fragment;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.JTrace.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.net.URI;

public class FriendRequestsViewHolder extends RecyclerView.ViewHolder {
    private ImageView avatarView,addView;
    private TextView authorView;
    private TextView msgView;
    public FriendRequestsViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        avatarView=itemView.findViewById(R.id.request_avatarView);
        authorView=itemView.findViewById(R.id.request_authorView);
        msgView=itemView.findViewById(R.id.request_msgView);
        addView=itemView.findViewById(R.id.request_addImageView);
    }
    //TODO: get avatar and set avatarURI
    public void bind(String author, String msg){
        //avatarView.setImageURI(avatarURI);
        authorView.setText(author);
        msgView.setText(msg);
    }
}
