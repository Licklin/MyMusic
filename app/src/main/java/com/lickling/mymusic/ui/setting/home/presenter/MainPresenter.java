package com.lickling.mymusic.ui.setting.home.presenter;


import com.lickling.mymusic.ui.setting.home.MainView;
import com.lickling.mymusic.ui.setting.home.model.MainModel;

import javax.security.auth.callback.Callback;

public class MainPresenter implements IPresenter, Callback {
    private MainView mainView;
    private MainModel mainModel;

    public MainPresenter(MainView mainView) {
        mainModel = new MainModel();
        this.mainView = mainView;

    }

    @Override
    public void goToSoundQuality() {

    }

    @Override
    public void goToNotice() {

    }

    @Override
    public void goToPassWord() {
       mainView.goToPassWord();
    }


}
