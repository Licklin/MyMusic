package com.lickling.mymusic.service;

import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.service.media.MediaBrowserService;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.media.MediaBrowserServiceCompat;

import com.lickling.mymusic.service.manager.MediaNotificationManager;
import com.lickling.mymusic.service.manager.MediaPlayerManager;

public abstract class BaseMusicService extends MediaBrowserServiceCompat {

    private static final String TAG = "BaseMusicService";

    private MediaControllerCompat mediaController;
    private MediaNotificationManager mediaNotificationManager;
    private MyBlueToothBroadcastReceiver blueToothReceiver;
    private boolean isStartForeground;
    private static final int MEDIA_CHANNEL_ID = 130;
    //通知Action，componentName
    public static final String MyMusic_CUSTOM_ACTION_COLLECT_SONGS = "collect_songs_MyMusic";
    public static final String MyMusic_CUSTOM_ACTION_SHOW_LYRICS = "show_lyrics_MyMusic";
    public static final String MyMusic_CUSTOM_ACTION_PLAY = "play_MyMusic";
    public static final String MyMusic_CUSTOM_ACTION_PAUSE = "pause_MyMusic";
    public static final String MyMusic_CUSTOM_ACTION_PREVIOUS = "previous_MyMusic";
    public static final String MyMusic_CUSTOM_ACTION_NEXT = "next_MyMusic";
    public static final String MyMusic_CUSTOM_ACTION_STOP = "stop_MyMusic";
    public static final String MyMusic_NOTIFICATION_STYLE = "notification_style_MyMusic";

    @Override
    public void onCreate() {
        super.onCreate();
        initManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaController != null) { mediaController = null; }
        if (blueToothReceiver != null) {
            unregisterReceiver(blueToothReceiver);
            blueToothReceiver = null;
        }
        if (mediaNotificationManager != null) {
            mediaNotificationManager.onDestroy();
            mediaNotificationManager = null;
        }
    }

    protected void setMediaController(MediaControllerCompat mediaController) {
        this.mediaController = mediaController;
    }

    protected boolean isStartForeground() { return isStartForeground; }

    private void initManager(){
        //初始化通知管理者和媒体按钮接收器
        mediaNotificationManager = new MediaNotificationManager(getApplication());
        SharedPreferences settings = getSharedPreferences("UserLastMusicPlay", 0);
        boolean notificationStyle = settings.getBoolean("NotificationStyle", false);
        mediaNotificationManager.setCustomNotification(notificationStyle);
        //Log.e(TAG, "initManager: "+notificationStyle);
        initReceiver();
    }

    private void initReceiver() {
        blueToothReceiver = new MyBlueToothBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(MyMusic_CUSTOM_ACTION_SHOW_LYRICS);
        filter.addAction(MyMusic_CUSTOM_ACTION_COLLECT_SONGS);
        filter.addAction(MediaPlayerManager.MyMusic_CUSTOM_ACTION_PLAYBACK_MODE_CHANGE);
        filter.addAction(MyMusic_CUSTOM_ACTION_PLAY);
        filter.addAction(MyMusic_CUSTOM_ACTION_PAUSE);
        filter.addAction(MyMusic_CUSTOM_ACTION_PREVIOUS);
        filter.addAction(MyMusic_CUSTOM_ACTION_NEXT);
        filter.addAction(MyMusic_CUSTOM_ACTION_STOP);
        registerReceiver(blueToothReceiver, filter);
    }

    // 启动通知栏控制器
    protected void StartForeground(MediaSessionCompat.Token token,
                                   @NonNull PlaybackStateCompat state){

        if (mediaController == null) {
            Log.e(TAG, "onReceive: mediaController == null");return;}

        MediaMetadataCompat metadata = mediaController.getMetadata();

        Notification notification =
                mediaNotificationManager.getNotification(metadata, state, token);

        if (isStartForeground) {
            Log.e(TAG, "startForeground: 已创建通知！更新通知");
            mediaNotificationManager.getNotificationManager()
                    .notify(MEDIA_CHANNEL_ID, notification);
        }else {
            notification.flags = Notification.FLAG_ONGOING_EVENT;//设置常驻通知
            this.startForeground(MEDIA_CHANNEL_ID,notification);
            isStartForeground = true;
        }
    }
    protected void StopForeground(){
        boolean isStopped =
                mediaController.getPlaybackState().getState() == PlaybackStateCompat.STATE_STOPPED;
        this.stopForeground(mediaNotificationManager.isCustomNotification() && isStopped);
        isStartForeground = false;
    }
    protected void setNotificationStyle(MediaSessionCompat.Token token,
                                        @NonNull PlaybackStateCompat playbackState,
                                        boolean nextNotificationStyle){
        if (mediaNotificationManager == null || mediaController == null) { return ; }
        mediaNotificationManager.setCustomNotification(nextNotificationStyle);

        //Log.d(TAG, "setNotificationStyle: "+nextNotificationStyle);
        int state = playbackState.getState();
        boolean isUpdate = state != PlaybackStateCompat.STATE_STOPPED &&
                state != PlaybackStateCompat.STATE_NONE;
        if (isUpdate) { StartForeground(token,playbackState); }
    }

    protected class MyBlueToothBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mediaController == null) {
                Log.e(TAG, "onReceive: mediaController == null");return;}
            MediaControllerCompat.TransportControls transportControls = mediaController.getTransportControls();
            String action = intent.getAction();
            Log.d(TAG, "onReceive: "+action);
            int bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, 0);
            Log.d(TAG, "onReceive: "+bluetoothState);
            switch (bluetoothState) {
                case BluetoothAdapter.STATE_DISCONNECTED:
                case BluetoothAdapter.STATE_TURNING_OFF:
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    //Log.e(TAG, "onReceive: 蓝牙已打开");
                    break;
                case BluetoothAdapter.STATE_CONNECTED:
                    //Log.e(TAG, "onReceive: 蓝牙已连接");
                    break;
            }
            if (MediaPlayerManager.MyMusic_CUSTOM_ACTION_PLAYBACK_MODE_CHANGE.equals(action)) {
                transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);
            }else if (MyMusic_CUSTOM_ACTION_PLAY.equals(action)){
                transportControls.play();
            }else if (MyMusic_CUSTOM_ACTION_PAUSE.equals(action)){
                transportControls.pause();
            }else if (MyMusic_CUSTOM_ACTION_PREVIOUS.equals(action)){
                transportControls.skipToPrevious();
            }else if (MyMusic_CUSTOM_ACTION_NEXT.equals(action)){
                transportControls.skipToNext();
            }else if (MyMusic_CUSTOM_ACTION_STOP.equals(action)){
                transportControls.stop();
            }
        }
    }
}
