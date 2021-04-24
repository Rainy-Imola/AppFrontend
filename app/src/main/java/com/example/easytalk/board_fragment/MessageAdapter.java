package com.example.easytalk.board_fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytalk.Message;
import com.example.easytalk.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private List<Message> mItems=new ArrayList<>();
    public MessageAdapter(List<Message> messages){
        this.mItems=messages;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view=View.inflate(parent.getContext(), R.layout.item_messageboard,null);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(mItems.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    public void notifyItems(@NonNull List<Message> items){
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }
}

