package com.example.easytalk.user_info_fragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.bumptech.glide.Glide;
import com.example.easytalk.Constants;
import com.example.easytalk.PublishActivity;
import com.example.easytalk.Util;
import com.example.easytalk.model.User;
import com.example.easytalk.model.message;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.easytalk.Constants.pictureUrl;

public class UserInfoViewModel extends AndroidViewModel {

    private SharedPreferences sharedPreferences;
    private SavedStateHandle handle;
    private User mUser = new User();


    private String path_avatar = "";
    private String status_avatar= "";
    private List<message> mMessage = new ArrayList<>();
    private MutableLiveData<String> status = new MutableLiveData<>();
    public MutableLiveData<String> getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status.postValue(status);
    }
    public UserInfoViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle =handle;
        sharedPreferences = application.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
    }
    public User readUser(){
        return mUser;
    }
    public List<message> getMessage() throws IOException {
        return mMessage;
    }
    public String getStatus_avatar() {
        return status_avatar;
    }

    public void setStatus_avatar(String status_avatar) {
        this.status_avatar = status_avatar;
    }

    public void requestMessage() throws IOException{
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
                            Log.d("userinfo Message", "resultLength:" + String.valueOf(result.length()));
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
                                Log.d("MessageInfo", "cur_msg_info:" + "id:" + " " + id + " author:" + author + " content:" + content);
                                //handle date
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");
                                Date FormattedDate = format.parse(date);
                                message msg = new message(id, author, content, FormattedDate, picture);
                                mMessage.add(msg);
                                Log.d("user_info MessageInfo", "finished one circle");
                            }
                            setStatus("0");
                            setStatus("message");
                            Log.d("MessageInfo", "msgs_Size: " + String.valueOf(mMessage.size()));
                        } catch (JSONException | ParseException e) {
                            Log.d("userinfo MessageInfo", "dateParse failed");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    public User getUser() {
        questUser();
        return mUser;
    }
    public void setUserName(String newname) {
        mUser.setUser_name(newname);
    }
    public void setUserHobby(List<String> hobbylist) {
        mUser.setUser_hobby(hobbylist);
    }
    public void setUserConstellation(String newconstellation) {
        mUser.setUser_constellation(newconstellation);
    }
    /**
     * 根据图片路径，把图片转为byte数组
     * @param imgSrc  图片路径
     * @return      byte[]
     */
    public byte[] image2Bytes(String imgSrc)
    {
        File file = null;
        FileInputStream fin;
        byte[] bytes = null;
        try {
            fin = new FileInputStream(new File(imgSrc));
            bytes  = new byte[fin.available()];
            //将文件内容写入字节数组
            fin.read(bytes);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public String getPath_avatar() {
        return path_avatar;
    }
    public void setPath_avatar(String avatar){
        this.path_avatar = avatar;
    }
    public void setUserAvatar(String avatar){
        new Thread() {
            @Override
            public void run() {
                Log.i("starting", "开始create");
                byte[] coverImageData = image2Bytes(avatar);
                MultipartBody.Part coverPart = MultipartBody.Part.createFormData("image", "cover.png",
                        RequestBody.create(MediaType.parse("multipart/form-data"), coverImageData));

                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = new MultipartBody.Builder().addPart(coverPart).build();
                Request request = new Request.Builder().url(pictureUrl).post(requestBody).build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        setStatus_avatar("avatar_failure");

                        Log.i("failure", "上传失败" + call.toString());
                        Log.i("failure", "上传失败" + e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i("response", response.protocol() + " " + response.code() + " " + response.message());
                        Headers headers = response.headers();
                        for (int i = 0; i < headers.size(); i++) {
                            Log.i("header:", headers.name(i) + ":" + headers.value(i));
                        }
                        //Log.i("onResponse: ", response.body().string());
                        try {
                            JSONObject res  = new JSONObject( response.body().string());
                            String picture = (String)res.get("name");
                            String user_avatar = pictureUrl+"/"+picture+".jpg";
                            mUser.setUser_avatar(user_avatar);
                            setStatus_avatar("avatar_success");
                            Log.i("SUCCESS", "图片链接已获取"+user_avatar);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }
    public void questUser() {
        new Thread() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                String token = sharedPreferences.getString("token", "");
                Log.d("MessageInfo_token", token);
                Request request = new Request.Builder().url(Constants.baseUrl + "/users/" + sharedPreferences.getString("username", "") + "/info")
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
                        Log.d("userinfo user", res);

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
                                Log.d("User_info", "设置成功");
                                setStatus("0");
                                setStatus("user");
                            }

                        } catch (JSONException e) {
                            Log.d("userinfo", "userinfo dateParse failed");
                            e.printStackTrace();
                        }
                    }
                });
                Log.d("userinfo username", mUser.getUser_name());
            }
        }.start();
    }

    public void requestSave() {
        new Thread() {
            @Override
            public void run() {
                // @Headers({"Content-Type:application/json","Accept: application/json"})//需要添加头
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {

                    json.put("hobby", new JSONArray(mUser.getUser_hobby()));
                    json.put("constellation", mUser.getUser_constellation());
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
                        .url(Constants.baseUrl+"/users/"+sharedPreferences.getString("username","")+"/info")
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
                                Log.d("save user hobby","success");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }
    public void requestAvatarPost(){
        new Thread() {
            @Override
            public void run() {
                if (getPath_avatar().length() != 0) {
                    setUserAvatar(path_avatar);
                    while (getStatus_avatar() != "avatar_success" && getStatus_avatar() != "avatar_success") {
                    }
                    if(getStatus_avatar() == "avatar_success"){
                        // @Headers({"Content-Type:application/json","Accept: application/json"})//需要添加头
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        JSONObject json = new JSONObject();
                        try {
                            json.put("avatar", mUser.getUser_avatar());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String token=sharedPreferences.getString("token","");
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                        Request request = new Request.Builder()
                                .url(Constants.baseUrl+"/users/"+sharedPreferences.getString("username","")+"/avatar")
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
                                        Log.d("save avatar","success");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }


            }
        }.start();
    }


    
}