package com.lickling.mymusic.ui.home.nsh.presenter;

import com.lickling.mymusic.ui.home.nsh.model.LoginModel;

public class LoginPresenter {
    private LoginView view;
    private LoginModel model;

    public LoginPresenter(LoginView view) {
        this.view = view;
        model = new LoginModel();
    }

    public void login(String username, String password) {
        model.login(username, password, new LoginModel.OnLoginFinishedListener() {
            @Override
            public void onUsernameError() {
                view.showUsernameError();
            }

            @Override
            public void onPasswordError() {
                view.showPasswordError();
            }

            @Override
            public void onSuccess() {
                view.showLoginSuccess();
            }
        });
    }
}
