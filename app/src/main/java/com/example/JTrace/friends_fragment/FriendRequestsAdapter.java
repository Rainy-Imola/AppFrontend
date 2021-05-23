package com.example.JTrace.friends_fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.JTrace.Constants;
import com.example.JTrace.R;
import com.example.JTrace.model.NewFriendMsg;
import com.example.JTrace.model.NewFriendMsgs;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsViewHolder> {
    private NewFriendMsgs msgs=new NewFriendMsgs();
    private SharedPreferences sharedPreferences;
    @NonNull
    @NotNull
    @Override
    public FriendRequestsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.item_friendrequest,null);
        final FriendRequestsViewHolder viewHolder=new FriendRequestsViewHolder(view);
        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run(){
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Request request = new Request.Builder().url(Constants.baseUrl + "/users/"+
                                viewHolder.authorView.getText()+"/info/")
                                .addHeader("Authorization", sharedPreferences.getString("token",""))
                                .build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("requestUsr","请求用户信息失败！");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String res=response.body().string();
                                Log.d("requestUsr",res);
                                try {
                                    JSONObject result=new JSONObject(res);
                                    JSONArray data=result.getJSONArray("data");
                                    JSONObject mid=data.getJSONObject(0);
                                    int id=mid.getInt("id");
                                    Intent intent=new Intent(v.getContext(), FriendDetailActivity.class);
                                    intent.putExtra("id",id);
                                    intent.putExtra("name", viewHolder.authorView.getText());
                                    v.getContext().startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }.start();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendRequestsViewHolder holder, int position) {
        holder.bind(msgs.getMsgByIndex(position).getFrom_author_name(),msgs.getMsgByIndex(position).getReqMsg());
    }

    @Override
    public int getItemCount() {
        return msgs.getMsgs().size();
    }

    public void setMsgs(NewFriendMsgs msgs){
        this.msgs=msgs;
    }
    public FriendRequestsAdapter(NewFriendMsgs msgs,SharedPreferences sp){
        setMsgs(msgs);
        setSharedPreferences(sp);
    }
    public void setSharedPreferences(SharedPreferences sp){
        sharedPreferences=sp;
    }
}
