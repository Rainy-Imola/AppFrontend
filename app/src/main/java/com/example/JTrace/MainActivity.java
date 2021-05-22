package com.example.JTrace;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.example.JTrace.chat.ChatService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends baseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(MainActivity.this);
        startChatService();
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();//注意是在 setContentView(R.layout.activity_main)后
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_board, R.id.navigation_friends,R.id.navigation_myinfo)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }
    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();

    }
    private void startChatService(){
        Intent intent = new Intent(MainActivity.this, ChatService.class);
        startService(intent);
    }

    @Override
    public void finish() {
        Intent intent = new Intent(MainActivity.this, ChatService.class);
        stopService(intent);
        super.finish();
    }
    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.friends_bar,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
}