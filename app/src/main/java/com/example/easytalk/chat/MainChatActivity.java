package com.example.easytalk.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
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
    boolean up = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initMsg();

        setContentView(R.layout.activity_main_chat);
        recyclerView = findViewById(R.id.chat_list);
        swipeRefreshLayout = findViewById(R.id.swipe_chat);
        send = findViewById(R.id.btn_send);
        editText = findViewById(R.id.et_content);
        LinearLayoutManager mLinearManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearManager);
        mAdapter = new ChatAdapter(msglist);
        recyclerView.setAdapter(mAdapter);
        initChat();
        upDateUI();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainChatActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                    }
                },2000);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void initMsg() {
        chatMsg item1 = new chatMsg("s","r", 0, "你好");
        msglist.add(item1);
        chatMsg item2 = new chatMsg("r","s", 1, "你好");
        msglist.add(item2);
        chatMsg item3 = new chatMsg("s","r", 0, "你好");
        msglist.add(item3);
    }

    private void initChat(){
        System.out.println("Begin to Connection");
        URI uri = null;
        try {
            uri = new URI("ws://echo.websocket.org");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        chatClient = new ChatClient(uri){
            @Override
            public void onMessage(String message) {
                super.onMessage(message);
                chatMsg msg = new chatMsg("test2","test2",1,message);
                msglist.add(msg);
                mAdapter.setmItems(msglist);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Begin to Connection");

                //try to connect
                try {
                    chatClient.connectBlocking();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                JSONObject msgbody = new JSONObject();
                try {
                    msgbody.put("From", "test2");
                    msgbody.put("To", "test2");
                    msgbody.put("msgContent", "Nice to meet you");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                chatClient.send("hello");
                if (chatClient != null && chatClient.isOpen()) {
                    chatClient.send(String.valueOf(msgbody));
                }


            }
        }).start();
    }
    private  void sendMessage(){
        String content = editText.getText().toString();
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
    private void upDateUI(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mAdapter.setmItems(msglist);
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        up = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(up){
            up=false;
        }
    }
}