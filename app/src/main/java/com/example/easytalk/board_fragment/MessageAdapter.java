package com.example.easytalk.board_fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytalk.R;
import com.example.easytalk.model.message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private List<message> mItems=new ArrayList<>();
    public MessageAdapter(List<message> messages){
        setmItems(messages);
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view=View.inflate(parent.getContext(), R.layout.item_messageboard,null);
        final MessageViewHolder viewHolder= new MessageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    public void notifyItems(@NonNull List<message> items){
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }
    public void setmItems(List<message> messages){
        this.mItems=messages;
    }
}

