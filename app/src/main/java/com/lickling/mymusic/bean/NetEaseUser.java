package com.lickling.mymusic.bean;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

@Table(name = "net_ease_user")
public class NetEaseUser extends SugarRecord {
    private String usrID = "";
    private String usrName = "";
    private String usrPWD = "";
    private String cookie = "";
    private String avatarURL = ""; // 头像url

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

    @Override
    public String toString() {
        return "NetEaseUser{" +
                "usrID='" + usrID + '\'' +
                ", usrName='" + usrName + '\'' +
                ", usrPWD='" + usrPWD + '\'' +
                ", cookie='" + cookie + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                '}';
    }
}
