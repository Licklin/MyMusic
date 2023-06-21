package com.lickling.mymusic.ui.home.nsh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.lickling.mymusic.R;

public class LoginWangyi extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginwangyi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        toolbar = findViewById(R.id.wangyi);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView account = findViewById(R.id.account);
        EditText code = findViewById(R.id.code);

        account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 如果 oldPassWordEdit 获得了焦点，则将下划线颜色设置为红色
                if (hasFocus) {
                    account.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.wy_red), PorterDuff.Mode.SRC_IN);
                }
                // 如果 oldPassWordEdit 失去了焦点，则将下划线颜色恢复默认颜色
                else {
                    account.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hint_text), PorterDuff.Mode.SRC_IN);
                }
            }
        });

        code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 如果 newPassWordEdit1 获得了焦点，则将下划线颜色设置为红色
                if (hasFocus) {
                    code.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.wy_red), PorterDuff.Mode.SRC_IN);
                }
                // 如果 newPassWordEdit1 失去了焦点，则将下划线颜色恢复默认颜色
                else {
                    code.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hint_text), PorterDuff.Mode.SRC_IN);
                }
            }
        });


//注册
        TextView register = findViewById(R.id.register);
        String text1 = register.getText().toString();
        SpannableString spannableString1 = new SpannableString(text1);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 点击时设置文字下划线和背景色
                spannableString1.setSpan(new UnderlineSpan(), 0, spannableString1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString1.setSpan(new BackgroundColorSpan(getColor(R.color.tianlan)), 0, spannableString1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString1.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                register.setText(spannableString1);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                // 设置文字颜色和下划线颜色
                super.updateDrawState(ds);
                ds.setColor(Color.GRAY);
                ds.setUnderlineText(false);
            }
        };

// 将 clickableSpan 应用到 spannableString 中的指定位置
        int start = text1.indexOf(register.getText().toString());
        int end = start + register.getText().toString().length();
        spannableString1.setSpan(clickableSpan1, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// 将 spannableString 应用到 TextView 控件中
        register.setText(spannableString1);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        register.setHighlightColor(Color.TRANSPARENT);

//忘记密码
        TextView forgetcode = findViewById(R.id.forgetcode);
        String text2 = forgetcode.getText().toString();
        SpannableString spannableString2 = new SpannableString(text2);

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View widget) {
                // 点击时设置文字下划线和背景色
                spannableString2.setSpan(new UnderlineSpan(), 0, spannableString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString2.setSpan(new BackgroundColorSpan(getColor(R.color.tianlan)), 0, spannableString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString2.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                forgetcode.setText(spannableString2);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                // 设置文字颜色和下划线颜色
                super.updateDrawState(ds);
                ds.setColor(Color.RED);
                ds.setUnderlineText(false);
            }
        };

// 将 clickableSpan 应用到 spannableString 中的指定位置
        int start2 = text2.indexOf(forgetcode.getText().toString());
        int end2 = start2 + forgetcode.getText().toString().length() ;
        spannableString2.setSpan(clickableSpan2, start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// 将 spannableString 应用到 TextView 控件中
        forgetcode.setText(spannableString2);
        forgetcode.setMovementMethod(LinkMovementMethod.getInstance());
        forgetcode.setHighlightColor(getColor(R.color.tianlan));
    }
}