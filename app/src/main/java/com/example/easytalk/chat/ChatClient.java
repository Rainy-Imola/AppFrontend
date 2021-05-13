package com.example.easytalk.chat;

//import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import javax.security.auth.callback.Callback;

public class ChatClient extends WebSocketClient{
    static String msg = "";
    public ChatClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        //send("Hello, I am wxz");
        //Log.e("JWebSocketClient", "onOpen()");
        System.out.println("JWebScoketClient: " + "onOpen()");
    }

    public static String getMsg() {
        return msg;
    }

    @Override
    public void onMessage(String message) {
//        Log.d("recv message:", message);
//        Log.e("JWebSocketClient", "onMessage()");
        System.out.println("WebSocketClient: onOpen()");
        System.out.println("Recv message:" + message);
        msg = message;

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        //Log.e("JWebSocketClient", "onClose()");
        System.out.println("WebSocketClient: onClose()");
        System.out.println("Connection Close by " + (remote ? "other" : "us") + " Code: " + code + " Reason: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        //Log.e("JWebSocketClient", "onError()");
        System.out.println("WebSocketClient: onErr()");
        System.out.println("ERROR: " + ex.getMessage());
    }

//    static ChatClient chatClient = null;
//    chat
//
//    public static void main(String[] args) throws URISyntaxException, InterruptedException {
//        URI uri = new URI("ws://echo.websocket.org");
//        ChatClient client = new ChatClient(uri);
//        System.out.println("Begin to Connection");
//        //try to connect
//        try{
//            client.connectBlocking();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return;
//        }
////        json 处理使用了Android的包，无法单个调试
////        JSONObject msgbody = new JSONObject();
////        try{
////            msgbody.put("sender","wxz");
////            msgbody.put("receiver","peer");
////            msgbody.put("msgContent","Nice to meet you");
////        }
////        catch (JSONException e){
////            e.printStackTrace();
////        }
////       json转字符串
////       String res = String.valueOf(res)；
////       字符串转json
////       JSONObject res = new JSONObject(res)；
////        client.send("hello");
////        if (client != null && client.isOpen()) {
//        client.send("你好");
////        }
//        //client.closeBlocking();
//
//    }
}
