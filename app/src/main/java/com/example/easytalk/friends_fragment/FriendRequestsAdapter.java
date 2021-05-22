package com.example.easytalk.friends_fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytalk.R;
import com.example.easytalk.model.NewFriendMsgs;

import org.jetbrains.annotations.NotNull;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsViewHolder> {
    private NewFriendMsgs msg=new NewFriendMsgs();
    @NonNull
    @NotNull
    @Override
    public FriendRequestsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.item_friendrequest,null);
        final FriendRequestsViewHolder viewHolder=new FriendRequestsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendRequestsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
