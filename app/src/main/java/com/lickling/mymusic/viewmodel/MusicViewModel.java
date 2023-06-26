package com.lickling.mymusic.viewmodel;

import android.app.Application;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CompoundButton;

import androidx.databinding.Bindable;

import com.lickling.mymusic.BR;
import com.lickling.mymusic.R;
import com.lickling.mymusic.adapter.MusicAdapter;
import com.lickling.mymusic.service.BaseMusicService;
import com.lickling.mymusic.utilty.HtmlStringUtil;

import java.lang.ref.SoftReference;
import java.util.TimerTask;

public class MusicViewModel extends BaseViewModel {

    private static final String TAG = "MusicViewModel";

    private Application mApplication;
    private Spanned title;
    private Spanned subtitle;
    private Spanned playbackInfo;
    //设置初始drawable文件源id
    private int playbackDrawable;
    private SoftReference<LayerDrawable> record;
    private int progress, secondProgress, max, phoneRefresh;
    private boolean isShowSearchView, isCustomStyle;
    private SoftReference<EditInputChangeListener> mTextListener;
    private SoftReference<MusicAdapter> adapter;

    public MusicViewModel(Application application) {
        super(application);
        mApplication = application;
        mTextListener = new SoftReference<>(new EditInputChangeListener());
        phoneRefresh = 60;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mApplication != null) {
            mApplication = null;
        }
        if (playbackInfo != null) {
            playbackInfo = null;
        }
        if (record != null) {
            record.clear();
            record = null;
        }
        if (mTextListener != null) {
            mTextListener.clear();
            mTextListener = null;
        }
        clearAdapter();
    }

    @Bindable
    public String getPhoneRefresh() {
        return phoneRefresh + "Hz";
    }

    public void setPhoneRefresh(int phoneRefresh) {
        this.phoneRefresh = phoneRefresh;
        notifyPropertyChanged(BR.phoneRefresh);
    }

    @Bindable
    public boolean isCustomStyle() {
        return isCustomStyle;
    }

    public void setCustomStyle(boolean customStyle) {
        this.isCustomStyle = customStyle;
        notifyPropertyChanged(BR.customStyle);
    }

    @Bindable
    public boolean isShowSearchView() {
        return isShowSearchView;
    }

    public void setSearchGroupVisible(boolean showSearch) {
        this.isShowSearchView = showSearch;
        notifyPropertyChanged(BR.showSearchView);
        if (!showSearch) clearAdapter();
    }

    @Bindable
    public LayerDrawable getRecord() {
        return record == null || record.get() == null ? null : record.get();
    }

    public void setRecord(LayerDrawable layerDrawable) {
        this.record = new SoftReference<>(layerDrawable);
        notifyPropertyChanged(BR.record);
    }

    @Bindable
    public int getProgress() {
        return progress;
    }

    @Bindable
    public int getMax() {
        return max;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        notifyPropertyChanged(BR.progress);
    }

    public void setMax(int max) {
        this.max = max;
        notifyPropertyChanged(BR.max);
    }

    @Bindable
    public Spanned getPlaybackInfo() {
        return playbackInfo;
    }

    public void setPlaybackInfo(Spanned t, Spanned a, Spanned info) {
        this.playbackInfo = info;
        this.title = t;
        this.subtitle = a;
        notifyPropertyChanged(BR.title);
        notifyPropertyChanged(BR.subtitle);
        notifyPropertyChanged(BR.playbackInfo);
    }

    public void setPlaybackInfo(Spanned info) {
        this.playbackInfo = info;
        notifyPropertyChanged(BR.playbackInfo);


    }

    @Bindable
    public int getPlaybackResId() {
        return playbackDrawable == 0 ? R.drawable.play : playbackDrawable;
    }

    public void setPlaybackState(@PlaybackStateCompat.State int playState) {
        boolean state = playState == PlaybackStateCompat.STATE_PLAYING ||
                playState == PlaybackStateCompat.STATE_PAUSED ||
                playState == PlaybackStateCompat.STATE_STOPPED ||
                playState == PlaybackStateCompat.STATE_NONE;
        if (!state) {
            return;
        }
        this.playbackDrawable = playState == PlaybackStateCompat.STATE_PLAYING ?
                R.drawable.pq_play : R.drawable.play;

        notifyPropertyChanged(BR.playbackResId);
    }

    public void playbackButton() {
        if (mMediaControllerCompat == null) return;

        // 因为这是一个播放/暂停按钮，所以需要测试当前状态，并相应地选择动作
        int pbState = mMediaControllerCompat.getPlaybackState().getState();
        Log.d(TAG, "initView: 点击了播放暂停按钮, 播放状态代码: " + pbState);
        if (pbState == PlaybackStateCompat.STATE_PLAYING) {
            mMediaControllerCompat.getTransportControls().pause();
            this.playbackDrawable = R.drawable.iv_main_play;
            notifyPropertyChanged(BR.playbackResId);
        } else if (pbState == PlaybackStateCompat.STATE_PAUSED) {
            mMediaControllerCompat.getTransportControls().play();
            this.playbackDrawable = R.drawable.iv_main_pause;
            notifyPropertyChanged(BR.playbackResId);
        } else {
            //Toast.makeText(this, "进入APP首次播放", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "playbackButton: 首次播放");
            String path = mMediaControllerCompat.getMetadata()
                    .getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI);
            if (path == null || TextUtils.isEmpty(path)) return;
            mMediaControllerCompat.getTransportControls().playFromUri(Uri.parse(path), null);
            this.playbackDrawable = R.drawable.iv_main_pause;
            notifyPropertyChanged(BR.playbackResId);
        }
    }

    public void clearAdapter() {
        if (adapter == null) {
            return;
        }
        adapter.get().release();
        adapter.clear();
        adapter = null;
    }

    public void setAdapter(MusicAdapter adapter) {
        clearAdapter();
        this.adapter = new SoftReference<>(adapter);
    }

    public EditInputChangeListener getTextListener() {
        return mTextListener.get();
    }

    private class EditInputChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            //Log.d(TAG, "输入了"+s);
            String newText = s.toString();
            //拦截歧义查询字段
            if (adapter == null || adapter.get() == null || newText.equals("'")) return;
            if (newText.contains("'s")) return;
            if (newText.length() > 0 && newText.substring(0, 1).contains("'")) return;
            if (newText.length() > 1 && newText.substring(newText.length() - 2).contains("'"))
                return;
            adapter.get().searchMediaItems(newText);
        }
    }

    public MyCheckedListener getCheckedListener() {
        return new SoftReference<>(new MyCheckedListener()).get();
    }

    private class MyCheckedListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mMediaControllerCompat == null) {
                return;
            }
            //!!!注意。一定通过Bundle把isChecked的值传过去
            MusicViewModel.this.setCustomStyle(isChecked);
            Bundle bundle = new Bundle();
            bundle.putBoolean(BaseMusicService.MyMusic_NOTIFICATION_STYLE, isChecked);
            mMediaControllerCompat.getTransportControls().sendCustomAction(
                    BaseMusicService.MyMusic_NOTIFICATION_STYLE, bundle
            );
        }
    }

    public BarTimerTask getCircleBarTask() {
        return getSoftReference(new BarTimerTask());
    }

    private class BarTimerTask extends TimerTask {

        @Override
        public void run() {

            if (mMediaControllerCompat == null ||
                    mMediaControllerCompat.getPlaybackState().getState() !=
                            PlaybackStateCompat.STATE_PLAYING)
                return;

            PlaybackStateCompat playbackState = mMediaControllerCompat.getPlaybackState();

            if (playbackState.getState() == PlaybackStateCompat.STATE_PLAYING) {//音乐正在播放
                int position = (int) playbackState.getPosition();
                /*long duration = mMediaControllerCompat.getMetadata()
                        .getLong(MediaMetadataCompat.METADATA_KEY_DURATION);*/
                /*Log.d(TAG, "run: 当前音乐播放进度 "+position+ "  音乐时长："+
                        mMediaControllerCompat.getMetadata()
                                .getLong(MediaMetadataCompat.METADATA_KEY_DURATION));*/
                //mMusicViewModel.setMax((int) duration);
                setProgress(position);
            }
        }
    }
    @Bindable
    public Spanned getTitle() {
        return title;
    }
    @Bindable
    public void setTitle(Spanned title) {
        this.title = title;
    }
    @Bindable
    public Spanned getSubtitle() {
        return subtitle;
    }
    @Bindable
    public void setSubtitle(Spanned subtitle) {
        this.subtitle = subtitle;
    }

    public void SyncMusicInformation() {
        if (mMediaControllerCompat == null) {
            Log.e(TAG, "SyncMusicInformation: controller为空");
            return;
        }
        MediaMetadataCompat lastMetadata = mMediaControllerCompat.getMetadata();
        if (lastMetadata == null) {
            return;
        }
        //歌名-歌手
        String title = lastMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE),
                artist = lastMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST);

        setPlaybackInfo(HtmlStringUtil.songName(title), HtmlStringUtil.songName(artist), HtmlStringUtil.SongSingerName(title, artist));

        //时长
        int position = mApplication.getSharedPreferences("UserLastMusicPlay", 0)
                .getInt("MusicPosition", 0);
        long duration = lastMetadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        Log.e(TAG, "onChildrenLoaded: 当前进度 " + position + " 总时长 " + duration);
        setMax((int) duration);
        setProgress(position);

        //显示处理成唱片样式的歌曲封面图片
        setRecord(getRecord(
                lastMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART), mApplication));
    }


}
