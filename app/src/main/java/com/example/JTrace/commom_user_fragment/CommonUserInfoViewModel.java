package com.example.JTrace.commom_user_fragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.JTrace.Constants;
import com.example.JTrace.model.User;
import com.example.JTrace.model.message;

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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommonUserInfoViewModel extends AndroidViewModel {
    private SharedPreferences sharedPreferences;
    private User mUser=new User();
    private MutableLiveData<String> status = new MutableLiveData<>();


    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void setUserMutableLiveData(User userMutableLiveData) {
        this.userMutableLiveData.postValue(userMutableLiveData);
    }

    private MutableLiveData<String> friendStatus = new MutableLiveData<>();
    public MutableLiveData<String> getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status.postValue(status);
    }
    private List<message> mMessage = new ArrayList<>();
    public MutableLiveData<String> getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        this.friendStatus.postValue(friendStatus);
    }
    public CommonUserInfoViewModel(@NonNull Application application, SavedStateHandle handle){
        super(application);
        sharedPreferences = application.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
    }
    public void requestUser(String mArgument) {
        new Thread() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                String token = sharedPreferences.getString("token", "");
                Log.d("MessageInfo_token", token);
                Request request = new Request.Builder().url(Constants.baseUrl + "/users/" + mArgument + "/info")
                        .addHeader("Authorization", token)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("UserInfo", "request_handle_failed");
                        //Toast.makeText(getContext(),"请求留言数据失败！",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res = response.body().string();
                        Log.d("User_info", res);
                        try {
                            JSONObject result = new JSONObject(res);
                            JSONArray data = (JSONArray) result.get("data");
                            int status = (int) result.get("status");
                            String msg = (String) result.get("msg");
                            if (status == 0) {
                                mUser.setUser_name((String) data.getJSONObject(0).get("username"));
                                JSONArray hobbyJson = (JSONArray) data.getJSONObject(0).get("hobby");
                                List<String> hobbyList = new ArrayList<>();
                                for (int i = 0; i < hobbyJson.length(); ++i) {
                                    hobbyList.add(hobbyJson.getString(i));
                                }
                                mUser.setUser_id((Integer) data.getJSONObject(0).get("id"));
                                mUser.setUser_hobby(hobbyList);
                                mUser.setUser_constellation((String) data.getJSONObject(0).get("constellation"));
                                mUser.setUser_avatar((String) data.getJSONObject(0).get("avatar"));
                                setUserMutableLiveData(mUser);
                                Log.d("User_info", "设置成功");
                            }
                            // TODO: failure question
                        } catch (JSONException e) {
                            Log.d("userinfo", "userinfo dateParse failed");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    public User readUser() {
        return mUser;
    }

    public void requestMessage(String mArgument) {
        new Thread() {
            @Override
            public void run() {
                mMessage = new ArrayList<>();
                OkHttpClient okHttpClient = new OkHttpClient();
                String token = sharedPreferences.getString("token", "");
                Integer user_id = sharedPreferences.getInt("id", 2);
                Request request = new Request.Builder().url(Constants.baseUrl + "/msgboard" + "?author="+mArgument)
                        .addHeader("Authorization", token)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("user_info", "request_handle_failed");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res = response.body().string();
                        Log.d("user_info_all_message", res);
                        try {
                            JSONObject results = new JSONObject(res);
                            JSONArray result = results.getJSONArray("data");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject cur_msg = result.getJSONObject(i);
                                String id = cur_msg.getString("id");
                                String author = cur_msg.getString("author");
                                String content = cur_msg.getString("content");
                                JSONObject json_date=cur_msg.getJSONObject("date");
                                String yyyy=String.valueOf(json_date.getInt("year")+1900);
                                String MM=String.valueOf(json_date.getInt("month")+1);
                                String dd=String.valueOf(json_date.getInt("date"));
                                String hh=String.valueOf(json_date.getInt("hours"));
                                String mm=String.valueOf(json_date.getInt("minutes"));
                                String ss=String.valueOf(json_date.getInt("seconds"));
                                String string_date=yyyy+"-"+MM+"-"+dd+" "+hh+":"+mm+":"+ss;
                                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date formatted_date=format.parse(string_date);
                                String picture;
                                try {
                                    picture = cur_msg.getString("picture");
                                } catch (JSONException e) {
                                    picture = "https://pic.cnblogs.com/avatar/1691282/20210114201236.png";
                                }
                                message msg = new message(id, author, content, formatted_date, picture);
                                mMessage.add(msg);
                            }
                            setStatus("0");
                            setStatus("message");
                        } catch (JSONException | ParseException e) {
                            Log.d("userinfo MessageInfo", "dateParse failed");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }
    public List<message> getMessage() {
        return  mMessage;
    }
    public void isFriend(String mArgument) {
        new Thread() {
            @Override
            public void run() {
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String username1 = sharedPreferences.getString("username", "");
                try {
                    json.put("username1", username1);
                    json.put("username2", mArgument);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String token=sharedPreferences.getString("token","");
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                Request request = new Request.Builder()
                        .url(Constants.baseUrl+"/friends/check")
                        .addHeader("Authorization",token)
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("check friend error",mArgument+username1);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        if(string.compareTo("1")==0){
                            setFriendStatus("0");
                            setFriendStatus("friendyes");
                        }else if (string.compareTo("0")==0){
                            setFriendStatus("0");
                            setFriendStatus("friendno");
                        }
                    }
                });

            }
        }.start();
    }
    public void requestAddFriend(String user_name, String newname) {
        new Thread() {
            @Override
            public void run() {
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("username1", sharedPreferences.getString("username","test2"));
                    json.put("username2", user_name);
                    json.put("reqMsg",newname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String token=sharedPreferences.getString("token","");
                OkHttpClient okHttpClient = new OkHttpClient();
                //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
                //json为String类型的json数据
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                //创建一个请求对象
//                        String format = String.format(KeyPath.Path.head + KeyPath.Path.waybillinfosensor, username, key, current_timestamp);
                Request request = new Request.Builder()
                        .url(Constants.baseUrl+"/friends/request")
                        .addHeader("Authorization",token)
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("common friend","failure");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        Log.d("info",string+"");
                        try {
                            JSONObject json = new JSONObject(string);
                            int status = (int) json.get("status");
                            String msg = (String) json.get("msg");
                            if (status==0) {
                                setStatus("0");
                                setStatus("addsuccess");
                                Log.d("add","success");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();

    }
}