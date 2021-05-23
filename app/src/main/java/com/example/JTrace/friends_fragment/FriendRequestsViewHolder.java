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
    public ImageView avatarView,addView,declineView,respondedView;
    public TextView authorView;
    public TextView msgView;
    public LinearLayout mainLayout;
    private String fromUsr;
    private String content;
    private int Status;
    public FriendRequestsViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        avatarView=itemView.findViewById(R.id.request_avatarView);
        authorView=itemView.findViewById(R.id.request_authorView);
        msgView=itemView.findViewById(R.id.request_msgView);
        addView=itemView.findViewById(R.id.request_addImageView);
        declineView=itemView.findViewById(R.id.request_declineImageView);
        mainLayout=itemView.findViewById(R.id.request_mainLayout);
        respondedView=itemView.findViewById(R.id.request_respondedImageView);
    }
    //TODO: get avatar and set avatarURI
    public void bind(String author, String msg,int status){
        //avatarView.setImageURI(avatarURI);
        fromUsr=author;
        content=msg;
        authorView.setText(author);
        msgView.setText(msg);
        Status=status;
        if(Status!=0){
            respondedView.setVisibility(View.VISIBLE);
            addView.setVisibility(View.INVISIBLE);
            addView.setClickable(false);
            declineView.setVisibility(View.INVISIBLE);
            declineView.setClickable(false);
        }else{
            respondedView.setVisibility(View.INVISIBLE);
            addView.setVisibility(View.VISIBLE);
            addView.setClickable(true);
            declineView.setVisibility(View.VISIBLE);
            declineView.setClickable(true);
        }
    }
    public String getFromUsr(){return fromUsr;}
    public String getContent(){return content;}
    public int getStatus(){return Status;}
}
