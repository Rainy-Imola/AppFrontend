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
import com.example.JTrace.model.comment;

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

public class commentViewModel extends AndroidViewModel {
    private List<comment> mComments=new ArrayList<>();
    private MutableLiveData<String> status = new MutableLiveData<>();
    private SharedPreferences sharedPreferences;
    private SavedStateHandle handle;

    public MutableLiveData<String> getStatus() {
        return status;
    }
    public commentViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle=handle;
        sharedPreferences=application.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
    }
    public List<comment> getmComments(){
        return this.mComments;
    }
    public void setStatus(String status){
        this.status.postValue(status);
    }

    public void requestData(String message_id){
        new Thread(){
            @Override
            public void run(){
                OkHttpClient okHttpClient = new OkHttpClient();
                String token = sharedPreferences.getString("token", "");
                Integer user_id = sharedPreferences.getInt("id", 2);
                Request request = new Request.Builder().url(Constants.baseUrl + "/msgboard/"+message_id+"/"+sharedPreferences.getInt("id",-1))
                        .addHeader("Authorization", token)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("comment_viewModel","requestData failed");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res=response.body().string();
                        //Log.d("comment_viewModel",res);
                        try{
                            JSONObject results = new JSONObject(res);
                            JSONArray data = results.getJSONArray("data");
                            JSONObject mid=data.getJSONObject(0);
                            JSONArray result=mid.getJSONArray("comment");
                            Log.d("comment_viewModel","comments_size:"+result.length());
                            for(int i=result.length()-1;i>=0;i--){
                                JSONObject cur_comment=result.getJSONObject(i);
                                String author=cur_comment.getString("author");
                                String content=cur_comment.getString("content");
                                String date=cur_comment.getString("date");
                                comment mComment=new comment(author,content,message_id,date);
                                mComments.add(mComment);
                            }
                            Log.d("comment_viewModel", String.valueOf(mComments.size()));
                            setStatus("comment");
                        } catch (JSONException e) {
                            Log.d("comment_viewModel","parse failed");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }
}
