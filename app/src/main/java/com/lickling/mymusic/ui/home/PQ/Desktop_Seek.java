package com.lickling.mymusic.ui.home.PQ;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.ui.load.ListAdapter;
import com.lickling.mymusic.ui.load.ListItem;

import java.util.ArrayList;
import java.util.List;

public class Desktop_Seek extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desktop_seek);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        recyclerView = findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        // 在此处添加数据到listItems
        listItems.add(new ListItem("晴天", "潘琪"));
        listItems.add(new ListItem("花海", "jay潘"));
        listItems.add(new ListItem("最伟大的作品", "jay潘"));
        listItems.add(new ListItem("搁浅", "jay潘"));
        listItems.add(new ListItem("青花瓷", "jay潘"));

        listAdapter = new ListAdapter(listItems, this);
        recyclerView.setAdapter(listAdapter);


        // 返回按键
        ImageView imageview_back = findViewById(R.id.imageview_back);
        imageview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop_Seek.this, R.anim.alpha);
                imageview_back.startAnimation(animation);

//                startActivity(new Intent(Desktop_Seek.this,Desktop.class));
                  finish();
            }
        });



    }
}
