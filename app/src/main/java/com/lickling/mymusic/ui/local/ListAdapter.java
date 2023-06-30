package com.lickling.mymusic.ui.local;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.util.Log;
import android.view.View;


import androidx.databinding.ObservableArrayList;

import com.lickling.mymusic.R;
import com.lickling.mymusic.adapter.BaseBindingAdapter;
import com.lickling.mymusic.bean.musicBean.MusicBean;
import com.lickling.mymusic.databinding.LocalSongListItemBinding;


import java.util.List;
import java.util.Objects;

public class ListAdapter extends BaseBindingAdapter<MediaItem, LocalSongListItemBinding> {

    private List<ListItem> listItems;
    private Context context;
    private SongOperationPopup songOperationPopup;
    private int selectedItem = -1;

   // private ViewHolder pre_holder;
    private OnCheckItemListener checkItemListener;


    private static final String TAG = "ListAdapter";
    private ObservableArrayList<MediaItem> mSearchMediaItems, mSheetMediaItems;
    private OnItemClickListener mItemClickListener;

    public ListAdapter( Application context) {
        super(context);
    }

    public interface OnItemClickListener{
        void ItemClickListener(ListAdapter adapter, int position);
        void ItemMoreClickListener(View v, int position);
    }
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface OnCheckItemListener {
        void onCheckItem(int position ,boolean tag);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setListItems(List<ListItem> Items){
        listItems = Items;
        notifyDataSetChanged();
    }
    public void setOnCheckItemListener(OnCheckItemListener listener) {
        checkItemListener = listener;
    }
    public void setDialog(SongOperationPopup dialog) {
        this.songOperationPopup = dialog;
    }

//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.local_song_list_item, parent, false);
//        return new ViewHolder(itemView);
//    }

    @Override
    protected int getLayoutResId(int ViewType) {
        return R.layout.local_song_list_item;
    }

    @Override
    protected void onBindItem(LocalSongListItemBinding binding, MediaItem item, int position) {
        int number = position;
        String artist = Objects.requireNonNull(item.getDescription().getSubtitle()).toString(),
                album = Objects.requireNonNull(item.getDescription().getDescription()).toString();
        MusicBean bean = new MusicBean(String.valueOf(++number),
                Objects.requireNonNull(item.getDescription().getTitle()).toString(),
                artist,album,
                Objects.requireNonNull(item.getDescription().getMediaUri()).toString(),
                Objects.requireNonNull(item.getDescription().getMediaUri()).toString(),
                100000);
        binding.setLocalMusicInfo(bean);
        if (mItemClickListener == null) return;
        binding.playBtn.setOnClickListener(v -> mItemClickListener.ItemClickListener(this,position));
        binding.extendBtn.setOnClickListener(v -> mItemClickListener.ItemMoreClickListener(v,position));
        Log.e(TAG,"onBindItem");
    }


    //@Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        ListItem listItem = listItems.get(position);
//        holder.title.setText(listItem.getTitle());
//        holder.subtitle.setText(listItem.getSubtitle());
//        holder.subtitle2.setText(listItem.getSubtitle2());
//        holder.subtitle3.setText(listItem.getSubtitle3());
//
//        holder.extend_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                songOperationPopup.setData(listItem,position);//把点击的item的数据给dialog
//                songOperationPopup.show(((AppCompatActivity) context).getSupportFragmentManager(), "menu");
//            }
//        });
//
//        holder.add_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        holder.play_btn.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View view) {
//                Drawable icon;
//                if(pre_holder!=null&&pre_holder!=holder){
//                    icon = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.load_add);
//                    icon.setColorFilter(context.getResources().getColor(R.color.black_gray), PorterDuff.Mode.SRC_IN);
//                    pre_holder.add_btn.setBackground(icon);
//
//                    icon = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.baseline_more_vert_24);
//                    icon.setColorFilter(context.getResources().getColor(R.color.black_gray), PorterDuff.Mode.SRC_IN);
//                    pre_holder.extend_btn.setBackground(icon);
//
//                    pre_holder.title.setTextColor(ContextCompat.getColor(context, android.R.color.black));
//                    pre_holder.subtitle.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
//                    pre_holder.subtitle2.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
//                    pre_holder.subtitle3.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
//                }
//
//                icon = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.load_add);
//                icon.setColorFilter(context.getResources().getColor(R.color.wy_red), PorterDuff.Mode.SRC_IN);
//                holder.add_btn.setBackground(icon);
//
//                icon = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.baseline_more_vert_24);
//                icon.setColorFilter(context.getResources().getColor(R.color.wy_red), PorterDuff.Mode.SRC_IN);
//                holder.extend_btn.setBackground(icon);
//
//                holder.title.setTextColor(context.getResources().getColor(R.color.wy_red));
//                holder.subtitle.setTextColor(context.getResources().getColor(R.color.wy_red));
//                holder.subtitle2.setTextColor(context.getResources().getColor(R.color.wy_red));
//                holder.subtitle3.setTextColor(context.getResources().getColor(R.color.wy_red));
//                pre_holder = holder;
//            }
//        });
//        holder.check_box.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (holder.check_box.isChecked())
//                    checkItemListener.onCheckItem(position,true);
//                else
//                    checkItemListener.onCheckItem(position,false);
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        if (listItems == null) { // 如果列表对象为空，则返回 0
            return 0;
        }
        return listItems.size();
    }

//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        public TextView title;
//        public TextView subtitle;
//        public TextView subtitle2;
//        public TextView subtitle3;
//
//        public View album_cover;
//
//        public Button add_btn;
//
//        public Button extend_btn;
//
//        public Button play_btn;
//
//        public CheckBox check_box;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            title = itemView.findViewById(R.id.title);
//            subtitle = itemView.findViewById(R.id.subtitle);
//            subtitle2 = itemView.findViewById(R.id.subtitle2);
//            subtitle3 = itemView.findViewById(R.id.subtitle3);
//
//            album_cover = itemView.findViewById(R.id.album_cover);
//            add_btn = itemView.findViewById(R.id.add_btn);
//            extend_btn = itemView.findViewById(R.id.extend_btn);
//            play_btn = itemView.findViewById(R.id.play_btn);
//            check_box = itemView.findViewById(R.id.check_box);
//
//            itemView.setOnClickListener(this);
//        }
//
//
//        @Override
//        public void onClick(View view) {
//
//        }
//    }


    public void release(){
        super.release();
        if (mItemClickListener != null) { mItemClickListener = null; }
        releaseThisMediaItems();
    }
    public void releaseThisMediaItems(){
        if (mSearchMediaItems != null) {
            if (mSearchMediaItems.size() > 0) { mSearchMediaItems.clear(); }
            mSearchMediaItems = null;
        }
        if (mSheetMediaItems != null) {
            if (mSheetMediaItems.size() > 0) { mSheetMediaItems.clear(); }
            mSheetMediaItems = null;
        }
    }

    public void setSheetMediaItems(ObservableArrayList<MediaItem> sheetMediaItems) {
        if (sheetMediaItems == null || sheetMediaItems.size() == 0) return;

        this.mSheetMediaItems = new ObservableArrayList<>();
        this.mSheetMediaItems.addAll(sheetMediaItems);

    }

}

