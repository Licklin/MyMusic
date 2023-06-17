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

    private View onlineTick1;
    private View onlineTick2;
    private View onlineTick3;
    private View downloadTick1;
    private View downloadTick2;
    private View downloadTick3;
    private Integer dpi;

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
        onlineTick1 = findViewById(R.id.online_tick1);
        onlineTick2 = findViewById(R.id.online_tick2);
        onlineTick3 = findViewById(R.id.online_tick3);
        downloadTick1 = findViewById(R.id.download_tick1);
        downloadTick2 = findViewById(R.id.download_tick2);
        downloadTick3 = findViewById(R.id.download_tick3);


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
//                        onlineTick.setY(66*dpi);
                        onlineTick1.setVisibility(View.VISIBLE);
                        onlineTick2.setVisibility(View.INVISIBLE);
                        onlineTick3.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.online_high:
                        onlineTick1.setVisibility(View.INVISIBLE);
                        onlineTick2.setVisibility(View.VISIBLE);
                        onlineTick3.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.online_hifi:
                        onlineTick1.setVisibility(View.INVISIBLE);
                        onlineTick2.setVisibility(View.INVISIBLE);
                        onlineTick3.setVisibility(View.VISIBLE);
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
//                        downloadTick1.setY(66*dpi);
                        downloadTick1.setVisibility(View.VISIBLE);
                        downloadTick2.setVisibility(View.INVISIBLE);
                        downloadTick3.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.download_high:
                        downloadTick1.setVisibility(View.INVISIBLE);
                        downloadTick2.setVisibility(View.VISIBLE);
                        downloadTick3.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.download_hifi:
                        downloadTick1.setVisibility(View.INVISIBLE);
                        downloadTick2.setVisibility(View.INVISIBLE);
                        downloadTick3.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });

    }
}