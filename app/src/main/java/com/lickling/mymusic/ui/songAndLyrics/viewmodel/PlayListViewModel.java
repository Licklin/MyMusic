package com.lickling.mymusic.ui.songAndLyrics.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lickling.mymusic.viewmodel.BaseViewModel;
import com.lickling.mymusic.viewmodel.MusicViewModel;

import java.lang.ref.SoftReference;

public class PlayListViewModel extends BaseViewModel {
    private Application mApplication;

    public PlayListViewModel(Application application) {
        super(application);
        mApplication = application;
    }

    private MutableLiveData<String> textData = new MutableLiveData<>();

    public LiveData<String> getTextData() {
        return textData;
    }

    public void updateTextData(String newText) {
        textData.setValue(newText);
    }
}
