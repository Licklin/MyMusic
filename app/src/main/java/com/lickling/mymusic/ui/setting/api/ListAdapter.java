package com.lickling.mymusic.ui.setting.api;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
    private int selectedItem = -1;

    public ListAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
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

    private void showMenu(View v,Integer selectedItem) {

        MenuDialogFragment dialog = new MenuDialogFragment(context,v,selectedItem);
        dialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "menu");
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.title.setText(listItem.getTitle());
        holder.subtitle.setText(listItem.getSubtitle());
        holder.customIcon.setVisibility(position == selectedItem ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
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

            showMenu(v,getAdapterPosition());  // 长按列表项时触发弹出菜单

            return true;
        }
    }
}