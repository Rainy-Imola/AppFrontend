package com.example.JTrace.chat;

//import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ChatClient extends WebSocketClient {
    static String msg = "";

    public ChatClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("JWebScoketClient: " + "onOpen()");
    }

    public static String getMsg() {
        return msg;
    }

    @Override
    public void onMessage(String message) {
        msg = message;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocketClient: onClose()");
        System.out.println("Connection Close by " + (remote ? "other" : "us") + " Code: " + code + " Reason: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("WebSocketClient: onErr()");
        System.out.println("ERROR: " + ex.getMessage());
    }
}
