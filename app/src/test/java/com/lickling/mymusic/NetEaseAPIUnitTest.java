package com.lickling.mymusic;

import android.annotation.SuppressLint;

import com.lickling.mymusic.network.NetEase.NetEaseApiHandler;
import com.lickling.mymusic.network.NetEase.NetEaseApiService;

import org.junit.Test;


public class NetEaseAPIUnitTest {
    private static final String BASE_URL = "http://localhost:4000";

    @SuppressLint("CheckResult")
    @Test
    public void NetEaseAPIUnitTestMain() {
        NetEaseApiHandler client = new NetEaseApiHandler();

        client.getQrCode();

        System.out.println("Alive");

        while (true) {
        }
    }
}