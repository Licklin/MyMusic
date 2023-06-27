package com.lickling.mymusic.service.manager;

import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;

import com.lickling.mymusic.R;

import java.io.IOException;
import java.util.Locale;

public class MediaPlayerManager {
    private static final String TAG = "MediaPlayManager";
    //
    private Application application;
    private MediaSessionCompat mediaSession;
    private MyAudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private boolean isFirstPlay = true;
    private int currentPosition = -1, currentAudioLevel = 0;
    //播放模式Flag
    private int MyMusic_PLAYBACK_MODE = 0;
    protected static final int MyMusic_PLAYBACK_MODE_ORDER = 1;
    protected static final int MyMusic_PLAYBACK_MODE_REPEAT = 3;
    protected static final int MyMusic_PLAYBACK_MODE_RANDOM = 2;
    public static final String MyMusic_CUSTOM_ACTION_PLAYBACK_MODE_CHANGE = "playback_mode_change_Likl";
    //MusicService 回调更新通知栏
    private NotificationListener mNotificationListener;
    public interface NotificationListener{
        void onUpdateNotification();
    }

    public MediaPlayerManager(Application application,
                              MediaSessionCompat mediaSession,
                              NotificationListener notificationListener,
                              AudioManager.OnAudioFocusChangeListener focusChangeListener){
        this.application = application;
        this.mediaSession = mediaSession;
        mNotificationListener = notificationListener;
        //初始化MediaPlayer
        mediaPlayer = new MediaPlayer();
        //唤醒锁定模式，关闭屏幕时，CPU不休眠
        mediaPlayer.setWakeMode(application, PowerManager.PARTIAL_WAKE_LOCK);
        //初始化MyAudioManager
        audioManager = new MyAudioManager(application,focusChangeListener,
                mediaPlayer.getAudioSessionId());

        mediaPlayer.setAudioAttributes(audioManager.getPlaybackAttributes());
        mediaPlayer.setOnErrorListener(new onErrorListener());
        mediaPlayer.setOnPreparedListener(new onPreparedListener());
        mediaPlayer.setOnCompletionListener(new onCompleteListener());
    }

    public void onDestroy(){
        if (application != null) { application = null; }
        if (mediaSession != null) { mediaSession = null; }
        if (mNotificationListener != null) { mNotificationListener = null; }
        if (audioManager != null) {
            audioManager.onDestroy();
            audioManager = null;
        }
        releaseMediaPlayer();
    }

    public void releaseMediaPlayer(){
        if (mediaPlayer != null) {
            StopMediaPlayer();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    //MediaPlayer 相关方法
    public void StopMediaPlayer(){
        //Log.d(TAG, "StopMediaPlayer: ");
        if (isFirstPlay() && currentPosition <= 0) return;

        if (mediaPlayer != null) {
            if (isPlaying())  {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                mediaPlayer.stop();
            }
            //释放wifi锁 , 释放音频焦点
            if (audioManager != null) { audioManager.releaseAudioFocus(); }
            //重置MediaPlayer
            mediaPlayer.reset();
            //适用于上次播放有进度，但是第一次播放了这首歌曲，所以播放进度保留
            if (!isFirstPlay()) resetCurrentPosition();

        }else System.out.println("MediaPlayer is null!");
    }
    public void setDataRes(String path){
        StopMediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void PlayFromUri(){
        if (mediaPlayer == null || isPlaying() ) return;
        if (!mediaSession.isActive()) { mediaSession.setActive(true); }
        if (audioManager != null) { setVolume(currentAudioLevel); }

        if (currentPosition <= 0 || isFirstPlay) {
            mediaSession.setPlaybackState(
                    newPlaybackState(PlaybackStateCompat.STATE_BUFFERING,null));
            mediaPlayer.prepareAsync();
        }else { //暂停后继续播放
            mediaPlayer.seekTo(currentPosition);

            Bundle bundle = new Bundle();
            bundle.putBoolean("Continue_Playing_Tips",true);
            checkFocusPlay(bundle);

            Log.d(TAG, "PlayMediaPlayer: 暂停后播放");
        }

    }
    public void OnPause(boolean notReleaseAudio){
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            //记录播放队列位置
            currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
        //停止播放音乐释放焦点
        if (!notReleaseAudio) { audioManager.releaseAudioFocus(); }

        mediaSession.setPlaybackState(newPlaybackState(PlaybackStateCompat.STATE_PAUSED,null));
        mNotificationListener.onUpdateNotification();
    }

    public void seekTo(long pos) {
        if (mediaPlayer == null && mediaSession == null) return;

        MediaControllerCompat mMediaController = mediaSession.getController();
        if (mMediaController.getPlaybackState().getState()
                == PlaybackStateCompat.STATE_PLAYING) mediaPlayer.pause();
        currentPosition = pos == 0 ? 1 : (int) pos;
        if (isFirstPlay()) {
            String path = mMediaController.getMetadata()
                    .getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI);
            setDataRes(path);
        }
        PlayFromUri();
    }
    //***************************音量更改与获取****************************/
    public boolean checkAudioChange(){ return currentAudioLevel != audioManager.getVolume();}
    public void lowerTheVolume(){ audioManager.lowerTheVolume(); }
    public void setVolume(int volume) {
        if (audioManager.setVolume(volume)){
            //设置播放流音量，防止静音时播放后，再调整音量无效的问题
            float percent = Float.parseFloat(String.format(Locale.CHINA, "%.2f",
                    (float)volume / (float)audioManager.getMaxVolume()));
            Log.d(TAG, "setVolume: "+percent);
            if (mediaPlayer != null) mediaPlayer.setVolume(percent,percent);

            currentAudioLevel = volume;
        }
    }
    public int getVolume(){
        //Log.d(TAG, "getVolume: "+audioManager.getVolume());
        return audioManager.getVolume();}
    public int getMaxVolume(){
        //Log.d(TAG, "getMaxVolume: "+audioManager.getMaxVolume());
        return audioManager.getMaxVolume();}
    //***************************获取与更改播放进度****************************/
    public void resetCurrentPosition() { currentPosition = 0; }
    public int getCurrentPosition() {
        return isPlaying() ? mediaPlayer.getCurrentPosition() : currentPosition;
    }
    public void initCurrentPosition(int position){ currentPosition = position;}
    //***************************一些播放状态****************************/
    public boolean isPlaying() { return mediaPlayer != null && mediaPlayer.isPlaying(); }
    public boolean isFirstPlay() { return isFirstPlay; }

    public void setLooping(){
        mediaPlayer.setLooping(getPlaybackMode() == getPlaybackModeRepeat());
    }
    //***************************MediaPlayer回调区****************************/
    private class onCompleteListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.d(TAG, "onCompletion: ");
            //播放下一曲
            mediaSession.getController().getTransportControls().skipToNext();
        }
    }

    private class onErrorListener implements  MediaPlayer.OnErrorListener{
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            //当播放错误时，MediaPlayer执行此回调并停止播放，故isPlayerPrepared 状态应为true，已准备好播放
            /*if (!isPlayerPrepared) isPlayerPrepared = true;
            isError_Flag = true;//必须要设置不同Path才能继续播放*/
            if (what != -38 && extra != 0) application.sendBroadcast(new Intent("error"));
            Log.d(TAG, "onErrorListener: what:"+what+" , extra = "+extra);
            switch (what) {
                case 1:
                    if (extra == 1)
                        Log.e(TAG,"播放错误，歌曲地址为空,请播放其他歌曲");
                    else if (extra == 2)
                        Log.e(TAG,"该音乐文件已损坏,请播放其他歌曲");
                    else if (extra == 28)
                        Log.e(TAG,"该音乐媒体对象为空，请重新打开App");
                    else if (extra == -2147483648)
                        Log.e(TAG,"音乐文件解码失败,请删除该文件,尝试播放网络版本");
                    else Log.e(TAG,"播放错误,请播放其他歌曲");
                    break;
                case 2:
                    if (extra == 2) Log.d(TAG, "onError: 音乐文件解码失败,请播放其他歌曲");
                    else Log.d(TAG, "onError: 播放错误,请播放其他歌曲");
                    Log.e(TAG, "Error Prepare 只能使用log或者通知");
                    break;
            }
            //onError返回值返回false会触发onCompletionListener，y
            //所以返回false，一般意味着会退出当前歌曲播放。
            //如果不想退出当前歌曲播放则应该返回true
            return true;
        }
    }

    private class onPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.e(TAG, "onPrepared: "+currentPosition);
            if (currentPosition > 0) mediaPlayer.seekTo(currentPosition);
            else if (currentPosition == -1) resetCurrentPosition();
            //获得音频焦点
            checkFocusPlay(null);
            if (isFirstPlay) { isFirstPlay = false; }
        }
    }
    private void checkFocusPlay(Bundle bundle){
        int audioFocusState = audioManager.registerAudioFocus();

        if(audioFocusState == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            mediaPlayer.start();
            //更新状态, 使该通知处于栈顶, 赋予播放进度初始值, 从而在通知中展示播放进度
            mediaSession.setPlaybackState(newPlaybackState(PlaybackStateCompat.STATE_PLAYING,bundle));
            mNotificationListener.onUpdateNotification();
        }else if (audioFocusState == AudioManager.AUDIOFOCUS_REQUEST_FAILED){//请求焦点失败
            Toast.makeText(application,"请求播放失败",Toast.LENGTH_SHORT).show();
        }else Log.e(TAG, "checkFocusPlay: 音频焦点延迟获得！");
    }
    //***************************获取新播放状态PlaybackStateCompat****************************/
    public PlaybackStateCompat newPlaybackState(@PlaybackStateCompat.State int newState, Bundle bundle){
        return new PlaybackStateCompat.Builder()
                .setExtras(bundle)
                //设置需使用的Action
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SEEK_TO |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                //关闭Notification
                                PlaybackStateCompat.ACTION_STOP |
                                //歌词action，翻译为字幕
                                PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED |
                                //歌曲收藏action，翻译为星级评级
                                PlaybackStateCompat.ACTION_SET_RATING |
                                //播放模式切换action，翻译为设置重复播放
                                PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE)
                .addCustomAction(MyMusic_CUSTOM_ACTION_PLAYBACK_MODE_CHANGE,
                        String.valueOf(getPlaybackMode()), R.drawable.iv_playback_mode_order)
                .setState(newState,currentPosition,1.0f).build();
    }
    //***************************播放模式更改与获取****************************/
    public void setPlayBackMode(int mode){
        MyMusic_PLAYBACK_MODE = (mode < 1 || mode > 3) ? 1 : mode;
        Log.e(TAG, "setPlayBackMode: "+mode);
    }

    public int getPlaybackMode(){
        Log.e(TAG, "getPlaybackMode: "+MyMusic_PLAYBACK_MODE);return MyMusic_PLAYBACK_MODE; }

    public static int getPlaybackModeOrder() { return MyMusic_PLAYBACK_MODE_ORDER; }

    public static int getPlaybackModeRandom() { return MyMusic_PLAYBACK_MODE_RANDOM; }

    public static int getPlaybackModeRepeat() { return MyMusic_PLAYBACK_MODE_REPEAT; }

    public int playbackModeChange(){
        if (MyMusic_PLAYBACK_MODE != MyMusic_PLAYBACK_MODE_REPEAT) { MyMusic_PLAYBACK_MODE++; }
        else MyMusic_PLAYBACK_MODE = MyMusic_PLAYBACK_MODE_ORDER;

        SharedPreferences settings = application.getSharedPreferences("UserLastMusicPlay", 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("MusicPlaybackMode",MyMusic_PLAYBACK_MODE);
        editor.apply();

        return MyMusic_PLAYBACK_MODE;
    }

}

