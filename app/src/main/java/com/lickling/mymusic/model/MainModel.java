package com.lickling.mymusic.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.lickling.mymusic.bean.SettingInfo;
import com.lickling.mymusic.bean.User;
import com.orm.SugarRecord;
import com.orm.dsl.Table;


public class MainModel {
    protected long userSaveID = 1;
    protected long settingInfoSaveID = 1;
    protected User user;
    protected SettingInfo settingInfo;

    public MainModel(long userSaveID, long settingInfoSaveID) {
        this.userSaveID = userSaveID;
        this.settingInfoSaveID = settingInfoSaveID;
        getInfoFromDisk();

    }

    private void getInfoFromDisk() {

        this.user = User.findById(User.class, userSaveID);
        this.settingInfo = SettingInfo.findById(SettingInfo.class, settingInfoSaveID);

        if (user == null) {
            user = new User();
            Log.e("user","null");
            user.save();
            userSaveID = this.user.getId();

        }
        if (settingInfo == null) {
            settingInfo = new SettingInfo();
            Log.e("settingInfo","null");
            settingInfo.save();
            settingInfoSaveID = settingInfo.getId();
        }
    }

    public User getUser() {
        return user;
    }


    public SettingInfo getSettingInfo() {
        return settingInfo;
    }


    public void saveSetting(SettingInfo s) {
        settingInfo = s;
        settingInfo.save();
        settingInfoSaveID = settingInfo.getId();

        Log.d("settingInfoSaveID:", String.valueOf(settingInfoSaveID));




    }

    public void saveLogin(User user) {
        this.user = user;
        this.user.save();
        userSaveID = this.user.getId();
    }

    public long getUserSaveID() {
        return userSaveID;
    }

    public void setUserSaveID(long userSaveID) {
        this.userSaveID = userSaveID;
    }

    public long getSettingInfoSaveID() {
        return settingInfoSaveID;
    }

    public void setSettingInfoSaveID(long settingInfoSaveID) {
        this.settingInfoSaveID = settingInfoSaveID;
    }
}
