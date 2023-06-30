package com.lickling.mymusic.ui.home.PQ;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.musicBean.MusicBean;
import com.lickling.mymusic.databinding.DesktopSeekBinding;
import com.lickling.mymusic.model.MusicModel;
import com.lickling.mymusic.ui.load.ListAdapter2;
import com.lickling.mymusic.ui.load.MulOperationPopup2;
import com.lickling.mymusic.ui.load.SongOperationPopup2;
import com.lickling.mymusic.utilty.ImmersiveStatusBarUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Desktop_Seek extends AppCompatActivity implements SongOperationPopup.OnDeleteItemListener,
        ListAdapter.OnCheckItemListener, MulOperationPopup.OnDeleteMulItemListener {


    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<MusicBean> listItems;

    private ListAdapter3 listAdapter3;
    private List<ListItem3> listItems3;

    private List<Integer> positions;

    private SongOperationPopup dialog;

    private int checked_item_num;
    private Context context;
    private View load_view;
    private MulOperationPopup mulOperationPopup;

    private Button loaded_btn;
    private Button loading_btn;
    private View select_tag1;
    private View select_tag2;
    private CheckBox all_choice_btn;
    private Button cancel_choice_btn;
    private Button play_btn;
    private Button multi_choice_btn;
    private TextView checked_item_info;

    private DesktopSeekBinding desktopSeekBinding;
    private MusicModel musicModel;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desktop_seek);
        ImmersiveStatusBarUtil.transparentBar(this, false);

        // 返回按键
        ImageView imageview_back = findViewById(R.id.imageview_back);
        imageview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop_Seek.this, R.anim.alpha);
                imageview_back.startAnimation(animation);

                finish();
            }
        });
        musicModel = new MusicModel();

        load_view = getWindow().getDecorView();
        context = load_view.getContext();
        positions = new ArrayList<>();
        checked_item_num = 0;

        mulOperationPopup = new MulOperationPopup(context);
        mulOperationPopup.setOnDeleteMulItemListener(this);

        checked_item_info = findViewById(R.id.checked_item_info);

        //已下载列表
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();


        dialog = new SongOperationPopup(this);
        dialog.setOnDeleteItemListener(this);

        listAdapter = new ListAdapter(listItems, this);
        listAdapter.setDialog(dialog);
        listAdapter.setOnCheckItemListener(this);
        recyclerView.setAdapter(listAdapter);

        //正在下载列表
        listItems3 = new ArrayList<>();

        listItems3.add(new ListItem3("歌曲1", "12首", "创建者", "1245"));
        listItems3.add(new ListItem3("歌曲2", "12首", "创建者", "57786"));
        listItems3.add(new ListItem3("歌曲3", "24首", "创建者", "254777"));

        listAdapter3 = new ListAdapter3(listItems3, this);
        ImageView imageView = findViewById(R.id.search_btn);
        // 搜索
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
//                musicModel.getSearchSong(desktopSeekBinding.searchEdit.getQuery().toString());

                listAdapter.setListItems(musicModel.getSearchSong("逆战"));
//                resetSurface();

                recyclerView.setAdapter(listAdapter);

//                recyclerView.notify();
            }
        });
//        desktopSeekBinding.searchBtn.setOnClickListener(view -> {
//            Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
//            musicModel.getSearchSong(desktopSeekBinding.searchEdit.getQuery().toString());
////            listAdapter.setListItems();
//
//        });


        //播放歌曲
        play_btn = findViewById(R.id.play_btn);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int visibility = select_tag2.getVisibility();

                if (visibility == View.VISIBLE)
                    recyclerView.setAdapter(listAdapter3);
            }
        });

        //多选按钮
        multi_choice_btn = findViewById(R.id.multi_choice_btn);
        multi_choice_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                checked_item_num = 0;
                positions.clear();

                play_btn.setVisibility(View.GONE);
                all_choice_btn.setVisibility(View.VISIBLE);
                cancel_choice_btn.setVisibility(View.VISIBLE);
                multi_choice_btn.setVisibility(View.GONE);
                checked_item_info.setText("已选" + checked_item_num + "首");
                checked_item_info.setVisibility(View.VISIBLE);

                if (select_tag1.getVisibility() == view.VISIBLE) {
                    //显示底部操作弹窗
                    mulOperationPopup.show(recyclerView);
                    // 遍历RecyclerView的每个Item，并将它们的按钮更改为可选框,将扩展按钮设置为不可见
                    for (int i = 0; i < recyclerView.getChildCount(); i++) {
                        View itemView = recyclerView.getChildAt(i);
                        Button add_btn = itemView.findViewById(R.id.add_btn);
                        add_btn.setVisibility(View.GONE);
                        CheckBox check_box = itemView.findViewById(R.id.check_box);
                        check_box.setVisibility(View.VISIBLE);
                        Button extend_btn = itemView.findViewById(R.id.extend_btn);
                        extend_btn.setVisibility(View.GONE);
                    }
                }
            }
        });

        //取消多选按钮
        cancel_choice_btn = findViewById(R.id.cancel_choice_btn);
        cancel_choice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                all_choice_btn.setChecked(false);
                checked_item_info.setVisibility(View.GONE);

                if (select_tag1.getVisibility() == view.VISIBLE) {
                    mulOperationPopup.dismiss();
                    play_btn.setVisibility(View.VISIBLE);
                    // 遍历RecyclerView的每个Item，并将它们的可选框更改为下曲播放按钮,将扩展按钮设置可见
                    for (int i = 0; i < recyclerView.getChildCount(); i++) {
                        View itemView = recyclerView.getChildAt(i);
                        Button add_btn = itemView.findViewById(R.id.add_btn);
                        add_btn.setVisibility(View.VISIBLE);
                        CheckBox check_box = itemView.findViewById(R.id.check_box);
                        check_box.setChecked(false);
                        check_box.setVisibility(View.GONE);
                        Button extend_btn = itemView.findViewById(R.id.extend_btn);
                        extend_btn.setVisibility(View.VISIBLE);
                    }

                }
                all_choice_btn.setVisibility(View.GONE);
                cancel_choice_btn.setVisibility(View.GONE);
                multi_choice_btn.setVisibility(View.VISIBLE);
            }

        });

        //全选按钮
        all_choice_btn = findViewById(R.id.all_choice_btn);
        all_choice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View itemView = recyclerView.getChildAt(i);
                    CheckBox check_box = itemView.findViewById(R.id.check_box);
                    if (all_choice_btn.isChecked()) {
                        check_box.setChecked(true);
                    } else {
                        check_box.setChecked(false);
                    }
                }

                positions.clear();
                if (all_choice_btn.isChecked()) {
                    if (select_tag1.getVisibility() == View.VISIBLE) {
                        for (int i = 0; i <= listItems.size() - 1; i++)
                            positions.add(i);
                        checked_item_num = listItems.size();
                    }
                } else
                    checked_item_num = 0;
                checked_item_info.setText("已选" + checked_item_num + "首");
            }
        });

        //显示已下载歌曲
        loaded_btn = findViewById(R.id.loaded_btn);
        select_tag1 = findViewById(R.id.select_tag1);
        loaded_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mulOperationPopup.dismiss();

                loaded_btn.setTypeface(null, Typeface.BOLD);
                select_tag1.setVisibility(View.VISIBLE);

                loading_btn.setTypeface(null, Typeface.NORMAL);
                select_tag2.setVisibility(View.INVISIBLE);

                resetSurface();

                recyclerView.setAdapter(listAdapter);
            }
        });

        //显示正在下载歌曲
        loading_btn = findViewById(R.id.loading_btn);
        select_tag2 = findViewById(R.id.select_tag2);
        loading_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loading_btn.setTypeface(null, Typeface.BOLD);
                select_tag2.setVisibility(View.VISIBLE);

                loaded_btn.setTypeface(null, Typeface.NORMAL);
                select_tag1.setVisibility(View.INVISIBLE);

                resetSurface();
                recyclerView.setAdapter(listAdapter3);
            }
        });


    }

    @Override
    public void onDeleteItem(int position) {
        listItems.remove(position);
        listAdapter.notifyItemRemoved(position);
        listAdapter.notifyItemRangeChanged(position, listAdapter.getItemCount() - position);
        recyclerView.setAdapter(listAdapter);
    }


    //重置布局
    @SuppressLint("SetTextI18n")
    public void resetSurface() {
        if (select_tag1.getVisibility() == View.VISIBLE) {
            play_btn.setVisibility(View.VISIBLE);
            multi_choice_btn.setVisibility(View.VISIBLE);
        } else if (select_tag2.getVisibility() == View.VISIBLE) {
            mulOperationPopup.dismiss();
            multi_choice_btn.setVisibility(View.INVISIBLE);
            play_btn.setVisibility(View.GONE);
        }
        cancel_choice_btn.setVisibility(View.GONE);

        all_choice_btn.setChecked(false);
        all_choice_btn.setVisibility(View.GONE);
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View itemView = recyclerView.getChildAt(i);
            CheckBox check_box = itemView.findViewById(R.id.check_box);
            check_box.setChecked(false);
        }
        checked_item_info.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCheckItem(int position, boolean tag) {
        if (tag) {
            positions.add(position);
            checked_item_num++;
            if (checked_item_num == listItems.size())
                all_choice_btn.setChecked(true);
        } else {
            if (positions.contains(position)) {
                all_choice_btn.setChecked(false);
                positions.remove((Integer) position);
                checked_item_num--;
            }
        }
        checked_item_info.setText("已选" + checked_item_num + "首");
    }

    @Override
    public void onDeleteMulItem() {
        //倒序遍历可以保证删除元素不会影响后续元素的下标
        Collections.sort(positions);
        for (int i = listItems.size() - 1; i >= 0; i--) {
            if (positions.contains(i)) {
                listItems.remove(i);
            }
        }
        for (int i = positions.size() - 1; i >= 0; i--) {
            int position = positions.get(i);
            listAdapter.notifyItemRemoved(position);
            listAdapter.notifyItemRangeChanged(position, listAdapter.getItemCount() - position);
        }
        checked_item_num = 0;
        positions.clear();
        recyclerView.setAdapter(listAdapter);
        resetSurface();
    }


}



