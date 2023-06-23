package com.lickling.mymusic.ui.home.PQ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.ui.home.test.MainActivity;
import com.lickling.mymusic.ui.load.ListAdapter;
import com.lickling.mymusic.ui.load.ListItem;

import java.util.ArrayList;
import java.util.List;

public class Desktop extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desktop);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }



        ImageView bottom1 = findViewById(R.id.bottom1);
        ImageView bottom2 = findViewById(R.id.bottom2);
        ImageView bottom3 = findViewById(R.id.bottom3);
        ImageView bottom4 = findViewById(R.id.bottom4);
        bottom1.setSelected(true);
        replaceFragment(new Desktop_one());

        // 底部第一个按键
        bottom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                bottom1.startAnimation(animation);
                replaceFragment(new Desktop_one());
                bottom1.setSelected(true);
                bottom2.setSelected(false);
                bottom3.setSelected(false);
                bottom4.setSelected(false);

            }
        });

        // 底部第二个按键
        bottom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                bottom2.startAnimation(animation);
                replaceFragment(new Desktop_two());
                bottom2.setSelected(true);
                bottom1.setSelected(false);
                bottom3.setSelected(false);
                bottom4.setSelected(false);
            }
        });

        // 底部第三个按键
        bottom3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Desktop_three());
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                bottom3.startAnimation(animation);
                bottom3.setSelected(true);
                bottom1.setSelected(false);
                bottom2.setSelected(false);
                bottom4.setSelected(false);
            }
        });

        // 底部第四个按键
        bottom4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Desktop_four());
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                bottom4.startAnimation(animation);
                bottom4.setSelected(true);
                bottom1.setSelected(false);
                bottom2.setSelected(false);
                bottom3.setSelected(false);
            }
        });

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout,fragment);
        transaction.commit();
    }

}