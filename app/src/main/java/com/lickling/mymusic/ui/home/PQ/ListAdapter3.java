package com.lickling.mymusic.ui.home.PQ;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.ui.load.ListAdapter2;
import com.lickling.mymusic.ui.load.SongOperationPopup2;

import java.util.List;

public class ListAdapter3 extends RecyclerView.Adapter<ListAdapter3.ViewHolder>{


    private List<ListItem3> listItems3;
    private Context context;
    private SongOperationPopup2 songOperationPopup2;

    private ListAdapter2.OnCheckItemListener2 checkItemListener2;

    private int selectedItem = -1;

    public ListAdapter3(List<ListItem3> listItems3, Context context) {
        this.listItems3 = listItems3;
        this.context = context;

    }

    public interface OnCheckItemListener3 {

    }

    public void setOnCheckItemListener3(ListAdapter3.OnCheckItemListener3 listener) {
        OnCheckItemListener3 checkItemListener3 = listener;
    }


    @NonNull
    @Override
    public ListAdapter3.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_collection_list, parent, false);
        return new ListAdapter3.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListItem3 listItem = listItems3.get(position);
        holder.title.setText(listItem.getTitle());
        holder.subtitle.setText(listItem.getSubtitle());
        holder.subtitle2.setText(listItem.getSubtitle2());
        holder.subtitle3.setText(listItem.getSubtitle3());



    }




    @Override
    public int getItemCount() {
        return listItems3.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;

        public TextView subtitle;
        public TextView subtitle2;
        public TextView subtitle3;

        public View album_cover;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            subtitle2 = itemView.findViewById(R.id.subtitle2);
            subtitle3 = itemView.findViewById(R.id.subtitle3);
            album_cover = itemView.findViewById(R.id.album_cover);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

        }
    }



}
