package com.example.JTrace.friends_fragment;

import androidx.navigation.Navigation;

import android.os.Bundle;

import com.example.JTrace.R;
import com.example.JTrace.baseActivity;

public class FriendDetailActivity extends baseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        this.getSupportActionBar().hide();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }
}