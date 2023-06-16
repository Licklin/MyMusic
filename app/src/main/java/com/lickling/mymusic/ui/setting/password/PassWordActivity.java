package com.lickling.mymusic.ui.setting.password;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.lickling.mymusic.R;

public class PassWordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_setting_password);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        EditText oldPassWordEdit = findViewById(R.id.old_password);
        EditText newPassWordEdit1 = findViewById(R.id.new_password1);
        EditText newPassWordEdit2 = findViewById(R.id.new_password2);
// 定义焦点变化的监听器
        oldPassWordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 如果 oldPassWordEdit 获得了焦点，则将下划线颜色设置为橙色
                if (hasFocus) {
                    oldPassWordEdit.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.our_orange), PorterDuff.Mode.SRC_IN);
                }
                // 如果 oldPassWordEdit 失去了焦点，则将下划线颜色恢复默认颜色
                else {
                    oldPassWordEdit.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hint_text), PorterDuff.Mode.SRC_IN);
                }
            }
        });
        newPassWordEdit1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 如果 newPassWordEdit1 获得了焦点，则将下划线颜色设置为橙色
                if (hasFocus) {
                    newPassWordEdit1.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.our_orange), PorterDuff.Mode.SRC_IN);
                }
                // 如果 newPassWordEdit1 失去了焦点，则将下划线颜色恢复默认颜色
                else {
                    newPassWordEdit1.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hint_text), PorterDuff.Mode.SRC_IN);
                }
            }
        });
        newPassWordEdit2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 如果 newPassWordEdit2 获得了焦点，则将下划线颜色设置为橙色
                if (hasFocus) {
                    newPassWordEdit2.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.our_orange), PorterDuff.Mode.SRC_IN);
                }
                // 如果 newPassWordEdit2 失去了焦点，则将下划线颜色恢复默认颜色
                else {
                    newPassWordEdit2.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hint_text), PorterDuff.Mode.SRC_IN);
                }
            }
        });
    }
}