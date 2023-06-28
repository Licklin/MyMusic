package com.lickling.mymusic.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.lickling.mymusic.bean.musicBean.MusicBean;

import java.io.IOException;
import java.util.List;

public class MusicService extends BaseService {
    private final String TAG = MusicService.class.getCanonicalName();
    private final IBinder binder = new MyMusicBinder();
    //    音频媒体、音频焦点、音量管理、WIFI锁
    private MediaPlayer mediaPlayer;
    private AudioAttributes audioAttributes;
    private AudioFocusRequest audioFocusRequest;
    private AudioManager audioManager;
    private WifiManager.WifiLock wifiLock;
    private MyOnAudioFocusChangeListener myOnAudioFocusChangeListener;
    private OnErrorListener onErrorListener;
    private OnCompletionListener onCompletionListener;
    private OnPreparedListener onPreparedListener;
    private List<MusicBean> playList;


    //    歌曲信息
    private String currentTitle, currentArtist, currentAlbum, currentAlbumPath, currentPath, currentPlaySource;
    private long currentDuration;
    private boolean isFirstPlay = true;
    private int currentPosition = 0;
    private SharedPreferences setting;

    public MusicService() {
    }

    public class MyMusicBinder extends Binder {
        public MusicService getMusicService() {
            Log.e("getMusicService", "ok");
            return MusicService.this;
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        init();
        getLastMusicInfo();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMediaPlay();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);

    }


    @Override
    protected void init() {
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        onPreparedListener = new OnPreparedListener();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(onPreparedListener);

//        锁屏时不关闭CPU
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
//        wifi锁
        wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "Lock");
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

//        音频焦点
        myOnAudioFocusChangeListener = new MyOnAudioFocusChangeListener();
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(audioAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setWillPauseWhenDucked(true)
                    .setOnAudioFocusChangeListener(myOnAudioFocusChangeListener)
                    .build();
        }
        mediaPlayer.setAudioAttributes(audioAttributes);
    }

    private void releaseMediaPlay() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //1.释放音频焦点
            audioManager.abandonAudioFocusRequest(audioFocusRequest);
            //2.释放WIFI锁
            if (wifiLock.isHeld()) wifiLock.release();
            if (wifiLock != null) wifiLock = null;
            //3.清空应用
            if (myOnAudioFocusChangeListener != null) myOnAudioFocusChangeListener = null;
            if (audioFocusRequest != null) audioFocusRequest = null;
            if (audioAttributes != null) audioAttributes = null;
            if (audioManager != null) audioManager = null;
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }

    private void stopMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
            if (!isFirstPlay()) { // 不是第一次播放
                currentPosition = 0;
                mediaPlayer.seekTo(0);
                mediaPlayer.stop();
                mediaPlayer.reset();
                if (wifiLock.isHeld()) wifiLock.release(); // 解除WiFi锁
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    audioManager.abandonAudioFocusRequest(audioFocusRequest); // 释放焦点
                }
            }
        } else Log.i(TAG, "mediaPlay为空（null）");
    }

    private void setMediaPlayerResource(String path, boolean isNetPlay) {
        stopMediaPlayer();
        if (mediaPlayer != null) {
            try {
                if (!isNetPlay) {
                    mediaPlayer.setDataSource(this, Uri.parse(path));
                } else
                    mediaPlayer.setDataSource(path);
                play(isNetPlay);
            } catch (IOException e) {
//                throw new RuntimeException(e);
                onErrorListener.onError(mediaPlayer, 1, 2);
            }
        } else Log.i(TAG, "mediaPlay为空（null）");


    }

    private void play(boolean isNetPlay) {
        if (mediaPlayer != null) {
            if (currentPosition == 0) {
                try {
                    if (isNetPlay) {
                        mediaPlayer.prepareAsync();
                    } else
                        mediaPlayer.prepare();
                } catch (IOException e) {
//                    throw new RuntimeException(e);
                    onErrorListener.onError(mediaPlayer, 2, 2);
                }
            }
        } else {
            if (isFirstPlay()) {

            } else { // 暂停再播放
                mediaPlayer.seekTo(currentPosition);
                requestFocus();//播放
            }
        }
        if (isFirstPlay()) { // 是第一次播放
            onErrorListener = new OnErrorListener();
            mediaPlayer.setOnErrorListener(onErrorListener);
            mediaPlayer.setOnCompletionListener(new OnCompletionListener());
            mediaPlayer.setOnPreparedListener(new OnPreparedListener());
        }
    }

    public boolean isFirstPlay() {
        return isFirstPlay;
    }

    public boolean isPlaying() {
        if (mediaPlayer != null) return mediaPlayer.isPlaying();
        else
            return false;
    }

    public void onPause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            //      释放音乐焦点
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioManager.abandonAudioFocusRequest(audioFocusRequest);
                if (wifiLock.isHeld()) wifiLock.release(); // 释放wifi锁
            }
            currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }

    public void onContinuePlay() {
        requestFocus();

    }

    public void onPlay(String path, boolean isNetPlay) {

        saveLastMusicInfo();
        setMediaPlayerResource(path, isNetPlay);

    }

    public void onPlay(int position) {
        saveLastMusicInfo();
        playListMusic(position);

    }

    private void playListMusic(int position) {
        MusicBean music = playList.get(position);
//        setMediaPlayerResource(music.get, isNetPlay);

    }

    private void requestFocus() {
        if (currentPosition > 0) mediaPlayer.seekTo(currentPosition);
//            申请焦点
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int audioFocus = audioManager.requestAudioFocus(audioFocusRequest);
            if (audioFocus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {//            wifi锁
                wifiLock.acquire();
                mediaPlayer.start();
            } else if (audioFocus == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
                onErrorListener.onError(mediaPlayer, 0, 1);
            } else if (audioFocus == AudioManager.AUDIOFOCUS_REQUEST_DELAYED) {
//                onErrorListener.onError(mediaPlayer, 0, 1);
            }

        }

    }

    private class OnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            //onError.返回值返回false会触发onCompletionListener
            //所以返回fa1se,一般意味着会退出当前歌曲播放。
            //如果不想退出当前歌曲播放则应该返回true
            switch (i) {
                case 1:
                    if (i1 == 1)
//                        Toast.makeText(MusicService.this, "播放错误，歌曲地址为空，请播放其他歌曲", Toast.LENGTH_SHORT).show();
                        break;
                case 2:
                    if (i1 == 2)
//                        Toast.makeText(MusicService.this, "播放错误，歌曲地址为空，请播放其他歌曲", Toast.LENGTH_SHORT).show();
                        break;
            }

            return false;
        }
    }

    private class OnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

        }
    }

    private class OnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            if (isFirstPlay()) {
                isFirstPlay = false;
            }
            requestFocus();
        }
    }

    private class MyOnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {
        @Override
        public void onAudioFocusChange(int i) {
//      获得长时间播放焦点，短暂失去焦点后触发此回调
            switch (i) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.d(TAG, "onAudioFocusChange:获得长时间播放焦点");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.d(TAG, "onAudioFocusChange:失去播放焦点");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.d(TAG, "onAudioFocusChange:失去焦点但可以同时播发");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.d(TAG, "onAudioFocusChange:长时间失去焦点");

            }
        }
    }

    private void getLastMusicInfo() {
//        获取上次播放音乐的信息
        setting = getSharedPreferences("UserLastMusicInfo", 0);
        setting.getString("title", null);
        setting.getString("artist", null);
        setting.getString("album", null);
        setting.getString("alumPath", null);
        setting.getString("path", null);
        setting.getLong("duration", 0);

    }

    private void saveLastMusicInfo() {
        if (mediaPlayer.isPlaying()) {
            currentPosition = mediaPlayer.getCurrentPosition();

        }
//        记录上次播放音乐的信息
        setting = getSharedPreferences("UserLastMusicInfo", 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putString("title", currentTitle);
        editor.putString("artist", currentArtist);
        editor.putString("album", currentAlbum);
        editor.putString("alumPath", currentAlbumPath);
        editor.putString("path", currentPath);
        editor.putLong("duration", currentDuration);
//        editor.putString("title",currentTitle);
//        editor.putString("title",currentTitle);

        if (editor.commit()) {
            Log.d(TAG, "save playing music info ok");
        }
        editor.apply();
    }

    public List<MusicBean> getPlayList() {
        return playList;
    }

    public void setPlayList(List<MusicBean> playList) {
        this.playList = playList;
    }

}