package com.lickling.mymusic.ui.local;


import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.databinding.LocalMusicFragmentBinding;
import com.lickling.mymusic.databinding.MylikeFragmentBinding;
import com.lickling.mymusic.service.BaseMusicService;
import com.lickling.mymusic.ui.BaseActivity;
import com.lickling.mymusic.ui.home.MainActivity;
import com.lickling.mymusic.ui.home.PQ.Desktop_three;
import com.lickling.mymusic.ui.home.PQ.Desktop_two;
import com.lickling.mymusic.ui.home.PQ.HomeFragment;
import com.lickling.mymusic.ui.home.PQ.UserFragment;
import com.lickling.mymusic.viewmodel.MusicViewModel;
import com.orm.SugarContext;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LocalActivity extends BaseActivity<MusicViewModel> implements SongOperationPopup.OnDeleteItemListener,
        ListAdapter.OnCheckItemListener, MulOperationPopup.OnDeleteMulItemListener {
    private ListAdapter listAdapter;
    private List<ListItem> listItems;
    private List<Integer> positions;
    private SongOperationPopup dialog;
    private boolean find_tag;
    private String keyword;
    private List<ListItem> found_items;
    private int checked_item_num;
    private Context context;
    private View mylike_view;
    private MulOperationPopup mulOperationPopup;
    private Toolbar back_btn;
    private RecyclerView recyclerView;
    private CheckBox all_choice_btn;
    private Button cancel_choice_btn;
    private Button play_btn;
    private Button multi_choice_btn;
    private TextView checked_item_info;
    private SearchView search_bar;



    private LocalMusicFragmentBinding localMusicFragmentBinding;
    private MusicViewModel mMusicViewModel;

    @Override
    protected MediaControllerCompat.Callback getControllerCallback() {
        return new LocalActivity.MyMediaControllerCallback();
    }

    @Override
    protected MediaBrowserCompat.SubscriptionCallback getSubscriptionCallback() {
        return new LocalActivity.MyMediaBrowserSubscriptionCallback();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localMusicFragmentBinding = DataBindingUtil.setContentView(this, R.layout.local_music_fragment);
        mMusicViewModel = new MusicViewModel(getApplication());
        localMusicFragmentBinding.setLocalInfo(mMusicViewModel);

        mylike_view = getWindow().getDecorView();
        context = mylike_view.getContext();
        positions = new ArrayList<>();
        checked_item_num = 0;
        found_items = new ArrayList<>();

        mulOperationPopup = new MulOperationPopup(context);
        mulOperationPopup.setOnDeleteMulItemListener(this);
        checked_item_info = findViewById(R.id.checked_item_info);

        //返回上一个页面
        back_btn = findViewById(R.id.load_navigation);
        back_btn.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 在此处添加菜单项的添加按钮到Toolbar上
        getMenuInflater().inflate(R.menu.load_toolbar_menu, back_btn.getMenu());

        //播放歌曲
        play_btn = findViewById(R.id.play_btn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItems.add(new ListItem("歌曲名", "歌手","128kp/s","3.3MB"));
                recyclerView.setAdapter(listAdapter);
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
                localMusicFragmentBinding.controller.setVisibility(View.GONE);

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
        });

        //取消多选按钮
        cancel_choice_btn = findViewById(R.id.cancel_choice_btn);
        cancel_choice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localMusicFragmentBinding.controller.setVisibility(View.VISIBLE);
                if (!search_bar.isIconified()) {
                    //调两次，第一次将文本清空，第二次将搜索栏关闭
                    search_bar.setIconified(true);
                    search_bar.setIconified(true);
                    closeSearchBarAct();
                } else {
                    all_choice_btn.setChecked(false);
                    checked_item_info.setVisibility(View.GONE);
                    search_bar.setVisibility(View.VISIBLE);

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
                    for (int i = 0; i <= listItems.size() - 1; i++)
                        positions.add(i);
                    checked_item_num = listItems.size();

                } else
                    checked_item_num = 0;
                checked_item_info.setText("已选" + checked_item_num + "首");
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
        listItems.add(new ListItem("歌曲11", "歌手21","128kp/s","3.3MB"));
        listItems.add(new ListItem("歌曲12", "歌手22","128kp/s","3.3MB"));
        listItems.add(new ListItem("歌曲13", "歌手23","128kp/s","3.3MB"));

        dialog = new SongOperationPopup(this);
        dialog.setOnDeleteItemListener(this);

        listAdapter = new ListAdapter(listItems, this);
        listAdapter.setDialog(dialog);
        listAdapter.setOnCheckItemListener(this);
        recyclerView.setAdapter(listAdapter);

    }

    @Override
    public void onDeleteItem(int position) {
        if (find_tag) {
            ListItem tmp = found_items.get(position);
            found_items.remove(tmp);
            listItems.remove(tmp);
        } else
            listItems.remove(position);
        listAdapter.notifyItemRemoved(position);
        listAdapter.notifyItemRangeChanged(position, listAdapter.getItemCount() - position);
        recyclerView.setAdapter(listAdapter);
    }


    //重置布局
    @SuppressLint("SetTextI18n")
    public void resetSurface() {
        play_btn.setVisibility(View.VISIBLE);
        search_bar.setIconified(true);
        search_bar.setVisibility(View.VISIBLE);

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

    public void showSearchBarAct() {
        multi_choice_btn.setVisibility(View.GONE);
        cancel_choice_btn.setVisibility(View.VISIBLE);
        play_btn.setVisibility(View.GONE);
    }

    public void closeSearchBarAct() {
        multi_choice_btn.setVisibility(View.VISIBLE);
        cancel_choice_btn.setVisibility(View.GONE);
        listAdapter.setListItems(listItems);
        find_tag = false;
        play_btn.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(listAdapter);
    }

    public void findItems() {
        keyword = search_bar.getQuery().toString();
        found_items = listItems.stream().filter(s -> s.getTitle().contains(keyword) ||
                s.getSubtitle().contains(keyword)).collect(Collectors.toList());
    }

    public void showFoundItems() {
        listAdapter.setListItems(found_items);
        recyclerView.setAdapter(listAdapter);
        find_tag = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mMusicViewModel != null) {
            mMusicViewModel = null;
        }
        if (localMusicFragmentBinding != null) {
            localMusicFragmentBinding.unbind();
            localMusicFragmentBinding = null;
        }
    }

    private class MyMediaBrowserSubscriptionCallback extends MediaBrowserCompat.SubscriptionCallback {
        @Override
        public void onChildrenLoaded(@NonNull String parentId,
                                     @NonNull List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);

            activityOnChildrenLoad(mMusicViewModel, localMusicFragmentBinding.imageViewPlaying, children);

            mMusicViewModel.setPhoneRefresh(mRefreshRateMax);
            //！！！少更新样式状态
            mMusicViewModel.setCustomStyle(MediaControllerCompat.getMediaController(LocalActivity.this)
                    .getMetadata().getLong(BaseMusicService.MyMusic_NOTIFICATION_STYLE) == 0
            );
        }

        @Override
        public void onError(@NonNull String parentId) {
            super.onError(parentId);
        }
    }
    private class MyMediaControllerCallback extends MediaControllerCompat.Callback {
        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
            mMusicViewModel.SyncMusicInformation();
        }

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat playbackState) {
            super.onPlaybackStateChanged(playbackState);
            //Log.w(TAG, "onPlaybackStateChanged: "+state);
            mMusicViewModel.setPlaybackState(playbackState.getState());
            playbackStateChanged(playbackState,
                    localMusicFragmentBinding.playBtn);
        }

        @Override
        public void onSessionEvent(String event, Bundle extras) {
            super.onSessionEvent(event, extras);
        }
    }

    private void initView() {

        //localMusicFragmentBinding.activityMainUiRoot.setOnApplyWindowInsetsListener(this);


        super.initAnimation(localMusicFragmentBinding.imageViewPlaying);

        // 设置文字自动滚动
        localMusicFragmentBinding.songName.setSingleLine(true);
        localMusicFragmentBinding.songName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        localMusicFragmentBinding.songName.setMarqueeRepeatLimit(-1);
        localMusicFragmentBinding.songName.setSelected(true);

        // 播放按键
        localMusicFragmentBinding.imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(LocalActivity.this, R.anim.alpha);
                localMusicFragmentBinding.imageViewPlay.startAnimation(animation);
                mMusicViewModel.playbackButton();

            }
        });

        // 下一首按键
        localMusicFragmentBinding.imageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(LocalActivity.this, R.anim.alpha);
                localMusicFragmentBinding.imageViewNext.startAnimation(animation);
                mMusicViewModel.skipToNextPlayBack();
            }
        });

        // 队列按键
        localMusicFragmentBinding.imageViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(LocalActivity.this, R.anim.alpha);
                localMusicFragmentBinding.imageViewList.startAnimation(animation);
            }
        });



    }
}

