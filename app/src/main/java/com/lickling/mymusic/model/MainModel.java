package com.lickling.mymusic.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.NetEaseUser;
import com.lickling.mymusic.bean.SettingInfo;
import com.lickling.mymusic.bean.User;
import com.lickling.mymusic.network.NetEase.NetEaseApiHandler;
import com.lickling.mymusic.utilty.PictureUtil;
import com.orm.SugarContext;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class MainModel {
    private final String TAG = "MainModel";
    private Context context;
    protected long userSaveID = 1;
    protected long settingInfoSaveID = 1;
    protected long netEaseUserInfoSaveID = 1;
    protected User user;
    protected NetEaseUser netEaseUser;
    protected SettingInfo settingInfo;
    private NetEaseApiHandler client;

    public MainModel(long userSaveID, long settingInfoSaveID) {
        this.userSaveID = userSaveID;
        this.settingInfoSaveID = settingInfoSaveID;
        getInfoFromDisk();
        client = new NetEaseApiHandler(); // 实例化网络模块

    }

    public MainModel(Context context) {
        this.context = context;
        client = new NetEaseApiHandler("http://192.168.31.31:3000");



//        client = new NetEaseApiHandler();
        // userSaveID和settingInfoSaveID存在SharedPreferences里
        SharedPreferences prefs = context.getSharedPreferences("userId", Context.MODE_PRIVATE);

        userSaveID = prefs.getLong("saveKeyOfUser", 1);
        settingInfoSaveID = prefs.getLong("saveKeyOfSetting", 1);
        netEaseUserInfoSaveID = prefs.getLong("saveKeyOfNetUser", 1);
        SugarContext.init(context);
        getInfoFromDisk(); // 从SugarORM里读取出详细的数据



        SharedPreferences.Editor editor = prefs.edit();
        // 保存userSaveID和settingInfoSaveID，应用第一次安装时SQLite里没有数据，要new数据进出会产生新的id，所以要保存id进SharedPreferences
        editor.putLong("saveKeyOfUser", getUserSaveID());
        editor.putLong("saveKeyOfSetting", getSettingInfoSaveID());
        editor.putLong("saveKeyOfNetUser", getNetEaseUserInfoSaveID());
        editor.apply();

        loadCookie(netEaseUser.getCookie());
    }

    private void getInfoFromDisk() {

        this.user = User.findById(User.class, userSaveID);
        this.settingInfo = SettingInfo.findById(SettingInfo.class, settingInfoSaveID);
        this.netEaseUser = NetEaseUser.findById(NetEaseUser.class, netEaseUserInfoSaveID);
        // 没有找到数据就会为null，就需要new一个user
        if (user == null) {
            user = new User();
            Log.e("user", "null");
            user.save();
            userSaveID = this.user.getId();

        }
        if (settingInfo == null) {
            settingInfo = new SettingInfo();
            Log.e("settingInfo", "null");
            settingInfo.save();
            settingInfoSaveID = settingInfo.getId();
        }
        if (netEaseUser == null) {
            netEaseUser = new NetEaseUser();
            Log.e("netEaseUser", "null");
            netEaseUser.save();
            netEaseUserInfoSaveID = netEaseUser.getId();
        }
    }

    public long getNetEaseUserInfoSaveID() {
        return netEaseUserInfoSaveID;
    }

    public void setNetEaseUserInfoSaveID(long netEaseUserInfoSaveID) {
        this.netEaseUserInfoSaveID = netEaseUserInfoSaveID;
    }

    public NetEaseUser getNetEaseUser() {
        return NetEaseUser.findById(NetEaseUser.class,netEaseUserInfoSaveID);
    }

    public void setNetEaseUser(NetEaseUser netEaseUser) {
        this.netEaseUser = netEaseUser;
    }

    public NetEaseApiHandler getClient() {
        return client;
    }

    public void setClient(NetEaseApiHandler client) {
        this.client = client;
    }

    public User getUser() {
        return User.findById(User.class,userSaveID);
    }


    public SettingInfo getSettingInfo() {
        return SettingInfo.findById(SettingInfo.class,settingInfoSaveID);
    }


    public void saveSetting(SettingInfo s) {
        settingInfo = s;
        settingInfo.save();
        settingInfoSaveID = settingInfo.getId();
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

    @SuppressLint("CheckResult")
    public void setQd2ImageView(ImageView imageView) {
        if (context == null) return;
        Glide.with(context).load(R.drawable.loading).into(imageView);
        client.getQrCode()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(QRCodeBase64 -> {
                    if (QRCodeBase64 != null) {
//                        Log.d(TAG, "[NetEaseTest subscribe: QRCodeBase64] " + QRCodeBase64);
                        Glide.with(context)
                                .asBitmap()
                                .load(PictureUtil.base64String2Bitmap(QRCodeBase64))
                                .into(imageView);
                    }

                }, client.defErrorHandler());

    }

    public String saveCookie(){
        netEaseUser.setCookie(client.cookie2Json());
        netEaseUser.save();
        return netEaseUser.getCookie();
    }

    public void loadCookie(String theCookie) {
        client.json2Cookie(theCookie);
    }
}
