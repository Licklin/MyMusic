package com.lickling.mymusic;

import android.annotation.SuppressLint;

import com.lickling.mymusic.model.MainModel;
import com.lickling.mymusic.network.NetEase.NetEaseApiHandler;

import org.junit.Test;


public class NetEaseAPIUnitTest_useCookies {
    private static final String BASE_URL = MainModel.BASE_URL;
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
    public void NetEaseAPIUnitTestMain() throws InterruptedException {
        String PATH = "D:\\Android_studio_Project\\app5\\java_cookies";
        String PLAYLIST_PATH = "F:\\Large_Machines\\Repos\\Android_Studio_Repos\\mi-ho-li_temp\\xx.json";
        NetEaseApiHandler client = new NetEaseApiHandler(BASE_URL);


        client.loadCookiesStringFromFile(PATH);

        // 注释摆烂了...，可以看看API接口，建议直接问我。
        client.getLoginStatus()
                .subscribe(result -> {
                    if (result != null) {
                        set_uid(result.getUserId());
                        System.out.println(result);
                        System.out.println(result.getUserId());
                        System.out.println(result);
                    }
                }, client.defErrorHandler());

//        Thread.sleep(3000);
        while (waitUid()) ;

        client.getUserPlaylist(_uid, 3, 0)
                .subscribe(result -> {
                    if (result != null) {
                        client.saveStringAsJsonFile(result, PLAYLIST_PATH);
                        System.out.println("[getUserPlaylist] " + result.getPlayListId());
                    }
                }, client.defErrorHandler());

        // 搜索搁浅的歌单
        client.getPlayListTrackAll("7651695953", 5, 0)
                .subscribe(result -> {
                    if (result != null) {
                        System.out.println("[getPlayListTrackAll] " + result.getSongsList());
                    }
                }, client.defErrorHandler());

        client.getCloudSearchSingleSong("周杰伦 搁浅", 5, 0)
                .subscribe(result -> {
                    if (result != null) {
                        System.out.println("[getCloudSearchSingleSong] " + result.getSongsList().get(0).name);
                        System.out.println("[getCloudSearchSingleSong] " + result.getSongsList().size());
                    }
                }, client.defErrorHandler());

        client.getCloudSearchPlayList("周杰伦 搁浅", 10, 0)
                .subscribe(result -> {
                    if (result != null) {
                        System.out.println("[getCloudSearchPlayList] " + result.getPlayList().get(0).name);
                        System.out.println("[getCloudSearchPlayList] " + result.getPlayList().size());
                    }
                }, client.defErrorHandler());

        client.getLikeList(_uid)
                .subscribe(result -> {
                    if (result != null) {
                        System.out.println("[getLikeList] " + result.ids);
                        System.out.println("[getLikeList] " + result.ids.size());
                    }
                }, client.defErrorHandler());

        client.getSongUrl("1403774122")
                .subscribe(result -> {
                    if (result != null) {
                        System.out.println("[getSongUrl] " + result.getSongUrl());
                    }
                }, client.defErrorHandler());


        System.out.println("Alive");
        while (true) ;


    }

}