package com.lickling.mymusic.utilty;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class MyGlide {

    //context上下文，url图片地址，展示图片的imageView
    public int setPicture(Context context, String url, View imageView) {
        Glide.with(context)
                .load(url)
                .into((ImageView) imageView);
        return 0;

    }
}
