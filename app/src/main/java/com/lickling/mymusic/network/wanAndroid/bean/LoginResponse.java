package com.lickling.mymusic.network.wanAndroid.bean;

import androidx.annotation.NonNull;

import java.util.List;

public class LoginResponse {

    public Data data;
    public int errorCode;
    public String errorMsg;

    public Data getData() {
        return data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public static class Data {
        public boolean admin;
        public List<?> chapterTops;
        public int coinCount;
        public List<Integer> collectIds;
        public String email;
        public String icon;
        public int id;
        public String nickname;
        public String password;
        public String publicName;
        public String token;
        public int type;
        public String username;

        public boolean isAdmin() {
            return admin;
        }

        public List<?> getChapterTops() {
            return chapterTops;
        }

        public int getCoinCount() {
            return coinCount;
        }

        public List<Integer> getCollectIds() {
            return collectIds;
        }

        public String getEmail() {
            return email;
        }

        public String getIcon() {
            return icon;
        }

        public int getId() {
            return id;
        }

        public String getNickname() {
            return nickname;
        }

        public String getPassword() {
            return password;
        }

        public String getPublicName() {
            return publicName;
        }

        public String getToken() {
            return token;
        }

        public int getType() {
            return type;
        }

        public String getUsername() {
            return username;
        }

        @NonNull
        @Override
        public String toString() {
            return "Data{" +
                    "admin=" + admin +
                    ", chapterTops=" + chapterTops +
                    ", coinCount=" + coinCount +
                    ", collectIds=" + collectIds +
                    ", email='" + email + '\'' +
                    ", icon='" + icon + '\'' +
                    ", id=" + id +
                    ", nickname='" + nickname + '\'' +
                    ", password='" + password + '\'' +
                    ", publicName='" + publicName + '\'' +
                    ", token='" + token + '\'' +
                    ", type=" + type +
                    ", username='" + username + '\'' +
                    '}';
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "ResponseBean{" +
                "data=" + data +
                ", errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}