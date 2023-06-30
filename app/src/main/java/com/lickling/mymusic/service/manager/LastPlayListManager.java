package com.lickling.mymusic.service.manager;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.ref.SoftReference;

public class LastPlayListManager {
    private static final String TAG = "LastPlayListManager";
    private final SoftReference<SharedPreferences> lastPlayListSave;

    public LastPlayListManager(Application application) {
        lastPlayListSave = new SoftReference<>(
                application.getSharedPreferences("UserLastPlayList", 0));
        if (lastPlayListSave.get() == null) Log.e(TAG, "LastPlayListManager: SharedPreferences为空");
    }

    public SharedPreferences get() {
        return lastPlayListSave == null ? null : lastPlayListSave.get();
    }

    private boolean checkSetting() {
        boolean isNull = lastPlayListSave == null || lastPlayListSave.get() == null;
        if (isNull) Log.e(TAG, "checkSetting: 为空！");
        return isNull;
    }
    // TODO 改为List保存整个list
    public void savePlayList(int id) {
        if (!checkSetting()) {
            SharedPreferences.Editor editor = get().edit();
            editor.putInt("PlayListId", id);
            editor.apply();
        }
    }
    // TODO 改为List保存整个list
    public int getLastPlayList(){
        return get().getInt("MusicPosition",0);
    }

    public void onDestroy(){
        if (lastPlayListSave != null) { lastPlayListSave.clear(); }
    }
}
