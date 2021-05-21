package com.example.easytalk.friends_fragment;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easytalk.Constants;
import com.example.easytalk.MainActivity;
import com.example.easytalk.R;
import com.example.easytalk.board_fragment.MessageAdapter;
import com.example.easytalk.board_fragment.boardFragment;
import com.example.easytalk.model.friend;
import com.example.easytalk.model.message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class friendsFragment extends Fragment {

    private FriendsViewModel mViewModel;

    public static friendsFragment newInstance() {
        return new friendsFragment();
    }
    private List<friend> messages;
    private FriendAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView newFriendView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.friends_fragment, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.friends);
        TextView title = (TextView) root.findViewById(R.id.title_f);
        newFriendView=root.findViewById(R.id.newFriendView);
        title.setText("Friends");
        title.setGravity(Gravity.CENTER);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        // TODO: Get friend requests


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

    @Override
    public void onStart() {
        super.onStart();
        try {
            messages=getMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("MessageInfo",messages.toString());
        mAdapter=new FriendAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public List<friend> getMessages() throws IOException {
        List<friend> msgs=new ArrayList<>();
        OkHttpClient okHttpClient=new OkHttpClient();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");
        String username = sharedPreferences.getString("username","");
        Log.d("MessageInfo_token",token);
        Request request=new Request.Builder().url(Constants.baseUrl+"/friends/"+username)
                .addHeader("Authorization",token)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("MessageInfo","request_handle_failed");
                Toast.makeText(getContext(),"用户数据拉取失败",Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                Log.d("MessageInfo",res);
                try {
                    JSONObject result=new JSONObject(res);
                    Log.d("MessageInfo", "resultLength:"+String.valueOf(result.length()));
                    JSONArray data = result.getJSONArray("data");

                    Log.d("friend_list", String.valueOf(data));

                    for(int i=0;i<data.length();i++){
                        String cur_msg=String.valueOf(data.get(i));
                        Log.d("friends info", String.valueOf(cur_msg));
                        //int id=cur_msg.getInt("id");
                        //String username=cur_msg.getString("username");
                        //String password=cur_msg.getString("password");
                        JSONObject jsonObject = null;
                        String name = null;
                        int status = 0;
                        try{
                            jsonObject = new JSONObject(cur_msg);
                            name = jsonObject.getString("name");
                            status = jsonObject.getInt("status");
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        Log.d("name: ",name);
                        Log.d("status", String.valueOf(status));
                        friend msg=new friend(0,name,0,status);
                        msgs.add(msg);
                        Log.d("MessageInfo","finished one circle");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return msgs;
    }
}