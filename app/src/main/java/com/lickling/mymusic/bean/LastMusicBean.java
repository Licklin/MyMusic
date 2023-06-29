package com.lickling.mymusic.bean;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * 作用: 抽象一首歌曲，用于展示或者设置其信息
 */
@Table
public class LastMusicBean extends SugarRecord {
    private String musicId;
    private String title;
    private String artist;
    private String album;
    private String albumPath;
    private String path;
    private long duration;

    public LastMusicBean(String id, String title, String artist, String album, String albumPath, String path, long duration) {
        this.musicId = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumPath = albumPath;
        this.path = path;
        this.duration = duration;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setId(String id) {
        this.musicId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(String albumPath) {
        this.albumPath = albumPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public CharSequence getSubtitle() {
        return null;
    }
}
