package com.lickling.mymusic.ui.home.PQ;

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
import android.widget.ListView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.ui.load.ListAdapter;
import com.lickling.mymusic.ui.load.ListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Desktop_two#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Desktop_two extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView recyclerView;
    private Desktop_ListAdapter2 Desktop_ListAdapter2;
    private List<Desktop_ListItem2> Desktop_ListItem2;





    public Desktop_two() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Desktop_two.
     */
    // TODO: Rename and change types and number of parameters
    public static Desktop_two newInstance(String param1, String param2) {
        Desktop_two fragment = new Desktop_two();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_desktop_two, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageView imageview_find = getActivity().findViewById(R.id.imageview_find);   // 发现按键
        ImageView circle_find = getActivity().findViewById(R.id.circle_find);   // 发现圆圈
        ImageView imageview_attention = getActivity().findViewById(R.id.imageview_attention);   // 关注按键
        ImageView circle_attention = getActivity().findViewById(R.id.circle_attention);   // 关注圆圈

        imageview_find.setSelected(true);
        circle_find.setSelected(true);


        // 发现按键
        imageview_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageview_find.startAnimation(animation);
                circle_find.startAnimation(animation);
                imageview_find.setSelected(true);
                circle_find.setSelected(true);
                imageview_attention.setSelected(false);
                circle_attention.setSelected(false);
            }
        });

        // 关注按键
        imageview_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                imageview_attention.startAnimation(animation);
                circle_attention.startAnimation(animation);
                imageview_find.setSelected(false);
                circle_find.setSelected(false);
                imageview_attention.setSelected(true);
                circle_attention.setSelected(true);
            }
        });



        recyclerView = getActivity().findViewById(R.id.community_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Desktop_ListItem2 = new ArrayList<>();
        // 在此处添加数据到listItems
        Desktop_ListItem2.add(new Desktop_ListItem2("潘琪"));
        Desktop_ListItem2.add(new Desktop_ListItem2("黎科良"));
        Desktop_ListItem2.add(new Desktop_ListItem2("朱纪达"));
        Desktop_ListItem2.add(new Desktop_ListItem2("倪浩深"));
        Desktop_ListItem2.add(new Desktop_ListItem2("赵胜葳"));

        Desktop_ListAdapter2 = new Desktop_ListAdapter2(Desktop_ListItem2, getActivity());
        recyclerView.setAdapter(Desktop_ListAdapter2);



    }

}


