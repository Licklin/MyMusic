package com.lickling.mymusic.ui.home.nsh.model;

public class LoginModel {
    public interface OnLoginFinishedListener {
        void onUsernameError();
        void onPasswordError();
        void onSuccess();
    }

    public void login(String username, String password, OnLoginFinishedListener listener) {
        // 在此处添加验证用户凭据和检索用户信息的代码

        // 如果发现用户名错误，则调用onUsernameError方法
        listener.onUsernameError();

        // 如果发现密码错误，则调用onPasswordError方法
        listener.onPasswordError();

        // 如果验证通过，则调用onSuccess方法
        listener.onSuccess();
    }
}
