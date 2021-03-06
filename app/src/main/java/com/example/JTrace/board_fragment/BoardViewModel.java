package com.example.JTrace.board_fragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.JTrace.Constants;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BoardViewModel extends AndroidViewModel {
    private List<message> mMessage = new ArrayList<>();
    private SavedStateHandle handle;
    private SharedPreferences sharedPreferences;
    private boolean isGetMsgSucc = true;
    private MutableLiveData<String> status = new MutableLiveData<>();

    public MutableLiveData<String> getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status.postValue(status);
    }

    public boolean isGetMsgSucc() {
        return this.isGetMsgSucc;
    }

    public BoardViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle = handle;
        sharedPreferences = application.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
    }

    public List<message> getmMessage() {
        return this.mMessage;
    }

    public void requestMessage() {
        new Thread() {
            @Override
            public void run() {
                mMessage = new ArrayList<>();
                OkHttpClient okHttpClient = new OkHttpClient();
                String token = sharedPreferences.getString("token", "");
                Integer user_id = sharedPreferences.getInt("id", 2);
                Request request = new Request.Builder().url(Constants.baseUrl + "/msgboard/" + sharedPreferences.getInt("id", -1))
                        .addHeader("Authorization", token)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("MessageInfo", "request_handle_failed");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res = response.body().string();
                        try {
                            JSONObject results = new JSONObject(res);
                            JSONArray result = results.getJSONArray("data");
                            mMessage.clear();//????????????????????????????????????????????????double
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
                                message msg = new message(id, author, content, date, picture);
                                mMessage.add(msg);
                            }
                            setStatus("message");
                            Log.d("MessageInfo_viewModel", "msgs_Size: " + String.valueOf(mMessage.size()));
                        } catch (JSONException e) {
                            isGetMsgSucc = false;
                            Log.d("MessageInfo_viewModel", "dateParse failed");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }
}