package com.lickling.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.musicBean.MusicBean;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Context context;
    private List<MusicBean> playMusicBeanList;
    private int selectedItem = -1;
    private ClickListener clickListener;
    public interface ClickListener{
        void ItemClickListener(View v,int positon);
        void ItemViewClickListener(View v,int positon);


    }

    public MainAdapter(List<MusicBean> listItems, Context context) {
        this.playMusicBeanList = listItems;
        this.context = context;
    }
    @Override
    public int getItemCount() {
        return playMusicBeanList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_api_list_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicBean listItem = playMusicBeanList.get(position);
        holder.title.setText(listItem.getTitle());
        holder.subtitle.setText(listItem.getSubtitle());
        holder.customIcon.setVisibility(position == selectedItem ? View.VISIBLE : View.GONE);


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView title;
        public TextView subtitle;
        public ImageView customIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            customIcon = itemView.findViewById(R.id.custom_icon);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int previousSelected = selectedItem;
            selectedItem = getAdapterPosition();
            if (previousSelected != -1) {
                notifyItemChanged(previousSelected);
            }
            notifyItemChanged(selectedItem);
        }

        @Override
        public boolean onLongClick(View v) {
            // 跳转到编辑页面
            // Intent intent = new Intent(context, EditActivity.class);
            // intent.putExtra("item", listItems.get(getAdapterPosition()));
            // context.startActivity(intent);
//            selectedItem = getAdapterPosition();

//            showMenu(v,getAdapterPosition());  // 长按列表项时触发弹出菜单
            return true;
        }


    }

}
