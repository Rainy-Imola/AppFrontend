package com.example.easytalk.friends_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.easytalk.R;
import com.example.easytalk.model.NewFriendMsgs;


public class FriendRequestsActivity extends AppCompatActivity {
    private NewFriendMsgs msgs;
    private RecyclerView mRecyclerView;
    private FriendRequestsAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);
    }
}