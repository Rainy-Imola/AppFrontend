package com.example.JTrace.friends_fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.JTrace.R;
import com.example.JTrace.model.friend;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {
    private List<friend> fItems = new ArrayList<friend>();
    public FriendAdapter(List<friend> friends){
        this.fItems=friends;
    }
    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.item_friends,null);
        final FriendViewHolder viewHolder= new FriendViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.bind(fItems.get(position).getId(),fItems.get(position).getName(),fItems.get(position).getImage(),fItems.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return fItems.size();
    }

    public void notifyItems(@NonNull List<friend> items){
        fItems.clear();
        fItems.addAll(items);
        notifyDataSetChanged();
    }
}
