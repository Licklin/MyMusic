package com.lickling.mymusic.network.NetEase.bean;

import androidx.annotation.Nullable;

import java.util.List;

public class SongUrlResponse {

    public List<Data> data;
    public int code;

    private static class Data {
        public String id;
        public String url;
        public String br;
        public String size;
        public String md5;
        public int code;
        public String expi;
        public String type;
        public double gain;
        public String peak;
        public String fee;
        public Object uf;
        public String payed;
        public String flag;
        public boolean canExtend;
        public Object freeTrialInfo;
        public String level;
        public String encodeType;
        public FreeTrialPrivilege freeTrialPrivilege;
        public FreeTimeTrialPrivilege freeTimeTrialPrivilege;
        public String urlSource;
        public String rightSource;
        public Object podcastCtrp;
        public Object effectTypes;
        public String time;

        public static class FreeTrialPrivilege {
            public boolean resConsumable;
            public boolean userConsumable;
            public Object listenType;
            public Object cannotListenReason;
        }

        public static class FreeTimeTrialPrivilege {
            public boolean resConsumable;
            public boolean userConsumable;
            public String type;
            public int remaStringime;
        }
    }

    @Nullable
    public String getSongUrl() {
        if (data.size() > 0)
            return data.get(0).url;
        return null;
    }
}
