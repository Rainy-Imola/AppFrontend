package com.example.JTrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.JTrace.model.friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private Button register;
    private TextView forgetpw;
    private EditText usernameEdit;
    private EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getSupportActionBar().hide();
        checkOut();
        login = (Button) findViewById(R.id.log_in);
        register = (Button) findViewById(R.id.register);
        forgetpw = (Button) findViewById(R.id.forget_pw);
        usernameEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);

        // login onclick event
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpAPI httpAPI = new HttpAPI();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    httpAPI.postApi(jsonObject, "/users/login", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(LoginActivity.this,"请求失败，请检查你的网络连接", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res = response.body().string();
                        JSONObject result= null;
                        int status = -100;
                        String msg = null;
                        int id = 0;
                        String username = null;
                        String password = null;
                        String token = null;
                        String avatar = null;

                        try {
                            result = new JSONObject(res);
                            status = (int) result.get("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (status != 0) {
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            return;
                        }
                        try {
                            JSONArray data = (JSONArray) result.get("data");
                            status = (int)result.get("status");
                            msg = (String)result.get("msg");
                            id = (int)data.getJSONObject(0).get("id");
                            username = (String)data.getJSONObject(0).get("username");
                            password = (String)data.getJSONObject(0).get("password");
                            avatar = (String)data.getJSONObject(0).get("avatar");
                            token = (String)data.getJSONObject(1).get("token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (status == 0) {
                            SharedPreferences user_profile = getSharedPreferences("user_profile", MODE_PRIVATE);
                            SharedPreferences.Editor editor = user_profile.edit();
                            editor.putInt("status", 0);
                            editor.putInt("id", id);
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putString("token", token);
                            editor.putString("avatar",avatar);
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Register onclick event
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Forget password onclick event
        forgetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    // Checking user_profile for login automatically
    private void checkOut(){
        int status;
        SharedPreferences sharedPreferences = getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        status = sharedPreferences.getInt("status", -1);
        if (status == 0) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

}
