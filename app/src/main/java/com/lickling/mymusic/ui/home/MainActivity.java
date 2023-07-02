package com.lickling.mymusic.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lickling.mymusic.R;
import com.lickling.mymusic.databinding.DesktopBinding;
import com.lickling.mymusic.model.MainModel;
import com.lickling.mymusic.service.BaseMusicService;
import com.lickling.mymusic.service.OurMusicService;
import com.lickling.mymusic.ui.BaseActivity;
import com.lickling.mymusic.ui.home.PQ.Desktop_three;
import com.lickling.mymusic.ui.home.PQ.Desktop_two;
import com.lickling.mymusic.ui.home.PQ.HomeFragment;
import com.lickling.mymusic.ui.home.PQ.UserFragment;
import com.lickling.mymusic.ui.songAndLyrics.SongLrcActivity;
import com.lickling.mymusic.utilty.PermissionUtil;
import com.lickling.mymusic.viewmodel.MusicViewModel;
import com.lickling.mymusic.viewmodel.UserViewModel;
import com.orm.SugarContext;

import java.util.List;
import java.util.Timer;

public class MainActivity extends BaseActivity<MusicViewModel> {


    private static final String TAG = "MainActivity";

    private DesktopBinding mMainBinding;
    private MusicViewModel mMusicViewModel;
    private MainModel mainModel;
    private UserViewModel userViewModel;
    private Timer mTimer;
    private Intent mIntentMusic;
    private HomeFragment homeFragment;
    private UserFragment userFragment;

    @Override
    protected MediaControllerCompat.Callback getControllerCallback() {
        return new MainActivity.MyMediaControllerCallback();
    }

    @Override
    protected MediaBrowserCompat.SubscriptionCallback getSubscriptionCallback() {
        return new MainActivity.MyMediaBrowserSubscriptionCallback();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (PermissionUtil.IsPermissionNotObtained(this)) {
            PermissionUtil.getStorage(this);
        }
        super.onCreate(savedInstanceState);

        mainModel = new MainModel(this);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.desktop);
        mMusicViewModel = new MusicViewModel(getApplication());
        userViewModel = new UserViewModel(getApplication());

        mMainBinding.setBaseInfo(mMusicViewModel);

        super.setBackToDesktop();

        initView();
        mIntentMusic = new Intent(this, OurMusicService.class);
        this.startService(mIntentMusic);

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
    protected void onDestroy() {
        super.onDestroy();
        if (mIntentMusic != null) {
            mIntentMusic = null;
        }
        if (mMusicViewModel != null) {
            mMusicViewModel = null;
        }
        if (mMainBinding != null) {
            mMainBinding.unbind();
            mMainBinding = null;
        }
        SugarContext.terminate();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PermissionUtil.REQUEST_PERMISSION_CODE) {
            if (PermissionUtil.IsPermissionNotObtained(this)) {
                PermissionUtil.getStorage(this);
            } else {
                Log.w(TAG, "onRequestPermissionsResult: 已获取读写权限");
                //添加列表
                super.subscribe();
            }
        }
    }

    private class MyMediaBrowserSubscriptionCallback extends MediaBrowserCompat.SubscriptionCallback {
        @Override
        public void onChildrenLoaded(@NonNull String parentId,
                                     @NonNull List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);

            activityOnChildrenLoad(mMusicViewModel, mMainBinding.imageViewPlaying, children);

            mMusicViewModel.setPhoneRefresh(mRefreshRateMax);
            //！！！少更新样式状态
            mMusicViewModel.setCustomStyle(MediaControllerCompat.getMediaController(MainActivity.this)
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
                    mMainBinding.bottom1);
        }

        @Override
        public void onSessionEvent(String event, Bundle extras) {
            super.onSessionEvent(event, extras);
        }
    }

    private void initView() {

        mMainBinding.activityMainUiRoot.setOnApplyWindowInsetsListener(this);
        homeFragment = new HomeFragment(mMusicViewModel);
        userFragment = new UserFragment(userViewModel);

        super.initAnimation(mMainBinding.imageViewPlaying);

        // 设置文字自动滚动
        mMainBinding.songName.setSingleLine(true);
        mMainBinding.songName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mMainBinding.songName.setMarqueeRepeatLimit(-1);
        mMainBinding.songName.setSelected(true);

        // 播放按键
        mMainBinding.imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
                mMainBinding.imageViewPlay.startAnimation(animation);
                mMusicViewModel.playbackButton();
            }
        });

        // 下一首按键
        mMainBinding.imageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
                mMainBinding.imageViewNext.startAnimation(animation);
                mMusicViewModel.skipToNextPlayBack();
            }
        });

        // 队列按键
        mMainBinding.imageViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
                mMainBinding.imageViewList.startAnimation(animation);
            }
        });
        // 底部导航栏
        mMainBinding.navigationBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Animation animation = null;
                switch (i) {
                    case R.id.home_btn:
                        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
                        mMainBinding.bottom1.startAnimation(animation);
                        replaceFragment(homeFragment);
                        mMainBinding.bottom1.setSelected(true);
                        mMainBinding.bottom2.setSelected(false);
                        mMainBinding.bottom3.setSelected(false);
                        mMainBinding.bottom4.setSelected(false);
                        break;
                    case R.id.blog_btn:
                        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
                        mMainBinding.bottom2.startAnimation(animation);
                        replaceFragment(new Desktop_two());
                        mMainBinding.bottom1.setSelected(false);
                        mMainBinding.bottom2.setSelected(true);
                        mMainBinding.bottom3.setSelected(false);
                        mMainBinding.bottom4.setSelected(false);
                        break;
                    case R.id.class_btn:
                        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
                        mMainBinding.bottom3.startAnimation(animation);
                        replaceFragment(new Desktop_three());
                        mMainBinding.bottom1.setSelected(false);
                        mMainBinding.bottom2.setSelected(false);
                        mMainBinding.bottom3.setSelected(true);
                        mMainBinding.bottom4.setSelected(false);
                        break;
                    case R.id.user_btn:
                        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
                        mMainBinding.bottom4.startAnimation(animation);
                        replaceFragment(userFragment);
                        mMainBinding.bottom1.setSelected(false);
                        mMainBinding.bottom2.setSelected(false);
                        mMainBinding.bottom3.setSelected(false);
                        mMainBinding.bottom4.setSelected(true);
                        break;
                    default:
                        break;
                }
            }
        });

        mMainBinding.bottom1.setSelected(true);
        replaceFragment(homeFragment);

        // 底边音乐播放栏
        mMainBinding.songLrcViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SongLrcActivity.class));
                overridePendingTransition(R.anim.push_in, 0);
            }
        });

    }


    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
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
}
