package com.example.easytalk;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easytalk.model.message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FormUrlEncoded;

public class PublishActivity extends AppCompatActivity {
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    private EditText editAuthor;
    private EditText editContent;
    private Button btn;
    private messagePost api;
    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
//        initNetwork();
        editAuthor = (EditText)findViewById(R.id.author);
        editContent = (EditText)findViewById(R.id.content);
        btn = (Button)findViewById(R.id.publish_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishContent();
            }
        });
    }

    private void publishContent(){
        String name = editAuthor.getText().toString();
        String content = editContent.getText().toString();
        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(content)){
//            Toast.makeText(this,"信息不完整",Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("author",name);
            jsonObject.put("content",content);
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.i("json",String.valueOf(jsonObject));
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody formBody = RequestBody.create(JSON,String.valueOf(jsonObject));
        Request request = new Request.Builder().url("http://47.103.123.145/msgboard/release").post(formBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.i("failure","上传失败"+call.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("response",response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.i("header:", headers.name(i) + ":" + headers.value(i));
                }
                Log.i("onResponse: " ,response.body().string());
            }
        });

//        MultipartBody.Part authorPart = MultipartBody.Part.createFormData("author",name);
//        MultipartBody.Part contentPart = MultipartBody.Part.createFormData("content",content);
//
//        Call<message> call = api.submitMessage(authorPart,contentPart);
//        call.enqueue(new Callback<message>() {
//            @Override
//            public void onResponse(Call<message> call, Response<message> response) {
//                Toast.makeText(PublishActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<message> call, Throwable t) {
//                Toast.makeText(PublishActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
//            }
//        });

    }

//    private void initNetwork() {
//        retrofit=new Retrofit.Builder()
//                .baseUrl(Constants.baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        Log.i("print test","aa");
//        api=retrofit.create(messagePost.class);
//    }

}