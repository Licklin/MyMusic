package com.lickling.mymusic.ui.home.nsh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lickling.mymusic.R;

public class Register extends AppCompatActivity {
    private Toolbar page_head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        page_head = findViewById(R.id.register_page);
        page_head.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.setClass(Register.this,LoginWangyi.class);
                startActivity(intent);
            }
        });
    }
}