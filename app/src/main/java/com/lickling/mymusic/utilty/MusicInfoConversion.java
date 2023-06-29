package com.lickling.mymusic.utilty;

import android.annotation.SuppressLint;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.lickling.mymusic.bean.musicBean.MusicBean;
import com.lickling.mymusic.bean.networkBean.CloudSearchPlayListResponse;
import com.lickling.mymusic.bean.networkBean.CloudSearchSingleSongResponse;
import com.lickling.mymusic.network.NetEase.NetEaseApiHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MusicInfoConversion {
    private static final String TAG = "MusicInfoConversion";

    public static MusicBean MediaItem2MusicBean(MediaBrowserCompat.MediaItem mediaItem) {
        String artist = Objects.requireNonNull(mediaItem.getDescription().getSubtitle()).toString(),
                album = Objects.requireNonNull(mediaItem.getDescription().getDescription()).toString();

        return new MusicBean(mediaItem.getMediaId(), Objects.requireNonNull(mediaItem.getDescription().getTitle()).toString(),
                artist, album,
                Objects.requireNonNull(mediaItem.getDescription().getMediaUri()).toString(),
                Objects.requireNonNull(mediaItem.getDescription().getMediaUri()).toString(),
                100000);
    }

    @SuppressLint("CheckResult")
    public static List<MusicBean> SearchMusicList2MusicBeanList(List<CloudSearchSingleSongResponse.Result.Songs> onlineLists) {
        NetEaseApiHandler client = new NetEaseApiHandler();
        List<MusicBean> tmpList = new ArrayList<>();
        for (CloudSearchSingleSongResponse.Result.Songs i : onlineLists) {
            MusicBean tmp = new MusicBean(i.id, i.name, i.ar.get(0).name, "", "", "", 100000);

            client.getSongUrl(tmp.getId())
                            .subscribe(result -> {
                                // 代码开始
                                // 代码, 比如更新ui, 或者打印
                                if (result != null) {
                                    tmp.setPath(result.getSongUrl());
                                }
                                // 代码结束
                            }, client.defErrorHandler());

            tmpList.add(tmp);
        }

        return tmpList;
    }

    public static List<MusicBean> OnlineMusicBean2MediaItem(List<CloudSearchSingleSongResponse.Result.Songs> onlineLists) {
        List<MusicBean> tmpList = new ArrayList<>();
        for (CloudSearchSingleSongResponse.Result.Songs i : onlineLists) {
            tmpList.add(new MusicBean(i.id, i.name, i.ar.get(0).name, "", "", "", 100000));
        }
        return tmpList;
    }
}
