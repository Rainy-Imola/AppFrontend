package com.example.easytalk.board_fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytalk.R;
import com.example.easytalk.model.comment;

import java.util.ArrayList;
import java.util.List;

public class commentAdapter extends RecyclerView.Adapter<commentViewHolder> {
    private List<comment> comments=new ArrayList<>();

    @NonNull
    @Override
    public commentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.item_comment,null);
        final commentViewHolder viewHolder=new commentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull commentViewHolder holder, int position) {
        holder.bind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<comment> comments){
        this.comments=comments;
    }
    public commentAdapter(List<comment> inputs){
        setComments(inputs);
    }

}
