package com.lickling.mymusic;

import android.annotation.SuppressLint;

import com.lickling.mymusic.network.NetEase.NetEaseApiHandler;

import org.junit.Test;


public class NetEaseAPIUnitTest_useCookies {
    private static final String BASE_URL = "http://192.168.31.31:3000";
    boolean _temp = false;
    String _uid = null;

    public void set_temp(boolean bool) {
        this._temp = bool;
    }

    public void set_uid(String uid) {
        synchronized (this) {
            _uid = uid;
        }
    }

    public boolean waitUid() {
        synchronized (this) {
            if (_uid != null)
                return false;
        }
        return true;
    }

    @SuppressLint("CheckResult")
    @Test
    public void NetEaseAPIUnitTestMain() {
        String PATH = "E:\\temp\\java_cookies";
        String PLAYLIST_PATH = "F:\\Large_Machines\\Repos\\Android_Studio_Repos\\mi-ho-li_temp\\xx.json";
        NetEaseApiHandler client = new NetEaseApiHandler(BASE_URL);


        client.loadCookiesStringFromFile(PATH);

        client.getLoginStatus()
                .subscribe(result -> {
                    if (result != null) {
                        set_uid(result.getUserId());
                        System.out.println(result);
                        System.out.println(result.getUserId());
                        System.out.println(result);
                    }
                }, client.defErrorHandler());

        while (waitUid()) ;

        client.getUserPlaylist(_uid, 3, 0)
                .subscribe(result -> {
                    if (result != null) {
                        client.saveStringAsJsonFile(result, PLAYLIST_PATH);
                        System.out.println(result.getPlayListId());
                    }
                }, client.defErrorHandler());

        client.getCloudSearchSingleSong("周杰伦 搁浅", 5, 0)
                .subscribe(result -> {
                    if (result != null) {
                        System.out.println(result.getSongsList().get(0).name);
                        System.out.println(result.getSongsList().size());
                    }
                }, client.defErrorHandler());

        client.getCloudSearchPlayList("周杰伦 搁浅", 5, 0)
                .subscribe(result -> {
                    if (result != null) {
                        System.out.println(result.getPlayList().get(0).name);
                        System.out.println(result.getPlayList().size());
                    }
                }, client.defErrorHandler());

        client.getLikeList(_uid)
                .subscribe(result -> {
                    if (result != null) {
                        System.out.println(result.ids);
                        System.out.println(result.ids.size());
                    }
                }, client.defErrorHandler());

        client.getSongUrl("1403774122")
                .subscribe(result -> {
                    if (result != null) {
                        System.out.println(result.getSongUrl());
                    }
                }, client.defErrorHandler());


        System.out.println("Alive");
        while (true) ;


    }

}