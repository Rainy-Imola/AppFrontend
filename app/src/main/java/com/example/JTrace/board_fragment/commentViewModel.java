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
import com.example.JTrace.model.User;
import com.example.JTrace.model.message;
//import com.example.JTrace.model.comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class commentViewModel extends AndroidViewModel {
    private MutableLiveData<String> status = new MutableLiveData<>();
    private SharedPreferences sharedPreferences;
    private SavedStateHandle handle;
    private int mCommentPage;
    private int mReplyPage;
    private message mmessage = new message();
    private User mUser = new User();

    public Map<String, String> getAvatar_map() {
        return avatar_map;
    }

    public void setAvatar_map(Map<String, String> avatar_map) {
        this.avatar_map = avatar_map;
    }

    private Map<String,String> avatar_map = new ConcurrentHashMap<>();
    public MutableLiveData<Map<String, String>> getResult_avatar_map() {
        return result_avatar_map;
    }

    public void setResult_avatar_map(Map<String, String> result_avatar_map) {
        this.result_avatar_map.postValue(result_avatar_map);
    }

    private MutableLiveData<Map<String,String>> result_avatar_map = new MutableLiveData<>();

    public MutableLiveData<message> getMessageMutableLiveData() {
        return messageMutableLiveData;
    }

    public void setMessageMutableLiveData(message messageMutableLiveData) {
        this.messageMutableLiveData.postValue(messageMutableLiveData);
    }

    private MutableLiveData<message> messageMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void setUserMutableLiveData(User userMutableLiveData) {
        this.userMutableLiveData.postValue(userMutableLiveData);
    }
    private String message_id;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }


    private CustomCommentModel mCustomCommentModel = new CustomCommentModel();
    private List<CustomCommentModel.CustomComment> mCustomComments;

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
        this.handle = handle;
        sharedPreferences = application.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
    }

    public void setStatus(String status) {
        this.status.postValue(status);
    }


    public void getCommentModel(int code, MessageDetailActivity.ActivityHandler activityHandler, int handlerId) {
        switch (code) {
            case 1:
                getPageCommentAndDetail(activityHandler, handlerId);
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
                Request request = new Request.Builder().url(Constants.baseUrl + "/msgboard/" + message_id + "/" + user_id.toString())
                        .addHeader("Authorization", token)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("comment_viewModel", "requestData failed");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res = response.body().string();
                        try {
                            JSONObject results = new JSONObject(res);
                            if (results.isNull("code")) {
                                JSONArray data = results.getJSONArray("data");

                                JSONObject mid = data.getJSONObject(0);
                                JSONObject message_detail = mid.getJSONObject("message");
                                JSONArray result = mid.getJSONArray("comment");
                                int m_accept = mid.getInt("accept");


                                mmessage.setAuthor(message_detail.getString("author"));
                                mmessage.setContent(message_detail.getString("content"));
                                mmessage.setCreatedAt(message_detail.getString("date"));
                                mmessage.setId(message_id);
                                mmessage.setLike(message_detail.getInt("like"));
                                mmessage.setImageUrl(message_detail.getString("picture"));
                                mmessage.setAccept(m_accept);
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
                                    for (int j = 0; j < result_replys.length(); j++) {
                                        JSONObject cur_reply = result_replys.getJSONObject(j);
                                        Integer id_reply = cur_reply.getInt("id");
                                        String author_reply = cur_reply.getString("author");
                                        String content_reply = cur_reply.getString("content");
                                        String date_reply = cur_reply.getString("date");
                                        Integer level = cur_reply.getInt("level");
                                        Integer comment_id = cur_reply.getInt("comment");
                                        CustomCommentModel.CustomComment.CustomReply mreply = new CustomCommentModel.CustomComment.CustomReply();
                                        mreply.setDate(date_reply);
                                        mreply.setId(id_reply);
                                        mreply.setLevel(level);
                                        mreply.setReplierName(author_reply);
                                        mreply.setData(content_reply);
                                        mreply.setComment_id(comment_id);
                                        mreplys.add(mreply);
                                    }
                                    for (CustomCommentModel.CustomComment.CustomReply mm : mreplys
                                    ) {
                                        Integer mlevel = mm.getLevel();
                                        if (mlevel != 0) {
                                            if (mlevel < mreplys.size()) {
                                                CustomCommentModel.CustomComment.CustomReply replied = mreplys.get(mlevel - 1);
                                                mm.setRepliedName(replied.getReplierName());
                                                mm.setData("回复 @" + mm.getRepliedName() + ": " + mm.getData());
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
                                }
                                mCustomCommentModel.setComments(mCustomComments);
                                Message message = Message.obtain();
                                message.what = handlerId;
                                message.obj = mCustomCommentModel;
                                activityHandler.sendMessage(message);
                            } else if (results.getInt("code") == 401) {
                                Log.d("token", "error");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    public void postReply(MessageDetailActivity.ActivityHandler activityHandler, CustomCommentModel.CustomComment.CustomReply post_reply) {
        new Thread() {
            @Override
            public void run() {
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("author", sharedPreferences.getString("username", "test2"));
                    json.put("content", post_reply.getData());
                    json.put("comment", post_reply.getComment_id());
                    json.put("level", post_reply.getLevel());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String token = sharedPreferences.getString("token", "");
                OkHttpClient okHttpClient = new OkHttpClient();
                //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
                //json为String类型的json数据
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                //创建一个请求对象
                Request request = new Request.Builder()
                        .url(Constants.baseUrl + "/reply/release/")
                        .addHeader("Authorization", token)
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("failure", "comment");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        try {
                            JSONObject json = new JSONObject(string);
                            int status = (int) json.get("status");
                            String msg = (String) json.get("msg");
                            if (status == 0) {
                                getPageCommentAndDetail(activityHandler, 2);
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
                    json.put("author", sharedPreferences.getInt("id", 3));
                    json.put("content", post_comment.getData());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String token = sharedPreferences.getString("token", "");
                OkHttpClient okHttpClient = new OkHttpClient();
                //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
                //json为String类型的json数据
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                //创建一个请求对象
                Request request = new Request.Builder()
                        .url(Constants.baseUrl + "/msgboard/" + message_id + "/comments/release/")
                        .addHeader("Authorization", token)
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("failure", "comment");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        try {
                            JSONObject json = new JSONObject(string);
                            int status = (int) json.get("status");
                            String msg = (String) json.get("msg");
                            if (status == 0) {
                                getPageCommentAndDetail(activityHandler, 2);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }

    public void postPrize(int like_status) {
        new Thread() {
            @Override
            public void run() {
                // @Headers({"Content-Type:application/json","Accept: application/json"})//需要添加头
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("user", sharedPreferences.getInt("id", 3));
                    json.put("like", like_status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String token = sharedPreferences.getString("token", "");
                OkHttpClient okHttpClient = new OkHttpClient();
                //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
                //json为String类型的json数据
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                //创建一个请求对象
                Request request = new Request.Builder()
                        .url(Constants.baseUrl + "/msgboard/like/" + message_id)
                        .addHeader("Authorization", token)
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("failure", "comment");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        try {
                            JSONObject json = new JSONObject(string);
                            int status = (int) json.get("status");
                            String msg = (String) json.get("msg");
                            if (status == 0) {
                                mmessage.setAccept(like_status);
                                int xx = mmessage.getLike();
                                mmessage.setLike(xx+1);
                                setMessageMutableLiveData(mmessage);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();

    }

    public void requestAvatar(String author) {
        new Thread() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                String token = sharedPreferences.getString("token", "");
                Request request = new Request.Builder().url(Constants.baseUrl + "/users/" + author + "/info")
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

    public void requestAllAvatar(String name){
        new Thread() {
            @Override
            public void run() {

                OkHttpClient okHttpClient = new OkHttpClient();
                String token = sharedPreferences.getString("token", "");
                Request request = new Request.Builder().url(Constants.baseUrl + "/users/" + name + "/info")
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
                                String avatar_link = (String) data.getJSONObject(0).get("avatar");
                                Log.d("request avatar:",avatar_link);
                                avatar_map.put(name,avatar_link);
                                setResult_avatar_map(avatar_map);
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
