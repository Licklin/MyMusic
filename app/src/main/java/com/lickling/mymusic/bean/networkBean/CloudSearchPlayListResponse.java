package com.lickling.mymusic.bean.networkBean;

import java.util.List;

public class CloudSearchPlayListResponse {

    public Result result;
    public int code;

    public static class Result {
        public Object searchQcReminder;
        public List<Playlists> playlists;
        public int playlistCount;

        public static class Playlists {
            public long id;
            public String name;
            public String coverImgUrl;
            public Creator creator;
            public boolean subscribed;
            public int trackCount;
            public String userId;
            public int playCount;
            public int bookCount;
            public int specialType;
            public Object officialTags;
            public Object action;
            public Object actionType;
            public Object recommendText;
            public Object score;
            public Object description;
            public boolean highQuality;

            public static class Creator {
                public String nickname;
                public String userId;
                public String userType;
                public Object avatarUrl;
                public String authStatus;
                public Object expertTags;
                public Object experts;
            }
        }
    }

    public List<Result.Playlists> getPlayList() {
        return result.playlists;
    }
}
