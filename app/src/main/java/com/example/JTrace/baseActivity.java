package com.example.JTrace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class baseActivity extends AppCompatActivity {

    //根据需求定义自己需要关闭页面的action
    public static final String RECEIVER_ACTION_FINISH= "receiver_action_finish";

    private FinishActivityRecevier mRecevier;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecevier = new FinishActivityRecevier();
        registerFinishReciver();
    }

    private void registerFinishReciver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION_FINISH);
        registerReceiver(mRecevier, intentFilter);
    }

    private class FinishActivityRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //根据需求添加自己需要关闭页面的action
            if (RECEIVER_ACTION_FINISH .equals(RECEIVER_ACTION_FINISH)) {
                Log.d("Test","已经结束运行");
                baseActivity.this.finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mRecevier != null) {
            unregisterReceiver(mRecevier);
        }
        super.onDestroy();
    }
}