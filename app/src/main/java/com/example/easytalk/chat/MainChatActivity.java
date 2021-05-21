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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.example.easytalk.MainActivity;
import com.example.easytalk.R;
import com.example.easytalk.baseActivity;
import com.example.easytalk.friends_fragment.FriendDetailActivity;
import com.example.easytalk.model.chatMsg;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
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
    private TextView chatName;
    private ChatClient chatClient ;
    private InputMethodManager inputMethodManager;
    //Service things
    private ChatService.JWebSocketClientBinder binder;
    private ChatService chatService;
    private ChatMessageReceiver chatMessageReceiver;
    //variable used
    private String To;
    private String From;
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
        setContentView(R.layout.activity_main_chat);
        To = getIntent().getStringExtra("name");
        SharedPreferences sharedPreferences = getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        From = sharedPreferences.getString("username","");

        //initMsg();
        //startChatService();
        bindservice();
        doRegisterReceiver();
        recyclerView = findViewById(R.id.chat_list);
        swipeRefreshLayout = findViewById(R.id.swipe_chat);
        send = findViewById(R.id.btn_send);
        back = findViewById(R.id.back_to_friend);
        editText = findViewById(R.id.et_content);
        chatName = findViewById(R.id.chat_name);
        chatName.setText(To);

        inputMethodManager = (InputMethodManager) getSystemService(MainChatActivity.this.INPUT_METHOD_SERVICE);
        initChatUi();
        //initChat();
        //upDateUI();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullHistory();
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
                sendMessage(To, From);

            }//pay attention
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
                return false;
            }

        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  finish();
                  Intent intent=new Intent(v.getContext(), MainActivity.class);
//                  intent.putExtra("id",0);
//                  intent.putExtra("name",From);
                  v.getContext().startActivity(intent);

            }
        });
    }

    private void bindservice(){
        Intent bindIntent = new Intent(MainChatActivity.this, ChatService.class);
        bindService(bindIntent,serviceConnection,BIND_AUTO_CREATE);
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
            JSONObject jsonObject = null;
            String From = null;
            String To = null;
            String content = null;
            try{
                jsonObject = new JSONObject(message);
                To = (String)jsonObject.get("to");
                From = (String)jsonObject.get("from");
                content = (String)jsonObject.get("message");
            }catch (JSONException e){
                e.printStackTrace();
            }
            chatMsg item1 = new chatMsg(From,To, 1,content);
            Log.d("receive"," receive add");
            msglist.add(item1);
            nowList.add(item1);
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
        //pullHistory();
    }



    private  void sendMessage(String To, String From){
        String content = editText.getText().toString();
        if(TextUtils.isEmpty(content)){
            Toast.makeText(MainChatActivity.this,"发送内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        editText.setText("");
        Log.d("send event:","content");
        chatMsg msg = new chatMsg("test2","test2",0,content);
        //nowList.add(msg);
        Log.d("send"," send add");
        msglist.add(msg);
        mAdapter.setmItems(msglist);
        if (chatClient != null && chatClient.isOpen()) {
            chatService.sendMsg(To, From, content);
        }
        initChatUi();
    }
    private void pullHistory(){
        Log.d("PullHistory","add");
        List<chatMsg> historyChatMessage = chatService.getMessageList();
        List<chatMsg> tempList = new ArrayList<chatMsg>();
        for(int i = 0;i < historyChatMessage.size(); i++){
            chatMsg temp = historyChatMessage.get(i);
            Log.d("chatMsg:",historyChatMessage.get(i).toString());
            Log.d("From",From);
            Log.d("To",To);
            Log.d("receiveid",temp.getReceiveID());
            Log.d("senderid",temp.getSenderID());
            if( From.equals(temp.getReceiveID())&& To.equals(temp.getSenderID())){
                Log.d("test","asasas");
                tempList.add(temp);
            }else if(To.equals(temp.getReceiveID())&& From.equals(temp.getSenderID())){
                tempList.add(temp);
            }
        }
        msglist = tempList;
//        for(int i = 0;i <tempList.size();i++){
//            Log.d("messageList:", tempList.get(i).toString());
//            msglist.add(tempList.get(i));
//        }
        //nowList = msglist;
        initChatUi();
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


    }