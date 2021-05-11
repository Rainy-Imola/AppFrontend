package com.example.easytalk.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytalk.R;
import com.example.easytalk.friends_fragment.FriendViewHolder;
import com.example.easytalk.model.chatMsg;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<chatMsg> mItems=new ArrayList<>();

    public ChatAdapter(List<chatMsg> msglist) {
        mItems = msglist;
    }


    static class Sender extends RecyclerView.ViewHolder{
        ImageView image;
        TextView content;
        public Sender(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.chat_send_header);
            content = (TextView) itemView.findViewById(R.id.chat_send_content_text);
        }
    }

    static class Receiver extends RecyclerView.ViewHolder{
        ImageView image;
        TextView content;
        public Receiver(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.chat_recv_header);
            content = (TextView) itemView.findViewById(R.id.chat_recv_content_text);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case 0:
                View view1 = View.inflate(parent.getContext(),R.layout.message_send,null);
                final Sender sender = new Sender(view1);
                return sender;
            case 1:
                View view2 = View.inflate(parent.getContext(),R.layout.message_receive,null);
                final Receiver receiver = new Receiver(view2);
                return receiver;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        chatMsg msg  = mItems.get(position);
        if(holder instanceof Sender){
            Sender sender = (Sender)holder;
            sender.content.setText(msg.getContent());
        }else if (holder instanceof Receiver){
            Receiver receiver = (Receiver)holder;
            receiver.content.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
       return mItems.get(position).getType();
    }
}
