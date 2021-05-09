package com.example.easytalk.user_info_fragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.easytalk.Constants;
import com.example.easytalk.model.User;
import com.example.easytalk.model.friend;
import com.example.easytalk.model.message;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserInfoViewModel extends AndroidViewModel {
    private SharedPreferences sharedPreferences;
    private SavedStateHandle handle;
    public UserInfoViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle =handle;
        sharedPreferences = application.getSharedPreferences("user_profile", Context.MODE_PRIVATE);

    }

    public List<message> getMessage() throws IOException{
        List<message> msgs=new ArrayList<>();
        OkHttpClient okHttpClient=new OkHttpClient();
        String token=sharedPreferences.getString("token","");
        Log.d("MessageInfo_token",token);
        Request request=new Request.Builder().url(Constants.baseUrl+"/msgboard/")
                .addHeader("Authorization",token)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("MessageInfo","request_handle_failed");
                //Toast.makeText(getContext(),"请求留言数据失败！",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                Log.d("userinfo Message",res);
                try {
                    JSONArray result=new JSONArray(res);
                    Log.d("userinfo Message", "resultLength:"+String.valueOf(result.length()));
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
                        Date FormattedDate=format.parse(date);

                        message msg=new message(id,author,content,FormattedDate,picture);
                        msgs.add(msg);
                        Log.d("userinfo MessageInfo","finished one circle");
                    }
                    Log.d("MessageInfo","msgs_Size: "+ String.valueOf(msgs.size()));
                } catch (JSONException | ParseException e) {
                    Log.d("userinfo MessageInfo","dateParse failed");
                    e.printStackTrace();
                }
            }
        });
        return msgs;
    }
    public User getUser() {
        User mUser = new User("default");
        OkHttpClient okHttpClient=new OkHttpClient();
        String token=sharedPreferences.getString("token","");
        Log.d("MessageInfo_token",token);
        Request request=new Request.Builder().url(Constants.baseUrl+"/users/"+sharedPreferences.getString("username","")+"/info")
                .addHeader("Authorization",token)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("UserInfo","request_handle_failed");
                //Toast.makeText(getContext(),"请求留言数据失败！",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                Log.d("User_info","接收成功");
                Log.d("userinfo user",res);

                try {
                    JSONObject result=new JSONObject(res);
                    JSONArray data = (JSONArray) result.get("data");
                    int status = (int) result.get("status");
                    String msg = (String) result.get("msg");
                    if(status==0){
                        mUser.setUser_name((String) data.getJSONObject(0).get("username"));
                        JSONArray hobbyJson = (JSONArray) data.getJSONObject(0).get("hobby");
                        List<String> hobbyList = new ArrayList<>();
                        for(int i = 0;i<hobbyJson.length();++i){
                            hobbyList.add(hobbyJson.getString(i));
                        }
                        mUser.setUser_hobby(hobbyList);
                        mUser.setUser_constellation((String) data.getJSONObject(0).get("constellation"));
                        Log.d("User_info","设置成功");
                    }

                } catch (JSONException e) {
                    Log.d("userinfo","userinfo dateParse failed");
                    e.printStackTrace();
                }
            }
        });
        Log.d("userinfo username",mUser.getUser_name());
        return mUser;
    }

    // TODO: Implement the ViewModel
    
}