package com.lickling.mymusic.ui.songAndLyrics.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lickling.mymusic.R;
import com.lickling.mymusic.databinding.PlayingListPopupBinding;
import com.lickling.mymusic.ui.songAndLyrics.viewmodel.PlayListViewModel;

public class PlayingListFragment extends BottomSheetDialogFragment {
    private PlayListViewModel playingListviewModel;
    private PlayingListPopupBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playingListviewModel = new ViewModelProvider(this).get(PlayListViewModel.class);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PlayingListPopupBinding.inflate(inflater, container, false);
        binding.setViewModel(playingListviewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }


}
