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

    public interface OnCheckItemListener2 {
        void onCheckItem2(int position ,boolean tag);
    }

    public void setOnCheckItemListener2(ListAdapter2.OnCheckItemListener2 listener) {
        checkItemListener2 = listener;
    }
    public void setDialog(SongOperationPopup2 dialog) {
        this.songOperationPopup2 = dialog;
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


        holder.suspend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


//    @Override
//    public void onBindViewHolder(@NonNull ListAdapter2.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        ListItem3 listItem = listItems3.get(position);
//        holder.title.setText(listItem.getTitle());
//        holder.subtitle.setText(listItem.getSubtitle());
//        holder.subtitle2.setText(listItem.getSubtitle2());
//        holder.subtitle3.setText(listItem.getSubtitle3());
//        holder.extend_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                songOperationPopup2.setData(listItem,position);//把点击的item的数据给dialog
//                songOperationPopup2.show(((AppCompatActivity) context).getSupportFragmentManager(), "menu");
//            }
//        });
//
//
//        holder.suspend_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        holder.check_box.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (holder.check_box.isChecked())
//                    checkItemListener2.onCheckItem2(position,true);
//                else
//                    checkItemListener2.onCheckItem2(position,false);
//            }
//        });
//    }

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

        public Button suspend_btn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            subtitle2 = itemView.findViewById(R.id.subtitle2);
            subtitle3 = itemView.findViewById(R.id.subtitle3);
            album_cover = itemView.findViewById(R.id.album_cover);
            suspend_btn = itemView.findViewById(R.id.suspend_btn);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

        }
    }



}
