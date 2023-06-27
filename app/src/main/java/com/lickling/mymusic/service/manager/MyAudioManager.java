package com.lickling.mymusic.service.manager;


import android.app.Application;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.audiofx.LoudnessEnhancer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;

import java.util.Optional;
import java.util.function.Function;

/**
 * 作用: 系统声音服务 所需 代码和方法  管理帮助类
 * 1.音频焦点管理、 顺便管理下Wifi锁
 * 2.调节系统音量
 * 3.播放声音增强
 */
public class MyAudioManager {

    private static final String TAG = "MyAudioManager";
    //音量Key
    public static final String MyMusic_CUSTOM_ACTION_MAX_VOLUME = "max_volume_Likl";
    public static final String MyMusic_CUSTOM_ACTION_CURRENT_VOLUME = "current_volume_Likl";

    private Application application;

    private AudioManager audioManager;
    private WindowManager windowManager;
    private WifiManager.WifiLock wifiLock;
    //音频焦点管理
    private AudioManager.OnAudioFocusChangeListener focusChangeListener;
    private AudioAttributes playbackAttributes;
    private AudioFocusRequest focusRequest;
    //人声增强
    private LoudnessEnhancer loudnessEnhancer;
    private long currentVoiceMb;

    public MyAudioManager(Application application,
                          AudioManager.OnAudioFocusChangeListener focusChangeListener,
                          int audioSessionId) {
        application = application;

        //初始化管理者
        audioManager = (AudioManager) application.getSystemService(Context.AUDIO_SERVICE);
        windowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
        wifiLock = ((WifiManager) application.getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "LiklLock");

        //音频焦点管理初始化
        focusChangeListener = focusChangeListener;
        playbackAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        //获取长时间音频播放焦点
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(playbackAttributes)
                    // 可让您的应用异步处理焦点请求。设置此标记后，
                    // 在焦点锁定时发出的请求会返回 AUDIOFOCUS_REQUEST_DELAYED。
                    // 当锁定音频焦点的情况不再存在时（例如当通话结束时），
                    // 系统会批准待处理的焦点请求，并调用 onAudioFocusChange() 来通知您的应用。
                    .setAcceptsDelayedFocusGain(true)
                    //播放通知铃声时自动降低音量，true则回调音频焦点更改回调，可在回调里暂停音乐
                    .setWillPauseWhenDucked(false)
                    .setOnAudioFocusChangeListener(focusChangeListener)
                    .build();
        }

        //人声增强器初始化
        loudnessEnhancer = new LoudnessEnhancer(audioSessionId);
        loudnessEnhancer.setTargetGain(1000);//调节此值 可按值增强声音 | 人声增强 loudnessEnhancer
        loudnessEnhancer.setEnabled(true);
    }

    public void onDestroy() {
        releaseAudioFocus();
        if (application != null) application = null;

        if (audioManager != null) audioManager = null;
        if (windowManager != null) windowManager = null;
        if (wifiLock != null) wifiLock = null;

        if (loudnessEnhancer != null) {
            loudnessEnhancer.release();
            loudnessEnhancer = null;
        }

        if (focusChangeListener != null) focusChangeListener = null;
        if (playbackAttributes != null) playbackAttributes = null;
        if (focusRequest != null) focusRequest = null;
    }

    public AudioAttributes getPlaybackAttributes() {
        return playbackAttributes;
    }

    public void lowerTheVolume() {
        int volume = getVolume();
        setVolume(volume > 4 ? volume - 2 : 2);
    }

    public int getMaxVolume() {
        if (audioManager != null) {
            return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        return 0;
    }

    public boolean setVolume(int volume) {
        boolean canSetVolume = audioManager != null && !audioManager.isVolumeFixed();

        if (canSetVolume) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
            //Log.d(TAG, "setVolume: "+percent);

        } else Log.e(TAG, "setVolume: 音量设置无效 参数 " + volume);

        return canSetVolume;
    }

    public int getVolume() {
        if (audioManager != null) {
            Object object = getVolume(audioManager);
            if (object instanceof Integer) {
                return Integer.parseInt(object.toString());
            }
        }
        return 0;
    }

    private Object getVolume(AudioManager manager) {
        return Optional.of(manager).map((Function<AudioManager, Object>)
                manager1 -> manager1.getStreamVolume(AudioManager.STREAM_MUSIC)).orElse("0");
    }

    //获取与设置用户信息-人声增强幅度
    public long getCurrentVoiceMb() {
        return currentVoiceMb;
    }

    public void setCurrentVoiceMb(long currentVoiceMb, boolean isSave) {
        if (currentVoiceMb == currentVoiceMb) return;

        if (loudnessEnhancer != null) {
            loudnessEnhancer.setEnabled(currentVoiceMb > 0);
            loudnessEnhancer.setTargetGain((int) currentVoiceMb);
        }

        if (!isSave) return;

        this.currentVoiceMb = currentVoiceMb;
        if (currentVoiceMb < 0) currentVoiceMb = 0;
        if (currentVoiceMb > 2600) currentVoiceMb = 2600;

        /*settings = getSharedPreferences("UserLastMusicPlay",0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("UserVoiceMb",currentVoiceMb);
        editor.apply();*/
    }

    public int registerAudioFocus() {
        //启动wifi锁,在暂停或者停止时释放WiFi锁
        wifiLock.acquire();
        //获得播放焦点
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return audioManager.requestAudioFocus(focusRequest);
        } else
            return 0;
    }

    public void releaseAudioFocus() {
        //停止播放音乐后释放焦点
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(focusRequest);
        }
        //释放wifi锁
        if (wifiLock.isHeld()) wifiLock.release();
    }
}
