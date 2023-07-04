package com.lickling.mymusic.ui.home.PQ;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.databinding.FragmentDesktopOneBinding;
import com.lickling.mymusic.viewmodel.MusicViewModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private MusicViewModel musicViewModel;
    private Desktop_ListAdapter Desktop_ListAdapter;
    private List<Desktop_Listltem> Desktop_Listltem;

    private FragmentDesktopOneBinding desktopOneBinding;


    public HomeFragment() {

    }

    public HomeFragment(MusicViewModel model) {
        // Required empty public constructor
        this.musicViewModel = model;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(MusicViewModel model) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        desktopOneBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_desktop_one, container, false);
        desktopOneBinding.setHomeInfo(musicViewModel);
        return desktopOneBinding.getRoot();
    }


    private Handler mHandler;
    private Handler handler;
    private int GET_DATA_SUCCESS = 101;
    private String data;

    // 获取子线程的数据，发送给主线程，由主线程更新UI
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = getDataFromServer();

                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("data",data);
                message.setData(bundle);
                message.what=GET_DATA_SUCCESS;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder;
    private HttpURLConnection connection;
    private Runnable runnable;
    private Handler uhandler;

    // 获取网上的数据
    private String getDataFromServer() {
        try {
            URL url = new URL("https://v1.hitokoto.cn/?c=f&encode=text");
            connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                stringBuilder = new StringBuilder();
                for (String line = ""; (line = bufferedReader.readLine()) != null; ) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();

                if (inputStream!=null)inputStream.close();
                if (connection!=null)connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    private void useHandler1(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    desktopOneBinding.heart.performClick();
            }
        },3000);
    }

    private void useHand(){
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    useHandler1();
                }
            }
        }).start();
    }





    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        desktopOneBinding.songName.setSingleLine(true);
        desktopOneBinding.songName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        desktopOneBinding.songName.setMarqueeRepeatLimit(-1);
        desktopOneBinding.songName.setSelected(true);
        desktopOneBinding.homeRecyclerView.setHasFixedSize(true);
        desktopOneBinding.homeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Desktop_Listltem = new ArrayList<>();
        // 在此处添加数据到listItems
        Desktop_Listltem.add(new Desktop_Listltem("晴天", "潘琪"));
        Desktop_Listltem.add(new Desktop_Listltem("花海", "jay潘"));
        Desktop_Listltem.add(new Desktop_Listltem("最伟大的作品", "jay潘"));
        Desktop_Listltem.add(new Desktop_Listltem("搁浅", "jay潘"));
        Desktop_Listltem.add(new Desktop_Listltem("青花瓷", "jay潘"));

        Desktop_ListAdapter = new Desktop_ListAdapter(Desktop_Listltem, getActivity());
        desktopOneBinding.homeRecyclerView.setAdapter(Desktop_ListAdapter);

        // 进入主页对自动点击一次，更换心灵鸡汤语句
        initData();

        // 心灵鸡汤语句点击事件，点一下切换一次
        desktopOneBinding.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });



        // 接受子线程返回的数据，由主线程更改UI
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                if(message.what==GET_DATA_SUCCESS){
                    data = message.getData().getString("data");
                    desktopOneBinding.heart.setText(data);
                }
                return false;
            }
        });


//        new Thread(new Runnable() {
//            public void run() {
//                while (true) {
//                    useHandler2();
//                }
//            }
//        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                useHandler2();
//            }
//        }).start();

//        // 创建Handler并关联主线程的Looper
//        uhandler = new Handler();
//
//        // 创建Runnable对象，在其中实现循环逻辑
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    useHandler2();
//                }
//            }
//        };
//
//        // 创建并启动子线程
//        Thread thread = new Thread(runnable);
//        thread.start();






        // 歌单按键
        desktopOneBinding.pqSongListSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(desktopOneBinding.pqSongListSelector.isSelected())
                {
                    desktopOneBinding.pqSongListSelector.setSelected(false);
                    desktopOneBinding.homeRecyclerView.setVisibility(View.VISIBLE);

                }
                else
                {
                    desktopOneBinding.pqSongListSelector.setSelected(true);
                    desktopOneBinding.homeRecyclerView.setVisibility(View.GONE);


                }
            }
        });



        // 搜索框
        desktopOneBinding.imageviewInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                v.startAnimation(animation);
                startActivity(new Intent(getActivity(), Desktop_Seek.class));

            }
        });



        // 扫描按键
        desktopOneBinding.imageviewScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                desktopOneBinding.imageviewScan.startAnimation(animation);
            }
        });

        // 识别按键
        desktopOneBinding.imageviewRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                desktopOneBinding.imageviewRecognition.startAnimation(animation);
            }
        });



        // 热门按键
        desktopOneBinding.imageviewHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                desktopOneBinding.imageviewHot.startAnimation(animation);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        desktopOneBinding.imageviewRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                desktopOneBinding.imageviewRank.startAnimation(animation);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        desktopOneBinding.imageviewStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                desktopOneBinding.imageviewStation.startAnimation(animation);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        ImageView imageview_recommend = getActivity().findViewById(R.id.imageview_recommend);
        imageview_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageview_recommend.startAnimation(animation);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        desktopOneBinding.imageviewSinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                desktopOneBinding.imageviewSinger.startAnimation(animation);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        desktopOneBinding.imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), MyTest.class));
            }
        });

        // 播放按键

        desktopOneBinding.imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                desktopOneBinding.imageViewPlay.startAnimation(animation);
                musicViewModel.playbackButton();

            }
        });

        // 下一首按键

        desktopOneBinding.imageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                desktopOneBinding.imageViewNext.startAnimation(animation);
            }
        });

        // 队列按键
        desktopOneBinding.imageViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                desktopOneBinding.imageViewList.startAnimation(animation);
            }
        });


    }

    public FragmentDesktopOneBinding getBinding(){
        return desktopOneBinding;
    }
    private void initView() {


    }


}
