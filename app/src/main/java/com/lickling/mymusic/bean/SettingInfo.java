package com.lickling.mymusic.bean;

import com.lickling.mymusic.model.MainModel;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;


@Table(name = "setting_info")
public class SettingInfo extends SugarRecord {
    private boolean isNotification;
    private boolean isNoticeLyric;
    private int onlineSoundQuality; // 0标准，1高品，2无损
    private int downloadSoundQuality; // 0标准，1高品，2无损
    private int cacheLimit = 4; // 1-8GB
    private long apiPositionId = -1; // SugarORM 数据库的ID
    private int apiPosition = -1; // APIList里的位置
    private String apiUrl = MainModel.BASE_URL;
    private String version;
    @Ignore
    private APIChangeListener apiChangeListener;

    public SettingInfo() {

    }

    public interface APIChangeListener{
        void setClientAPI(String api);
    }
    public void setApiPositionId(long apiPositionId) {
        this.apiPositionId = apiPositionId;
        this.save();
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
//        apiChangeListener.setClientAPI(apiUrl);
        this.save();
    }

    public int getApiPosition() {
        return apiPosition;
    }

    public void setApiPosition(int apiPosition) {
        this.apiPosition = apiPosition;
        this.save();
    }


    public long getApiPositionId() {
        return apiPositionId;
    }


    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean notification) {
        isNotification = notification;
        this.save();
    }

    public boolean isNoticeLyric() {
        return isNoticeLyric;
    }

    public void setNoticeLyric(boolean noticeLyric) {
        isNoticeLyric = noticeLyric;
        this.save();
    }

    public int getOnlineSoundQuality() {
        return onlineSoundQuality;
    }

    public void setOnlineSoundQuality(int onlineSoundQuality) {
        this.onlineSoundQuality = onlineSoundQuality;
        this.save();
    }

    public int getDownloadSoundQuality() {
        return downloadSoundQuality;
    }

    public void setDownloadSoundQuality(int downloadSoundQuality) {
        this.downloadSoundQuality = downloadSoundQuality;
        this.save();
    }

    public int getCacheLimit() {
        return cacheLimit;
    }

    public void setCacheLimit(int cacheLimit) {
        this.cacheLimit = cacheLimit;
        this.save();
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
        this.save();
    }
    public void setApiChangeListener(APIChangeListener listener){
        this.apiChangeListener = listener;
    }
}
