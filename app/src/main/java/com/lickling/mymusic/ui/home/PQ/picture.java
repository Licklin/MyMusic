package com.lickling.mymusic.ui.home.PQ;

import static java.security.AccessController.getContext;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.lickling.mymusic.R;

public class picture extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_slide);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

//        //---------------------------------
//        viewFlipper.findViewById(R.id.viewFlipper);
//        viewFlipper.removeAllViews();
//        View view = View.inflate(getContext(), R.layout.picture_slide_list, null);
//        MellowImageView carouselImageView=view.findViewById(R.id.like_icon2);
//        View view1 = View.inflate(getContext(), R.layout.picture_slide_list, null);
//        MellowImageView carouselImageView1=view.findViewById(R.id.like_icon2);
//
//        // 循环滚动图片的点击事件
//        // iv.setOnClickListener(new ....);
//        //添加view
//        viewFlipper.addView(view);
//        viewFlipper.addView(view1);


    }
}
