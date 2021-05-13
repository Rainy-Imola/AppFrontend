package com.example.easytalk.commom_user_fragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.easytalk.Constants;
import com.example.easytalk.model.User;
import com.example.easytalk.model.message;

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
    public MutableLiveData<String> getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status.postValue(status);
    }
    private List<message> mMessage = new ArrayList<>();
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
                        Log.d("User_info", "接收成功");
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
                                Log.d("User_info", "设置成功");
                            }

                        } catch (JSONException e) {
                            Log.d("userinfo", "userinfo dateParse failed");
                            e.printStackTrace();
                        }
                    }
                });
                setStatus("0");
                setStatus("user");
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
                Request request = new Request.Builder().url(Constants.baseUrl + "/msgboard/")
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
                                String date = cur_msg.getString("date");
                                String picture;
                                try {
                                    picture = cur_msg.getString("picture");
                                } catch (JSONException e) {
                                    picture = "https://pic.cnblogs.com/avatar/1691282/20210114201236.png";
                                }
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");
                                Date FormattedDate = format.parse(date);
                                message msg = new message(id, author, content, FormattedDate, picture);
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
    public boolean isFriend() {
        // TODO: 判断是否是好友
        return true;
    }

    public void requestAddFriend(String user_name, String newname) {
        new Thread() {
            @Override
            public void run() {
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("username1", mUser.getUser_name());
                    json.put("username2", sharedPreferences.getString("username","test2"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //申明给服务端传递一个json串
                //创建一个OkHttpClient对象
                String token=sharedPreferences.getString("token","");
                OkHttpClient okHttpClient = new OkHttpClient();
                //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
                //json为String类型的json数据
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                //创建一个请求对象
//                        String format = String.format(KeyPath.Path.head + KeyPath.Path.waybillinfosensor, username, key, current_timestamp);
                Request request = new Request.Builder()
                        .url(Constants.baseUrl+"/friends/add")
                        .addHeader("Authorization",token)
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //DialogUtils.showPopMsgInHandleThread(Release_Fragment.this.getContext(), mHandler, "数据获取失败，请重新尝试！");
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