package com.lickling.mymusic.ui.setting.api;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.lickling.mymusic.ui.setting.home.SettingHomeActivity;
import com.lickling.mymusic.ui.setting.sound_quality.SoundQualityActivity;

public class MenuDialogFragment extends BottomSheetDialogFragment {

    private String titleString;
    private String urlString;
    private Context context;
    private Integer selectedItem;
    private View view;

    private OnDeleteItemListener deleteItemListener;

    public MenuDialogFragment() {

    }

    public MenuDialogFragment(Context context) {
        this.context = context;
    }

    public interface OnDeleteItemListener {
        void onDeleteItem(int position);
    }


    public void setOnDeleteItemListener(OnDeleteItemListener listener) {
        deleteItemListener = listener;
    }

    public void setData(View v,int selectedItem) {
        view = v;
        this.selectedItem = selectedItem;
        TextView tmp = v.findViewById(R.id.title);
        this.titleString = tmp.getText().toString();

        tmp = v.findViewById(R.id.subtitle);
        this.urlString = tmp.getText().toString();
    }

    public MenuDialogFragment(Context context, View v, int selectedItem) {
        view = v;
        this.selectedItem = selectedItem;
        TextView tmp = v.findViewById(R.id.title);
        this.titleString = tmp.getText().toString();

        tmp = v.findViewById(R.id.subtitle);
        this.urlString = tmp.getText().toString();
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
        dialog.setContentView(R.layout.menu_layout);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_layout, container, false);

        TextView title = view.findViewById(R.id.title);
        assert title != null;
        title.setText(this.titleString);
        TextView url = view.findViewById(R.id.subtitle);
        assert url != null;
        url.setText(this.urlString);

        Button edit = view.findViewById(R.id.edit_btn);
        Button copy = view.findViewById(R.id.copy_btn);
        Button delete = view.findViewById(R.id.delete_btn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("Num", selectedItem);
                intent.putExtra("Title", titleString);
                intent.putExtra("Url", urlString);
                startActivityForResult(intent, 1);
                dismiss();
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("url", url.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, "已复制到剪切板", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItem != -1) {
                    deleteItemListener.onDeleteItem(selectedItem);
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
        return view;
    }
}