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
import com.example.JTrace.model.friend;

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

public class FriendsViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private List<friend> mMessage = new ArrayList<>();
    private MutableLiveData<String> status = new MutableLiveData<>();
    private boolean isGetMsgSucc=true;
    private SharedPreferences sharedPreferences;
    private SavedStateHandle handle;
    public FriendsViewModel(@NonNull Application application, SavedStateHandle handle){
        super(application);
        this.handle=handle;
        sharedPreferences=application.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
    }

    public MutableLiveData<String> getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status.postValue(status);
    }
    public List<friend> getMessage() throws IOException {
        return mMessage;
    }

    public void requestMessage(){
        new Thread(){
            @Override
            public void run(){
                mMessage = new ArrayList<>();
                OkHttpClient okHttpClient=new OkHttpClient();
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
                                int onoffstatus = 0;
                                try{
                                    jsonObject = new JSONObject(cur_msg);
                                    name = jsonObject.getString("name");
                                    onoffstatus = jsonObject.getInt("status");
                                }catch(JSONException e){
                                    e.printStackTrace();
                                }
                                Log.d("name: ",name);
                                Log.d("status", String.valueOf(status));

                                OkHttpClient okHttpClient = new OkHttpClient();
                                String token = sharedPreferences.getString("token", "");
                                Log.d("MessageInfo_token", token);
                                Request request = new Request.Builder().url(Constants.baseUrl + "/users/" +name + "/info")
                                        .addHeader("Authorization", token)
                                        .build();
                                int finalOnoffstatus = onoffstatus;
                                okHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.d("UserInfo", "request_handle_failed");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String res = response.body().string();
                                        Log.d("User_info", "接收成功");
                                        Log.d("userinfo user", res);

                                        try {
                                            JSONObject result = new JSONObject(res);
                                            JSONArray data = (JSONArray) result.get("data");
                                            int status = (int) result.get("status");
                                            String msg = (String) result.get("msg");
                                            if (status == 0) {
                                                String image = (String) data.getJSONObject(0).get("avatar");
                                                String name = (String) data.getJSONObject(0).get("username");
                                                Log.d("username:",name);
                                                Log.d("avatar",image);
                                                friend eachFriend=new friend(0,name,image, finalOnoffstatus);
                                                mMessage.add(eachFriend);
                                                Log.d("MessageInfo","finished one circle");
                                                setStatus("message");
                                            }
                                        } catch (JSONException e) {
                                            Log.d("userinfo", "userinfo dateParse failed");
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            isGetMsgSucc=false;
                            Log.d("MessageInfo_viewModel", "dateParse failed");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }
    public boolean isGetMsgSucc() {
        return isGetMsgSucc;
    }
}