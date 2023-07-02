package com.lickling.mymusic.ui.home.PQ;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.databinding.CommunityListBinding;
import com.lickling.mymusic.ui.load.ListAdapter;
import com.lickling.mymusic.ui.load.ListItem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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


    private Handler mHandler;
    private Handler handler;
    private int GET_DATA_SUCCESS = 101;
    private String data;
    private String data2;
    private String data3;
    private String data4;
    private String data5;
    private String[] data6;

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


//        for (int i=1;i<=5;i++){
//            int finalI = i;

            initData();

            // 接受子线程返回的数据，由主线程更改UI

            mHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message message) {
                    if(message.what==GET_DATA_SUCCESS){

                        data = message.getData().getString("data");


                        // 在此处添加数据到listItems
                        Desktop_ListItem2.add(new Desktop_ListItem2("潘琪",data));
                        Desktop_ListItem2.add(new Desktop_ListItem2("黎科良",data));
                        Desktop_ListItem2.add(new Desktop_ListItem2("朱纪达",data));
                        Desktop_ListItem2.add(new Desktop_ListItem2("倪浩深",data));
                        Desktop_ListItem2.add(new Desktop_ListItem2("赵胜葳",data));

                        Desktop_ListAdapter2 = new Desktop_ListAdapter2(Desktop_ListItem2, getActivity());
                        recyclerView.setAdapter(Desktop_ListAdapter2);

                    }
                    return false;
                }
            });


//        }

//        initData();
//
//        // 接受子线程返回的数据，由主线程更改UI
//        mHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message message) {
//                if(message.what==GET_DATA_SUCCESS){
//
//                        data = message.getData().getString("data");
//
//                    // 在此处添加数据到listItems
//                    Desktop_ListItem2.add(new Desktop_ListItem2("潘琪",data));
//                    Desktop_ListItem2.add(new Desktop_ListItem2("黎科良",data));
//                    Desktop_ListItem2.add(new Desktop_ListItem2("朱纪达",data));
//                    Desktop_ListItem2.add(new Desktop_ListItem2("倪浩深",data));
//                    Desktop_ListItem2.add(new Desktop_ListItem2("赵胜葳",data));
//
//                    Desktop_ListAdapter2 = new Desktop_ListAdapter2(Desktop_ListItem2, getActivity());
//                    recyclerView.setAdapter(Desktop_ListAdapter2);
//
//                }
//                return false;
//            }
//        });








    }

}


