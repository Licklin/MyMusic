package com.lickling.mymusic.ui.load;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;


import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    private SongOperationPopup dialog;

    private int selectedItem = -1;

    public ListAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public void setDialog(SongOperationPopup dialog) {
        this.dialog = dialog;
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
                dialog.setData(listItem,position);//把点击的item的数据给dialog
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "menu");
            }
        });

        holder.add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            album_cover = itemView.findViewById(R.id.album_cover);
            add_btn = itemView.findViewById(R.id.add_btn);
            extend_btn = itemView.findViewById(R.id.extend_btn);
            play_btn = itemView.findViewById(R.id.play_btn);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

        }
    }


}
