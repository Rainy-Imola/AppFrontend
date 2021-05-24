package com.example.JTrace.chat;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.JTrace.Constants;
import com.example.JTrace.R;
import com.example.JTrace.model.chatMsg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ChatService extends Service {
    //private URI uri;

    private List<chatMsg> msglist = new ArrayList<chatMsg>();
    public ChatClient client;
    private JWebSocketClientBinder mBinder = new JWebSocketClientBinder();
    private final static int GRAY_SERVICE_ID = 1001;
    private boolean exit = false;
    public class JWebSocketClientBinder extends Binder {
        public ChatService getService() {
            return ChatService.this;
        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initSocket();
        mHandler.postDelayed(heartBeatRunnable,HEART_BEAT_RATE);
        //startForeground(GRAY_SERVICE_ID, new Notification());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        closeConnect();
        exit = true;
        super.onDestroy();
    }
    public ChatService(){
    }

    private void initSocket() {
        URI uri = null;
        try{
            String name = getSharedPreferences("user_profile",Context.MODE_PRIVATE).getString("username","");
            uri = new URI("ws://47.103.123.145/webSocket/"+name);

            //uri = new URI("ws://echo.websocket.org");
        }catch (Exception e){
            e.printStackTrace();
        }

        client = new ChatClient(uri){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMessage(String message) {
                super.onMessage(message);
//                JSONObject msg = null;
//                try{
//                    msg = new JSONObject(message);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                //chatMsg msg = new chatMsg("test2","test2",1,message);
                Log.d("receive message；", message);
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
                chatMsg item1 = new chatMsg(From,To, 1,content,null);
                msglist.add(item1);

                Intent intent = new Intent();
                intent.setAction("com.xch.servicecallback.content");
                intent.putExtra("msg",message);
                sendBroadcast(intent);
                try {
                    checkLockAndShowNotification(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        connect();
    }

    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void sendMsg(String To, String From, String msg) {
        JSONObject msgbody = new JSONObject();
        try{
            msgbody.put("To",To);
            msgbody.put("From",From);
            msgbody.put("message",msg);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        chatMsg item = new chatMsg(From, To, 0, msg,null);
        msglist.add(item);
        if(null != client){
            client.send(String.valueOf(msgbody));
        }
    }

    private void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }
    public List<chatMsg> getMessageList(){
        return msglist;
    }
    private static final long HEART_BEAT_RATE = 20 * 1000;//每隔10秒进行一次对长连接的心跳检测
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {

            if(exit == false){
                Log.e("JWebSocketClientService", "心跳包检测websocket连接状态");
                if (client != null) {
                    if (client.isClosed()) {
                        reconnectWs();
                    }
                } else {
                    //如果client已为空，重新初始化连接
                    client = null;
                    initSocket();
                }
                //每隔一定的时间，对长连接进行一次心跳检测
                mHandler.postDelayed(this, HEART_BEAT_RATE);
            }else{
                Log.d("ChatService","连接已主动关闭");
                closeConnect();
            }

        }
    };

    /**
     * 开启重连
     */
    private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("JWebSocketClientService", "开启重连");
                    client.reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    PowerManager.WakeLock wakeLock;//锁屏唤醒
    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    @SuppressLint("InvalidWakeLockTag")
    private void acquireWakeLock()
    {
        if (null == wakeLock)
        {
            PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock)
            {
                wakeLock.acquire();
            }
        }
    }

    public static class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
    /**
     * 检查锁屏状态，如果锁屏先点亮屏幕
     *
     * @param message
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkLockAndShowNotification(String message) throws InterruptedException {
        sendNotification(message);
        //管理锁屏的一个服务
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {//锁屏
            //获取电源管理器对象
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            if (!pm.isScreenOn()) {
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                wl.acquire();  //点亮屏幕
                wl.release();  //任务结束后释放
            }
            sendNotification(message);
        } else {
            sendNotification(message);
        }
    }

    /**
     * 发送通知
     *
     * @param message
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String message) throws InterruptedException {
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

        Intent intent = new Intent();
        intent.setClass(this, MainChatActivity.class);
        intent.putExtra("id",0);
        intent.putExtra("name",From);
        String avatar = getAvatar(From);
        intent.putExtra("avatar",avatar);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel notificationChannel =  new NotificationChannel("Chat","ChatMain", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifyManager.createNotificationChannel(notificationChannel);
        Notification notification = new NotificationCompat.Builder(this)
                .setChannelId("Chat")
                .setAutoCancel(true)
                .setTicker("Nature")
                .setSmallIcon(R.drawable.picture_icon_wechat_down)
                .setContentTitle(From + " 向 "+ To + "发送了：")
                .setContentIntent(pendingIntent)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .build();

        notifyManager.notify(1, notification);//id要保证唯一

    }
    String getAvatar(String name) throws InterruptedException {
        final String[] avatar = {""};
        OkHttpClient okHttpClient = new OkHttpClient();
        SharedPreferences sharedPreferences = getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Log.d("MessageInfo_token", token);
        Request request = new Request.Builder().url(Constants.baseUrl + "/users/" + name + "/info")
                .addHeader("Authorization", token)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("user_info", "request_handle_failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d("User_info", "接收成功");
                Log.d("userinfo user", res);

                try {
                    JSONObject result = new JSONObject(res);
                    JSONArray data = (JSONArray) result.get("data");
                    int status = (int) result.get("status");
                    String msg = (String) result.get("msg");
                    if (status == 0) {
                       avatar[0] = (String) data.getJSONObject(0).get("avatar");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        Thread.sleep(200);
        Log.d("notifition avatar", avatar[0]);
        return avatar[0];
    }
}
