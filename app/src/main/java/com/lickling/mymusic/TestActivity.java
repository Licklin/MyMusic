package com.lickling.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lickling.mymusic.service.MusicService;
import com.lickling.mymusic.utility.MyGlide;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                MusicService testService = new MusicService();
                testService.onPlay("https://music.163.com/song/media/outer/url?id=2046330392.mp3",false);
                Toast.makeText(TestActivity.this, "ok", Toast.LENGTH_SHORT).show();
            }
        });
        MyGlide myGlide = new MyGlide();
        myGlide.setPicture(this,"https://p1.music.126.net/pleQjKOI26fSenkUGipDLw==/109951166537300832.jpg",imageView);
    }
}