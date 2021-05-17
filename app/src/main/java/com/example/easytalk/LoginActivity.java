package com.example.easytalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private Button register;
    private TextView forgetpw;
    private EditText usernameEdit;
    private EditText passwordEdit;
//    private EditText emailEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        TextView tv_title = (TextView) findViewById(R.id.title_w);
//        tv_title.setText("登录");
//        tv_title.setGravity(Gravity.CENTER);

        this.getSupportActionBar().hide();//在 setContentView(R.layout.activity_main)后
        //checkOut();
        login = (Button) findViewById(R.id.log_in);
        register = (Button) findViewById(R.id.register);
        forgetpw = (Button) findViewById(R.id.forget_pw);
        usernameEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);

        //登陆点击事件
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if(TextUtils.isEmpty(username)||TextUtils.isEmpty((password))){
                    Toast.makeText(LoginActivity.this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
                    Log.d("Login_info","用户名和密码不能为空");
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
                        Log.e("error", e.getMessage().toString());
                        Toast.makeText(LoginActivity.this,"网络出错,请检查网络",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res = response.body().string();
                        Log.d("Login_info","连接成功");
                        JSONObject result= null;
                        int status = -100;
                        String msg = null;
                        int id = 0;
                        String username = null;
                        String password = null;
                        String token = null;
                        try{
                            result = new JSONObject(res);
                            Log.e("Login_in",result.toString());
                            status = (int)result.get("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status!=0){
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            Log.d("Login_info","用户名和密码错误");
                            return;
                        }
                        try{
                           //result  = new JSONObject(res);

                           JSONArray data = (JSONArray) result.get("data");
                           status = (int)result.get("status");
                           msg = (String)result.get("msg");
                           id = (int)data.getJSONObject(0).get("id");
                           username = (String)data.getJSONObject(0).get("username");
                           System.out.println(username);
                           password = (String)data.getJSONObject(0).get("password");
                           System.out.println(password);
                           token = (String)data.getJSONObject(1).get("token");
                           System.out.println(token);
                            Log.d("Login_info",msg);
                            Log.d("Login_info", String.valueOf(status));
                        } catch (JSONException e) {
                            Log.d("Login_info",e.getMessage());
                            e.printStackTrace();
                        }
                        if(status==0){
                            SharedPreferences user_profile = getSharedPreferences("user_profile",MODE_PRIVATE);
                            SharedPreferences.Editor editor = user_profile.edit();
                            editor.putInt("status",0);
                            editor.putInt("id",id);
                            editor.putString("username",username);
                            editor.putString("password",password);
                            editor.putString("token",token);
                            editor.commit();
                            //Toast.makeText(LoginActivity.this,"success",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            
                        }else{
                            Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                        }

                    }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //注册点击事件
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //忘记密码
        forgetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });
    }
    //通过保存信息检查是否自动登录
    void checkOut(){
        int status;
        SharedPreferences sharedPreferences = getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        status = sharedPreferences.getInt("status",-1);
        System.out.println(status);
        if(status==0){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
