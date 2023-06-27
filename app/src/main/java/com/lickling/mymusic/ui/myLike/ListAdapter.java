package com.lickling.mymusic.ui.myLike;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.ui.load.ListItem;



import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    private SongOperationPopup songOperationPopup;

    private int selectedItem = -1;

    private ViewHolder pre_holder;
    private OnCheckItemListener checkItemListener;

    public ListAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public interface OnCheckItemListener {
        void onCheckItem(int position ,boolean tag);
    }
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.load_song_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListItem listItem = listItems.get(position);
        holder.title.setText(listItem.getTitle());
        holder.subtitle.setText(listItem.getSubtitle());
        holder.extend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songOperationPopup.setData(listItem,position);//把点击的item的数据给dialog
                songOperationPopup.show(((AppCompatActivity) context).getSupportFragmentManager(), "menu");
            }
        });

        holder.add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.play_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                Drawable icon;
                if(pre_holder!=null&&pre_holder!=holder){
                    icon = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.load_add);
                    icon.setColorFilter(context.getResources().getColor(R.color.black_gray), PorterDuff.Mode.SRC_IN);
                    pre_holder.add_btn.setBackground(icon);

                    icon = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.baseline_more_vert_24);
                    icon.setColorFilter(context.getResources().getColor(R.color.black_gray), PorterDuff.Mode.SRC_IN);
                    pre_holder.extend_btn.setBackground(icon);

                    pre_holder.title.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                    pre_holder.subtitle.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
                }

                icon = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.load_add);
                icon.setColorFilter(context.getResources().getColor(R.color.wy_red), PorterDuff.Mode.SRC_IN);
                holder.add_btn.setBackground(icon);

                icon = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.baseline_more_vert_24);
                icon.setColorFilter(context.getResources().getColor(R.color.wy_red), PorterDuff.Mode.SRC_IN);
                holder.extend_btn.setBackground(icon);

                holder.title.setTextColor(context.getResources().getColor(R.color.wy_red));
                holder.subtitle.setTextColor(context.getResources().getColor(R.color.wy_red));
                pre_holder = holder;
            }
        });
        holder.check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.check_box.isChecked())
                    checkItemListener.onCheckItem(position,true);
                else
                    checkItemListener.onCheckItem(position,false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView subtitle;

        public View album_cover;

        public Button add_btn;

        public Button extend_btn;

        public Button play_btn;

        public CheckBox check_box;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            album_cover = itemView.findViewById(R.id.album_cover);
            add_btn = itemView.findViewById(R.id.add_btn);
            extend_btn = itemView.findViewById(R.id.extend_btn);
            play_btn = itemView.findViewById(R.id.play_btn);
            check_box = itemView.findViewById(R.id.check_box);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

        }
    }


}

