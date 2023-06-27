package com.lickling.mymusic.bean.networkBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 必选参数 : uid : 用户 id
 * <p>
 * 可选参数 :
 * <p>
 * limit : 返回数量 , 默认为 30
 * <p>
 * offset : 偏移数量，用于分页 , 如 :( 页数 -1)*30, 其中 30 为 limit 的值 , 默认为 0
 * <p>
 * 'http://localhost:4000/'+/user/playlist
 */

public class UserPlaylistResponse {

    public String version;
    public boolean more;
    public List<Playlist> playlist;
    public int code;

    public static class Playlist {
        public List<?> subscribers;
        public boolean subscribed;
        public Creator creator;
        public Object artists;
        public Object tracks;
        public Object updateFrequency;
        public int backgroundCoverId;
        public Object backgroundCoverUrl;
        public int titleImage;
        public Object titleImageUrl;
        public Object englishTitle;
        public boolean opRecommend;
        public Object recommendInfo;
        public int subscribedCount;
        public int cloudTrackCount;
        public int userId;
        public int totalDuration;
        public long coverImgId;
        public int privacy;
        public long trackUpdateTime;
        public int trackCount;
        public long updateTime;
        public String commentThreadId;
        public String coverImgUrl;
        public int specialType;
        public boolean anonimous;
        public long createTime;
        public boolean highQuality;
        public boolean newImported;
        public long trackNumberUpdateTime;
        public int playCount;
        public int adType;
        public Object description;
        public List<?> tags;
        public boolean ordered;
        public int status;
        public String name;
        public String id;
        public String coverImgId_str;
        public Object sharedUsers;
        public Object shareStatus;
        public boolean copied;

        public String getId() {
            return id;
        }

        public static class Creator {
            public boolean defaultAvatar;
            public int province;
            public int authStatus;
            public boolean followed;
            public String avatarUrl;
            public int accountStatus;
            public int gender;
            public int city;
            public int birthday;
            public int userId;
            public int userType;
            public String nickname;
            public String signature;
            public String description;
            public String detailDescription;
            public long avatarImgId;
            public long backgroundImgId;
            public String backgroundUrl;
            public int authority;
            public boolean mutual;
            public Object expertTags;
            public Object experts;
            public int djStatus;
            public int vipType;
            public Object remarkName;
            public int authenticationTypes;
            public Object avatarDetail;
            public String avatarImgIdStr;
            public String backgroundImgIdStr;
            public boolean anchor;
            public String avatarImgId_str;

            @Override
            public String toString() {
                return "Creator{" +
                        "defaultAvatar=" + defaultAvatar +
                        ", province=" + province +
                        ", authStatus=" + authStatus +
                        ", followed=" + followed +
                        ", avatarUrl='" + avatarUrl + '\'' +
                        ", accountStatus=" + accountStatus +
                        ", gender=" + gender +
                        ", city=" + city +
                        ", birthday=" + birthday +
                        ", userId=" + userId +
                        ", userType=" + userType +
                        ", nickname='" + nickname + '\'' +
                        ", signature='" + signature + '\'' +
                        ", description='" + description + '\'' +
                        ", detailDescription='" + detailDescription + '\'' +
                        ", avatarImgId=" + avatarImgId +
                        ", backgroundImgId=" + backgroundImgId +
                        ", backgroundUrl='" + backgroundUrl + '\'' +
                        ", authority=" + authority +
                        ", mutual=" + mutual +
                        ", expertTags=" + expertTags +
                        ", experts=" + experts +
                        ", djStatus=" + djStatus +
                        ", vipType=" + vipType +
                        ", remarkName=" + remarkName +
                        ", authenticationTypes=" + authenticationTypes +
                        ", avatarDetail=" + avatarDetail +
                        ", avatarImgIdStr='" + avatarImgIdStr + '\'' +
                        ", backgroundImgIdStr='" + backgroundImgIdStr + '\'' +
                        ", anchor=" + anchor +
                        ", avatarImgId_str='" + avatarImgId_str + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Playlist{" +
                    "subscribers=" + subscribers +
                    ", subscribed=" + subscribed +
                    ", creator=" + creator +
                    ", artists=" + artists +
                    ", tracks=" + tracks +
                    ", updateFrequency=" + updateFrequency +
                    ", backgroundCoverId=" + backgroundCoverId +
                    ", backgroundCoverUrl=" + backgroundCoverUrl +
                    ", titleImage=" + titleImage +
                    ", titleImageUrl=" + titleImageUrl +
                    ", englishTitle=" + englishTitle +
                    ", opRecommend=" + opRecommend +
                    ", recommendInfo=" + recommendInfo +
                    ", subscribedCount=" + subscribedCount +
                    ", cloudTrackCount=" + cloudTrackCount +
                    ", userId=" + userId +
                    ", totalDuration=" + totalDuration +
                    ", coverImgId=" + coverImgId +
                    ", privacy=" + privacy +
                    ", trackUpdateTime=" + trackUpdateTime +
                    ", trackCount=" + trackCount +
                    ", updateTime=" + updateTime +
                    ", commentThreadId='" + commentThreadId + '\'' +
                    ", coverImgUrl='" + coverImgUrl + '\'' +
                    ", specialType=" + specialType +
                    ", anonimous=" + anonimous +
                    ", createTime=" + createTime +
                    ", highQuality=" + highQuality +
                    ", newImported=" + newImported +
                    ", trackNumberUpdateTime=" + trackNumberUpdateTime +
                    ", playCount=" + playCount +
                    ", adType=" + adType +
                    ", description=" + description +
                    ", tags=" + tags +
                    ", ordered=" + ordered +
                    ", status=" + status +
                    ", name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    ", coverImgId_str='" + coverImgId_str + '\'' +
                    ", sharedUsers=" + sharedUsers +
                    ", shareStatus=" + shareStatus +
                    ", copied=" + copied +
                    '}';
        }
    }

    public List<String> getPlayListId() {
        if (playlist == null) return null;
        List<String> ret = new ArrayList<>();
        for (Playlist item : playlist) {
            ret.add(item.getId());
        }
        return ret;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "UserPlaylistResponse{" +
                "version='" + version + '\'' +
                ", more=" + more +
                ", playlist=" + playlist +
                ", code=" + code +
                '}';
    }
}
