package com.lickling.mymusic.ui.local;


import android.app.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.musicBean.MusicBean;


public class SongOperationPopup extends BottomSheetDialogFragment{

    private String singer_name;
    private String singer_info;
    private String song_title;
    private Context context;
    private Integer selectedItem;
    private View view;

    private OnPopupClickListener onPopupClickListener;

    public SongOperationPopup() {

    }

    public SongOperationPopup(Context context) {
        this.context = context;
    }

    public interface OnPopupClickListener {
        void deleteItemClickListener(int position);

        void nextPlayItemClickListener(int position);
    }

    public void setOnPopupClickListener(OnPopupClickListener listener) {
        onPopupClickListener = listener;
    }

    public void setData(MusicBean item, int selectedItem) {
        this.selectedItem = selectedItem;

        this.singer_name = item.getArtist();

        this.song_title = item.getTitle();

        this.singer_info ="歌手："+item.getArtist();
    }

    public SongOperationPopup(Context context, View v, int selectedItem) {
        view = v;
        this.selectedItem = selectedItem;
        TextView tmp = v.findViewById(R.id.title);
        this.singer_name = tmp.getText().toString();

        tmp = v.findViewById(R.id.subtitle);
        this.song_title = tmp.getText().toString();

        tmp=v.findViewById(R.id.singer_info);
        this.singer_info =tmp.getText().toString();
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置背景透明，才能显示出layout中诸如圆角的布局，否则会有白色底（框）
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.loaded_song_op_popup);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loaded_song_op_popup, container, false);

        TextView sn = view.findViewById(R.id.subtitle);
        assert sn != null;
        sn.setText(this.singer_name);

        TextView st = view.findViewById(R.id.title);
        assert st != null;
        st.setText(this.song_title);

        TextView si = view.findViewById(R.id.singer_info);
        assert si != null;
        si.setText(this.singer_info);

        Button next_play_btn = view.findViewById(R.id.next_play_btn);
        Button delete_btn = view.findViewById(R.id.delete_btn);


        next_play_btn.setOnClickListener(v -> onPopupClickListener.nextPlayItemClickListener(selectedItem));
        delete_btn.setOnClickListener(v -> onPopupClickListener.deleteItemClickListener(selectedItem));
//        //下一曲播放
//        next_play_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "已添加到下一曲播放", Toast.LENGTH_SHORT).show();
//                dismiss();
//            }
//        });
//
//
//        //删除
//        delete_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedItem != -1) {
//                    deleteItemListener.onDeleteItem(selectedItem);
//                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
//                }
//                dismiss();
//            }
//        });

        return view;
    }
}

