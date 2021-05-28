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
import com.example.JTrace.model.message;
//import com.example.JTrace.model.comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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

    public MutableLiveData<message> getMessageMutableLiveData() {
        return messageMutableLiveData;
    }

    public void setMessageMutableLiveData(message messageMutableLiveData) {
        this.messageMutableLiveData.postValue(messageMutableLiveData);
    }

    private MutableLiveData<message> messageMutableLiveData = new MutableLiveData<>();

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

    public void setStatus(String status){
        this.status.postValue(status);
    }



    public void getCommentModel(int code, MessageDetailActivity.ActivityHandler activityHandler, int handlerId) {
        switch (code){
            case 1:
                getPageCommentAndDetail(activityHandler,handlerId);
                break;

        }
    }

    private void getPageCommentAndDetail(MessageDetailActivity.ActivityHandler activityHandler, int handlerId) {
        new Thread() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                String token = sharedPreferences.getString("token", "");
                Integer user_id = sharedPreferences.getInt("id", 2);
                Request request = new Request.Builder().url(Constants.baseUrl + "/msgboard/"+message_id+"/"+user_id.toString())
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
                            if(results.isNull("code")) {
                                JSONArray data = results.getJSONArray("data");
                                JSONObject mid = data.getJSONObject(0);
                                JSONObject message_detail = mid.getJSONObject("message");
                                JSONArray result = mid.getJSONArray("comment");
                                message mmessage = new message();
                                mmessage.setAuthor(message_detail.getString("author"));
                                mmessage.setContent(message_detail.getString("content"));
                                mmessage.setCreatedAt(message_detail.getString("date"));
                                mmessage.setId(message_id);
                                mmessage.setLike(message_detail.getInt("like"));
                                mmessage.setImageUrl(message_detail.getString("picture"));
                                setMessageMutableLiveData(mmessage);

                                mCustomComments = new ArrayList<>();
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject cur_comment = result.getJSONObject(i);
                                    Integer id_comment = cur_comment.getInt("id");
                                    String author_comment = cur_comment.getString("author");
                                    String content_comment = cur_comment.getString("content");
                                    String date_comment = cur_comment.getString("date");
                                    JSONArray result_replys = cur_comment.getJSONArray("reply");
                                    List<CustomCommentModel.CustomComment.CustomReply> mreplys = new ArrayList<>();
                                    for (int j = 0;j<result_replys.length();j++){
                                        JSONObject cur_reply = result_replys.getJSONObject(j);
                                        Integer id_reply = cur_reply.getInt("id");
                                        String author_reply = cur_reply.getString("author");
                                        String content_reply = cur_reply.getString("content");
                                        String date_reply = cur_reply.getString("date");
                                        Integer level = cur_reply.getInt("level");
                                        Integer comment_id = cur_reply.getInt("comment");
                                        CustomCommentModel.CustomComment.CustomReply mreply = new CustomCommentModel.CustomComment.CustomReply();
                                        mreply.setData(date_reply);
                                        mreply.setId(id_reply);
                                        mreply.setLevel(level);
                                        mreply.setReplierName(author_reply);
                                        mreply.setData(content_reply);
                                        mreply.setComment_id(comment_id);
                                        mreplys.add(mreply);
                                    }
                                    for (CustomCommentModel.CustomComment.CustomReply mm: mreplys
                                         ) {
                                        Integer mlevel = mm.getLevel();
                                        if(mlevel !=0){
                                            if(mlevel<mreplys.size()) {
                                                CustomCommentModel.CustomComment.CustomReply replied = mreplys.get(mlevel-1);
                                                mm.setRepliedName(replied.getReplierName());
                                                mm.setData("回复 @"+mm.getRepliedName()+": " + mm.getData());
                                            }
                                        }
                                    }
                                    

                                    CustomCommentModel.CustomComment mcustomComment = new CustomCommentModel.CustomComment();
                                    mcustomComment.setReplies(mreplys);
                                    mcustomComment.setPosterName(author_comment);
                                    mcustomComment.setData(content_comment);
                                    mcustomComment.setId(id_comment);
                                    mcustomComment.setDate(date_comment);
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
                            }else if(results.getInt("code") == 401){
                                Log.d("token","error");
                            }
                        } catch (JSONException e) {
                            Log.d("comment_viewModel","parse failed");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    public void postReply(MessageDetailActivity.ActivityHandler activityHandler, CustomCommentModel.CustomComment.CustomReply post_reply){
        new Thread() {
            @Override
            public void run() {
                // @Headers({"Content-Type:application/json","Accept: application/json"})//需要添加头
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {

                    json.put("author",sharedPreferences.getString("username","test2"));
                    json.put("content",post_reply.getData());
                    json.put("comment",post_reply.getComment_id());
                    json.put("level",post_reply.getLevel());
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
                        .url(Constants.baseUrl+"/reply/release/")
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
                                getPageCommentAndDetail(activityHandler,2);
                                Log.d("save reply","success");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }.start();
    }



    public void postMessageComment(MessageDetailActivity.ActivityHandler activityHandler, CustomCommentModel.CustomComment post_comment) {
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
                                getPageCommentAndDetail(activityHandler,2);
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
