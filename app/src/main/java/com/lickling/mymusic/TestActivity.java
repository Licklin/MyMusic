package com.lickling.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lickling.mymusic.model.MainModel;
import com.lickling.mymusic.service.MusicService;

import com.lickling.mymusic.utilty.MyGlide;

public class TestActivity extends AppCompatActivity {

    private MusicService musicService;
    private MyConn myConn;
    private Intent intent;


    protected void init() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myConn = new MyConn();
        intent = new Intent(this, MusicService.class);
        this.bindService(intent, myConn, Context.BIND_AUTO_CREATE);

        setContentView(R.layout.test_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        ImageView imageView = findViewById(R.id.image_test);
        Button testButton = findViewById(R.id.test_btn);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService == null) {
                    Toast.makeText(TestActivity.this, "null", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(TestActivity.this, "ok", Toast.LENGTH_SHORT).show();
                }

                String path = "android.resource://" + getPackageName() + "/" + R.raw.jin_test;

//                musicService.onPlay("https://music.163.com/song/media/outer/url?id=2046330392.mp3", true);
//                musicService.onPlay("http://m701.music.126.net/20230623112724/8189a4ad4376aeb755d9122c02345d66/jdymusic/obj/wo3DlMOGwrbDjj7DisKw/27590105925/72df/e26e/621b/8e74be9aea666c141b07ea0d4c4d59ab.mp3", true);
                // 下一曲或者第一次播放
                if (musicService.isFirstPlay()) musicService.onPlay(path, false);
                else { // 暂停或继续播放
                    if (musicService.isPlaying()) musicService.onPause();
                    else
                        musicService.onContinuePlay();
                }


            }
        });
        MyGlide myGlide = new MyGlide();
//        myGlide.setPicture(this, "https://p1.music.126.net/pleQjKOI26fSenkUGipDLw==/109951166537300832.jpg", imageView);

        String base64String = "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";
//        byte[] imageBytes = Base64.decode(base64String, Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//        Glide.with(this).load(R.drawable.loading).into(imageView);
        MainModel mainModel = new MainModel(this);
        mainModel.setQd2ImageView(imageView);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(myConn);
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