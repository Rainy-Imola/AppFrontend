package com.example.JTrace.friends_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.JTrace.R;
import com.example.JTrace.model.NewFriendMsgs;

//TODO: item_friendRequest样式待调
//TODO: 待定义点击加好友按钮通过好友请求方法
public class FriendRequestsActivity extends Activity {
    private NewFriendMsgs msgs;
    private RecyclerView mRecyclerView;
    private FriendRequestsAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        Intent intent=getIntent();
        msgs= (NewFriendMsgs) intent.getSerializableExtra("requests");
        mRecyclerView=findViewById(R.id.FriendRequestsRecyclerView);
        mAdapter=new FriendRequestsAdapter(msgs);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
