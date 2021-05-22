package com.example.JTrace.friends_fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.JTrace.R;

public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView nameEditor;
    private TextView statusEditor;
    private int id;
    private String name;
    public FriendViewHolder(@NonNull View itemView) {
        super(itemView);
        nameEditor = (TextView) itemView.findViewById(R.id.name);
        statusEditor = (TextView) itemView.findViewById(R.id.status);
        itemView.setOnClickListener(this);
    }

    public void bind(int id,String name, int image,int status){
        this.id = id;
        this.name = name;
        nameEditor.setText(name);
        if(status == 0){
            statusEditor.setText("状态:离线");
        }else {
            statusEditor.setText("状态:在线");
        }

    }
    @Override
    public void onClick(View v) {
        Log.d("ClickEvent","onClick called");
        Intent intent=new Intent(v.getContext(), FriendDetailActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name",name);
        v.getContext().startActivity(intent);
    }
}
