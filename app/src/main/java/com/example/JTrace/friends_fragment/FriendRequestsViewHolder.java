package com.example.JTrace.friends_fragment;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.JTrace.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.net.URI;

public class FriendRequestsViewHolder extends RecyclerView.ViewHolder {
    //TODO:为了在adapter中使用，去耦方便，这里都设成了public，代码不优雅
    public ImageView avatarView,addView,declineView;
    public TextView authorView;
    public TextView msgView;
    public LinearLayout mainLayout;
    public FriendRequestsViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        avatarView=itemView.findViewById(R.id.request_avatarView);
        authorView=itemView.findViewById(R.id.request_authorView);
        msgView=itemView.findViewById(R.id.request_msgView);
        addView=itemView.findViewById(R.id.request_addImageView);
        declineView=itemView.findViewById(R.id.request_declineImageView);
        mainLayout=itemView.findViewById(R.id.request_mainLayout);
    }
    //TODO: get avatar and set avatarURI
    public void bind(String author, String msg){
        //avatarView.setImageURI(avatarURI);
        authorView.setText(author);
        msgView.setText(msg);
    }
}
