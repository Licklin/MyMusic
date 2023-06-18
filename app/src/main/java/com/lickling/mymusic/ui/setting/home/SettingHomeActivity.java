package com.lickling.mymusic.ui.setting.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lickling.mymusic.R;
import com.lickling.mymusic.ui.setting.api.APIActivity;
import com.lickling.mymusic.ui.setting.home.presenter.IPresenter;
import com.lickling.mymusic.ui.setting.home.presenter.MainPresenter;
import com.lickling.mymusic.ui.setting.notice.NoticeActivity;
import com.lickling.mymusic.ui.setting.password.PassWordActivity;
import com.lickling.mymusic.ui.setting.sound_quality.SoundQualityActivity;

public class SettingHomeActivity extends AppCompatActivity implements MainView {

    private IPresenter mainPresenter;//V层拥有P层

    private Button passWord;
    private Button notice;
    private Button soundQuality;
    private Button logOut;
    private Button accountCancellation;
    private Button cacheLimit;
    private Button clearCache;
    private Button api;
    private Button versionBtn;
    private TextView cacheSize;
    private TextView version;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_setting);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        mainPresenter = new MainPresenter(this); // V层交给P层
        initFindView();
        toolbarBack();
        setClick();


    }

    private void initFindView() {
        passWord = findViewById(R.id.change_password);
        notice = findViewById(R.id.notice);
        soundQuality = findViewById(R.id.sound_quality);
        logOut = findViewById(R.id.log_out);
        accountCancellation = findViewById(R.id.account_cancellation);
        cacheLimit = findViewById(R.id.cache_limit);
        clearCache = findViewById(R.id.clear_cache);
        api = findViewById(R.id.api);
        versionBtn = findViewById(R.id.version_btn);
        cacheSize = findViewById(R.id.cache_size);
        version = findViewById(R.id.version);
    }

    void toolbarBack() {
        toolbar = findViewById(R.id.setting_navigation);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    void setClick() {
        passWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.goToPassWord();
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingHomeActivity.this, NoticeActivity.class));
            }
        });
        soundQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingHomeActivity.this, SoundQualityActivity.class));
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        accountCancellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cacheLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingHomeActivity.this, APIActivity.class));
            }
        });
        versionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public void goToPassWord() {
        startActivity(new Intent(this, PassWordActivity.class));
    }

    @Override
    public void showTip(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialogBox() {

    }

    @Override
    public void upgradeCacheSize(Integer size) {
        cacheSize.setText(size + "GB");
    }

    @Override
    public void upgradeVersion(String version) {

    }
}