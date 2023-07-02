package com.lickling.mymusic.ui.songAndLyrics;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lickling.mymusic.databinding.PlayingListPopupBinding;
import com.lickling.mymusic.ui.BaseActivity;
import com.lickling.mymusic.ui.local.ListAdapter;
import com.lickling.mymusic.ui.songAndLyrics.util.LrcUtil;
import com.lickling.mymusic.ui.songAndLyrics.view.PlayingListFragment;
import com.lickling.mymusic.ui.songAndLyrics.view.SlideView;
import com.lickling.mymusic.R;
import com.lickling.mymusic.databinding.ActivitySongLrcBinding;
import com.lickling.mymusic.service.manager.MediaPlayerManager;
import com.lickling.mymusic.service.manager.MyAudioManager;
import com.lickling.mymusic.ui.songAndLyrics.viewmodel.PlayListViewModel;
import com.lickling.mymusic.viewmodel.SongLrcViewModel;

import java.util.List;
import java.util.Timer;

public class SongLrcActivity extends BaseActivity<SongLrcViewModel> {

    private static final String TAG = "SongLrcActivity";

    private ActivitySongLrcBinding mSongLrcBinding;
    private SongLrcViewModel mSongLrcViewModel;
    private PlayListViewModel mPlayListViewModel;
    private Timer mTimer;
    private SlideView mSlideView;
    private ObjectAnimator mRecordAnimator;
    private ListAdapter listAdapter;
    PlayingListPopupBinding mPlayingListPopupBinding;
    private final static int pauseRotation = -32, playRotation = -2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlayingListPopupBinding = DataBindingUtil.setContentView(this, R.layout.playing_list_popup);
        mPlayListViewModel = new PlayListViewModel(getApplication());
        // mPlayingListPopupBinding.setLocalInfo(mPlayListViewModel);

        mSongLrcBinding = DataBindingUtil.setContentView(this, R.layout.activity_song_lrc);
        mSongLrcViewModel = new SongLrcViewModel(getApplication());
        mSongLrcBinding.setSongLrcInfo(mSongLrcViewModel);


        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        UpdateProgressBar();
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
        if (mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        release();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void initView() {
        // 绑定歌曲及词界面
        mSlideView = new SlideView(this, SlideView.SLIDE_DIRECTION_DOWN);
        mSongLrcBinding.songLrcRootLayout.setOnApplyWindowInsetsListener(this);
        mRecordAnimator = super.initAnimation(mSongLrcBinding.songLrcCslCenterIvAlbum);

        mSongLrcBinding.songLrcTopReturn.setOnClickListener(v -> onFinish());

        mSongLrcBinding.songLrcBar.setOnSeekBarChangeListener(mSongLrcViewModel.getSeekBarListener());
        mSongLrcBinding.songLrcTopBarVolume
                .setOnSeekBarChangeListener(mSongLrcViewModel.getVolumeListener());

        mSongLrcBinding.songLrcCenterLrc.setOnClickListener(v -> mSongLrcViewModel.setShowLyric(false));
        mSongLrcBinding.songLrcBottomList.setOnClickListener(v -> mSongLrcViewModel.playListButton(getSupportFragmentManager()));

        // 绑定播放列表界面
        mPlayingListPopupBinding.playingListPopup.setOnApplyWindowInsetsListener(this);
        mPlayingListPopupBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        listAdapter = new ListAdapter(getApplication());
        listAdapter.setContext(this);
        mPlayingListPopupBinding.recyclerView.setAdapter(listAdapter);

        // 缩小唱片和音乐图片的组合
        sureRecordSize();

        //让vector资源替换颜色
//        int color_play_ctrl = Color.parseColor("#cc3946");
//        mSongLrcBinding.songLrcBottomLeft.getDrawable().setTint(color_play_ctrl);
//        mSongLrcBinding.songLrcBottomRight.getDrawable().setTint(color_play_ctrl);
//        mSongLrcBinding.songLrcBottomPlay.getDrawable().setTint(color_play_ctrl);
//
//        int color_bottom_btn = Color.parseColor("#cc3946");
//        mSongLrcBinding.songLrcCenterLove.getDrawable().setTint(color_bottom_btn);
//        mSongLrcBinding.songLrcBottomList.getDrawable().setTint(color_bottom_btn);
//        mSongLrcBinding.songLrcCenterOpen.getDrawable().setTint(color_bottom_btn);
//        mSongLrcBinding.songLrcBottomPlayMode.getDrawable().setTint(color_bottom_btn);
//        mSongLrcBinding.songLrcCenterDescargar.getDrawable().setTint(color_bottom_btn);
    }

    private void release() {
        if (mSlideView != null) {
            mSlideView.onDestroy();
            mSlideView = null;
        }
        if (mSongLrcViewModel != null) {
            mSongLrcViewModel = null;
        }
        if (mSongLrcBinding != null) {
            mSongLrcBinding.unbind();
            mSongLrcBinding = null;
        }
    }

    public void ShowPlayList() {
        Log.i(TAG, "[SongLrcActivity] None");
        PlayingListFragment bottomSheetDialog = new PlayingListFragment();
        bottomSheetDialog.show(getSupportFragmentManager(), "SongLrcActivity");
    }

    private void UpdateProgressBar() {
        if (mTimer != null) {
            return;
        }

        mTimer = new Timer();
        mTimer.schedule(mSongLrcViewModel.getTimerTask(), 300, 300);
    }

    @Override
    protected MediaControllerCompat.Callback getControllerCallback() {
        return new MyMediaControllerCallback();
    }

    @Override
    protected MediaBrowserCompat.SubscriptionCallback getSubscriptionCallback() {
        return new MyMediaBrowserSubscriptionCallback();
    }


    private class MyMediaBrowserSubscriptionCallback extends MediaBrowserCompat.SubscriptionCallback {
        @Override
        public void onChildrenLoaded(@NonNull String parentId,
                                     @NonNull List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);
            activityOnChildrenLoad(mSongLrcViewModel,
                    mSongLrcBinding.songLrcIvLoading,
                    children);
            MediaControllerCompat mediaController =
                    MediaControllerCompat.getMediaController(SongLrcActivity.this);
            //同步歌词
            mSongLrcBinding.songLrcCenterLrc.setMediaController(mediaController);
            mSongLrcBinding.songLrcCenterLrc.setLrc(LrcUtil.getLocalLrc(
                    mSongLrcViewModel.getMusicName() + ".lrc"));
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
            mSongLrcViewModel.SyncMusicInformation();
            mSongLrcBinding.songLrcCenterLrc.setLrc(LrcUtil.getLocalLrc(
                    mSongLrcViewModel.getMusicName() + ".lrc"));
        }

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat playbackState) {
            super.onPlaybackStateChanged(playbackState);
            mSongLrcViewModel.setPlaybackState(playbackState.getState());
            updatePlaybackStateAnimator(playbackState);

        }

        @Override
        public void onSessionEvent(String event, Bundle extras) {
            super.onSessionEvent(event, extras);
            if (MyAudioManager.MyMusic_CUSTOM_ACTION_CURRENT_VOLUME.equals(event)) {
                int currentVolume = extras.getInt(event);
                Log.d(TAG, "onSessionEvent: 更新音量 " + currentVolume);
                mSongLrcViewModel.setProgressV(currentVolume);
            } else if (MediaPlayerManager.MyMusic_CUSTOM_ACTION_PLAYBACK_MODE_CHANGE.equals(event)) {
                mSongLrcViewModel.setPlaybackModeRes(
                        extras.getInt(MediaPlayerManager.MyMusic_CUSTOM_ACTION_PLAYBACK_MODE_CHANGE));
            }
        }
    }

    private void updatePlaybackStateAnimator(PlaybackStateCompat playbackState) {
        int state = playbackState.getState();
        //加载与唱片转动动画
        if (state == PlaybackStateCompat.STATE_PLAYING) {
            mSongLrcBinding.songLrcIvLoading.clearAnimation();
            mSongLrcBinding.songLrcIvLoading.setVisibility(View.GONE);

        } else if (state == PlaybackStateCompat.STATE_BUFFERING) {
            mSongLrcBinding.songLrcIvLoading.setVisibility(View.VISIBLE);
            mSongLrcBinding.songLrcIvLoading.startAnimation(getLoadingAnimation());
        }
        updateRecordState(MediaControllerCompat.getMediaController(SongLrcActivity.this).getPlaybackState());
    }

    private void sureRecordSize() {
        ViewGroup.LayoutParams params =
                mSongLrcBinding.songLrcCslCenterIvAlbumBottom.getLayoutParams();
        int width = Math.min(mPhoneWidth, mPhoneHeight);
        // 如果长宽差小于250, 怀疑窗口化, 因此使用宽减去250的单位来进设置尺寸
        width = Math.abs(mPhoneWidth - mPhoneHeight) < 250 ? width - 150 : width;
        int size = (int) (width * 0.70);
        params.width = size;
        params.height = size;

        params = mSongLrcBinding.songLrcCslCenterIvAlbum.getLayoutParams();
        // 设置留白
        size -= dpToPx(2);
        params.width = size;
        params.height = size;
        mSongLrcViewModel.setAlbumViewSize(size);

    }

    private void updateRecordState(PlaybackStateCompat playbackState) {
        if (mRecordAnimator == null) return;
        int state = playbackState.getState();
        if (state == PlaybackStateCompat.STATE_PLAYING) {

            if (playbackState.getExtras() == null) mRecordAnimator.start();//动画开始
            else if (playbackState.getExtras().getBoolean("Continue_Playing_Tips")) {
                Log.d(TAG, "onChildrenLoaded: " + mRecordAnimator.isStarted());
                if (mRecordAnimator.isStarted()) {
                    mRecordAnimator.resume();
                } else mRecordAnimator.start();
            }

        } else if (state == PlaybackStateCompat.STATE_PAUSED ||
                state == PlaybackStateCompat.STATE_STOPPED) {
            mRecordAnimator.pause();//动画暂停
        }
    }

}
