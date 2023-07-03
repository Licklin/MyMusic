package com.lickling.mymusic.ui.local;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.adapter.BaseBindingAdapter;
import com.lickling.mymusic.bean.musicBean.MusicBean;
import com.lickling.mymusic.databinding.ItemMusicListBinding;
import com.lickling.mymusic.databinding.LocalSongListItemBinding;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class ListAdapter extends BaseBindingAdapter<MediaItem, LocalSongListItemBinding> {

    //private List<MusicBean> listItems;
    //private Context context;

    public List<Boolean> items_state;
    private MediaItem selectedItem;
    private static final String TAG = "MusicAdapter";
    private ObservableArrayList<MediaItem> mSearchMediaItems, mSheetMediaItems;
    private OnItemClickListener mItemClickListener;
    private Context m_context;
    private LocalSongListItemBinding pre_binding;

    public interface OnItemClickListener {
        void ItemClickListener(ListAdapter adapter, int position, LocalSongListItemBinding binding);

        void ItemMoreClickListener(View v, int position);

        void ItemNextPlayClickListener(View v, int position);

        void ItemCheckedClickListener(LocalSongListItemBinding binding, int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public ListAdapter(Application context) {
        super(context);
    }

    public void setContext(Context context1) {
        m_context = context1;
    }

    public Context getContext() {
        return m_context;
    }

    @Override
    protected int getLayoutResId(int ViewType) {
        return R.layout.local_song_list_item;
    }

    @Override
    protected void onBindItem(LocalSongListItemBinding binding, MediaItem item, int position) {
        int number = position;
        String artist = Objects.requireNonNull(item.getDescription().getSubtitle()).toString(),
                album = Objects.requireNonNull(item.getDescription().getDescription()).toString();
        MusicBean bean = new MusicBean(String.valueOf(number),
                Objects.requireNonNull(item.getDescription().getTitle()).toString(),
                artist, album,
                Objects.requireNonNull(item.getDescription().getMediaUri()).toString(),
                Objects.requireNonNull(item.getDescription().getMediaUri()).toString(),
                100000);
        binding.setLocalMusicInfo(bean);

        synBindingState(binding, position);

        if(selectedItem==item)
            pre_binding = binding;
        resetItemColor(binding, position);

        if (mItemClickListener == null) return;
        binding.playBtn.setOnClickListener(v -> mItemClickListener.ItemClickListener(this, position, binding));
        binding.extendBtn.setOnClickListener(v -> mItemClickListener.ItemMoreClickListener(v, position));
        binding.addBtn.setOnClickListener(v -> mItemClickListener.ItemNextPlayClickListener(v, position));
        binding.checkBox.setOnClickListener(v -> mItemClickListener.ItemCheckedClickListener(binding, position));
    }


    public void release() {
        super.release();
        if (mItemClickListener != null) {
            mItemClickListener = null;
        }
        releaseThisMediaItems();
    }

    public void releaseThisMediaItems() {
        if (mSearchMediaItems != null) {
            if (mSearchMediaItems.size() > 0) {
                mSearchMediaItems.clear();
            }
            mSearchMediaItems = null;
        }
        if (mSheetMediaItems != null) {
            if (mSheetMediaItems.size() > 0) {
                mSheetMediaItems.clear();
            }
            mSheetMediaItems = null;
        }
    }


    public MusicBean toMusicBean(MediaItem item) {
        String artist = Objects.requireNonNull(item.getDescription().getSubtitle()).toString(),
                album = Objects.requireNonNull(item.getDescription().getDescription()).toString();
        MusicBean bean = new MusicBean(String.valueOf(1),
                Objects.requireNonNull(item.getDescription().getTitle()).toString(),
                artist, album,
                Objects.requireNonNull(item.getDescription().getMediaUri()).toString(),
                Objects.requireNonNull(item.getDescription().getMediaUri()).toString(),
                100000);
        return bean;
    }

    public void resetItemColor(LocalSongListItemBinding binding, int position) {
        Drawable icon;

        if (selectedItem != null && selectedItem == getItems().get(position)) {
            icon = ContextCompat.getDrawable(m_context.getApplicationContext(), R.drawable.load_add);
            icon.setColorFilter(m_context.getResources().getColor(R.color.wy_red), PorterDuff.Mode.SRC_IN);
            binding.addBtn.setBackground(icon);

            icon = ContextCompat.getDrawable(m_context.getApplicationContext(), R.drawable.baseline_more_vert_24);
            icon.setColorFilter(m_context.getResources().getColor(R.color.wy_red), PorterDuff.Mode.SRC_IN);
            binding.extendBtn.setBackground(icon);

            binding.title.setTextColor(m_context.getResources().getColor(R.color.wy_red));
            binding.subtitle.setTextColor(m_context.getResources().getColor(R.color.wy_red));
        } else {
            icon = ContextCompat.getDrawable(m_context.getApplicationContext(), R.drawable.load_add);
            icon.setColorFilter(m_context.getResources().getColor(R.color.black_gray), PorterDuff.Mode.SRC_IN);
            binding.addBtn.setBackground(icon);

            icon = ContextCompat.getDrawable(m_context.getApplicationContext(), R.drawable.baseline_more_vert_24);
            icon.setColorFilter(m_context.getResources().getColor(R.color.black_gray), PorterDuff.Mode.SRC_IN);
            binding.extendBtn.setBackground(icon);

            binding.title.setTextColor(ContextCompat.getColor(m_context, android.R.color.black));
            binding.subtitle.setTextColor(ContextCompat.getColor(m_context, android.R.color.darker_gray));
        }

    }

    public void mySetItems(List<MediaItem> items) {
        super.setItems(items);
        items_state = new ArrayList<>(Collections.nCopies(items.size(), false));
    }


    public void synBindingState(LocalSongListItemBinding binding, int position) {
        CheckBox checkBox = ((Activity) m_context).findViewById(R.id.all_choice_btn);
        if (checkBox.getVisibility() == View.VISIBLE) {
            binding.checkBox.setVisibility(View.VISIBLE);
            binding.addBtn.setVisibility(View.GONE);
            binding.extendBtn.setVisibility(View.GONE);
            if (checkBox.isChecked() || items_state.get(position))
                binding.checkBox.setChecked(true);
            else
                binding.checkBox.setChecked(false);
        }
    }

    public void setPreBinding(LocalSongListItemBinding binding) {
        this.pre_binding = binding;
    }

    public LocalSongListItemBinding getPreBinding() {
        return pre_binding;
    }

    public void setSelectedItem(MediaItem item) {
        this.selectedItem = item;
    }

}

