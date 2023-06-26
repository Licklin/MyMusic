package com.lickling.mymusic.ui.home.PQ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.ui.load.ListAdapter;
import com.lickling.mymusic.ui.load.ListAdapter2;
import com.lickling.mymusic.ui.load.ListItem;
import com.lickling.mymusic.ui.load.SongOperationPopup;
import com.lickling.mymusic.ui.load.SongOperationPopup2;

import java.util.ArrayList;
import java.util.List;

public class Desktop_Seek extends AppCompatActivity implements SongOperationPopup.OnDeleteItemListener,SongOperationPopup2.OnDeleteItemListener{


//    private RecyclerView recyclerView;
//    private ListAdapter listAdapter;
//    private List<ListItem> listItems;


    private Toolbar page_head;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<ListItem> listItems;

    private ListAdapter2 listAdapter2;

    private List<ListItem> loading_listItems;
    private SongOperationPopup dialog;

    private SongOperationPopup2 dialog2;
    private Button loaded_btn;
    private Button loading_btn;
    private View select_tag1;
    private View select_tag2;






    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desktop_seek);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

//        recyclerView = findViewById(R.id.recycler_view1);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        listItems = new ArrayList<>();
//        // 在此处添加数据到listItems
//        listItems.add(new ListItem("晴天", "潘琪"));
//        listItems.add(new ListItem("花海", "jay潘"));
//        listItems.add(new ListItem("最伟大的作品", "jay潘"));
//        listItems.add(new ListItem("搁浅", "jay潘"));
//        listItems.add(new ListItem("青花瓷", "jay潘"));
//
//        listAdapter = new ListAdapter(listItems, this);
//        recyclerView.setAdapter(listAdapter);


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





        // 在此处添加菜单项的添加按钮到Toolbar上
        getMenuInflater().inflate(R.menu.load_toolbar_menu, page_head.getMenu());

        Button play_btn = findViewById(R.id.play_btn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading_listItems.add(new ListItem("歌曲名", "歌手"));
                int visibility = select_tag2.getVisibility();

                if(visibility == View.VISIBLE)
                    recyclerView.setAdapter(listAdapter2);
            }
        });

        loaded_btn =findViewById(R.id.loaded_btn);
        select_tag1=findViewById(R.id.select_tag1);
        loaded_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                loaded_btn.setTypeface(null, Typeface.BOLD);
                select_tag1.setVisibility(View.VISIBLE);

                loading_btn.setTypeface(null, Typeface.NORMAL);
                select_tag2.setVisibility(View.INVISIBLE);

                recyclerView.setAdapter(listAdapter);
            }
        });

        loading_btn =findViewById(R.id.loading_btn);
        select_tag2=findViewById(R.id.select_tag2);
        loading_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading_btn.setTypeface(null, Typeface.BOLD);
                select_tag2.setVisibility(View.VISIBLE);

                loaded_btn.setTypeface(null, Typeface.NORMAL);
                select_tag1.setVisibility(View.INVISIBLE);

                recyclerView.setAdapter(listAdapter2);
            }
        });

        //已下载列表
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        // 在此处添加数据到listItems
        listItems.add(new ListItem("歌曲1", "歌手1"));
        listItems.add(new ListItem("歌曲2", "歌手2"));
        listItems.add(new ListItem("歌曲3", "歌手3"));

        FragmentManager manager = getSupportFragmentManager();

        dialog =new SongOperationPopup(this);
        dialog.setOnDeleteItemListener(this);

        listAdapter = new ListAdapter(listItems, this);
        listAdapter.setDialog(dialog);
        recyclerView.setAdapter(listAdapter);

        //正在下载列表
        loading_listItems = new ArrayList<>();

        loading_listItems.add(new ListItem("歌曲1", "歌手1"));
        loading_listItems.add(new ListItem("歌曲2", "歌手2"));
        loading_listItems.add(new ListItem("歌曲3", "歌手3"));

        dialog2 =new SongOperationPopup2(this);
        dialog2.setOnDeleteItemListener(this);

        listAdapter2 = new ListAdapter2(loading_listItems, this);
        listAdapter2.setDialog(dialog2);






    }

    @Override
    public void onDeleteItem(int position) {
        listItems.remove(position);
        listAdapter.notifyItemRemoved(position);
        listAdapter.notifyItemRangeChanged(position, listAdapter.getItemCount() - position);
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void onDeleteItem(int position,int tag){
        loading_listItems.remove(position);
        listAdapter2.notifyItemRemoved(position);
        listAdapter2.notifyItemRangeChanged(position, listAdapter2.getItemCount() - position);
        recyclerView.setAdapter(listAdapter2);

    }
}
