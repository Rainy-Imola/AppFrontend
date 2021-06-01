package com.example.JTrace.user_info_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.JTrace.R;
import com.example.JTrace.model.message;
import com.facebook.drawee.view.SimpleDraweeView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> implements View.OnClickListener,View.OnLongClickListener{


    //定义Item点击事件
    public  interface OnRecyclerViewItemClickListener{
        //点击事件
        void onItemClick(View view, int str);
        //长按事件
        void onItemLongClick(View view, int str);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private Context mContext;
    private List<message> mItems=new ArrayList<>();
    public MessageAdapter(Context mContext, List<message> mItems) {
        this.mContext = mContext;
        this.mItems = mItems;
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }
    @Override
    public boolean onLongClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemLongClick(v, (Integer) v.getTag());
        }
        return false;
    }




    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_card_base,parent,false);
        MessageViewHolder holder = new MessageViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.mContent.setText(mItems.get(position).getContent());
        holder.mcreateat.setText(mItems.get(position).getCreatedAt());
        holder.mcoverView.setImageURI(mItems.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView mAuthor,mContent,mcreateat;
        private SimpleDraweeView mcoverView;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            mContent = (TextView)itemView.findViewById(R.id.message_content);
            mcreateat = itemView.findViewById(R.id.usr_msg_date);
            mcoverView = itemView.findViewById(R.id.sd_cover1);
        }
    }


    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}
