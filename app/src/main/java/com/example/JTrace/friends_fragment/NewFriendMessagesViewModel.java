package com.example.JTrace.friends_fragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.JTrace.Constants;
import com.example.JTrace.model.NewFriendMsg;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewFriendMessagesViewModel extends AndroidViewModel {
    private List<NewFriendMsg> NewFriendMsgs=new ArrayList<>();
    private MutableLiveData<String> status = new MutableLiveData<>();
    private SharedPreferences sharedPreferences;
    public MutableLiveData<String> getStatus() {
        return status;
    }

    public List<NewFriendMsg> getNewFriendMsgs() {
        return NewFriendMsgs;
    }
    public void setStatus(String status){
        this.status.postValue(status);
    }

    public NewFriendMessagesViewModel(@NonNull @NotNull Application application) {
        super(application);
        sharedPreferences=application.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
    }

    public void requestData(){
        new Thread(){
            @Override
            public void run(){
                OkHttpClient okHttpClient=new OkHttpClient();
                String token = sharedPreferences.getString("token", "");
                String username=sharedPreferences.getString("username","defaultAuthor");
                Request request = new Request.Builder().url(Constants.baseUrl + "/friends/requests/"+username)
                        .addHeader("Authorization", token)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("friendRequests","onResponse called");
                        String res=response.body().string();
                        Log.d("friendRequests",res);
                        try{
                            JSONObject results = new JSONObject(res);
                            JSONArray data = results.getJSONArray("data");
                            for(int i=data.length()-1;i>=0;i--){
                                JSONObject cur_msg=data.getJSONObject(i);
                                String reqMsg=cur_msg.getString("reqMsg");
                                String username1=cur_msg.getString("username1");
                                String username2=cur_msg.getString("username2");
                                int status=cur_msg.getInt("status");
                                NewFriendMsg cur=new NewFriendMsg(username1,username2,reqMsg,status);
                                NewFriendMsgs.add(cur);
                                Log.d("friendRequests",reqMsg);
                            }
                            Log.d("friendRequests", String.valueOf(NewFriendMsgs.size()));
                            setStatus("request");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }
}
