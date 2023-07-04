package com.lickling.mymusic.ui.local;


import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.databinding.LocalMusicFragmentBinding;
import com.lickling.mymusic.databinding.LocalSongListItemBinding;

import com.lickling.mymusic.ui.BaseActivity;

import com.lickling.mymusic.ui.login.LoginNetEase;
import com.lickling.mymusic.ui.songAndLyrics.SongLrcActivity;
import com.lickling.mymusic.viewmodel.MusicViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class LocalActivity extends BaseActivity<MusicViewModel> {
    private List<Integer> positions;
    private boolean find_tag;
    private String keyword;
    private List<MediaItem> found_items;
    private int checked_item_num;
    private Context context;

    private RecyclerView recyclerView;
    private CheckBox all_choice_btn;
    private Button cancel_choice_btn;
    private Button play_btn;
    private Button multi_choice_btn;
    private TextView checked_item_info;
    private SearchView search_bar;

    private static final String TAG = "LocalActivity";
    private LocalMusicFragmentBinding localMusicFragmentBinding;
    private MusicViewModel mMusicViewModel;
    private ListAdapter listAdapter;
    private SongOperationPopup songOperationPopup;
    private MulOperationPopup mulOperationPopup;
    private MyAdapterItemClickListener myItemClickListener;
    private MyPopupClickListener myPopupClickListener;
    private MyMulPopupClickListener myMulPopupClickListener;
    private int selected_item_id = -1;
    boolean tag = true;
    private Intent mIntentMusic;

    private LocalSongListItemBinding pre_binding;
    private Timer mTimer;
    private View local_view;
    private List<MediaItem> local_items;
    private Runnable runnable;
    private Handler handler;
    private boolean index = false;

    @Override
    protected MediaControllerCompat.Callback getControllerCallback() {
        return new LocalActivity.MyMediaControllerCallback();
    }

    @Override
    protected MediaBrowserCompat.SubscriptionCallback getSubscriptionCallback() {
        return new LocalActivity.MyMediaBrowserSubscriptionCallback();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_music_fragment);
        local_view = getWindow().getDecorView();
        context = local_view.getContext();

        localMusicFragmentBinding = DataBindingUtil.setContentView(this, R.layout.local_music_fragment);
        mMusicViewModel = new MusicViewModel(getApplication());
        localMusicFragmentBinding.setLocalInfo(mMusicViewModel);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                synMediaState();
            }
        };

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //UpdateProgressBar();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 250 毫秒后执行该任务，同步正在播放的歌曲在recycleView中的状态
        handler.postDelayed(runnable,250);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
//
//        StopProgressBar();
    }

    private void initView() {
        positions = new ArrayList<>();//记录选中的Item下标
        checked_item_num = 0;   //记录选中的Item数量
        found_items = new ArrayList<>();  //存储查找到的Item
        checked_item_info = findViewById(R.id.checked_item_info); //显示选中的Item数量的文本框
        play_btn = findViewById(R.id.play_btn);   //播放按钮
        multi_choice_btn = findViewById(R.id.multi_choice_btn); //多选按钮
        cancel_choice_btn = findViewById(R.id.cancel_choice_btn); //取消多选按钮
        all_choice_btn = findViewById(R.id.all_choice_btn);    //全选按钮
        search_bar = findViewById(R.id.search_bar);     //搜索栏
        recyclerView = findViewById(R.id.recycler_view); //显示Item的View

        localMusicFragmentBinding.musicActivityUiRoot.setOnApplyWindowInsetsListener(this);
        // 在此处添加菜单项的添加按钮到Toolbar上
        getMenuInflater().inflate(R.menu.load_toolbar_menu, localMusicFragmentBinding.loadNavigation.getMenu());
        //设置返回点击事件
        localMusicFragmentBinding.loadNavigation.setNavigationOnClickListener(v -> returnClick());

        //初始化RecyclerView
        //localMusicFragmentBinding.recyclerView.setHasFixedSize(true);
        localMusicFragmentBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        listAdapter = new ListAdapter(getApplication());
        listAdapter.setContext(this);
        localMusicFragmentBinding.recyclerView.setAdapter(listAdapter);

        myItemClickListener = new MyAdapterItemClickListener();
        listAdapter.setItemClickListener(myItemClickListener);

        //Item更多弹窗
        songOperationPopup = new SongOperationPopup(this);
        myPopupClickListener = new MyPopupClickListener();
        songOperationPopup.setOnPopupClickListener(myPopupClickListener);

        //多选弹窗
        mulOperationPopup = new MulOperationPopup(this);
        myMulPopupClickListener = new MyMulPopupClickListener();
        mulOperationPopup.setOnMulPopupClickListener(myMulPopupClickListener);

        //初始化唱片旋转动画
        super.initAnimation(localMusicFragmentBinding.imageViewPlaying);
        // 设置文字自动滚动
        localMusicFragmentBinding.songName.setSingleLine(true);
        localMusicFragmentBinding.songName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        localMusicFragmentBinding.songName.setMarqueeRepeatLimit(-1);
        localMusicFragmentBinding.songName.setSelected(true);

        //设置本地音乐页面的控件的点击事件
        setClickListener();

    }

    private void setClickListener() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                alterItemLayout();

                //Toast.makeText(context, "滚动位置发生变换"+dy, Toast.LENGTH_SHORT).show();
            }

        });

        //播放歌曲
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "开始播放全部歌曲", Toast.LENGTH_SHORT).show();
            }
        });

        //多选按钮
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

                    //重置Item状态列表
                    for (int i = 0; i < listAdapter.items_state.size(); i++) {
                        listAdapter.items_state.set(i, false);
                    }

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
                    for (int i = 0; i <= local_items.size() - 1; i++)
                        positions.add(i);
                    checked_item_num = local_items.size();

                    //重置Item状态列表
                    for (int i = 0; i < listAdapter.items_state.size(); i++) {
                        listAdapter.items_state.set(i, true);
                    }
                } else {
                    checked_item_num = 0;
                    //重置Item状态列表
                    for (int i = 0; i < listAdapter.items_state.size(); i++) {
                        listAdapter.items_state.set(i, false);
                    }
                }

                checked_item_info.setText("已选" + checked_item_num + "首");
            }
        });

        //搜索栏
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

        /*下方播放栏*/
        //
        localMusicFragmentBinding.controller.setOnClickListener(view -> {
            startActivity(new Intent(LocalActivity.this, SongLrcActivity.class));
        });
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

                // listAdapter.setSelectedItemId((listAdapter.getSelectedItemId() + 1) % (listAdapter.getItems().size()));
                // refreshRecycleView();
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


    //重置布局
    @SuppressLint("SetTextI18n")
    public void resetSurface() {
        localMusicFragmentBinding.controller.setVisibility(View.VISIBLE);
        play_btn.setVisibility(View.VISIBLE);
        search_bar.setIconified(true);
        search_bar.setVisibility(View.VISIBLE);

        cancel_choice_btn.setVisibility(View.GONE);
        multi_choice_btn.setVisibility(View.VISIBLE);
        all_choice_btn.setChecked(false);
        all_choice_btn.setVisibility(View.GONE);
        for (int i = 0; i < localMusicFragmentBinding.recyclerView.getChildCount(); i++) {
            View itemView = localMusicFragmentBinding.recyclerView.getChildAt(i);
            CheckBox check_box = itemView.findViewById(R.id.check_box);
            check_box.setChecked(false);
        }
        checked_item_info.setVisibility(View.GONE);

        //重置Item状态列表
        for (int i = 0; i < listAdapter.items_state.size(); i++) {
            listAdapter.items_state.set(i, false);
        }
    }

    public void showSearchBarAct() {
        multi_choice_btn.setVisibility(View.GONE);
        cancel_choice_btn.setVisibility(View.VISIBLE);
        play_btn.setVisibility(View.GONE);
    }

    public void closeSearchBarAct() {
        multi_choice_btn.setVisibility(View.VISIBLE);
        cancel_choice_btn.setVisibility(View.GONE);
        listAdapter.mySetItems(local_items);
        find_tag = false;
        play_btn.setVisibility(View.VISIBLE);
        localMusicFragmentBinding.recyclerView.setAdapter(listAdapter);
    }

    public void findItems() {
        keyword = search_bar.getQuery().toString();
        found_items = local_items.stream().filter(s -> listAdapter.toMusicBean(s).getTitle().contains(keyword) ||
                listAdapter.toMusicBean(s).getArtist().contains(keyword)).collect(Collectors.toList());
    }

    public void showFoundItems() {
        listAdapter.mySetItems(found_items);
        localMusicFragmentBinding.recyclerView.setAdapter(listAdapter);
        find_tag = true;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //当打开软键盘时，再按返回按键，则由软键盘本身消耗本次按键事件，此处不会收到该按键回调
        if (keyCode == KeyEvent.KEYCODE_BACK) return returnClick();
        return super.onKeyDown(keyCode, event);
    }

    //多选弹窗点击事件
    private class MyMulPopupClickListener implements MulOperationPopup.OnMulPopupClickListener {
        //删除多个Item
        @Override
        public void deleteMulItemClickListener() {
            //倒序遍历可以保证删除元素不会影响后续元素的下标
            Collections.sort(positions);
            for (int i = local_items.size() - 1; i >= 0; i--) {
                if (positions.contains(i)) {
                    listAdapter.items_state.remove(i);//移除对应Item在状态标志
                    local_items.remove(i);
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

            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            mulOperationPopup.dismiss();
        }

        //添加多个Item
        @Override
        public void addMulItemClickListener() {
            resetSurface();
            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
            mulOperationPopup.dismiss();
        }

        //下一曲播放多个Item
        @Override
        public void nextPlayMulItemClickListener() {
            resetSurface();
            Toast.makeText(context, "已添加到下一曲播放", Toast.LENGTH_SHORT).show();
            mulOperationPopup.dismiss();
        }
    }

    //弹窗点击事件
    private class MyPopupClickListener implements SongOperationPopup.OnPopupClickListener {
        //删除该歌曲
        @Override
        public void deleteItemClickListener(int position) {
            if (position != -1) {
                if (find_tag) {
                    MediaItem tmp = found_items.get(position);
                    found_items.remove(tmp);
                    local_items.remove(tmp);
                    //listAdapter.setItems(found_items);
                    listAdapter.mySetItems(found_items);
                } else if (local_items != null && position < local_items.size()) {
                    local_items.remove(position);
                    //listAdapter.setItems(local_items);
                    listAdapter.mySetItems(local_items);
                }
                localMusicFragmentBinding.recyclerView.setAdapter(listAdapter);
                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
            }
            songOperationPopup.dismiss();
        }

        //下一曲播放该歌曲
        @Override
        public void nextPlayItemClickListener(int position) {
            Toast.makeText(context, "已添加到下一曲播放", Toast.LENGTH_SHORT).show();
            songOperationPopup.dismiss();
        }
    }

    //Item点击事件
    private class MyAdapterItemClickListener implements ListAdapter.OnItemClickListener {
        //播放
        @Override
        public void ItemClickListener(ListAdapter adapter, int position, LocalSongListItemBinding binding) {
            if (all_choice_btn.getVisibility() == View.VISIBLE) {
                if (!binding.checkBox.isChecked()) {
                    listAdapter.items_state.set(position, true);//Item被选中
                    binding.checkBox.setChecked(true);
                    positions.add(position);
                    checked_item_num++;
                    if (checked_item_num == local_items.size())
                        all_choice_btn.setChecked(true);
                } else {
                    if (positions.contains(position)) {
                        listAdapter.items_state.set(position, false);//Item取消选中
                        binding.checkBox.setChecked(false);
                        all_choice_btn.setChecked(false);
                        positions.remove((Integer) position);
                        checked_item_num--;
                    }
                }
                checked_item_info.setText("已选" + checked_item_num + "首");
            } else {
                MediaControllerCompat mediaController =
                        MediaControllerCompat.getMediaController(LocalActivity.this);
                String mediaId = mediaController.getMetadata()
                        .getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID),
                        currentMediaId = adapter.getItems().get(position).getMediaId();
                if (mediaId.equals(currentMediaId)) {
                    LocalActivity.this.mMusicViewModel.playbackButton();
                } else mediaController.getTransportControls().playFromMediaId(currentMediaId, null);


//                listAdapter.setSelectedItem(listAdapter.getItems().get(position));//设置当前选中的Item
//                listAdapter.setSelectedItemId(position);
//                selected_item_id = position;

                //refreshRecycleView();

                Log.d(TAG, "ItemClickListener: 点击了 " + mediaId + ", " + currentMediaId);
            }
        }

        //更多
        @Override
        public void ItemMoreClickListener(View v, int position) {
            MediaItem tmp = listAdapter.getItems().get(position);
            songOperationPopup.setData(listAdapter.toMusicBean(tmp), position);//把点击的item的数据给dialog
            //songOperationPopup.show(((AppCompatActivity) listAdapter.getContext()).getSupportFragmentManager(), "menu");
            songOperationPopup.show(getSupportFragmentManager(), "menu");
            Log.d(TAG, "ItemMoreClickListener: 点击了更多 " + position);
        }

        //下一曲播放
        @Override
        public void ItemNextPlayClickListener(View v, int position) {
            Toast.makeText(context, "已添加到下一曲播放", Toast.LENGTH_SHORT).show();
        }

        //可选框
        @Override
        public void ItemCheckedClickListener(LocalSongListItemBinding binding, int position) {
            if (binding.checkBox.isChecked()) {
                positions.add(position);
                checked_item_num++;
                listAdapter.items_state.set(position, true);//Item被选中
                if (checked_item_num == local_items.size())
                    all_choice_btn.setChecked(true);
            } else {
                if (positions.contains(position)) {
                    listAdapter.items_state.set(position, false);//Item取消选中
                    all_choice_btn.setChecked(false);
                    positions.remove((Integer) position);
                    checked_item_num--;
                }
            }
            checked_item_info.setText("已选" + checked_item_num + "首");
        }
    }

    private void UpdateProgressBar() {
        if (mTimer != null) {
            return;
        }

        mTimer = new Timer();
        mTimer.schedule(mMusicViewModel.getCircleBarTask(), 300, 300);
    }

    private void StopProgressBar() {
        if (mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
            mTimer = null;
        }
    }

    private boolean returnClick() {
        this.finish();
        overridePendingTransition(0, R.anim.push_out);
        Log.d(TAG, "returnClick: ");
        return true;
    }


    private void openActivity(int mode) {
        if (mode == 0) {
//
//            startActivity(new Intent(MusicActivity.this, SongLrcActivity.class));
            overridePendingTransition(R.anim.push_in, 0);
        }

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

            Log.e(TAG, "onChildrenLoaded: MyMusic");

            if (local_items == null) {
                local_items = new ArrayList<>();
                listAdapter.mySetItems(children);
                local_items = children;
            } else
                listAdapter.mySetItems(local_items);

            //synMediaState();
            //refreshRecycleView();

            activityOnChildrenLoad(mMusicViewModel, localMusicFragmentBinding.imageViewPlaying, children);
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
            synMediaState(); //同步正在播放的歌曲在recycleView中的状态
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

    @SuppressLint("ResourceAsColor")
    void alterItemColor(LocalSongListItemBinding binding, int position) {
        Drawable icon;
        pre_binding = listAdapter.getPreBinding();

        if (pre_binding != null && pre_binding != binding) {
            icon = ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.load_add);
            icon.setColorFilter(this.getResources().getColor(R.color.black_gray), PorterDuff.Mode.SRC_IN);
            pre_binding.addBtn.setBackground(icon);

            icon = ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.baseline_more_vert_24);
            icon.setColorFilter(this.getResources().getColor(R.color.black_gray), PorterDuff.Mode.SRC_IN);
            pre_binding.extendBtn.setBackground(icon);

            pre_binding.title.setTextColor(this.getResources().getColor(R.color.black));
            pre_binding.subtitle.setTextColor(this.getResources().getColor(android.R.color.darker_gray));
        }
        icon = ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.load_add);
        icon.setColorFilter(this.getResources().getColor(R.color.wy_red), PorterDuff.Mode.SRC_IN);
        binding.addBtn.setBackground(icon);

        icon = ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.baseline_more_vert_24);
        icon.setColorFilter(this.getResources().getColor(R.color.wy_red), PorterDuff.Mode.SRC_IN);
        binding.extendBtn.setBackground(icon);

        binding.title.setTextColor(this.getResources().getColor(R.color.wy_red));
        binding.subtitle.setTextColor(this.getResources().getColor(R.color.wy_red));

        listAdapter.setPreBinding(binding);
    }

    void alterItemLayout() {
        selected_item_id = listAdapter.getSelectedItemId();
        if (all_choice_btn.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                View itemView = recyclerView.getChildAt(i);
                Button add_btn = itemView.findViewById(R.id.add_btn);
                add_btn.setVisibility(View.GONE);
                CheckBox check_box = itemView.findViewById(R.id.check_box);
                check_box.setVisibility(View.VISIBLE);
                Button extend_btn = itemView.findViewById(R.id.extend_btn);
                extend_btn.setVisibility(View.GONE);

                TextView textView = itemView.findViewById(R.id.id);
                String text = textView.getText().toString();
                int pos = Integer.parseInt(text);
                if (all_choice_btn.isChecked() || listAdapter.items_state.get(pos))
                    check_box.setChecked(true);
                else
                    check_box.setChecked(false);

                alterItemColor(itemView);
            }
        } else {
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

                alterItemColor(itemView);
            }
        }
    }

    //同步正在播放的歌曲在recycleView中的状态
    void synMediaState() {
        MediaControllerCompat mediaController =
                MediaControllerCompat.getMediaController(LocalActivity.this);
        if (mediaController == null) {
            Toast.makeText(context, "mediaController为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String mediaId = mediaController.getMetadata()
                .getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);

        for (int i = 0; i < listAdapter.getItems().size(); i++) {
            if (listAdapter.getItems().get(i).getMediaId().equals(mediaId)) {
                selected_item_id = i;
                listAdapter.setSelectedItemId(i);
                listAdapter.setSelectedItem(listAdapter.getItems().get(i));//设置当前选中的Item
                //recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, i);//丝滑定位Item


                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
                    private static final float MILLISECONDS_PER_INCH = 20f;
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(i);
                recyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
                //recyclerView.getLayoutManager().scrollToPosition(i); // 立即定位Item
                break;
            }
        }
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View itemView = recyclerView.getChildAt(i);
            alterItemColor(itemView);
        }

    }

//    void refreshRecycleView() {
//        selected_item_id = listAdapter.getSelectedItemId();
//        for (int i = 0; i < recyclerView.getChildCount(); i++) {
//            View itemView = recyclerView.getChildAt(i);
//            alterItemColor(itemView);
//        }
//    }

    void alterItemColor(View itemView) {
        Drawable icon;
        TextView textView = itemView.findViewById(R.id.id);
        String text = textView.getText().toString();
        if (text.equals(""))
            return;
        int id = Integer.parseInt(text);

        if (selected_item_id == id) {
            icon = ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.load_add);
            icon.setColorFilter(this.getResources().getColor(R.color.wy_red), PorterDuff.Mode.SRC_IN);
            itemView.findViewById(R.id.add_btn).setBackground(icon);

            icon = ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.baseline_more_vert_24);
            icon.setColorFilter(this.getResources().getColor(R.color.wy_red), PorterDuff.Mode.SRC_IN);
            itemView.findViewById(R.id.extend_btn).setBackground(icon);

            textView = itemView.findViewById(R.id.title);
            textView.setTextColor(this.getResources().getColor(R.color.wy_red));

            textView = itemView.findViewById(R.id.subtitle);
            textView.setTextColor(this.getResources().getColor(R.color.wy_red));
        } else {
            icon = ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.load_add);
            icon.setColorFilter(this.getResources().getColor(R.color.black_gray), PorterDuff.Mode.SRC_IN);
            itemView.findViewById(R.id.add_btn).setBackground(icon);

            icon = ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.baseline_more_vert_24);
            icon.setColorFilter(this.getResources().getColor(R.color.black_gray), PorterDuff.Mode.SRC_IN);
            itemView.findViewById(R.id.extend_btn).setBackground(icon);

            textView = itemView.findViewById(R.id.title);
            textView.setTextColor(this.getResources().getColor(R.color.black));

            textView = itemView.findViewById(R.id.subtitle);
            textView.setTextColor(this.getResources().getColor(android.R.color.darker_gray));
        }

    }

}

