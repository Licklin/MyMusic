package com.lickling.mymusic.ui.setting.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.lickling.mymusic.R;
import com.lickling.mymusic.ui.setting.home.presenter.IPresenter;
import com.lickling.mymusic.ui.setting.home.presenter.MainPresenter;
import com.lickling.mymusic.ui.setting.password.view.PassWordActivity;

public class HomeActivity extends AppCompatActivity implements MainView {

    private IPresenter mainPresenter;//V层拥有P层

    private Button passWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_setting);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        mainPresenter = new MainPresenter(this);//V层交给P层
        passWord = findViewById(R.id.change_password);
        setClick();
    }
    void setClick(){
        passWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.goToPassWord();
            }
        });
    }
    @Override
    public void goToPassWord() {
        startActivity(new Intent(this, PassWordActivity.class));
    }
}