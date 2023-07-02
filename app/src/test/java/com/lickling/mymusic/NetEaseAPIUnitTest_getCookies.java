package com.lickling.mymusic;

import android.annotation.SuppressLint;

import com.lickling.mymusic.model.MainModel;
import com.lickling.mymusic.network.NetEase.NetEaseApiHandler;

import org.junit.Test;


public class NetEaseAPIUnitTest_getCookies {
    private static final String BASE_URL = MainModel.BASE_URL;
    boolean temp = true;

    @SuppressLint("CheckResult")
    @Test
    public void NetEaseAPIUnitTestMain() {
        String PATH = "F:\\Large_Machines\\Repos\\Android_Studio_Repos\\mi-ho-li_temp\\java_cookies";
        NetEaseApiHandler client = new NetEaseApiHandler();

        client.getQrCode()
                .subscribe(QRCodeBase64 -> {
                    if (client.__DEBUG__)
                        System.out.println("[NetEaseTest subscribe: QRCodeBase64] " + QRCodeBase64);
                    // do something?
                }, client.defErrorHandler());

        while (temp) {
            client.checkQrCodeStatus()
                    .subscribe(qrCodeCheckResponse -> {

                        if (client.__DEBUG__)
                            System.out.println("[checkQrCodeStatus] " + qrCodeCheckResponse.toString());
                        if (qrCodeCheckResponse.code == 803) {
                            client.saveCookiesVarToFile(PATH);
                            setTemp(false);
                        }
                        if (qrCodeCheckResponse.code == 800) {
                            System.out.println("[checkQrCodeStatus] Cookie被偷了！");
                            setTemp(false);
                        }
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

    public void setTemp(boolean bool) {
        this.temp = bool;
    }
}