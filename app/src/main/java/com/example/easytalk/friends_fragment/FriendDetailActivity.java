package com.example.easytalk.friends_fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easytalk.Constants;
import com.example.easytalk.R;
import com.example.easytalk.model.friend;
import com.example.easytalk.model.message;

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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FriendDetailActivity extends AppCompatActivity {
    private TextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        info =getWindow().getDecorView().findViewById(R.id.FriendDetail);
        int id = getIntent().getIntExtra("id",0);
        String name = getIntent().getStringExtra("name");
        info.setText(id+"\n"+name);
    }
}