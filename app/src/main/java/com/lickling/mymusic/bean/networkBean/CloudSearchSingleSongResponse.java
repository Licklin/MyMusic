package com.lickling.mymusic.bean.networkBean;

import java.util.List;

public class CloudSearchSingleSongResponse {

    public Result result;
    public int code;

    public static class Result {
        public Object searchQcReminder;
        public List<Songs> songs;
        public int songCount;

        public static class Songs {
            public String name;
            public String id;
            public String pst;
            public String t;
            public List<Ar> ar;
            public List<?> alia;
            public String pop;
            public String st;
            public String rt;
            public String fee;
            public String v;
            public Object crbt;
            public String cf;
            public Al al;
            public String dt;
            public H h;
            public M m;
            public L l;
            public Sq sq;
            public Object hr;
            public Object a;
            public String cd;
            public String no;
            public Object rtUrl;
            public String ftype;
            public List<?> rtUrls;
            public String djId;
            public String copyright;
            public String s_id;
            public String mark;
            public String originCoverType;
            public Object originSongSimpleData;
            public Object tagPicList;
            public boolean resourceState;
            public String version;
            public Object songJumpInfo;
            public Object entertainmentTags;
            public String single;
            public Object noCopyrightRcmd;
            public String rtype;
            public Object rurl;
            public String mv;
            public String mst;
            public String cp;
            public String publishTime;
            public Privilege privilege;

            public static class Al {
                public String id;
                public String name;
                public String picUrl;
                public List<?> tns;
                public String pic_str;
                public long pic;
            }

            public static class H {
                public String br;
                public String fid;
                public String size;
                public String vd;
                public String sr;
            }

            public static class M {
                public String br;
                public String fid;
                public String size;
                public String vd;
                public String sr;
            }

            public static class L {
                public String br;
                public String fid;
                public String size;
                public String vd;
                public String sr;
            }

            public static class Sq {
                public String br;
                public String fid;
                public String size;
                public String vd;
                public String sr;
            }

            public static class Privilege {
                public String id;
                public String fee;
                public String payed;
                public String st;
                public String pl;
                public String dl;
                public String sp;
                public String cp;
                public String subp;
                public boolean cs;
                public String maxbr;
                public String fl;
                public boolean toast;
                public String flag;
                public boolean preSell;
                public String playMaxbr;
                public String downloadMaxbr;
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
                    public String listenType;
                }

                public static class ChargeInfoList {
                    public String rate;
                    public Object chargeUrl;
                    public Object chargeMessage;
                    public String chargeType;
                }
            }

            public static class Ar {
                public String id;
                public String name;
                public List<?> tns;
                public List<?> alias;
            }
        }
    }

    public List<Result.Songs> getSongsList() {
        return result.songs;
    }
}
