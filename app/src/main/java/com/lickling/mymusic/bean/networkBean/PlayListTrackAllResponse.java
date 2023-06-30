package com.lickling.mymusic.bean.networkBean;

import java.util.List;

public class PlayListTrackAllResponse {

    public List<Songs> songs;
    public List<Privileges> privileges;
    public int code;

    private static class Songs {
        public String name;
        public int id;
        public int pst;
        public int t;
        public List<Ar> ar;
        public List<?> alia;
        public int pop;
        public int st;
        public String rt;
        public int fee;
        public int v;
        public Object crbt;
        public String cf;
        public Al al;
        public int dt;
        public H h;
        public M m;
        public L l;
        public Sq sq;
        public Object hr;
        public Object a;
        public String cd;
        public int no;
        public Object rtUrl;
        public int ftype;
        public List<?> rtUrls;
        public int djId;
        public int copyright;
        public int s_id;
        public int mark;
        public int originCoverType;
        public Object originSongSimpleData;
        public Object tagPicList;
        public boolean resourceState;
        public int version;
        public Object songJumpInfo;
        public Object entertainmentTags;
        public Object awardTags;
        public int single;
        public NoCopyrightRcmd noCopyrightRcmd;
        public int rtype;
        public Object rurl;
        public int mst;
        public int cp;
        public int mv;
        public long publishTime;
        public Pc pc;
        public List<String> tns;

        public static class Al {
            public int id;
            public String name;
            public String picUrl;
            public List<String> tns;
            public String pic_str;
            public long pic;
        }

        public static class H {
            public int br;
            public int fid;
            public int size;
            public int vd;
            public int sr;
        }

        public static class M {
            public int br;
            public int fid;
            public int size;
            public int vd;
            public int sr;
        }

        public static class L {
            public int br;
            public int fid;
            public int size;
            public int vd;
            public int sr;
        }

        public static class Sq {
            public int br;
            public int fid;
            public int size;
            public int vd;
            public int sr;
        }

        public static class NoCopyrightRcmd {
            public int type;
            public String typeDesc;
            public Object songId;
            public Object thirdPartySong;
            public Object expInfo;
        }

        public static class Pc {
            public String nickname;
            public String cid;
            public String fn;
            public int uid;
            public String alb;
            public int br;
            public String ar;
            public String sn;
        }

        public static class Ar {
            public int id;
            public String name;
            public List<?> tns;
            public List<?> alias;
        }
    }

    private static class Privileges {
        public int id;
        public int fee;
        public int payed;
        public int st;
        public int pl;
        public int dl;
        public int sp;
        public int cp;
        public int subp;
        public boolean cs;
        public int maxbr;
        public int fl;
        public boolean toast;
        public int flag;
        public boolean preSell;
        public int playMaxbr;
        public int downloadMaxbr;
        public String maxBrLevel;
        public String playMaxBrLevel;
        public String downloadMaxBrLevel;
        public String plLevel;
        public String dlLevel;
        public String flLevel;
        public Object rscl;
        public FreeTrialPrivilege freeTrialPrivilege;
        public List<ChargeInfoList> chargeInfoList;

        public static class FreeTrialPrivilege {
            public boolean resConsumable;
            public boolean userConsumable;
            public Object listenType;
        }

        public static class ChargeInfoList {
            public int rate;
            public Object chargeUrl;
            public Object chargeMessage;
            public int chargeType;
        }
    }

    public List<Songs> getSongsList() {
        return songs;
    }

    public List<Privileges> getPrivilegesList() {
        return privileges;
    }
}
