package com.lickling.mymusic.bean.networkBean;

public class LoginStatusResponse {

    public Data data;

    private static class Data {
        public int code;
        public Account account;
        public Profile profile;

        public static class Account {
            public String id;
            public String userName;
            public int type;
            public int status;
            public int whitelistAuthority;
            public long createTime;
            public int tokenVersion;
            public int ban;
            public int baoyueVersion;
            public int donateVersion;
            public int vipType;
            public boolean anonimousUser;
            public boolean paidFee;

            @Override
            public String toString() {
                return "Account{" +
                        "id=" + id +
                        ", userName='" + userName + '\'' +
                        ", type=" + type +
                        ", status=" + status +
                        ", whitelistAuthority=" + whitelistAuthority +
                        ", createTime=" + createTime +
                        ", tokenVersion=" + tokenVersion +
                        ", ban=" + ban +
                        ", baoyueVersion=" + baoyueVersion +
                        ", donateVersion=" + donateVersion +
                        ", vipType=" + vipType +
                        ", anonimousUser=" + anonimousUser +
                        ", paidFee=" + paidFee +
                        '}';
            }
        }

        public static class Profile {
            public String userId;
            public int userType;
            public String nickname;
            public String avatarImgId;
            public String avatarUrl;
            public String backgroundImgId;
            public String backgroundUrl;
            public String signature;
            public long createTime;
            public String userName;
            public int accountType;
            public String shortUserName;
            public long birthday;
            public int authority;
            public int gender;
            public int accountStatus;
            public int province;
            public int city;
            public int authStatus;
            public String description;
            public String detailDescription;
            public boolean defaultAvatar;
            public String expertTags;
            public String experts;
            public int djStatus;
            public int locationStatus;
            public int vipType;
            public boolean followed;
            public boolean mutual;
            public boolean authenticated;
            public long lastLoginTime;
            public String lastLoginIP;
            public String remarkName;
            public long viptypeVersion;
            public int authenticationTypes;
            public String avatarDetail;
            public boolean anchor;

            @Override
            public String toString() {
                return "Profile{" +
                        "userId='" + userId + '\'' +
                        ", userType=" + userType +
                        ", nickname='" + nickname + '\'' +
                        ", avatarImgId=" + avatarImgId +
                        ", avatarUrl='" + avatarUrl + '\'' +
                        ", backgroundImgId=" + backgroundImgId +
                        ", backgroundUrl='" + backgroundUrl + '\'' +
                        ", signature='" + signature + '\'' +
                        ", createTime=" + createTime +
                        ", userName='" + userName + '\'' +
                        ", accountType=" + accountType +
                        ", shortUserName='" + shortUserName + '\'' +
                        ", birthday=" + birthday +
                        ", authority=" + authority +
                        ", gender=" + gender +
                        ", accountStatus=" + accountStatus +
                        ", province=" + province +
                        ", city=" + city +
                        ", authStatus=" + authStatus +
                        ", description='" + description + '\'' +
                        ", detailDescription='" + detailDescription + '\'' +
                        ", defaultAvatar=" + defaultAvatar +
                        ", expertTags='" + expertTags + '\'' +
                        ", experts='" + experts + '\'' +
                        ", djStatus=" + djStatus +
                        ", locationStatus=" + locationStatus +
                        ", vipType=" + vipType +
                        ", followed=" + followed +
                        ", mutual=" + mutual +
                        ", authenticated=" + authenticated +
                        ", lastLoginTime=" + lastLoginTime +
                        ", lastLoginIP='" + lastLoginIP + '\'' +
                        ", remarkName='" + remarkName + '\'' +
                        ", viptypeVersion=" + viptypeVersion +
                        ", authenticationTypes=" + authenticationTypes +
                        ", avatarDetail='" + avatarDetail + '\'' +
                        ", anchor=" + anchor +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Data{" +
                    "code=" + code +
                    ", account=" + account +
                    ", profile=" + profile +
                    '}';
        }
    }

    public String getUserId() {
        if (data == null || data.profile == null || data.profile.userId == null)
            return "";
        return data.profile.userId;
    }

    public String getNickname() {
        if (data == null || data.profile == null || data.profile.nickname == null)
            return "";
        return data.profile.nickname;
    }

    public String getAvatar() {
        if (data == null || data.profile == null || data.profile.avatarUrl == null)
            return "";
        return data.profile.avatarUrl;
    }

    public int getData() {
        if (data == null)
            return -1;
        return data.code;
    }

    @Override
    public String toString() {
        return "LoginStatusResponse{" +
                "data=" + data +
                '}';
    }
}
