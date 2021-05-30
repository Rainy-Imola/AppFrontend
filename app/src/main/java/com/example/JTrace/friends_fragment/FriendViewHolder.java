package com.example.JTrace.friends_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.JTrace.R;
import com.example.JTrace.widget.RoundImageView;

public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView nameEditor;
    private TextView statusEditor;
    private RoundImageView avatar;
    private int id;
    private String name;
    private String image;
    private int status;
    private Context mContext;
    private View view;

    public FriendViewHolder(@NonNull View itemView, Context mContext) {
        super(itemView);
        this.mContext = mContext;
        nameEditor = (TextView) itemView.findViewById(R.id.name);
        statusEditor = (TextView) itemView.findViewById(R.id.status);
        avatar = (RoundImageView) itemView.findViewById(R.id.image_avatar);
        view = itemView;
        itemView.setOnClickListener(this);
    }

    @SuppressLint("ResourceType")
    public void bind(int id, String name, String image, int status) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        nameEditor.setText(name);
        if (image != null && image.length() > 0) {
            Glide.with(mContext).load(image).error(R.string.default_avatar).into(avatar);
        }
        else {
            Glide.with(mContext).load(R.string.default_avatar).into(avatar);
        }
        if (status == 0) {
            statusEditor.setText("离线");
            statusEditor.setCompoundDrawablesRelativeWithIntrinsicBounds(null, view.getResources().getDrawable(R.drawable.ic_offline_list, null), null, null);

        } else {
            statusEditor.setText("在线");
            statusEditor.setCompoundDrawablesRelativeWithIntrinsicBounds(null, view.getResources().getDrawable(R.drawable.ic_online_list, null), null, null);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), FriendDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("status", status);
        intent.putExtra("avatar", image);
        v.getContext().startActivity(intent);
    }
}
