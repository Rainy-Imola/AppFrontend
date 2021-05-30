package com.example.JTrace;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.JTrace.Constants.baseUrl;
import static com.example.JTrace.Constants.pictureUrl;

public class PublishActivity extends baseActivity {
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    private EditText editAuthor;
    private TextInputLayout editContent;
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    private static final int REQUEST_CODE_COVER_IMAGE = 101;
    private static final String COVER_IMAGE_TYPE = "image/*";
    private Uri coverImageUri;
    private SimpleDraweeView coverSD;
    private Button publish;
    private Button selectPicture;
    private messagePost api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(PublishActivity.this);
        setContentView(R.layout.activity_publish);

        editContent = (TextInputLayout) findViewById(R.id.textField);
        coverSD = (SimpleDraweeView) findViewById(R.id.sd_cover);
        publish = (Button)findViewById(R.id.publish_btn);

        coverSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(REQUEST_CODE_COVER_IMAGE, COVER_IMAGE_TYPE,"Select Picture");
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    publishContent();
                    finish();
            }
        });
    }

    private void publishContent()  {

        byte[] coverImageData = readDataFromUri(coverImageUri);
        if(coverImageData == null)
        {
            Publish_core("null");
            return;
        }
        Log.i("length", String.valueOf(coverImageData.length));

        if (coverImageData.length == 0) {
            Toast.makeText(this, "Picture not exists", Toast.LENGTH_SHORT).show();
            Publish_core("null");
            return;
        }
        if ( coverImageData.length >= MAX_FILE_SIZE) {
            Toast.makeText(this, "Size of file is too large", Toast.LENGTH_SHORT).show();
            Publish_core("null");
            return;
        }
        OkHttpClient pictureClient = new OkHttpClient();
        if (coverImageData != null) {
            MultipartBody.Part coverPart = MultipartBody.Part.createFormData("image", "cover.png",
                    RequestBody.create(MediaType.parse("multipart/form-data"), coverImageData));
            RequestBody requestBody = new MultipartBody.Builder().addPart(coverPart).build();
            Log.i("starting", "Start loading");
            Request request = new Request.Builder().url(pictureUrl).post(requestBody).build();
            pictureClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("failure", "uploading failed" + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Headers headers = response.headers();
                    for (int i = 0; i < headers.size(); i++) {
                        Log.i("header:", headers.name(i) + ":" + headers.value(i));
                    }
                    try {
                        JSONObject res  = new JSONObject( response.body().string());
                        String picture = (String)res.get("name");
                        Publish_core(pictureUrl+"/" + picture + ".jpg");
                        Looper.prepare();
                        Toast.makeText(PublishActivity.this, "Uploading picture successfully", Toast.LENGTH_SHORT).show();
                        Looper.loop();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_COVER_IMAGE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                coverImageUri = data.getData();
                coverSD.setImageURI(coverImageUri);

                if (coverImageUri != null) {
                    Log.d("infomation_tag", "pick cover image " + coverImageUri.toString());
                } else {
                    Log.d("infomation_tag", "uri2File fail " + data.getData());
                }
            } else {
                Log.d("infomation_tag", "file pick fail");
            }
        }
    }

    private void getFile(int requestCode, String type, String title) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);
    }
    private byte[] readDataFromUri(Uri uri) {
        byte[] data = null;
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            data = Util.inputStream2bytes(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    private void Publish_core(String picture){
        String content = editContent.getEditText().getText().toString();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        int author = sharedPreferences.getInt("id", 0);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("author", sharedPreferences.getInt("id", 0));
            jsonObject.put("content", content);
            if (picture != "null") {
                jsonObject.put("picture", picture);
            }else {
                jsonObject.put("picture", "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //publish part
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = RequestBody.create(JSON,String.valueOf(jsonObject));
        Request request = new Request.Builder().url(baseUrl + "/msgboard/release").post(formBody).addHeader("Authorization", token).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("failure","Uploading failed" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("response",response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.i("header:", headers.name(i) + ":" + headers.value(i));
                }
                Log.i("onResponse: ", response.body().string());
                Looper.prepare();
                Toast.makeText(PublishActivity.this, "Uploading message successfully", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }
}