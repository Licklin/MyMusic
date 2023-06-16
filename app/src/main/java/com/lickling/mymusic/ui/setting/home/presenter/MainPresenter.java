package com.lickling.mymusic.ui.setting.home.presenter;


import android.content.Context;
import android.content.Intent;

import com.lickling.mymusic.ui.setting.home.HomeActivity;
import com.lickling.mymusic.ui.setting.home.MainView;
import com.lickling.mymusic.ui.setting.home.model.MainModel;
import com.lickling.mymusic.ui.setting.password.view.PassWordActivity;

import javax.security.auth.callback.Callback;

public class MainPresenter implements IPresenter, Callback {
    private MainView mainView;
    private MainModel mainModel;

    public MainPresenter(MainView mainView) {
        mainModel = new MainModel();
        this.mainView = mainView;

    }

    @Override
    public void goToPassWord() {
       mainView.goToPassWord();
    }


}
