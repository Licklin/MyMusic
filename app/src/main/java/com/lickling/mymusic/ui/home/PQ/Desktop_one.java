package com.lickling.mymusic.ui.home.PQ;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.ui.load.ListAdapter;
import com.lickling.mymusic.ui.load.ListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Desktop_one#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Desktop_one extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<ListItem> listItems;



    public Desktop_one() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Desktop_one.
     */
    // TODO: Rename and change types and number of parameters
    public static Desktop_one newInstance(String param1, String param2) {
        Desktop_one fragment = new Desktop_one();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private ImageView imageView_scan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_desktop_one, container, false);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = getActivity().findViewById(R.id.recycler_view4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listItems = new ArrayList<>();
        // 在此处添加数据到listItems
        listItems.add(new ListItem("晴天", "潘琪"));
        listItems.add(new ListItem("花海", "jay潘"));
        listItems.add(new ListItem("最伟大的作品", "jay潘"));
        listItems.add(new ListItem("搁浅", "jay潘"));
        listItems.add(new ListItem("青花瓷", "jay潘"));

        listAdapter = new ListAdapter(listItems, getActivity());
        recyclerView.setAdapter(listAdapter);



        // 搜索框
        ImageView imageview_input = getActivity().findViewById(R.id.imageview_input);
        imageview_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageview_input.startAnimation(animation);
                startActivity(new Intent(getActivity(),Desktop_Seek.class));

            }
        });

        // 扫描按键
        ImageView imageView_scan = getActivity().findViewById(R.id.imageview_scan);
        imageView_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageView_scan.startAnimation(animation);
            }
        });

        // 识别按键
        ImageView imageview_recognition = getActivity().findViewById(R.id.imageview_recognition);
        imageview_recognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageview_recognition.startAnimation(animation);
            }
        });

        //个人头像按键
        ImageView imageview_personage = getActivity().findViewById(R.id.imageview_personage);
        imageview_personage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageview_personage.startAnimation(animation);

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
        ImageView imageview_hot = getActivity().findViewById(R.id.imageview_hot);
        imageview_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageview_hot.startAnimation(animation);

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
        ImageView imageview_rank = getActivity().findViewById(R.id.imageview_rank);
        imageview_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageview_rank.startAnimation(animation);

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
        ImageView imageview_station = getActivity().findViewById(R.id.imageview_station);
        imageview_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageview_station.startAnimation(animation);

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
        ImageView imageview_singer = getActivity().findViewById(R.id.imageview_singer);
        imageview_singer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageview_singer.startAnimation(animation);

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
        ImageView imageview_like = getActivity().findViewById(R.id.imageview_like);
        imageview_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageview_like.startAnimation(animation);
            }
        });

        // 播放按键
        ImageView imageView_play = getActivity().findViewById(R.id.imageView_play);
        imageView_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageView_play.startAnimation(animation);
            }
        });

        // 下一首按键
        ImageView imageView_next = getActivity().findViewById(R.id.imageView_next);
        imageView_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageView_next.startAnimation(animation);
            }
        });

        // 队列按键
        ImageView imageView_list = getActivity().findViewById(R.id.imageView_list);
        imageView_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageView_list.startAnimation(animation);
            }
        });





    }






}