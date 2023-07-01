package com.lickling.mymusic.viewmodel;

import android.app.Application;
import android.widget.ImageView;

import androidx.databinding.Bindable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.lickling.mymusic.BR;
import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.NetEaseUser;
import com.lickling.mymusic.bean.User;
import com.lickling.mymusic.model.MainModel;

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

    public void notifyInfoChange(){
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
}
