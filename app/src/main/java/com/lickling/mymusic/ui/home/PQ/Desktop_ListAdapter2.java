package com.lickling.mymusic.ui.home.PQ;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;


import java.text.BreakIterator;
import java.util.List;

public class Desktop_ListAdapter2 extends RecyclerView.Adapter<Desktop_ListAdapter2.ViewHolder> {

    private List<Desktop_ListItem2> Desktop_ListItem2;
    private Context context;

    public Desktop_ListAdapter2(List<Desktop_ListItem2> Desktop_ListItem2, Context context) {
        this.Desktop_ListItem2 = Desktop_ListItem2;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.community_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Desktop_ListItem2 listItem = Desktop_ListItem2.get(position);
        holder.title.setText(listItem.getTitle());
        holder.condition.setText(listItem.getCondition());
    }

    @Override
    public int getItemCount() {
        return Desktop_ListItem2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView title;
        public TextView condition;
        public ImageView pq_head_photo;
        public ImageView pq_watch_count;
        public ImageView pq_collect_count;
        public ImageView pq_comment_count;
        public ImageView pq_upvote_count;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            condition = itemView.findViewById(R.id.condition);
            pq_head_photo = itemView.findViewById(R.id.pq_head_photo);
            pq_watch_count = itemView.findViewById(R.id.pq_watch_count);
            pq_collect_count = itemView.findViewById(R.id.pq_collect_count);
            pq_comment_count = itemView.findViewById(R.id.pq_comment_count);
            pq_upvote_count = itemView.findViewById(R.id.pq_upvote_count);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }



        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}

