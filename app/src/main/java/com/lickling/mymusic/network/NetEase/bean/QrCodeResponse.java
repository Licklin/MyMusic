package com.lickling.mymusic.network.NetEase.bean;

public class QrCodeResponse {

    public int code;
    public Data data;

    public static class Data {
        public String qrurl;
        public String qrimg;

        @Override
        public String toString() {
            return "Data{" +
                    "qrurl='" + qrurl + '\'' +
                    ", qrimg='" + qrimg + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "QrCodeResponse{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
