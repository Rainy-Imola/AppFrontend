package com.example.JTrace;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeAcitivity extends AppCompatActivity {
    private Button go_on;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        this.getSupportActionBar().hide();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeAcitivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };
        mHandler.postDelayed(runnable,2000);
        go_on = (Button) findViewById(R.id.go_on);

        go_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(runnable);
                Intent intent = new Intent(WelcomeAcitivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

