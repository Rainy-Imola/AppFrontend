package com.example.JTrace.board_fragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.JTrace.Constants;
//import com.example.JTrace.model.comment;

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

public class commentViewModel extends AndroidViewModel {
//    private List<comment> mComments=new ArrayList<>();
    private MutableLiveData<String> status = new MutableLiveData<>();
    private SharedPreferences sharedPreferences;
    private SavedStateHandle handle;
    private int mCommentPage;
    private int mReplyPage;


    private String message_id;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }



    private CustomCommentModel mCustomCommentModel = new CustomCommentModel();
    private List<CustomCommentModel.CustomComment> mCustomComments ;
    public CustomCommentModel getmCustomCommentModel() {
        return mCustomCommentModel;
    }

    public void setmCustomCommentModel(CustomCommentModel mCustomCommentModel) {
        this.mCustomCommentModel = mCustomCommentModel;
    }

    public MutableLiveData<String> getStatus() {
        return status;
    }
    public commentViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle=handle;
        sharedPreferences=application.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
    }
//    public List<comment> getmComments(){
//        return this.mComments;
//    }
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
                Request request = new Request.Builder().url(Constants.baseUrl + "/msgboard/"+message_id)
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
                                //process date
                                JSONObject json_date=cur_comment.getJSONObject("date");
                                String yyyy=String.valueOf(json_date.getInt("year")+1900);
                                String MM=String.valueOf(json_date.getInt("month")+1);
                                String dd=String.valueOf(json_date.getInt("date"));
                                String hh=String.valueOf(json_date.getInt("hours"));
                                String mm=String.valueOf(json_date.getInt("minutes"));
                                String ss=String.valueOf(json_date.getInt("seconds"));
                                String string_date=yyyy+"-"+MM+"-"+dd+" "+hh+":"+mm+":"+ss;
                                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date formatted_date=format.parse(string_date);

                                //String msg_id=cur_comment.getString("message");
//                                comment mComment=new comment(author,content,message_id,formatted_date);
//                                mComments.add(mComment);
                            }
//                            Log.d("comment_viewModel", String.valueOf(mComments.size()));
                            setStatus("comment");
                        } catch (JSONException | ParseException e) {
                            Log.d("comment_viewModel","parse failed");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    public void getCommentModel(int code, MessageDetailActivity.ActivityHandler activityHandler, int handlerId) {
        switch (code){
            case 1:
                getPageComment(activityHandler,handlerId);
                break;

        }
    }

    private void getPageComment(MessageDetailActivity.ActivityHandler activityHandler, int handlerId) {
        new Thread() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                String token = sharedPreferences.getString("token", "");
                Integer user_id = sharedPreferences.getInt("id", 2);
                Request request = new Request.Builder().url(Constants.baseUrl + "/msgboard/"+message_id)
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
                            mCustomComments = new ArrayList<>();
                            for(int i=result.length()-1;i>=0;i--){
                                JSONObject cur_comment=result.getJSONObject(i);
                                String author=cur_comment.getString("author");
                                String content=cur_comment.getString("content");
                                //process date
                                JSONObject json_date=cur_comment.getJSONObject("date");
                                String yyyy=String.valueOf(json_date.getInt("year")+1900);
                                String MM=String.valueOf(json_date.getInt("month")+1);
                                String dd=String.valueOf(json_date.getInt("date"));
                                String hh=String.valueOf(json_date.getInt("hours"));
                                String mm=String.valueOf(json_date.getInt("minutes"));
                                String ss=String.valueOf(json_date.getInt("seconds"));
                                String string_date=yyyy+"-"+MM+"-"+dd+" "+hh+":"+mm+":"+ss;
                                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date formatted_date=format.parse(string_date);
                                CustomCommentModel.CustomComment mcustomComment = new CustomCommentModel.CustomComment();
                                mcustomComment.setReplies(new ArrayList<CustomCommentModel.CustomComment.CustomReply >());
                                mcustomComment.setPosterName(author);
                                mcustomComment.setData(content);
                                mCustomComments.add(mcustomComment);
                                //String msg_id=cur_comment.getString("message");
//                                comment mComment=new comment(author,content,message_id,formatted_date);
//                                mComments.add(mComment);
                            }
                            mCustomCommentModel.setComments(mCustomComments);
                            Message message = Message.obtain();
                            message.what = handlerId;
                            message.obj = mCustomCommentModel;
                            activityHandler.sendMessage(message);
//                            setStatus("comment");
                        } catch (JSONException | ParseException e) {
                            Log.d("comment_viewModel","parse failed");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    public void postMessageComment(CustomCommentModel.CustomComment post_comment) {
        new Thread() {
            @Override
            public void run() {
                // @Headers({"Content-Type:application/json","Accept: application/json"})//需要添加头
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {

                    json.put("author",sharedPreferences.getInt("id",3));
                    json.put("content",post_comment.getData());
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
                        .url(Constants.baseUrl+"/msgboard/"+message_id+"/comments/release/")
                        .addHeader("Authorization",token)
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("failure","comment");
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
                                Log.d("save comment","success");
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
