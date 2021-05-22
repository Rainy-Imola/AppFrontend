package com.example.JTrace.friends_fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.JTrace.R;
import com.example.JTrace.model.NewFriendMsg;
import com.example.JTrace.model.NewFriendMsgs;
import com.example.JTrace.model.friend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class friendsFragment extends Fragment {

    private FriendsViewModel mViewModel;

    public static friendsFragment newInstance() {
        return new friendsFragment();
    }
    private List<friend> messages;
    private FriendAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RelativeLayout NewFriendLayout;
    private ImageView newFriendReminderView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sp;
    private NewFriendMsgs friendRequests=new NewFriendMsgs();
    private NewFriendMessagesViewModel mFriendRequestsViewModel;
    private boolean isNew=false;//标记是否有新的待处理消息
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.friends_fragment, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.friends);
        TextView title = (TextView) root.findViewById(R.id.title_f);
        NewFriendLayout=root.findViewById(R.id.newFriendLayout);
        newFriendReminderView=root.findViewById(R.id.newFriend_reminder);
        swipeRefreshLayout = root.findViewById(R.id.friends_list_refresh);
        title.setText("Friends");
        title.setGravity(Gravity.CENTER);
        setHasOptionsMenu(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Log.d("Location","OnCreateView");
                    getMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        // TODO: Get friend requests
        try {
            Log.d("Location","OnActivityCreated");
            getMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mFriendRequestsViewModel=new ViewModelProvider(getActivity()).get(NewFriendMessagesViewModel.class);
        getFriendRequests();
        NewFriendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("friendRequests","onClick called");
                Intent intent=new Intent(getActivity(),FriendRequestsActivity.class);
                intent.putExtra("requests", friendRequests);
                Log.d("friendRequests","intent put");
                startActivity(intent);
            }
        });
    }
    public void getFriendRequests(){
        mFriendRequestsViewModel.requestData();
        mFriendRequestsViewModel.getStatus().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String status) {
                Log.d("friendRequests","onchanged called");

                if(status=="request"){
                    friendRequests.clearMsgs();
                    isNew=false;
                    for(NewFriendMsg request:mFriendRequestsViewModel.getNewFriendMsgs()){
                        friendRequests.addMsg(request);
                        if(request.getStatus()==0){
                            isNew=true;
                        }
                    }
                    if(isNew){
                        newFriendReminderView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.friends_bar,menu);
        MenuItem searchItem = menu.findItem((R.id.user_add));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if(searchItem != null){
            searchView = (SearchView) searchItem.getActionView();
        }
        if(searchView != null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.user_add:
                Toast.makeText(getContext(),"添加好友", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void initUi() {
        LinearLayoutManager mLinearManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearManager);
        Log.d("friend list size",String.valueOf(messages.size()));
        mAdapter=new FriendAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);
    }


    public void getMessages() throws IOException {
        mViewModel.requestMessage();
        mViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if(status=="message"){
                    messages = new ArrayList<>();

                    try {
                        Log.d("MessageStatus",String.valueOf(mViewModel.getMessage().size()));
                        for(friend imessage:mViewModel.getMessage()){
                            messages.add(imessage);
                        }
                        initUi();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("MessageStatus", String.valueOf(messages.size()));
                }
            }
        });
        if(!mViewModel.isGetMsgSucc()){
            Toast.makeText(getContext(),"用户状态异常，请重新登陆！",Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_profile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("status",2);
            editor.commit();
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_myinfo_to_loginActivity);
        }
    }
}