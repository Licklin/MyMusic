package com.lickling.mymusic.ui.setting.sound_quality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lickling.mymusic.R;

public class SoundQualityActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RadioGroup onlineRadioGroup;
    private RadioGroup downloadRadioGroup;

    private View onlineTick;
    private View downloadTick;
    private int dpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_sound_quality);
        toolbar = findViewById(R.id.setting_navigation_quality);
        onlineRadioGroup = findViewById(R.id.online_radio_group);
        downloadRadioGroup = findViewById(R.id.download_radio_group);
        onlineTick = findViewById(R.id.online_tick);
        downloadTick = findViewById(R.id.download_tick);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 获取设备DPI
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        dpi = (int) dm.density;

        onlineRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.online_standard:
                        onlineTick.setY(66*dpi);
                        break;
                    case R.id.online_high:
                        onlineTick.setY(112*dpi);
                        break;
                    case R.id.online_hifi:
                        onlineTick.setY(157*dpi);
                        break;
                    default:
                        break;
                }
            }
        });

        downloadRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.download_standard:
                        downloadTick.setY(66*dpi);
                        break;
                    case R.id.download_high:
                        downloadTick.setY(112*dpi);
                        break;
                    case R.id.download_hifi:
                        downloadTick.setY(157*dpi);
                        break;
                    default:
                        break;
                }
            }
        });

    }
}