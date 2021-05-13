package com.example.easytalk.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.easytalk.R;
import com.example.easytalk.model.chatMsg;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainChatActivity extends AppCompatActivity {
    private List<chatMsg> msglist = new ArrayList<chatMsg>();
    private ChatAdapter mAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button send;
    private EditText editText;
    private ChatClient chatClient ;
    //Service things
    private ChatService.JWebSocketClientBinder binder;
    private ChatService chatService;
    private ChatMessageReceiver chatMessageReceiver;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("ChatMainActivity","服务与活动成功绑定");
            binder = (ChatService.JWebSocketClientBinder) service;
            chatService = binder.getService();
            chatClient = chatService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("ChatMainActivity","服务与活动成功断开");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_main_chat);
        initMsg();
        startChatService();
        bindservice();
        doRegisterReceiver();
        recyclerView = findViewById(R.id.chat_list);
        swipeRefreshLayout = findViewById(R.id.swipe_chat);
        send = findViewById(R.id.btn_send);
        editText = findViewById(R.id.et_content);
        initChatUi();
        //initChat();
        //upDateUI();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        mAdapter.setmItems(msglist);
                        Toast.makeText(MainChatActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                    }
                },2000);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();

            }//pay attention
        });
    }

    private void bindservice(){
        Intent bindIntent = new Intent(MainChatActivity.this, ChatService.class);
        bindService(bindIntent,serviceConnection,BIND_AUTO_CREATE);
    }

    private void startChatService(){
        Intent intent = new Intent(MainChatActivity.this, ChatService.class);
        startService(intent);
    }

    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.xch.servicecallback.content");
        registerReceiver(chatMessageReceiver, filter);
    }

    private class ChatMessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("msg");
            Log.d("updateUI:",message);
            chatMsg item1 = new chatMsg("s","r", 1,message);
            msglist.add(item1);
            initChatUi();
        }
    }

    private void initMsg() {
        chatMsg item1 = new chatMsg("s","r", 0, "你好");
        msglist.add(item1);
        chatMsg item2 = new chatMsg("r","s", 1, "你好");
        msglist.add(item2);
        chatMsg item3 = new chatMsg("s","r", 0, "你好");
        msglist.add(item3);
    }

    private void initChatUi(){
        LinearLayoutManager mLinearManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearManager);
        mAdapter = new ChatAdapter(msglist);
        recyclerView.setAdapter(mAdapter);
    }



    private  void sendMessage(){
        String content = editText.getText().toString();
        if(TextUtils.isEmpty(content)){
            Toast.makeText(MainChatActivity.this,"发送内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        editText.setText("");
        Log.d("send event:","content");
        chatMsg msg = new chatMsg("test2","test2",0,content);
        msglist.add(msg);
        mAdapter.setmItems(msglist);
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("TO","test2");
            jsonObject.put("From","test2");
            jsonObject.put("message", "hello");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (chatClient != null && chatClient.isOpen()) {
            chatClient.send(String.valueOf(jsonObject));
        }
    }

//    private void initChat(){
//        System.out.println("Begin to Connection");
//        URI uri = null;
//        try {
//            uri = new URI("ws://echo.websocket.org");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        chatClient = new ChatClient(uri){
//            @Override
//            public void onMessage(String message) {
//                super.onMessage(message);
//                chatMsg msg = new chatMsg("test2","test2",1,message);
//                msglist.add(msg);
//                mAdapter.setmItems(msglist);
//            }
//        };
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Begin to Connection");
//
//                //try to connect
//                try {
//                    chatClient.connectBlocking();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return;
//                }
//                JSONObject msgbody = new JSONObject();
//                try {
//                    msgbody.put("From", "test2");
//                    msgbody.put("To", "test2");
//                    msgbody.put("msgContent", "Nice to meet you");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                chatClient.send("hello");
//                if (chatClient != null && chatClient.isOpen()) {
//                    chatClient.send(String.valueOf(msgbody));
//                }
//
//
//            }
//        }).start();
//    }
//


    }