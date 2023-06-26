package com.lickling.mymusic.ui.home;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lickling.mymusic.MyTest;
import com.lickling.mymusic.R;
import com.lickling.mymusic.databinding.ActivityMainBinding;
import com.lickling.mymusic.databinding.DesktopBinding;
import com.lickling.mymusic.service.BaseMusicService;
import com.lickling.mymusic.service.OurMusicService;
import com.lickling.mymusic.ui.BaseActivity;
import com.lickling.mymusic.ui.home.PQ.Desktop;
import com.lickling.mymusic.ui.home.PQ.Desktop_one;
import com.lickling.mymusic.utilty.PermissionUtil;
import com.lickling.mymusic.utilty.PictureUtil;
import com.lickling.mymusic.viewmodel.MusicViewModel;

import java.util.List;
import java.util.Timer;

public class MainActivity extends BaseActivity {


    private static final String TAG = "MainActivity";

    private DesktopBinding mMainBinding;
    private MusicViewModel mMusicViewModel;
    private Timer mTimer;
    private Intent mIntentMusic;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);

        mMainBinding = DataBindingUtil.setContentView(this, R.layout.desktop);
        mMusicViewModel = new MusicViewModel(getApplication());
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
            activityOnChildrenLoad(mMusicViewModel,
                    mMainBinding.bottom1,
                    children);
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


        mMainBinding.navigationBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Animation animation = null;
                switch (i) {
                    case R.id.home_btn:
                        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
                        mMainBinding.bottom1.startAnimation(animation);
//                replaceFragment(new Desktop_one());
                        mMainBinding.bottom1.setSelected(true);
                        mMainBinding.bottom2.setSelected(false);
                        mMainBinding.bottom3.setSelected(false);
                        mMainBinding.bottom4.setSelected(false);
                        break;
                    case R.id.blog_btn:
                        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
                        mMainBinding.bottom2.startAnimation(animation);
//                replaceFragment(new Desktop_one());
                        mMainBinding.bottom1.setSelected(false);
                        mMainBinding.bottom2.setSelected(true);
                        mMainBinding.bottom3.setSelected(false);
                        mMainBinding.bottom4.setSelected(false);
                        break;
                    case R.id.class_btn:
                        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
                        mMainBinding.bottom3.startAnimation(animation);
//                replaceFragment(new Desktop_one());
                        mMainBinding.bottom1.setSelected(false);
                        mMainBinding.bottom2.setSelected(false);
                        mMainBinding.bottom3.setSelected(true);
                        mMainBinding.bottom4.setSelected(false);
                        break;
                    case R.id.user_btn:
                        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
                        mMainBinding.bottom4.startAnimation(animation);
//                replaceFragment(new Desktop_one());
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
//        replaceFragment(new Desktop_one());


    }


    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager == null) {
            Log.e(TAG, "fragmentManager = null");
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (transaction == null) {
            Log.e(TAG, "transaction = null");
            return;
        }
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
