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


    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<ListItem> listItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desktop);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        recyclerView = findViewById(R.id.recycler_view3);
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

        // 搜索框
        ImageView imageview_input = findViewById(R.id.imageview_input);
        imageview_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                imageview_input.startAnimation(animation);

                startActivity(new Intent(Desktop.this,Desktop_Seek.class));

            }
        });


        // 扫描按键
        ImageView imageView_scan = findViewById(R.id.imageview_scan);
        imageView_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                imageView_scan.startAnimation(animation);
            }
        });

        // 识别按键
        ImageView imageview_recognition = findViewById(R.id.imageview_recognition);
        imageview_recognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                imageview_recognition.startAnimation(animation);
            }
        });

        // 热门按键
        ImageView imageview_hot = findViewById(R.id.imageview_hot);
        imageview_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                imageview_hot.startAnimation(animation);

                AlertDialog.Builder builder = new AlertDialog.Builder(Desktop.this);
                builder.setIcon(R.drawable.baseline_lightbulb_24);
                builder.setTitle("功能待完善");
                builder.setMessage("敬请期待");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();

            }
        });

        // 排行按键
        ImageView imageview_rank = findViewById(R.id.imageview_rank);
        imageview_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                imageview_rank.startAnimation(animation);

                AlertDialog.Builder builder = new AlertDialog.Builder(Desktop.this);
                builder.setIcon(R.drawable.baseline_lightbulb_24);
                builder.setTitle("功能待完善");
                builder.setMessage("敬请期待");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();

            }
        });

        // 电台按键
        ImageView imageview_station = findViewById(R.id.imageview_station);
        imageview_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                imageview_station.startAnimation(animation);

                AlertDialog.Builder builder = new AlertDialog.Builder(Desktop.this);
                builder.setIcon(R.drawable.baseline_lightbulb_24);
                builder.setTitle("功能待完善");
                builder.setMessage("敬请期待");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();

            }
        });

        // 推荐按键
        ImageView imageview_recommend = findViewById(R.id.imageview_recommend);
        imageview_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                imageview_recommend.startAnimation(animation);

                AlertDialog.Builder builder = new AlertDialog.Builder(Desktop.this);
                builder.setIcon(R.drawable.baseline_lightbulb_24);
                builder.setTitle("功能待完善");
                builder.setMessage("敬请期待");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();

            }
        });

        // 歌手按键
        ImageView imageview_singer = findViewById(R.id.imageview_singer);
        imageview_singer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                imageview_singer.startAnimation(animation);

                AlertDialog.Builder builder = new AlertDialog.Builder(Desktop.this);
                builder.setIcon(R.drawable.baseline_lightbulb_24);
                builder.setTitle("功能待完善");
                builder.setMessage("敬请期待");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();

            }
        });

        // 我喜欢按钮
        ImageView imageview_like = findViewById(R.id.imageview_like);
        imageview_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                imageview_like.startAnimation(animation);
            }
        });

        //个人头像按键
        ImageView imageview_personage = findViewById(R.id.imageview_personage);
        imageview_personage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                imageview_personage.startAnimation(animation);

                AlertDialog.Builder builder = new AlertDialog.Builder(Desktop.this);
                builder.setIcon(R.drawable.baseline_lightbulb_24);
                builder.setTitle("功能待完善");
                builder.setMessage("敬请期待");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();

            }
        });

        // 播放按键
        ImageView imageView_play = findViewById(R.id.imageView_play);
        imageView_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                imageView_play.startAnimation(animation);
            }
        });

        // 下一首按键
        ImageView imageView_next = findViewById(R.id.imageView_next);
        imageView_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                imageView_next.startAnimation(animation);
            }
        });

        // 队列按键
        ImageView imageView_list = findViewById(R.id.imageView_list);
        imageView_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop.this, R.anim.alpha);
                imageView_list.startAnimation(animation);
            }
        });


        ImageView bottom1 = findViewById(R.id.bottom1);
        ImageView bottom2 = findViewById(R.id.bottom2);
        ImageView bottom3 = findViewById(R.id.bottom3);
        ImageView bottom4 = findViewById(R.id.bottom4);
        bottom1.setSelected(true);

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