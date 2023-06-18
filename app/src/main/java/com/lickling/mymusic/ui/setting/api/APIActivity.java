package com.lickling.mymusic.ui.setting.api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lickling.mymusic.R;
import com.lickling.mymusic.ui.setting.home.SettingHomeActivity;
import com.lickling.mymusic.ui.setting.sound_quality.SoundQualityActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class APIActivity extends AppCompatActivity {

    private static final String EDIT_CODE = "1";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        toolbar = findViewById(R.id.setting_navigation_api);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 在此处添加菜单项的添加按钮到Toolbar上
        getMenuInflater().inflate(R.menu.setting_toolbar_menu, toolbar.getMenu());
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(APIActivity.this, EditActivity.class);
                intent.putExtra("Title", "");
                intent.putExtra("URL", "");
                intent.putExtra("isAdd",true);
                startActivity(intent);
                return true;
            }
        });

        // api列表
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        // 在此处添加数据到listItems
        listItems.add(new ListItem("腾讯云", "https://service-hrf5csss-1318703950.gz.apigw.tencentcs.com/release/"));
        listItems.add(new ListItem("Title 2", "Subtitle 2"));
        listItems.add(new ListItem("Title 3", "Subtitle 3"));
        FragmentManager manager = getSupportFragmentManager();
        listAdapter = new ListAdapter(listItems, this);
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {

            Bundle dataBundle = data.getBundleExtra("result");
            if (dataBundle.getString("mode").equals(EDIT_CODE))
            {
                String name = dataBundle.getString("resultName");
                String URL = dataBundle.getString("resultURL");
                Integer num = dataBundle.getInt("resultNum",0);
                Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
                listItems.get(num).setTitle(name);
                listItems.get(num).setSubtitle(URL);
            }
            else {
                String name = dataBundle.getString("resultName");
                String URL = dataBundle.getString("resultURL");
                Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
                listItems.add(new ListItem(name, URL));

            }

            listAdapter = new ListAdapter(listItems, this);
            recyclerView.setAdapter(listAdapter);

        }


    }
}