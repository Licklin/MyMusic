package com.lickling.mymusic.ui.setting.api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.APIListItem;

public class EditActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private boolean isAdd = false;
    private Intent intent;
    private EditText apiName;
    private EditText URL;
    private APIListItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_edit);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        toolbar = findViewById(R.id.setting_navigation_api_edit);
        apiName = findViewById(R.id.api_name);
        URL = findViewById(R.id.api_url);

        intent = getIntent();
        apiName.setText(intent.getStringExtra("Title"));
        URL.setText(intent.getStringExtra("Url"));
        isAdd = intent.getBooleanExtra("isAdd", false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 在此处添加菜单项（添加按钮，保存按钮）到Toolbar上
        getMenuInflater().inflate(R.menu.setting_toolbar_menu_tick, toolbar.getMenu());
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (isAdd) {
                    Intent backIntent = new Intent();
                    Bundle addData = new Bundle();
                    addData.putString("mode", "0");
//                    addData.putInt("mode",0);
                    addData.putString("resultName", apiName.getText().toString());
                    addData.putString("resultURL", URL.getText().toString());
                    backIntent.putExtra("result", addData);
                    setResult(RESULT_OK, backIntent);

                    Toast.makeText(EditActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

                } else {
                    Intent backIntent = new Intent();
                    Bundle addData = new Bundle();
                    addData.putString("mode", "1");
                    addData.putString("resultName", apiName.getText().toString());
                    addData.putString("resultURL", URL.getText().toString());
                    addData.putInt("resultNum", intent.getIntExtra("Num",0));
                    backIntent.putExtra("result", addData);

                    setResult(RESULT_OK, backIntent);
                    Toast.makeText(EditActivity.this, "编辑成功", Toast.LENGTH_SHORT).show();
                }
                finish();

                return true;
            }
        });

    }
}