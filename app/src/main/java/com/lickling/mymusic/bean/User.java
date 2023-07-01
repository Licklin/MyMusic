package com.lickling.mymusic.bean;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

@Table(name = "user")
public class User extends SugarRecord {
    private String ourUserID;
    private String ourUserName;
    private String ourUserPWD;
    @Ignore
    private SettingInfo settingInfo;

    public SettingInfo getSettingInfo() {
        return settingInfo;
    }

    public void setSettingInfo(SettingInfo settingInfo) {
        this.settingInfo = settingInfo;
    }

    public String getOurUserID() {
        return ourUserID;
    }

    public void setOurUserID(String ourUserID) {
        this.ourUserID = ourUserID;
        this.save();
    }

    public String getOurUserName() {
        return ourUserName;
    }

    public void setOurUserName(String ourUserName) {
        this.ourUserName = ourUserName;
        this.save();
    }

    public String getOurUserPWD() {
        return ourUserPWD;
    }

    public void setOurUserPWD(String ourUserPWD) {
        this.ourUserPWD = ourUserPWD;
        this.save();
    }

}
