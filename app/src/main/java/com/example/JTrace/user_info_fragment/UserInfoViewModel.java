package com.example.JTrace.user_info_fragment;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import static com.example.JTrace.Constants.pictureUrl;

public class UserInfoViewModel extends AndroidViewModel {

    private SharedPreferences sharedPreferences;
    private SavedStateHandle handle;
    private User mUser = new User();


    private String path_avatar = "";
    private String status_avatar = "";
    private List<message> mMessage = new ArrayList<>();

    public MutableLiveData<String> getStatus_save() {
        return status_save;
    }

    public void setStatus_save(String status_save) {
        this.status_save.postValue(status_save);
    }

    private  MutableLiveData<String> status_save = new MutableLiveData<>();
    private MutableLiveData<String> status = new MutableLiveData<>();

    public MutableLiveData<String> getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status.postValue(status);
    }

    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void setUserMutableLiveData(User userMutableLiveData) {
        this.userMutableLiveData.postValue(userMutableLiveData);
    }

    public UserInfoViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle = handle;
        sharedPreferences = application.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
    }

    public User readUser() {
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

    public void requestMessage() throws IOException {
        new Thread() {
            @Override
            public void run() {
                mMessage = new ArrayList<>();
                OkHttpClient okHttpClient = new OkHttpClient();
                String token = sharedPreferences.getString("token", "");
                Integer user_id = sharedPreferences.getInt("id", 2);
                String user_name = sharedPreferences.getString("username", "");
                Request request = new Request.Builder().url(Constants.baseUrl + "/msgboard" + "?author=" + user_name)
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
                        try {
                            JSONObject results = new JSONObject(res);
                            JSONArray result = results.getJSONArray("data");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject cur_msg = result.getJSONObject(i);
                                String id = String.valueOf(cur_msg.getInt("id"));
                                String author = user_name;
                                String content = cur_msg.getString("content");
                                String date = cur_msg.getString("date");
                                String picture;
                                try {
                                    picture = cur_msg.getString("picture");
                                } catch (JSONException e) {
                                    picture = "";
                                }

                                message msg = new message(id, author, content, date, picture);
                                mMessage.add(msg);
                            }
                            setStatus("0");
                            setStatus("message");
                        } catch (JSONException e) {
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
        setUserMutableLiveData(mUser);
    }

    /**
     * ????????????????????????????????????byte??????
     *
     * @param imgSrc ????????????
     * @return byte[]
     */
    public static byte[] image2Bytes(String imgSrc) {
        File file = null;
        FileInputStream fin;
        byte[] bytes = null;
        try {
            fin = new FileInputStream(new File(imgSrc));
            bytes = new byte[fin.available()];
            //?????????????????????????????????
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

    public void setPath_avatar(String avatar) {
        this.path_avatar = avatar;
    }

    public void setUserAvatar(String avatar) {
        new Thread() {
            @Override
            public void run() {
                Log.i("starting", "??????create");
                if (avatar.equals("")) {
                    setStatus_avatar("avatar_fail");
                    return;
                }
                byte[] coverImageData = image2Bytes(avatar);
                MultipartBody.Part coverPart = MultipartBody.Part.createFormData("image", "cover.png",
                        RequestBody.create(MediaType.parse("multipart/form-data"), coverImageData));

                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = new MultipartBody.Builder().addPart(coverPart).build();
                Request request = new Request.Builder().url(pictureUrl).post(requestBody).build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        setStatus_avatar("avatar_fail");

                        Log.i("failure", "????????????" + call.toString());
                        Log.i("failure", "????????????" + e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Headers headers = response.headers();
                        for (int i = 0; i < headers.size(); i++) {
                            Log.i("header:", headers.name(i) + ":" + headers.value(i));
                        }
                        try {
                            JSONObject res = new JSONObject(response.body().string());
                            String picture = (String) res.get("name");
                            String user_avatar = pictureUrl + "/" + picture + ".jpg";
                            mUser.setUser_avatar(user_avatar);
                            setStatus_avatar("avatar_success");
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
                Request request = new Request.Builder().url(Constants.baseUrl + "/users/" + sharedPreferences.getString("username", "") + "/info")
                        .addHeader("Authorization", token)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("UserInfo", "request_handle_failed");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res = response.body().string();

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
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    public void requestSave() {
        new Thread() {
            @Override
            public void run() {
                // @Headers({"Content-Type:application/json","Accept: application/json"})//???????????????
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {

                    json.put("hobby", new JSONArray(mUser.getUser_hobby()));
                    json.put("constellation", mUser.getUser_constellation());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //??????????????????????????????json???
                //????????????OkHttpClient??????
                String token = sharedPreferences.getString("token", "");
                OkHttpClient okHttpClient = new OkHttpClient();
                //????????????RequestBody(??????1??????????????? ??????2?????????json???)
                //json???String?????????json??????
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                //????????????????????????
                Request request = new Request.Builder()
                        .url(Constants.baseUrl + "/users/" + sharedPreferences.getString("username", "") + "/info")
                        .addHeader("Authorization", token)
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        try {
                            JSONObject json = new JSONObject(string);
                            int status = (int) json.get("status");
                            String msg = (String) json.get("msg");
                            if (status == 0) {
                                Log.d("save user hobby", "success");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    public void requestAvatarPost() {
        new Thread() {
            @Override
            public void run() {
                if (getPath_avatar().length() != 0) {
                    setUserAvatar(path_avatar);
                    while (getStatus_avatar() != "avatar_success" && getStatus_avatar() != "avatar_fail") {
                    }
                    if (getStatus_avatar() == "avatar_success") {
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        JSONObject json = new JSONObject();
                        try {
                            json.put("avatar", mUser.getUser_avatar());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String token = sharedPreferences.getString("token", "");
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                        Request request = new Request.Builder()
                                .url(Constants.baseUrl + "/users/" + sharedPreferences.getString("username", "") + "/avatar")
                                .addHeader("Authorization", token)
                                .post(requestBody)
                                .build();

                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String string = response.body().string();
                                try {
                                    JSONObject json = new JSONObject(string);
                                    int status = (int) json.get("status");
                                    String msg = (String) json.get("msg");
                                    if (status == 0) {
                                        Log.d("save avatar", "success");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    else if (getStatus_avatar() == "avatar_fail") {
                        setStatus_save("fail_avatar");
                    }
                }


            }
        }.start();
    }


    public void deleteMessage(String id, int position) {
        new Thread() {
            @Override
            public void run() {

                String token = sharedPreferences.getString("token", "");
                OkHttpClient okHttpClient = new OkHttpClient();
                //????????????????????????
                Request request = new Request.Builder()
                        .url(Constants.baseUrl + "/msgboard/" + id)
                        .addHeader("Authorization", token)
                        .delete()
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        try {
                            JSONObject json = new JSONObject(string);
                            int status = (int) json.get("status");
                            String msg = (String) json.get("msg");
                            if (status == 0) {
                                mMessage.remove(position);
                                setStatus("0");
                                setStatus("message");
                                Log.d("delete", "success");
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