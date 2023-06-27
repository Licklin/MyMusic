package com.lickling.mymusic.network.NetEase.bean;

import java.util.List;

public class LikeListResponse {

    public List<Integer> ids;
    public String checkPoint;
    public int code;

    public List<Integer> getIds() {
        return ids;
    }
}
