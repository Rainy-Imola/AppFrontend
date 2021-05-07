package com.example.easytalk.board_fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.easytalk.Constants;
import com.example.easytalk.HttpAPI;
import com.example.easytalk.PublishActivity;
import com.example.easytalk.R;
import com.example.easytalk.model.message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class boardFragment extends Fragment {

    //private BoardViewModel mViewModel;
    private FloatingActionButton fabAdd;
    public static boardFragment newInstance() {
        return new boardFragment();
    }
    private List<message> messages;
    private MessageAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.board_fragment, container, false);
        mRecyclerView=root.findViewById(R.id.board);
        fabAdd = root.findViewById(R.id.fab);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PublishActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(BoardViewModel.class);
        // TODO: Use the ViewModel
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
        mAdapter=new MessageAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    public List<message> getMessages() throws IOException {
        List<message> msgs=new ArrayList<>();
        OkHttpClient okHttpClient=new OkHttpClient();
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        String token=sharedPreferences.getString("token","");
        Log.d("MessageInfo_token",token);
        Request request=new Request.Builder().url(Constants.baseUrl+"/msgboard/")
                .addHeader("Authorization",token)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("MessageInfo","request_handle_failed");
                Toast.makeText(getContext(),"请求留言数据失败！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                Log.d("MessageInfo",res);
                try {
                    JSONArray result=new JSONArray(res);
                    //JSONArray result = (new JSONObject(res)).getJSONArray("data");
                    Log.d("MessageInfo", "resultLength:"+String.valueOf(result.length()));
                    for(int i=0;i<result.length();i++){
                        JSONObject cur_msg=result.getJSONObject(i);
                        String id=cur_msg.getString("id");
                        String author=cur_msg.getString("author");
                        String content=cur_msg.getString("content");
                        String date=cur_msg.getString("date");
                        String picture=cur_msg.getString("picture");
                        Log.d("MessageInfo","cur_msg_info:"+"id:"+id+" "+" author:"+author+" content:"+content);
                        //handle date
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                        Log.d("MessageInfo","test");
                        Date FormattedDate=format.parse(date);

                        message msg=new message(author,content,FormattedDate,picture);
                        msgs.add(msg);
                        Log.d("MessageInfo","finished one circle");
                    }
                    Log.d("MessageInfo","msgs_Size: "+ String.valueOf(msgs.size()));
                } catch (JSONException | ParseException e) {
                    Log.d("MessageInfo","dateParse failed");
                    e.printStackTrace();
                }

            }
        });
        return msgs;
    }
}