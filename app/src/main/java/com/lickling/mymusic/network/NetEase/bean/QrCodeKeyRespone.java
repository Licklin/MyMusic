package com.lickling.mymusic.network.NetEase.bean;

public class QrCodeKeyRespone {

    public Data data;
    public int code;

    public static class Data {
        public int code;
        public String unikey;

        @Override
        public String toString() {
            return "Data{" +
                    "code=" + code +
                    ", uniKey='" + unikey + '\'' +
                    '}';
        }
    }

    public String getUniKey() {
        return data.unikey;
    }

    @Override
    public String toString() {
        return "QrCodeKey{" +
                "data=" + data.toString() +
                ", code=" + code +
                '}';
    }
}
