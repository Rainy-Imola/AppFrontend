package com.example.JTrace.friends_fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.JTrace.R;
import com.example.JTrace.model.NewFriendMsg;
import com.example.JTrace.model.NewFriendMsgs;

import org.jetbrains.annotations.NotNull;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsViewHolder> {
    private NewFriendMsgs msgs=new NewFriendMsgs();
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
        holder.bind(msgs.getMsgByIndex(position).getFrom_author_name(),msgs.getMsgByIndex(position).getReqMsg());
    }

    @Override
    public int getItemCount() {
        return msgs.getMsgs().size();
    }

    public void setMsgs(NewFriendMsgs msgs){
        this.msgs=msgs;
    }
    public FriendRequestsAdapter(NewFriendMsgs msgs){
        setMsgs(msgs);
    }
}
