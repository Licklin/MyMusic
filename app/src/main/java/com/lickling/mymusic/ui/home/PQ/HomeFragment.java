package com.lickling.mymusic.ui.home.PQ;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lickling.mymusic.MyTest;
import com.lickling.mymusic.R;
import com.lickling.mymusic.databinding.FragmentDesktopOneBinding;
import com.lickling.mymusic.ui.load.ListAdapter;
import com.lickling.mymusic.ui.load.ListItem;
import com.lickling.mymusic.viewmodel.MusicViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private ListAdapter listAdapter;
    private List<ListItem> listItems;
    private FragmentDesktopOneBinding desktopOneBinding;


    public HomeFragment(){

    }
    public HomeFragment(MusicViewModel model) {
        // Required empty public constructor
        this.musicViewModel=model;
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


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        desktopOneBinding.songName.setSingleLine(true);
        desktopOneBinding.songName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        desktopOneBinding.songName.setMarqueeRepeatLimit(-1);
        desktopOneBinding.songName.setSelected(true);
        desktopOneBinding.homeRecyclerView.setHasFixedSize(true);
        desktopOneBinding.homeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listItems = new ArrayList<>();
        // 在此处添加数据到listItems
        listItems.add(new ListItem("晴天", "潘琪"));
        listItems.add(new ListItem("花海", "jay潘"));
        listItems.add(new ListItem("最伟大的作品", "jay潘"));
        listItems.add(new ListItem("搁浅", "jay潘"));
        listItems.add(new ListItem("青花瓷", "jay潘"));

        listAdapter = new ListAdapter(listItems, getActivity());
        desktopOneBinding.homeRecyclerView.setAdapter(listAdapter);

        // 搜索框
        desktopOneBinding.imageviewInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                v.startAnimation(animation);
//                startActivity(new Intent(getActivity(), Desktop_Seek.class));

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

        //个人头像按键
        desktopOneBinding.imageviewPersonage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                desktopOneBinding.imageviewPersonage.startAnimation(animation);

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
                startActivity(new Intent(getActivity(), MyTest.class));
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
