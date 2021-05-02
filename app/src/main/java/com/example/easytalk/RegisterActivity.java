package com.example.easytalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {
    private Button registerBtn;
    private Button sendBtn;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private EditText emailEdit;
    private EditText codeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        usernameEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);
        emailEdit = (EditText) findViewById(R.id.email);
        codeEdit = (EditText) findViewById(R.id.codeEdit);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            Toast.makeText(RegisterActivity.this,"网络出错,请检查网络",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String res = response.body().string();
                            Log.d("Login_info","连接成功");
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
                String password = passwordEdit.getText().toString();
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
                            Log.d("Register","连接成功");

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
                                        jsonObject1.put("password", password);
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

                                                    JSONArray data = (JSONArray) result1.get("data");
                                                    status1 = (Integer) result1.get("status");
                                                    id = (Integer) data.getJSONObject(0).get("id");
                                                    username = (String) data.getJSONObject(0).get("username");
                                                    password = (String) data.getJSONObject(0).get("password");
                                                    token = (String) data.getJSONObject(1).get("token");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                if (status1 == 0) {
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
                                                    Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Toast.makeText(RegisterActivity.this, "Wrong CAPTCHA", Toast.LENGTH_SHORT).show();
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
}