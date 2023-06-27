package com.lickling.mymusic;

import android.annotation.SuppressLint;

import com.lickling.mymusic.network.NetEase.NetEaseApiHandler;

import org.junit.Test;


public class NetEaseAPIUnitTest_useCookies {
    private static final String BASE_URL = "http://localhost:4000";
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

    public boolean waitUiu() {
        synchronized (this) {
            if (_uid != null)
                return false;
        }
        return true;
    }

    @SuppressLint("CheckResult")
    @Test
    public void NetEaseAPIUnitTestMain() {
        String PATH = "F:\\Large_Machines\\Repos\\Android_Studio_Repos\\mi-ho-li_temp\\java_cookies";
        String PLAYLIST_PATH = "F:\\Large_Machines\\Repos\\Android_Studio_Repos\\mi-ho-li_temp\\xx.json";
        NetEaseApiHandler client = new NetEaseApiHandler();


        client.loadCookiesStringFromFile(PATH);

        client.getLoginStatus()
                .subscribe(loginStatusResponse -> {
                    if (loginStatusResponse != null) {
                        set_uid(loginStatusResponse.getUserId());
                        System.out.println(loginStatusResponse.getUserId());
                    }
                });

        while (waitUiu()) ;

        client.getUserPlaylist(_uid, 3, 0)
                .subscribe(userPlaylistResponse -> {
                    if (userPlaylistResponse != null) {
                        client.saveStringAsJsonFile(userPlaylistResponse, PLAYLIST_PATH);
                        System.out.println(userPlaylistResponse.getPlayListId());
                    }
                });

        System.out.println("Alive");
        while (true) ;


    }

}