package com.lickling.mymusic.ui.songAndLyrics.viewmodel;

import android.app.Application;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lickling.mymusic.databinding.LocalSongListItemBinding;
import com.lickling.mymusic.ui.local.ListAdapter;
import com.lickling.mymusic.ui.local.LocalActivity;
import com.lickling.mymusic.viewmodel.BaseViewModel;

public class PlayingListViewModel extends BaseViewModel {
    private final String TAG = "[PlayingListViewModel] ";
    private Application mApplication;

    public PlayingListViewModel(Application application) {
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
