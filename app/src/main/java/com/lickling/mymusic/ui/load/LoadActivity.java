package com.lickling.mymusic.ui.load;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class LoadActivity extends AppCompatActivity implements SongOperationPopup.OnDeleteItemListener,
        SongOperationPopup2.OnDeleteItemListener, ListAdapter.OnCheckItemListener, MulOperationPopup.OnDeleteMulItemListener,
        MulOperationPopup2.OnDeleteMulItemListener2, ListAdapter2.OnCheckItemListener2 {
    private ListAdapter listAdapter;
    private List<ListItem> listItems;
    private List<Integer> positions;
    private ListAdapter2 listAdapter2;
    private List<ListItem> loading_listItems;
    private SongOperationPopup dialog;
    private SongOperationPopup2 dialog2;
    private boolean find_tag;
    private String keyword;
    private List<ListItem> found_items;
    private int checked_item_num;
    private Context context;

    private View load_view;
    private MulOperationPopup mulOperationPopup;
    private MulOperationPopup2 mulOperationPopup2;
    private Toolbar back_btn;
    private RecyclerView recyclerView;
    private Button loaded_btn;
    private Button loading_btn;
    private View select_tag1;
    private View select_tag2;
    private CheckBox all_choice_btn;
    private Button cancel_choice_btn;
    private Button play_btn;
    private Button multi_choice_btn;
    private TextView checked_item_info;
    private SearchView search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_loaded);
        load_view = getWindow().getDecorView();
        context = load_view.getContext();
        positions = new ArrayList<>();
        checked_item_num = 0;
        found_items = new ArrayList<>();

        mulOperationPopup = new MulOperationPopup(context);
        mulOperationPopup.setOnDeleteMulItemListener(this);
        mulOperationPopup2 = new MulOperationPopup2(context);
        mulOperationPopup2.setOnDeleteMulItemListener2(this);
        checked_item_info = findViewById(R.id.checked_item_info);

        //返回上一个页面
        back_btn = findViewById(R.id.load_navigation);
        back_btn.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItems.add(new ListItem("歌曲名", "歌手"));
                int visibility = select_tag1.getVisibility();

                if (visibility == View.VISIBLE)
                    recyclerView.setAdapter(listAdapter);
            }
        });
        // 在此处添加菜单项的添加按钮到Toolbar上
        getMenuInflater().inflate(R.menu.load_toolbar_menu, back_btn.getMenu());

        //播放歌曲
        play_btn = findViewById(R.id.play_btn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading_listItems.add(new ListItem("歌曲名", "歌手"));
                int visibility = select_tag2.getVisibility();

                if (visibility == View.VISIBLE)
                    recyclerView.setAdapter(listAdapter2);
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
                search_bar.setVisibility(View.INVISIBLE);
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
                } else if (select_tag2.getVisibility() == view.VISIBLE) {
                    //显示底部操作弹窗
                    mulOperationPopup2.show(recyclerView);
                    // 遍历RecyclerView的每个Item，并将它们的按钮更改为可选框,将扩展按钮设置为不可见
                    for (int i = 0; i < recyclerView.getChildCount(); i++) {
                        View itemView = recyclerView.getChildAt(i);
                        CheckBox check_box = itemView.findViewById(R.id.check_box);
                        check_box.setVisibility(View.VISIBLE);
                        Button extend_btn = itemView.findViewById(R.id.extend_btn);
                        extend_btn.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        //取消多选按钮
        cancel_choice_btn = findViewById(R.id.cancel_choice_btn);
        cancel_choice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!search_bar.isIconified()) {
                    //调两次，第一次将文本清空，第二次将搜索栏关闭
                    search_bar.setIconified(true);
                    search_bar.setIconified(true);
                    closeSearchBarAct();
                } else {
                    all_choice_btn.setChecked(false);
                    checked_item_info.setVisibility(View.GONE);
                    search_bar.setVisibility(View.VISIBLE);
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
                    } else if (select_tag2.getVisibility() == view.VISIBLE) {
                        mulOperationPopup2.dismiss();
                        // 遍历RecyclerView的每个Item，并将它们的可选框设置为不可见,将扩展按钮设置为可见
                        for (int i = 0; i < recyclerView.getChildCount(); i++) {
                            View itemView = recyclerView.getChildAt(i);
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
                    } else if (select_tag2.getVisibility() == View.VISIBLE) {
                        for (int i = 0; i <= loading_listItems.size() - 1; i++)
                            positions.add(i);
                        checked_item_num = loading_listItems.size();
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
                mulOperationPopup2.dismiss();

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
                mulOperationPopup.dismiss();

                loading_btn.setTypeface(null, Typeface.BOLD);
                select_tag2.setVisibility(View.VISIBLE);

                loaded_btn.setTypeface(null, Typeface.NORMAL);
                select_tag1.setVisibility(View.INVISIBLE);

                resetSurface();
                closeSearchBarAct();
                recyclerView.setAdapter(listAdapter2);
            }
        });

        //搜索栏
        search_bar = findViewById(R.id.search_bar);
        search_bar.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchBarAct();
            }
        });
        search_bar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                closeSearchBarAct();
                return false;
            }
        });
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                findItems();
                showFoundItems();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                findItems();
                showFoundItems();
                return false;
            }
        });

        //已下载列表
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        // 在此处添加数据到listItems
        listItems.add(new ListItem("歌曲11", "歌手21"));
        listItems.add(new ListItem("歌曲12", "歌手22"));
        listItems.add(new ListItem("歌曲13", "歌手23"));

        dialog = new SongOperationPopup(this);
        dialog.setOnDeleteItemListener(this);

        listAdapter = new ListAdapter(listItems, this);
        listAdapter.setDialog(dialog);
        listAdapter.setOnCheckItemListener(this);
        recyclerView.setAdapter(listAdapter);

        //正在下载列表
        loading_listItems = new ArrayList<>();

        loading_listItems.add(new ListItem("歌曲1", "歌手1"));
        loading_listItems.add(new ListItem("歌曲2", "歌手2"));
        loading_listItems.add(new ListItem("歌曲3", "歌手3"));

        dialog2 = new SongOperationPopup2(this);
        dialog2.setOnDeleteItemListener(this);

        listAdapter2 = new ListAdapter2(loading_listItems, this);
        listAdapter2.setDialog(dialog2);
        listAdapter2.setOnCheckItemListener2(this);
    }

    @Override
    public void onDeleteItem(int position) {
        if(find_tag){
            ListItem tmp = found_items.get(position);
            found_items.remove(tmp);
            listItems.remove(tmp);
        }
        else
            listItems.remove(position);
        listAdapter.notifyItemRemoved(position);
        listAdapter.notifyItemRangeChanged(position, listAdapter.getItemCount() - position);
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void onDeleteItem(int position, int tag) {
        loading_listItems.remove(position);
        listAdapter2.notifyItemRemoved(position);
        listAdapter2.notifyItemRangeChanged(position, listAdapter2.getItemCount() - position);
        recyclerView.setAdapter(listAdapter2);

    }

    //重置布局
    @SuppressLint("SetTextI18n")
    public void resetSurface() {
        if (select_tag1.getVisibility() == View.VISIBLE) {
            play_btn.setVisibility(View.VISIBLE);
            search_bar.setIconified(true);
            search_bar.setVisibility(View.VISIBLE);
        } else if (select_tag2.getVisibility() == View.VISIBLE) {
            search_bar.setIconified(true);
            search_bar.setVisibility(View.GONE);
            play_btn.setVisibility(View.GONE);
        }
        cancel_choice_btn.setVisibility(View.GONE);
        multi_choice_btn.setVisibility(View.VISIBLE);
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

    @Override
    public void onCheckItem2(int position, boolean tag) {
        if (tag) {
            positions.add(position);
            checked_item_num++;
            if (checked_item_num == loading_listItems.size())
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
    public void onDeleteMulItem2() {
        //倒序遍历可以保证删除元素不会影响后续元素的下标
        Collections.sort(positions);
        for (int i = loading_listItems.size() - 1; i >= 0; i--) {
            if (positions.contains(i)) {
                loading_listItems.remove(i);
            }
        }
        for (int i = positions.size() - 1; i >= 0; i--) {
            int position = positions.get(i);
            listAdapter2.notifyItemRemoved(position);
            listAdapter2.notifyItemRangeChanged(position, listAdapter2.getItemCount() - position);
        }
        checked_item_num = 0;
        positions.clear();
        recyclerView.setAdapter(listAdapter2);
        resetSurface();
    }

    public void showSearchBarAct() {
        multi_choice_btn.setVisibility(View.GONE);
        cancel_choice_btn.setVisibility(View.VISIBLE);
        if (select_tag1.getVisibility() == View.VISIBLE) {
            play_btn.setVisibility(View.GONE);
        }
    }

    public void closeSearchBarAct() {
        multi_choice_btn.setVisibility(View.VISIBLE);
        cancel_choice_btn.setVisibility(View.GONE);
        listAdapter.setListItems(listItems);
        find_tag = false;
        if (select_tag1.getVisibility() == View.VISIBLE) {
            play_btn.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(listAdapter);
        } else if (select_tag2.getVisibility() == View.VISIBLE) {
            recyclerView.setAdapter(listAdapter2);
        }
    }

    public void findItems(){
        keyword = search_bar.getQuery().toString();
        found_items = listItems.stream().filter(s -> s.getTitle().contains(keyword)||
                s.getSubtitle().contains(keyword)).collect(Collectors.toList());
    }
    public void showFoundItems(){
        listAdapter.setListItems(found_items);
        recyclerView.setAdapter(listAdapter);
        find_tag = true;
    }

}
