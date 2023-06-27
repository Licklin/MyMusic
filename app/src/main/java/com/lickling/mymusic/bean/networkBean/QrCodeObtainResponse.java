package com.lickling.mymusic.bean.networkBean;

public class QrCodeObtainResponse {

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
