package com.lickling.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lickling.mymusic.service.MusicService;

import com.lickling.mymusic.ui.BaseActivity;
import com.lickling.mymusic.utility.MyGlide;

public class TestActivity extends AppCompatActivity {

    private MusicService musicService;
    private MyConn myConn;
    private Intent intent;


    protected void init() {
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MyMusicBinder myMusicBinder = (MusicService.MyMusicBinder) iBinder;
            musicService = myMusicBinder.getMusicService();
//            if(musicService == null) Toast.makeText(TestActivity.this, "null", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        myConn = new MyConn();
        ImageView imageView = findViewById(R.id.image_test);
        Button testButton = findViewById(R.id.test_btn);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MusicService service = new MusicService();
                if (musicService == null) {
                    Toast.makeText(TestActivity.this, "null", Toast.LENGTH_SHORT).show();
                    return;
                }

//                String path = "android.resource://" + getPackageName() + "/" + R.raw.jin_test;
//                testService.onStartCommand(intent, 0, 1);
                musicService.onPlay("https://music.163.com/song/media/outer/url?id=2046330392.mp3", true);
//                Toast.makeText(musicService, "播放", Toast.LENGTH_SHORT).show();
//                musicService.onPlay(path, false);
                Toast.makeText(TestActivity.this, "ok", Toast.LENGTH_SHORT).show();
            }
        });
//        MyGlide myGlide = new MyGlide();
//        myGlide.setPicture(this, "https://p1.music.126.net/pleQjKOI26fSenkUGipDLw==/109951166537300832.jpg", imageView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        intent = new Intent(TestActivity.this, MusicService.class);
//        this.startService(intent);
        this.bindService(intent, serviceConnection, Context.BIND_ABOVE_CLIENT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unbindService(myConn);

//        this.stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myConn != null) myConn = null;
        if (intent != null) intent = null;
        if (musicService != null) musicService = null;
//        if (musicService != null) musicService = null;

    }

    private class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MyMusicBinder myMusicBinder = (MusicService.MyMusicBinder) iBinder;
            musicService = myMusicBinder.getMusicService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

        @Override
        public void onBindingDied(ComponentName name) {
            ServiceConnection.super.onBindingDied(name);
        }

        @Override
        public void onNullBinding(ComponentName name) {
            ServiceConnection.super.onNullBinding(name);
        }
    }
}