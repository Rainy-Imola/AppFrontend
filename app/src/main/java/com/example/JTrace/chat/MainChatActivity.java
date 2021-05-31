package com.example.JTrace.chat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.JTrace.MainActivity;
import com.example.JTrace.R;
import com.example.JTrace.baseActivity;
import com.example.JTrace.model.chatMsg;
import com.loper7.layout.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainChatActivity extends baseActivity {
    private List<chatMsg> msglist = new ArrayList<chatMsg>();
    private List<chatMsg> nowList = new ArrayList<chatMsg>();
    private ChatAdapter mAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button send;
    private ImageView back;
    private EditText editText;
    private TitleBar titleBar;
    private Context mContext;
    private ChatClient chatClient;
    private InputMethodManager inputMethodManager;
    //Service things
    private ChatService.JWebSocketClientBinder binder;
    private ChatService chatService;
    private ChatMessageReceiver chatMessageReceiver;
    //variable used
    private String To;
    private String From;
    private String senderImage;
    private String friendImage;
    private int status;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("ChatMainActivity", "服务与活动成功绑定");
            binder = (ChatService.JWebSocketClientBinder) service;
            chatService = binder.getService();
            chatClient = chatService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("ChatMainActivity", "服务与活动成功断开");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        To = getIntent().getStringExtra("name");
        friendImage = getIntent().getStringExtra("avatar");
        status = getIntent().getIntExtra("status", 0);
        SharedPreferences sharedPreferences = getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        From = sharedPreferences.getString("username", null);
        senderImage = sharedPreferences.getString("avatar", null);

        bindservice();
        doRegisterReceiver();

        recyclerView = findViewById(R.id.chat_list);
        swipeRefreshLayout = findViewById(R.id.swipe_chat);
        send = findViewById(R.id.btn_send);
        back = findViewById(R.id.back_to_friend);
        editText = findViewById(R.id.et_content);
        titleBar = findViewById(R.id.title_bar);

        if (status != 0) {
            titleBar.setTitleText(To + ":离线");
            titleBar.setTitleTextColor(Color.GRAY);
        } else {
            titleBar.setTitleText(To);
        }

        mContext = titleBar.getContext();

        inputMethodManager = (InputMethodManager) getSystemService(MainChatActivity.this.INPUT_METHOD_SERVICE);
        initChatUi();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullHistory();
                        swipeRefreshLayout.setRefreshing(false);
                        mAdapter.setmItems(msglist);
                        Toast.makeText(MainChatActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(To, From);

            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                return false;
            }
        });
        titleBar.setOnBackListener(new TitleBar.OnBackListener() {
            @Override
            public void onBackClick() {
                finish();
                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    private void bindservice() {
        Intent bindIntent = new Intent(MainChatActivity.this, ChatService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }


    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.xch.servicecallback.content");
        registerReceiver(chatMessageReceiver, filter);
    }

    private class ChatMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("msg");
            JSONObject jsonObject = null;
            String From = null;
            String To = null;
            String content = null;
            try {
                jsonObject = new JSONObject(message);
                To = (String) jsonObject.get("to");
                From = (String) jsonObject.get("from");
                content = (String) jsonObject.get("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            chatMsg item1 = new chatMsg(From, To, 1, content, friendImage);
            msglist.add(item1);
            nowList.add(item1);
            initChatUi();
        }
    }


    private void initChatUi() {
        LinearLayoutManager mLinearManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearManager);
        mAdapter = new ChatAdapter(msglist);
        recyclerView.setAdapter(mAdapter);
    }


    private void sendMessage(String To, String From) {
        String content = editText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(MainChatActivity.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        editText.setText("");
        chatMsg msg = new chatMsg("test2", "test2", 0, content, senderImage);
        msglist.add(msg);
        mAdapter.setmItems(msglist);
        if (chatClient != null && chatClient.isOpen()) {
            chatService.sendMsg(To, From, content);
        }
        initChatUi();
    }

    private void pullHistory() {
        List<chatMsg> historyChatMessage = chatService.getMessageList();
        List<chatMsg> tempList = new ArrayList<chatMsg>();
        for (int i = 0; i < historyChatMessage.size(); i++) {
            chatMsg temp = historyChatMessage.get(i);
            if (From.equals(temp.getReceiveID()) && To.equals(temp.getSenderID())) {
                temp.setAvatar(friendImage);
                tempList.add(temp);
            } else if (To.equals(temp.getReceiveID()) && From.equals(temp.getSenderID())) {
                temp.setAvatar(senderImage);
                tempList.add(temp);
            }
        }
        msglist = tempList;
        initChatUi();
    }
}