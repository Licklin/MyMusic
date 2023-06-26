package com.lickling.mymusic.ui.load;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lickling.mymusic.R;

public class MulOperationPopup extends PopupWindow {
    private Context context;
    private Integer selectedItem;
    private View view;

    Button next_play_btn;
    Button add_btn;
    Button delete_btn;

    private OnDeleteMulItemListener deleteMulItemListener;

    public MulOperationPopup(Context context) {
        this.context = context;
        init();
    }

    public interface OnDeleteMulItemListener {
        void onDeleteMulItem();
    }

    public void setOnDeleteMulItemListener(OnDeleteMulItemListener listener) {
        deleteMulItemListener = listener;
    }

    private void init() {
        view = LayoutInflater.from(context).inflate(R.layout.loaded_mul_popup, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        next_play_btn = view.findViewById(R.id.next_play_btn);
        add_btn = view.findViewById(R.id.add_btn);
        delete_btn = view.findViewById(R.id.delete_btn);

        next_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMulItemListener.onDeleteMulItem();
                dismiss();
            }
        });
    }

    public void show(View anchorView) {
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        showAtLocation(anchorView, Gravity.BOTTOM, location[0], 0);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
