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
import com.orm.SugarContext;

import java.util.Objects;

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
        return "网易：" + netEaseUser.getUserName();
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
//        setNetEaseAvatar();
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
    public void test() {
        mainModel.saveCookie();
        mainModel.getClient().getLoginStatus()
                .subscribe(result-> {
                    Log.e("6", "test: " + result.toString());
                });
    }
}
