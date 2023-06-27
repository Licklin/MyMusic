package com.lickling.mymusic.ui.setting.home.presenter;


import com.lickling.mymusic.ui.setting.home.MainView;
import javax.security.auth.callback.Callback;

public class MainPresenter implements IPresenter, Callback {
    private MainView mainView;

    public MainPresenter(MainView mainView) {
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
