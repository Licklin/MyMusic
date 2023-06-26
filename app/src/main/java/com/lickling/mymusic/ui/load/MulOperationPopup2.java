package com.lickling.mymusic.ui.load;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.lickling.mymusic.R;

public class MulOperationPopup2 extends PopupWindow {
    private Context context;
    private Integer selectedItem;
    private View view;

    Button cancel_load_btn;
    private OnDeleteMulItemListener2 deleteMulItemListener2;

    public MulOperationPopup2(Context context) {
        this.context = context;
        init();
    }

    public interface OnDeleteMulItemListener2 {
        void onDeleteMulItem2();
    }

    public void setOnDeleteMulItemListener2(OnDeleteMulItemListener2 listener) {
        deleteMulItemListener2 = listener;
    }

    private void init() {
        view = LayoutInflater.from(context).inflate(R.layout.loading_mul_popup, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        cancel_load_btn = view.findViewById(R.id.cancel_load_btn);

        cancel_load_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMulItemListener2.onDeleteMulItem2();
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
