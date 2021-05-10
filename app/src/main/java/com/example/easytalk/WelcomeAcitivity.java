package com.example.easytalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeAcitivity extends AppCompatActivity {
    private Button go_on;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        this.getSupportActionBar().hide();

        go_on = (Button) findViewById(R.id.go_on);
        //注册点击事件
        go_on.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeAcitivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

}

