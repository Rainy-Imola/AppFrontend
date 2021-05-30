package com.example.JTrace.friends_fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.JTrace.Constants;
import com.example.JTrace.HttpAPI;
import com.example.JTrace.R;
import com.example.JTrace.model.NewFriendMsg;
import com.example.JTrace.model.NewFriendMsgs;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import android.os.Handler;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsViewHolder> {
    private NewFriendMsgs msgs = new NewFriendMsgs();
    private SharedPreferences sharedPreferences;
    private Handler mHandler = new Handler();

    @NonNull
    @NotNull
    @Override
    public FriendRequestsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_friendrequest, null);
        final FriendRequestsViewHolder viewHolder = new FriendRequestsViewHolder(view);
        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Request request = new Request.Builder().url(Constants.baseUrl + "/users/" +
                                viewHolder.getFromUsr() + "/info/")
                                .addHeader("Authorization", sharedPreferences.getString("token", ""))
                                .build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("requestUsr", "请求用户信息失败！");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String res = response.body().string();
                                try {
                                    JSONObject result = new JSONObject(res);
                                    JSONArray data = result.getJSONArray("data");
                                    JSONObject mid = data.getJSONObject(0);
                                    int id = mid.getInt("id");
                                    Intent intent = new Intent(v.getContext(), FriendDetailActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("name", viewHolder.getFromUsr());
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
        viewHolder.addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpAPI httpAPI = new HttpAPI();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username1", viewHolder.getFromUsr());
                    jsonObject.put("username2", sharedPreferences.getString("username", null));
                    //同意加好友，status=1
                    jsonObject.put("status", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    httpAPI.postApi_withToken(jsonObject, "/friends/response/", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(v.getContext(), "加好友请求发送失败！", Toast.LENGTH_SHORT).show();
                                }
                            };
                            mHandler.post(runnable);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(v.getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                    viewHolder.bind(viewHolder.getFromUsr(), viewHolder.getContent(), 1);
                                }
                            };
                            mHandler.post(runnable);
                        }
                    }, sharedPreferences.getString("token", null));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        viewHolder.declineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpAPI httpAPI = new HttpAPI();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username1", viewHolder.getFromUsr());
                    jsonObject.put("username2", sharedPreferences.getString("username", null));
                    //同意加好友，status=1
                    jsonObject.put("status", 2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    httpAPI.postApi_withToken(jsonObject, "/friends/response/", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(v.getContext(), "拒绝加好友请求发送失败！", Toast.LENGTH_SHORT).show();
                                }
                            };
                            mHandler.post(runnable);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(v.getContext(), "已拒绝", Toast.LENGTH_SHORT).show();
                                    viewHolder.bind(viewHolder.getFromUsr(), viewHolder.getContent(), 2);
                                }
                            };
                            mHandler.post(runnable);
                        }
                    }, sharedPreferences.getString("token", null));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendRequestsViewHolder holder, int position) {
        holder.bind(msgs.getMsgByIndex(position).getFrom_author_name(), msgs.getMsgByIndex(position).getReqMsg()
                , msgs.getMsgByIndex(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return msgs.getMsgs().size();
    }

    public void setMsgs(NewFriendMsgs msgs) {
        this.msgs = msgs;
    }

    public FriendRequestsAdapter(NewFriendMsgs msgs, SharedPreferences sp) {
        setMsgs(msgs);
        setSharedPreferences(sp);
    }

    public void setSharedPreferences(SharedPreferences sp) {
        sharedPreferences = sp;
    }
}
