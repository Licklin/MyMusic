package com.lickling.mymusic;

import android.annotation.SuppressLint;

import com.lickling.mymusic.network.NetEase.NetEaseApiHandler;

import org.junit.Test;


public class NetEaseAPIUnitTest {
    private static final String BASE_URL = "http://localhost:4000";
    String xQRCodeBase64;

    @SuppressLint("CheckResult")
    @Test
    public void NetEaseAPIUnitTestMain() {
        NetEaseApiHandler client = new NetEaseApiHandler();

        client.getQrCode()
                .subscribe(QRCodeBase64 -> {
                    if (client.__DEBUG__)
                        System.out.println("[NetEaseTest subscribe: QRCodeBase64] " + QRCodeBase64);
                    x(QRCodeBase64);
                    // do something?
                }, client.defErrorHandler());

        while (true) {
            client.checkQrCodeStatus_rawReturn()
                    .subscribe(responseBody -> {
                        if (client.__DEBUG__)
                            System.out.println("[checkQrCodeStatus] " + responseBody.string());
                    });
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

//        System.out.println("Alive");
//
//        while (true) {
//        }
    }

    public void x(String y) {
        this.xQRCodeBase64 = y;
    }
}