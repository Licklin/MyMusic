package com.lickling.mymusic.bean.networkBean;

public class QrCodeCheckResponse {
    public int code;
    public String message;
    public String cookie;
    public String nickname;
    public String avatarUrl;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getCookie() {
        return cookie;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public String toString() {
        return "QrCodeCheckResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", cookie='" + cookie + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }

}
