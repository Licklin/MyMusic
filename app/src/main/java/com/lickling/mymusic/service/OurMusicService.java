package com.lickling.mymusic.service;

import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.lickling.mymusic.service.manager.LastMetaManager;
import com.lickling.mymusic.service.manager.MediaPlayerManager;
import com.lickling.mymusic.service.manager.MyAudioManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class OurMusicService extends BaseMusicService {


    private static final String TAG = "MusicService";
    private static final String MY_MEDIA_ROOT_ID = "media_root_id";
    private static final String MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id";

    private MediaSessionCompat mediaSession;
    private int queueIndex = -1;
    private MediaPlayerManager mediaPlayerManager;
    private boolean IS_AUDIO_FOCUS_LOSS_TRANSIENT;
    private LastMetaManager lastMetaManager;
    //    private BaseModel model;
    private Timer timer;
    private final List<MediaSessionCompat.QueueItem> playList = new ArrayList<>();

    private LinkedHashMap<String, MediaMetadataCompat> musicList;
    private int currentPosition;

    @Override
    public void onCreate() {
        super.onCreate();
        intiMediaSession();
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
    }
//************************************本Service处理与客户端的链接**********************************/
    //参考：https://developer.android.google.cn/guide/topics/media-apps/audio-app/building-a-mediabrowserservice

    /**
     * {@link MediaBrowserServiceCompat#onGetRoot(String, int, Bundle)} 控制对服务的访问
     * 1.onGetRoot() 方法返回内容层次结构的根节点。如果该方法返回 null，则会拒绝连接。
     * 2.要允许客户端连接到您的服务并浏览其媒体内容，onGetRoot() 必须返回非 null 的 BrowserRoot，
     * 这是代表您的内容层次结构的根 ID。
     * 3.要允许客户端连接到您的 MediaSession 而不进行浏览，
     * onGetRoot() 仍然必须返回非 null 的 BrowserRoot，但此根 ID 应代表一个空的内容层次结构。
     * <p>
     * 注意：onGetRoot() 方法应该快速返回一个非 null 值。用户身份验证和其他运行缓慢的进程不应在 onGetRoot() 中运行。
     * 大多数业务逻辑应该在 onLoadChildren() 方法中处理。
     */
    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {

        //（可选）控制指定包名称的访问级别，要做到这一点，您需要编写自己的逻辑。
        if (allowBrowsing(clientPackageName, clientUid)) { //允许浏览
            // Returns a root ID that clients can use with onLoadChildren() to retrieve
            // the content hierarchy.
            return new BrowserRoot(MY_MEDIA_ROOT_ID, null);
        } else {
            // Clients can connect, but this BrowserRoot is an empty hierachy
            // so onLoadChildren returns nothing. This disables the ability to browse for content.
            return new BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null);
        }
    }

    private boolean allowBrowsing(String clientPackageName, int clientUid) {
        return true;
    }

    /**
     * {@link MediaBrowserServiceCompat#onLoadChildren(String, Result)}
     * 使客户端能够构建和显示 内容层次结构菜单,并通过 onLoadChildren() 传达内容
     * 1.客户端连接后，可以通过重复调用 MediaBrowserCompat.subscribe() 来遍历内容层次结构，以构建界面的本地表示方式。
     * subscribe() 方法将回调 onLoadChildren() 发送给服务，该服务会返回 MediaBrowser.MediaItem 对象的列表。
     * 2.每个 MediaItem 都有一个唯一的 ID 字符串，这是一个不透明令牌。当客户端想要打开子菜单或播放某项内容时，
     * 它就会传递此 ID。您的服务负责将此 ID 与相应的菜单节点或内容项关联起来。
     * <p>
     * 注意：MediaBrowserService 传送的 MediaItem 对象不应包含图标位图。
     * 当您为每项内容构建 MediaDescription 时，请通过调用 setIconUri() 来使用 Uri。
     */
    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
//将信息从当前线程中移除，允许后续调用sendResult方法
        result.detach();
        //浏览不被允许
        if (TextUtils.equals(MY_EMPTY_MEDIA_ROOT_ID, parentId)) {
            result.sendResult(null);
            return;
        }
        Log.d(TAG, "onLoadChildren: " + parentId);

//        new MusicModel().getLocalMusicMetadata(musicMaps -> {
//            musicList = musicMaps;
//            result.sendResult(getMediaItems(musicMaps));
//        }, getContentResolver());
        super.setMediaController(mediaSession.getController());
    }

    private void intiMediaSession() {
        //初始化MediaSession | 媒体会话
        mediaSession = new MediaSessionCompat(this, getPackageName());
        mediaSession.setActive(true);
        mediaSession.setQueue(playList);

        // !!!启用来自MediaButtons和TransportControl的回调
        // 1.允许媒体按钮回调：其他蓝牙设备或者安卓智能设备 通过 媒体响应按钮 发送 播放控制消息 给 Service服务
        // 2.允许媒体队列管理：onAddQueueMediaItem()允许队列管理，为执行上、下一曲相关方法
        // 3.允许媒体命令传输：View客户端 播放控制消息 发给 Service服务 执行相关方法
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //给MediaSession设置来自MediaController的回调内部类
        mediaSession.setCallback(new MyMediaSessionCallback());

        // 设置会话的令牌，以便客户端活动可以与其通信。
        setSessionToken(mediaSession.getSessionToken());

//        initMediaPlayerManager();

        //给MediaSession设置初始状态
        mediaSession.setPlaybackState(
                mediaPlayerManager.newPlaybackState(PlaybackStateCompat.STATE_NONE, null));

    }

    private void initMediaPlayerManager() {
        if (mediaPlayerManager != null) { return; }

        mediaPlayerManager = new MediaPlayerManager(getApplication(),
                mediaSession,
                new MyNotificationListener(),
                new MyAudioFocusChangeListener());
        lastMetaManager = new LastMetaManager(getApplication());
        //初始化播放模式
        mediaPlayerManager.setPlayBackMode(lastMetaManager
                .getLastPlaybackMode(MediaPlayerManager.getPlaybackModeOrder()));
        //获得上次播放进度int，默认为0
        mediaPlayerManager.initCurrentPosition(lastMetaManager.getLastMusicPosition());
    }

    private void releaseMediaPlayerManager(){
        if (mediaSession != null) {
            mediaSession.setPlaybackState(mediaPlayerManager.newPlaybackState(
                    PlaybackStateCompat.STATE_STOPPED,null));
            mediaSession.setActive(false);
        }

        if (mediaPlayerManager != null) {
            mediaPlayerManager.onDestroy();
            mediaPlayerManager = null;
        }
        if (lastMetaManager != null) { lastMetaManager.onDestroy(); lastMetaManager = null;}

    }

    private void releaseMedia() {
        playList.clear();
        if (musicList != null) {
            if (musicList.size() > 0) { musicList.clear(); }
            musicList = null;
        }
        releaseMediaPlayerManager();
        if (mediaSession != null) {
            mediaSession.release();
            mediaSession = null;
        }
    }
    //**********************************************Metadata元数据相关方法***************************/
    private List<MediaBrowserCompat.MediaItem> getMediaItems(LinkedHashMap<String, MediaMetadataCompat> musicMaps) {
        List<MediaBrowserCompat.MediaItem> result = new ArrayList<>();
        for (MediaMetadataCompat metadata : musicMaps.values()) {
            result.add(
                    new MediaBrowserCompat.MediaItem(
                            metadata.getDescription(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
            /*Log.d(TAG, "getMediaItems: "+metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE)+
                    " 键值 "+metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID));*/
        }
        Log.d(TAG, "getMediaItems: "+result.size());
        return result;
    }

    private class MyMediaSessionCallback extends MediaSessionCompat.Callback {
        // TODO: 所有的MediaSession播放动作回调
        @Override
        public void onAddQueueItem(MediaDescriptionCompat description) {
            super.onAddQueueItem(description);
            //Log.d(TAG, "onAddQueueItem: ");
            playList.add(new MediaSessionCompat.QueueItem(description, description.hashCode()));

            queueIndex = (queueIndex == -1) ? 0 : queueIndex;
            mediaSession.setQueue(playList);
        }

        @Override
        public void onRemoveQueueItem(MediaDescriptionCompat description) {
            super.onRemoveQueueItem(description);
            Log.d(TAG, "onRemoveQueueItem: ");
            playList.remove(new MediaSessionCompat.QueueItem(description, description.hashCode()));
            queueIndex = (playList.isEmpty()) ? -1 : queueIndex;
            mediaSession.setQueue(playList);
        }

        @Override
        public void onSetShuffleMode(int shuffleMode) {//设置随机播放模式
            //super.onSetShuffleMode(shuffleMode);
            Bundle bundle = new Bundle();
            bundle.putInt(MediaPlayerManager.MyMusic_CUSTOM_ACTION_PLAYBACK_MODE_CHANGE,
                    mediaPlayerManager.playbackModeChange());
            mediaSession.sendSessionEvent(
                    MediaPlayerManager.MyMusic_CUSTOM_ACTION_PLAYBACK_MODE_CHANGE, bundle);
            StartForeground(mediaSession.getSessionToken(),
                    mediaPlayerManager.newPlaybackState(
                            mediaSession.getController().getPlaybackState().getState(), null));
        }

        @Override
        public void onSetRating(RatingCompat rating) {//设置收藏
            super.onSetRating(rating);
        }

        @Override
        public void onPlay() {
            if (mediaPlayerManager == null && !mediaSession.isActive()) {
                initMediaPlayerManager();
                //播放上次记录的播放进度.第一次播放且拖动音乐进度条
                mediaPlayerManager.seekTo(lastMetaManager.getLastMusicPosition());
            } else mediaPlayerManager.PlayFromUri();
        }


        @Override
        public void onPause() {
            mediaPlayerManager.OnPause(IS_AUDIO_FOCUS_LOSS_TRANSIENT);
            IS_AUDIO_FOCUS_LOSS_TRANSIENT = false;
            stopNotification();
        }

        private void stopNotification() {

        }

        @Override
        public void onStop() {
            releaseMediaPlayerManager();
            stopNotification();
        }

        private void releaseMediaPlayerManager() {
        }

        @Override
        public void onPrepare() {
            if (playList.size() == 0) {
                Log.e(TAG, "onPrepare: 确定初始队列位置失败！");
                return;
            }

            MediaMetadataCompat metadata = mediaSession.getController().getMetadata();
            String mediaId = metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
            int i = 0;
            for (MediaSessionCompat.QueueItem item : playList) {
                //Log.d(TAG, "onPrepare: "+mediaId+", "+item.getDescription().getMediaId());
                if (mediaId.equals(item.getDescription().getMediaId())) {
                    queueIndex = i;
                }
                i++;
            }
            Log.d(TAG, "onPrepare: 确定初始队列位置 " + queueIndex);
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            MediaPlayerNextPlay(true);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            MediaPlayerNextPlay(false);
        }

        @Override
        public void onSeekTo(long pos) {
            if (mediaPlayerManager == null && !mediaSession.isActive()) {
                initMediaPlayerManager();
            }
            mediaPlayerManager.seekTo(pos);
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            Log.w(TAG, "onPlayFromMediaId: " + mediaId);
            if (mediaPlayerManager == null && !mediaSession.isActive()) {
                initMediaPlayerManager();
            }
            mediaPlayerManager.resetCurrentPosition();
            mediaPlayerManager.setDataRes(
                    Objects.requireNonNull(musicList.get(mediaId))
                            .getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI));
            mediaPlayerManager.PlayFromUri();
            mediaSession.setMetadata(getMetadata(mediaId));
            SaveLastMusicPlay();
            //！！！确认列表位置，因为此回调会包含搜索列表点击
            onPrepare();
        }

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            //super.onPlayFromUri(uri, extras);
            if (uri == null) {
                return;
            }
            if (mediaPlayerManager == null && !mediaSession.isActive()) {
                initMediaPlayerManager();
            }
            mediaPlayerManager.setDataRes(uri.getPath());
            mediaPlayerManager.PlayFromUri();
        }

        @Override
        public void onCustomAction(String action, Bundle extras) {
            super.onCustomAction(action, extras);
            Bundle bundle = new Bundle();
            if (action.equals(MyAudioManager.MyMusic_CUSTOM_ACTION_CURRENT_VOLUME)) {
                if (extras == null) {
                    Log.e(TAG, "onCustomAction: 音量新消息为空！");
                    return;
                }
                int volume = extras.getInt(MyAudioManager.MyMusic_CUSTOM_ACTION_CURRENT_VOLUME);
                setVolume(volume);
                bundle.putInt(action, volume);
            } else if (action.equals(BaseMusicService.MyMusic_NOTIFICATION_STYLE)) {
                //样式更改
                boolean style = extras.getBoolean(BaseMusicService.MyMusic_NOTIFICATION_STYLE);
                OurMusicService.super.setNotificationStyle(mediaSession.getSessionToken(),
                        mediaSession.getController().getPlaybackState(), style);
                //保存当前样式状态
                if (lastMetaManager != null) {
                    lastMetaManager.saveNotificationStyle(style);
                }
            }
            mediaSession.sendSessionEvent(action, bundle);
        }

        private void MediaPlayerNextPlay(boolean isSkipNext) {
            if (mediaPlayerManager == null) {
                initMediaPlayerManager();
            }
            int mode = mediaPlayerManager.getPlaybackMode();
            Log.d(TAG, "MediaPlayerNextPlay: 当前播放队列" + queueIndex + ", 播放模式 " + mode);
            mediaPlayerManager.setLooping();
            int musicCount = playList.size();
            //确定上、下一曲的位置
            if (musicCount < 2) {
                //如果音乐列表里只有一首歌曲，则getMusicListSize()为 1，
                // 那么mMusicQueueIndex为getMusicListSize()-1
                if (musicCount == 0) {
                    Toast.makeText(OurMusicService.this, "没有发现歌曲哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                queueIndex = 0;
            } else {
                if (mode == MediaPlayerManager.getPlaybackModeOrder()) {
                    nextQueueOrder(isSkipNext, musicCount);
                } else if (mode == MediaPlayerManager.getPlaybackModeRandom()) {
                    if (musicCount <= 3) {
                        nextQueueOrder(isSkipNext, musicCount);
                    } else {
                        int index = new Random().nextInt(musicCount - 1);
                        while (index == queueIndex) {
                            //当音乐列表里有至少5首时才能随机到不同的歌曲
                            index = new Random().nextInt(musicCount - 1);
                        }
                        queueIndex = index;
                    }
                } else {
                    Log.d(TAG, "NextMediaPlayer: 重复播放");
                }
            }
            //获得歌曲信息，并播放歌曲
            final String mediaId = playList.get(queueIndex).getDescription().getMediaId();
            onPlayFromMediaId(mediaId, null);
            Log.d(TAG, "MediaPlayerNextPlay: " + mediaId + ", " + queueIndex);
        }

        private void nextQueueOrder(boolean isSkipNext, int musicCount) {
            if (isSkipNext) {//前缀运算符先于取余运算符执行【顺序播放】下一曲
                queueIndex = (queueIndex + 1) % musicCount;
            } else {//【顺序播放】上一曲
                queueIndex = queueIndex > 0 ? queueIndex - 1 : musicCount - 1;
            }
        }
    }


    private class MyNotificationListener implements MediaPlayerManager.NotificationListener {

        @Override
        public void onUpdateNotification() {
            Log.w(TAG, "onUpdateNotification: ");
            updateNotification();
        }
    }

    private void updateNotification() {
        super.StartForeground(mediaSession.getSessionToken(),
                mediaSession.getController().getPlaybackState());
    }

    private void stopNotification() {
        super.StopForeground();
    }

    private class MyAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {

        @Override
        public void onAudioFocusChange(int focusChange) {
            //Log.d(TAG, "onAudioFocusChange: "+focusChange);
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN: //获得长时间播放焦点,短暂失去焦点后触发此回调
                    Log.e(TAG, "onAudioFocusChange: 获得长时间播放焦点");
                    mediaSession.getController().getTransportControls().play();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: //短暂失去焦点
                    Log.e(TAG, "onAudioFocusChange: 短暂失去焦点");
                    //如果焦点更改是暂时性的（AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK
                    // 或 AUDIOFOCUS_LOSS_TRANSIENT），
                    // 的应用应该降低音量（如果您不依赖于自动降低音量）或暂停播放，否则保持相同的状态。
                    // 在暂时性失去音频焦点时，您应该继续监控音频焦点的变化，
                    // 并准备好在重新获得焦点后恢复正常播放。当抢占焦点的应用放弃焦点时，
                    // 您会收到一个回调 (AUDIOFOCUS_GAIN)。
                    // 此时，您可以将音量恢复到正常水平或重新开始播放。
                    // 已设置自动降低播放音量 | Bilibili客户端播放视频时会收到此回调
                    IS_AUDIO_FOCUS_LOSS_TRANSIENT = true;
                    mediaSession.getController().getTransportControls().pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //失去焦点，但可以共同使用，需主动降低声量
                    Log.e(TAG, "onAudioFocusChange: 失去焦点，但可以共同使用");
                    // 已设置自动降低播放音量
                    break;
                case AudioManager.AUDIOFOCUS_LOSS://长时间失去焦点
                    Log.e(TAG, "onAudioFocusChange: 长时间失去焦点");
                    IS_AUDIO_FOCUS_LOSS_TRANSIENT = true;
                    mediaSession.getController().getTransportControls().pause();
                    break;
            }
        }
    }

    private void initLifeBackground() {
        if (timer != null) {
            return;
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mediaSession != null && !mediaSession.isActive()) {
                    return;
                } //当音乐播放时监听进度和音量更改

                //Log.d(TAG, "报告组织！MusicService还活着！position = "+getPosition());

                lastMetaManager.saveMusicPosition(mediaPlayerManager.getCurrentPosition());
                //监听音量更改
                if (mediaPlayerManager != null && mediaPlayerManager.checkAudioChange()) {
                    setVolume(mediaPlayerManager.getVolume());
                    Bundle bundle = new Bundle();
                    bundle.putInt(MyAudioManager.MyMusic_CUSTOM_ACTION_CURRENT_VOLUME, mediaPlayerManager.getVolume());
                    mediaSession.sendSessionEvent(MyAudioManager.MyMusic_CUSTOM_ACTION_CURRENT_VOLUME, bundle);
                }
                // Log.d(TAG, "报告组织！MusicService还活着！"+sdf.format(System.currentTimeMillis()));
            }
        }, 300, 1000);
    }

    private MediaMetadataCompat getMetadata(String mediaId) {
        MediaMetadataCompat metadata = musicList.get(mediaId);
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
//
//        mModel.getLocalMusicAlbum(
//                bitmap -> builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART,bitmap.get()),
//                Objects.requireNonNull(metadata).getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI),
//                getResources());

        for (String key :
                new String[]{
                        MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                        MediaMetadataCompat.METADATA_KEY_ALBUM,
                        MediaMetadataCompat.METADATA_KEY_ARTIST,
                        MediaMetadataCompat.METADATA_KEY_GENRE,
                        MediaMetadataCompat.METADATA_KEY_TITLE,
                        MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
                        MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI,
                        MediaMetadataCompat.METADATA_KEY_MEDIA_URI
                }) {
            builder.putString(key, metadata.getString(key));
            //Log.d(TAG, "getMetadata: "+metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI));
        }
        //放入播放时长 long
        builder.putLong(
                MediaMetadataCompat.METADATA_KEY_DURATION,
                metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
        //放入当前音量 long
        builder.putLong(
                MyAudioManager.MyMusic_CUSTOM_ACTION_CURRENT_VOLUME, mediaPlayerManager.getVolume());
        //放入最大音量 long
        builder.putLong(
                MyAudioManager.MyMusic_CUSTOM_ACTION_MAX_VOLUME, mediaPlayerManager.getMaxVolume());
        //放入播放模式 long
        builder.putLong(
                MediaPlayerManager.MyMusic_CUSTOM_ACTION_PLAYBACK_MODE_CHANGE, mediaPlayerManager.getPlaybackMode());
        return builder.build();
    }

    //**************************************记录上次播放音乐信息*************************************//
    private void GetLastMusicPlay() {
        int mode = mediaPlayerManager.getPlaybackMode();
        //将上次播放的音乐信息放入MediaSession
        MediaMetadataCompat.Builder metadataBuilder = lastMetaManager.getLastMusicPlay();
        String albumPath = lastMetaManager.getLastAlbumPath();
        //装载播放模式、当前音量，最大音量
        metadataBuilder
                .putLong(MediaPlayerManager.MyMusic_CUSTOM_ACTION_PLAYBACK_MODE_CHANGE, mode)
                .putLong(MyAudioManager.MyMusic_CUSTOM_ACTION_MAX_VOLUME, mediaPlayerManager.getMaxVolume())
                .putLong(MyAudioManager.MyMusic_CUSTOM_ACTION_CURRENT_VOLUME, mediaPlayerManager.getVolume())
                //个性样式为0，系统样式为1
                .putLong(BaseMusicService.MyMusic_NOTIFICATION_STYLE, lastMetaManager.getNotificationStyle() ? 0 : 1);
        //装载专辑图片 Bitmap
//        mModel.getLocalMusicAlbum(bitmap -> mediaSession.setMetadata(
//                        metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART,bitmap.get()).build()),
//                albumPath,getResources());

    }

    private void SaveLastMusicPlay() {
        lastMetaManager.SaveLastMusicPlay(
                mediaSession.getController().getMetadata(), mediaPlayerManager.getCurrentPosition());
    }

    private void setVolume(int level) {
        mediaPlayerManager.setVolume(level);
    }

}
