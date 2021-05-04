package com.example.easytalk.friends_fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytalk.R;
import com.example.easytalk.board_fragment.MessageDetailActivity;

public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView nameEditor;
    private String name;
    public FriendViewHolder(@NonNull View itemView) {
        super(itemView);
        nameEditor = (TextView) itemView.findViewById(R.id.name);
        itemView.setOnClickListener(this);
    }

    public void bind(String name,int image){
        this.name = name;
        nameEditor.setText(name);
    }
    @Override
    public void onClick(View v) {
        Log.d("ClickEvent","onClick called");
        Intent intent=new Intent(v.getContext(), FriendDetailActivity.class);
        intent.putExtra("name",name);
        v.getContext().startActivity(intent);
    }
}
