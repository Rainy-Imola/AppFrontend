package com.example.JTrace;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpAPI {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public void postApi_withToken(JSONObject jsonObject,String address,Callback callback,String token) throws IOException{
        final String Response="";
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(jsonObject));
        Request request = new Request.Builder().url(Constants.baseUrl + address)
                .addHeader("Authorization",token)
                .post(requestBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
    public void postApi(JSONObject jsonObject, String address,Callback callback) throws IOException {
        final String Response="";
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(jsonObject));
        Request request = new Request.Builder().url(Constants.baseUrl + address)
                .post(requestBody).build();
        okHttpClient.newCall(request).enqueue(callback);
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("error", e.getMessage().toString());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                System.out.println(Response);
//            }
//
//        });
    }

    public void getApi(String address,String token) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Constants.baseUrl + address)
                .method("GET",null)
                .addHeader("Authorization",token)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", e.getMessage().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
                Log.d("debug",response.body().string());
            }
        });
    }

    /*
     * Todo:
     * 只能打印返回信息，没有返回值
     */
}
