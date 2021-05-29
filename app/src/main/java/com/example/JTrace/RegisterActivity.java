package com.example.JTrace;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class  RegisterActivity extends AppCompatActivity {
    private Button registerBtn;
    private Button sendBtn;
    private EditText usernameEdit;
    private EditText password1Edit;
    private EditText password2Edit;
    private EditText emailEdit;
    private EditText codeEdit;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        TextView tv_title = (TextView) findViewById(R.id.title_w);
//        tv_title.setText("注册");
//        tv_title.setGravity(Gravity.CENTER);

        this.getSupportActionBar().hide();//注意是在 setContentView(R.layout.activity_main)后
        registerBtn = (Button) findViewById(R.id.registerBtn);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        usernameEdit = (EditText) findViewById(R.id.username);
        password1Edit = (EditText) findViewById(R.id.password1);
        password2Edit = (EditText) findViewById(R.id.password2);

        emailEdit = (EditText) findViewById(R.id.email);
        codeEdit = (EditText) findViewById(R.id.codeEdit);
        time = new TimeCount(60000, 1000);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                time.start();
                String email = emailEdit.getText().toString();

                HttpAPI httpAPI = new HttpAPI();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    httpAPI.postApi(jsonObject, "/email/sendemail", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("error", e.getMessage().toString());
                            Toast.makeText(RegisterActivity.this, "网络出错,请检查网络", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String res = response.body().string();
                            JSONObject result = null;
                            Integer status = -100;
                            try {
                                result = new JSONObject(res);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                status = (Integer) result.get("status");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (status != 0) {
                                Looper.prepare();
                                Toast.makeText(RegisterActivity.this, "You should register with a SJTU email", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                            Log.d("Login_info", "连接成功");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEdit.getText().toString();
                String password1 = password1Edit.getText().toString();
                String password2 = password2Edit.getText().toString();
                String email = emailEdit.getText().toString();
                String code = codeEdit.getText().toString();

                HttpAPI httpAPI = new HttpAPI();

                // 1. check CAPTCHA
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email);
                    jsonObject.put("code", code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    httpAPI.postApi(jsonObject, "/email/checkemail", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("error", e.getMessage().toString());
                            Toast.makeText(RegisterActivity.this, "网络出错,请检查网络", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String res = response.body().string();
                            Log.d("Register", "连接成功");

                            JSONObject result = null;
                            Integer status = -100;
                            String msg = null;

                            try {
                                result = new JSONObject(res);
                                status = (Integer) result.get("status");
                                if (status == 0) {
                                    Log.d("Register", "CAPTCHA right");

                                    // 2. post register request
                                    HttpAPI httpAPI1 = new HttpAPI();

                                    JSONObject jsonObject1 = new JSONObject();
                                    try {
                                        jsonObject1.put("username", username);
                                        jsonObject1.put("password1", password1);
                                        jsonObject1.put("password2", password2);
                                        jsonObject1.put("email", email);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        httpAPI1.postApi(jsonObject1, "/users/register", new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                Log.e("error", e.getMessage().toString());
                                                Toast.makeText(RegisterActivity.this, "网络出错,请检查网络", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String res1 = response.body().string();
                                                Log.d("Register", "连接成功");

                                                JSONObject result1 = null;
                                                Integer status1 = -100;
                                                Integer id = 0;
                                                String username = null;
                                                String password = null;
                                                String email = null;
                                                String token = null;

                                                try {
                                                    result1 = new JSONObject(res1);
                                                    Log.e("Register", result1.toString());
                                                    status1 = (Integer) result1.get("status");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                if (status1 == 0) {
                                                    try {
                                                        JSONArray data = (JSONArray) result1.get("data");
                                                        id = (Integer) data.getJSONObject(0).get("id");
                                                        username = (String) data.getJSONObject(0).get("username");
                                                        password = (String) data.getJSONObject(0).get("password");
                                                        token = (String) data.getJSONObject(1).get("token");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    SharedPreferences user_profile = getSharedPreferences("user_profile", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = user_profile.edit();
                                                    editor.putInt("status", 0);
                                                    editor.putInt("id", id);
                                                    editor.putString("username", username);
                                                    editor.putString("password", password);
                                                    editor.putString("token", token);
                                                    editor.commit();
                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    if (status1 == -100) {
                                                        Looper.prepare();
                                                        Toast.makeText(RegisterActivity.this, "用户名重复", Toast.LENGTH_SHORT).show();
                                                        Looper.loop();
                                                    } else if (status1 == -99) {
                                                        Looper.prepare();
                                                        Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                                                        Looper.loop();
                                                    }
                                                    if (status1 == -98) {
                                                        Looper.prepare();
                                                        Toast.makeText(RegisterActivity.this, "邮箱重复，请更改邮箱", Toast.LENGTH_SHORT).show();
                                                        Looper.loop();
                                                    }
                                                    if (status1 == -97) {
                                                        Looper.prepare();
                                                        Toast.makeText(RegisterActivity.this, "邮箱格式不合要求", Toast.LENGTH_SHORT).show();
                                                        Looper.loop();
                                                    } else {
                                                        Looper.prepare();
                                                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                                        Looper.loop();
                                                    }

                                                }
                                            }
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Looper.prepare();
                                    Toast.makeText(RegisterActivity.this, "Wrong CAPTCHA", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); //参数依次为总时长,和计时的时间间隔
        }


        @SuppressLint("ResourceAsColor")
        @Override
        public void onFinish() {
            sendBtn.setText("Send");
            sendBtn.setBackgroundColor(0xee6699ff);
            sendBtn.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) { //计时过程显示
            sendBtn.setClickable(false);
            sendBtn.setBackgroundColor(0xff888888);
            sendBtn.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}