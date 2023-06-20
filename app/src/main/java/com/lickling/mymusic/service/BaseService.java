package com.lickling.mymusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class BaseService extends Service {
    public BaseService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    protected void init(){

    }

}
