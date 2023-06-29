package com.lickling.mymusic.ui.home.PQ;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;


import java.util.List;

public class Desktop_ListAdapter extends RecyclerView.Adapter<Desktop_ListAdapter.ViewHolder> {

    private List<Desktop_Listltem> Desktop_Listltem;
    private Context context;

    public Desktop_ListAdapter(List<Desktop_Listltem> Desktop_Listltem, Context context) {
        this.Desktop_Listltem = Desktop_Listltem;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.desktop_song_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Desktop_Listltem listItem = Desktop_Listltem.get(position);
        holder.title.setText(listItem.getTitle());
        holder.subtitle.setText(listItem.getSubtitle());
    }

    @Override
    public int getItemCount() {
        return Desktop_Listltem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView title;
        public TextView subtitle;

        public View album_cover;

        public Button extend_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            album_cover = itemView.findViewById(R.id.album_cover);
            extend_btn = itemView.findViewById(R.id.extend_btn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            context.startActivity(new Intent(context, Desktop_Seek.class));
        }



        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}

