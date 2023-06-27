package com.lickling.mymusic.ui.load;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;

import java.util.List;

public class ListAdapter2 extends RecyclerView.Adapter<ListAdapter2.ViewHolder>{

    private List<ListItem> listItems;
    private Context context;
    private SongOperationPopup2 songOperationPopup2;

    private OnCheckItemListener2 checkItemListener2;

    private int selectedItem = -1;

    public ListAdapter2(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public interface OnCheckItemListener2 {
        void onCheckItem2(int position ,boolean tag);
    }

    public void setOnCheckItemListener2(OnCheckItemListener2 listener) {
        checkItemListener2 = listener;
    }
    public void setDialog(SongOperationPopup2 dialog) {
        this.songOperationPopup2 = dialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loading_song_list_item, parent, false);
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
                songOperationPopup2.setData(listItem,position);//把点击的item的数据给dialog
                songOperationPopup2.show(((AppCompatActivity) context).getSupportFragmentManager(), "menu");
            }
        });


        holder.suspend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.check_box.isChecked())
                    checkItemListener2.onCheckItem2(position,true);
                else
                    checkItemListener2.onCheckItem2(position,false);
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
        public Button extend_btn;
        public Button suspend_btn;
        public CheckBox check_box;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            album_cover = itemView.findViewById(R.id.album_cover);
            extend_btn = itemView.findViewById(R.id.extend_btn);
            suspend_btn = itemView.findViewById(R.id.suspend_btn);
            check_box = itemView.findViewById(R.id.check_box);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

        }
    }

}
