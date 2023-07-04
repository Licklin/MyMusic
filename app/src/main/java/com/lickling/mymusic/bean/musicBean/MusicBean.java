package com.lickling.mymusic.bean.musicBean;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * 作用: 抽象一首歌曲，用于展示或者设置其信息
 */
public class MusicBean  {
    private String id;

    private String listId;

    private String netId;
    private String title;
    private String artist;
    private String album;
    private String albumPath;
    private String path;
    private long duration;

    public MusicBean(String id, String title, String artist, String album, String albumPath, String path, long duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumPath = albumPath;
        this.path = path;
        this.duration = duration;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


    public String getSubtitle() { return "";
    }
}
