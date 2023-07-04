package com.lickling.mymusic.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.Bindable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.lickling.mymusic.BR;
import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.NetEaseUser;
import com.lickling.mymusic.bean.User;
import com.lickling.mymusic.model.MainModel;
import com.lickling.mymusic.utilty.PictureUtil;
import com.orm.SugarContext;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class UserViewModel extends BaseViewModel {
    private Application application;
    User user;
    NetEaseUser netEaseUser;
    boolean isLoginNetEase = false;
    MainModel mainModel;

    public UserViewModel(Application application) {
        super(application);
        this.application = application;
        mainModel = new MainModel(application);
        user = mainModel.getUser();
        netEaseUser = mainModel.getNetEaseUser();

        if (!netEaseUser.getCookie().equals("")) isLoginNetEase = true;
    }

    public void notifyInfoChange() {
        user = mainModel.getUser();
        netEaseUser = mainModel.getNetEaseUser();
        if (!netEaseUser.getCookie().equals("")) isLoginNetEase = true;
    }

    @Bindable
    public String getNetEaseName() {
        return netEaseUser.getUserName();
    }

    public void setNetEaseAvatar(ImageView imageView) {
        Glide.with(application)
                .load(netEaseUser.getAvatarURL())
                .placeholder(R.drawable.default_record)
                .transform(new CircleCrop())
                .into(imageView);
    }

    @Bindable
    public String getUserName() {
        return user.getOurUserName();
    }

    public boolean isLoginNetEase() {
        return isLoginNetEase;
    }

    public void setLoginNetEase(boolean loginNetEase) {
        mainModel.saveCookie();
        isLoginNetEase = loginNetEase;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NetEaseUser getNetEaseUser() {
        return netEaseUser;
    }

    public void setNetEaseUser(NetEaseUser netEaseUser) {
        this.netEaseUser = netEaseUser;
        notifyPropertyChanged(BR.netEaseName);
    }
    public void loginNetEase(NetEaseUser netEaseUser){
        this.netEaseUser = netEaseUser;
        mainModel.saveCookie();
        notifyPropertyChanged(BR.netEaseName);
    }

    public void logoutUser(NetEaseUser netEaseUser) {
        mainModel.getClient().logOut();
    }

    public MainModel getMainModel() {
        return mainModel;
    }

    public void setMainModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }




    @SuppressLint("CheckResult")
    public void upgradeNteEaseInfo(ImageView imageView) { // 更新个人主页的网易信息，参数：显示头像的ImageView
        if (isLoginNetEase()) {
            mainModel.loadCookie(mainModel.getNetEaseUser().getCookie()); // 加载cookie
            mainModel.getClient().getLoginStatus()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        NetEaseUser netEaseUser = getNetEaseUser(); //
                        netEaseUser.setUserName(result.getNickname()); // 昵称
                        netEaseUser.setAvatarURL(result.getAvatar()); // 保存头像地址
                        netEaseUser.setUserID(result.getUserId()); // id
                        netEaseUser.save();
                        setNetEaseUser(netEaseUser);
                        setNetEaseAvatar(imageView); // 设置头像
                    }, mainModel.getClient().defErrorHandler());
        }
    }

    public boolean logoutNetEase() {
        if (isLoginNetEase()) {
            NetEaseUser netEaseUser = getNetEaseUser();
            SugarContext.init(Objects.requireNonNull(application));
            netEaseUser.setUserID("");
            netEaseUser.setUserName("");
            netEaseUser.setUserPWD("");
            netEaseUser.setCookie("");
            mainModel.setNetEaseUser(netEaseUser);
            setLoginNetEase(false);
            mainModel.getClient().logOut();
            return true;
        } else
            return false;
    }

    @SuppressLint("CheckResult")
    public void setQd2ImageView(ImageView imageView) {
        if (application == null) return;
        Glide.with(application).load(R.drawable.loading).into(imageView);
        mainModel.getClient().getQrCode()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(QRCodeBase64 -> {
                    if (QRCodeBase64 != null) {
//                        Log.d(TAG, "[NetEaseTest subscribe: QRCodeBase64] " + QRCodeBase64);
                        Glide.with(application)
                                .asBitmap()
                                .load(PictureUtil.base64String2Bitmap(QRCodeBase64))
                                .into(imageView);
                    }

                }, mainModel.getClient().defErrorHandler());

    }

    public void saveLogin(User user) {
        mainModel.saveLogin(user);
    }
}
