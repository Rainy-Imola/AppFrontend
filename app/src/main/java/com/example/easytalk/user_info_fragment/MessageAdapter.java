package com.example.easytalk.user_info_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.easytalk.R;
import com.example.easytalk.model.message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> implements View.OnClickListener,View.OnLongClickListener{
    //定义Item点击事件
    public  interface OnRecyclerViewItemClickListener{
        //点击事件
        void onItemClick(View view, String str);
        //长按事件
        void onItemLongClick(View view, String str);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private Context mContext;
    private List<message> mItems=new ArrayList<>();
    public MessageAdapter(Context mContext, List<message> mItems, OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.mContext = mContext;
        this.mItems = mItems;
        mOnItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v, (String)v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemLongClick(v, (String)v.getTag());
        }
        return false;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_card_base,parent,false);
        MessageViewHolder holder = new MessageViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.itemView.setTag(mItems.get(position));
        holder.mAuthor.setText(mItems.get(position).getAuthor());
        holder.mContent.setText(mItems.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView mAuthor;
        private TextView mContent;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthor = (TextView)itemView.findViewById(R.id.message_author);
            mContent = (TextView)itemView.findViewById(R.id.message_content);
        }
    }


    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}
