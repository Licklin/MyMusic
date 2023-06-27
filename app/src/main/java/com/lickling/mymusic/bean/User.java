package com.lickling.mymusic.bean;

public class User {
    private String ourUserID;
    private String ourUserName;
    private String ourUserPWD;

    public String getOurUserID() {
        return ourUserID;
    }

    public void setOurUserID(String ourUserID) {
        this.ourUserID = ourUserID;
    }

    public String getOurUserName() {
        return ourUserName;
    }

    public void setOurUserName(String ourUserName) {
        this.ourUserName = ourUserName;
    }

    public String getOurUserPWD() {
        return ourUserPWD;
    }

    public void setOurUserPWD(String ourUserPWD) {
        this.ourUserPWD = ourUserPWD;
    }

    private class netUser {
        private String usrID;
        private String usrName;
        private String usrPWD;
        private String cookie;
        private String avatarURL;

        public String getUserID() {
            return usrID;
        }

        public void setUserID(String usrID) {
            this.usrID = usrID;
        }

        public String getUserName() {
            return usrName;
        }

        public void setUserName(String usrName) {
            this.usrName = usrName;
        }

        public String getUserPWD() {
            return usrPWD;
        }

        public void setUserPWD(String usrPWD) {
            this.usrPWD = usrPWD;
        }

        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }

        public String getAvatarURL() {
            return avatarURL;
        }

        public void setAvatarURL(String avatarURL) {
            this.avatarURL = avatarURL;
        }
    }
    private class Setting{
//        private int
    }
}
