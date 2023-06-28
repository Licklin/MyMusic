package com.lickling.mymusic.ui.setting.api;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.APIListItem;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<APIListItem> APIListItems;
    private Context context;
    private int selectedItem = -1;
    private MenuDialogFragment dialogFragment;
    private  OnItemClickListener save;

    public interface OnItemClickListener {
        void saveSelectId(int selectedItem);
    }
    
    public void setItemClickListener(OnItemClickListener listener){
        save = listener;
    }

    public ListAdapter(List<APIListItem> APIListItems, Context context) {
        this.APIListItems = APIListItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_api_list_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);

        return holder;
    }

    public void setDialog(MenuDialogFragment dialog) {
        dialogFragment = dialog;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        APIListItem APIListItem = APIListItems.get(position);
        holder.title.setText(APIListItem.getTitle());
        holder.subtitle.setText(APIListItem.getSubtitle());
        holder.customIcon.setVisibility(position == selectedItem ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return APIListItems.size();
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
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
            save.saveSelectId(selectedItem);
            notifyItemChanged(selectedItem);
        }

        @Override
        public boolean onLongClick(View v) {
            showMenu(v, getAdapterPosition());  // 长按列表项时触发弹出菜单
            return true;
        }
    }

    private void showMenu(View v, int selectedItem) {
        dialogFragment.setData(v, selectedItem);//把长按的item的数据给dialog
        dialogFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "menu");
    }

}