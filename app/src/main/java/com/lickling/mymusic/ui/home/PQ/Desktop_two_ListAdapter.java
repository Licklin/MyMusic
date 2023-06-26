//package com.lickling.mymusic.ui.home.PQ;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.lickling.mymusic.R;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Desktop_two_ListAdapter extends RecyclerView.Adapter<Desktop_two_ListAdapter.ViewHolder> {
//
//    private List<Desktop_two_Listltem> two_listltems;
//    private Context context;
//
//    public Desktop_two_ListAdapter(List<Desktop_two_Listltem> two_listltems, Context context) {
//        this.two_listltems = two_listltems;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public Desktop_two_ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.community_list, parent, false);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull Desktop_two_ListAdapter.ViewHolder holder, int position) {
//        Desktop_two_Listltem listltem = Desktop_two_Listltem.
//        Desktop_two_Listltem listItem = Desktop_two_Listltem.get(position);
//        holder.nickname.setText(Desktop_two_Listltem.());
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//
//        }
//
//        @Override
//        public void onClick(View v) {
//
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            return false;
//        }
//    }
//}
