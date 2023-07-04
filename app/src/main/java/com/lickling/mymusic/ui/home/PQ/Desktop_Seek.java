package com.lickling.mymusic.ui.home.PQ;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.adapter.MusicAdapter;
import com.lickling.mymusic.bean.musicBean.MusicBean;
import com.lickling.mymusic.databinding.DesktopSeekBinding;
import com.lickling.mymusic.databinding.LocalMusicFragmentBinding;
import com.lickling.mymusic.model.MainModel;
import com.lickling.mymusic.model.MusicModel;
import com.lickling.mymusic.network.NetEase.NetEaseApiHandler;
import com.lickling.mymusic.service.BaseMusicService;
import com.lickling.mymusic.service.OurMusicService;
import com.lickling.mymusic.ui.BaseActivity;
import com.lickling.mymusic.ui.home.MainActivity;
import com.lickling.mymusic.ui.local.LocalActivity;
import com.lickling.mymusic.ui.songAndLyrics.SongLrcActivity;
import com.lickling.mymusic.utilty.ImmersiveStatusBarUtil;
import com.lickling.mymusic.utilty.MusicInfoConversion;
import com.lickling.mymusic.utilty.PermissionUtil;
import com.lickling.mymusic.viewmodel.MusicViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

public class Desktop_Seek extends BaseActivity<MusicViewModel> implements SongOperationPopup.OnDeleteItemListener,
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
    private List<MusicBean> searchList;
    private ProgressDialog progressDialog;
    private NetEaseApiHandler client;
    private MainModel mainModel;
   private Timer mTimer;

    private Intent mIntentMusic;
    private static final String TAG = "Desktop_seek";
    private MusicViewModel mMusicViewModel;


    @Override
    protected MediaControllerCompat.Callback getControllerCallback() {
        return new Desktop_Seek.MyMediaControllerCallback();
    }

    @Override
    protected MediaBrowserCompat.SubscriptionCallback getSubscriptionCallback() {
        return new Desktop_Seek.MyMediaBrowserSubscriptionCallback();
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        desktopSeekBinding = DataBindingUtil.setContentView(this, R.layout.desktop_seek);
        mMusicViewModel = new MusicViewModel(getApplication());
        desktopSeekBinding.setSearchInfo(mMusicViewModel);
        initView();

        ImmersiveStatusBarUtil.transparentBar(this, false);
        mainModel = new MainModel(this);

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
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("加载中...");

        client = mainModel.getClient();

        dialog = new SongOperationPopup(this);
        dialog.setOnDeleteItemListener(this);
        searchList = new ArrayList<>();
        listAdapter = new ListAdapter(searchList, this);
        listAdapter.setDialog(dialog);
        listAdapter.setOnCheckItemListener(Desktop_Seek.this);

        recyclerView.setAdapter(listAdapter);


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

        listAdapter.notifyDataSetChanged();

        //歌单列表
        listItems3 = new ArrayList<>();

        listItems3.add(new ListItem3("歌曲1", "12首", "创建者", "1245"));
        listItems3.add(new ListItem3("歌曲2", "12首", "创建者", "57786"));
        listItems3.add(new ListItem3("歌曲3", "24首", "创建者", "254777"));

        listAdapter3 = new ListAdapter3(listItems3, this);
        ImageView imageView = findViewById(R.id.search_btn);
        // 搜索
        imageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NotifyDataSetChanged", "CheckResult"})
            @Override
            public void onClick(View view) {
                progressDialog.show();
                // 获取歌曲ID，歌曲名，歌手
                client.getCloudSearchSingleSong(desktopSeekBinding.searchEdit.getQuery().toString(), 30, 0)
                        .observeOn(AndroidSchedulers.mainThread()) // 切换回主线程
                        .subscribe(result -> {
                            // 代码开始
                            // 代码, 比如更新ui, 或者打印
                            if (result != null) {
                                searchList.clear();
                                searchList.addAll(MusicInfoConversion.SearchMusicList2MusicBeanList(result.getSongsList()));
                                listAdapter.setListItems(searchList);
                                recyclerView.setAdapter(listAdapter);
                                progressDialog.dismiss();
                            }
                            // 代码结束
                        }, client.defErrorHandler());

            }
        });


        // 播放歌曲
        play_btn = findViewById(R.id.play_btn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int visibility = select_tag2.getVisibility();

                if (visibility == View.VISIBLE)
                    recyclerView.setAdapter(listAdapter3);
            }
        });

        // 多选按钮
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

        // 取消多选按钮
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

        // 全选按钮
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

        //显示单曲
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

        // 显示歌单
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

    // 重置布局
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













    @Override
    protected void onStart() {
        super.onStart();
//
//        UpdateProgressBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mIntentMusic != null) {
            mIntentMusic = null;
        }
        if (mMusicViewModel != null) {
            mMusicViewModel = null;
        }
        if (desktopSeekBinding != null) {
            desktopSeekBinding.unbind();
            desktopSeekBinding = null;
        }
    }


    private class MyMediaBrowserSubscriptionCallback extends MediaBrowserCompat.SubscriptionCallback {
        @Override
        public void onChildrenLoaded(@NonNull String parentId,
                                     @NonNull List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);

            activityOnChildrenLoad(mMusicViewModel, desktopSeekBinding.imageViewPlaying, children);

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
                    desktopSeekBinding.playBtn);
        }

        @Override
        public void onSessionEvent(String event, Bundle extras) {
            super.onSessionEvent(event, extras);
        }
    }


    private void initView() {


        desktopSeekBinding.seekMainUiRoot.setOnApplyWindowInsetsListener(this);




        //初始化唱片旋转动画
        super.initAnimation(desktopSeekBinding.imageViewPlaying);
        // 设置文字自动滚动
        desktopSeekBinding.songName.setSingleLine(true);
        desktopSeekBinding.songName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        desktopSeekBinding.songName.setMarqueeRepeatLimit(-1);
        desktopSeekBinding.songName.setSelected(true);

        // 播放按键
        desktopSeekBinding.imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop_Seek.this, R.anim.alpha);
                desktopSeekBinding.imageViewPlay.startAnimation(animation);
                mMusicViewModel.playbackButton();

            }
        });

        // 下一首按键
        desktopSeekBinding.imageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop_Seek.this, R.anim.alpha);
                desktopSeekBinding.imageViewNext.startAnimation(animation);
                mMusicViewModel.skipToNextPlayBack();
            }
        });

        // 队列按键
        desktopSeekBinding.imageViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Desktop_Seek.this, R.anim.alpha);
                desktopSeekBinding.imageViewList.startAnimation(animation);
            }
        });

        desktopSeekBinding.songLrcViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Desktop_Seek.this, SongLrcActivity.class));
                overridePendingTransition(R.anim.push_in, 0);
            }
        });

    }



}










