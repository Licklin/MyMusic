package com.lickling.mymusic.ui.setting.api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.APIListItem;
import com.lickling.mymusic.bean.SettingInfo;
import com.lickling.mymusic.model.MainModel;
import com.lickling.mymusic.utilty.ImmersiveStatusBarUtil;
import com.orm.SugarContext;

import java.util.List;

public class APIActivity extends AppCompatActivity implements MenuDialogFragment.OnDeleteItemListener,ListAdapter.onItemClick{

    private static final String EDIT_CODE = "1";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private MenuDialogFragment dialog;
    private SettingInfo settingInfo;
    private MainModel mainModel;

    private List<APIListItem> APIListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        ImmersiveStatusBarUtil.transparentBar(this, false);

        // 获取 SharedPreferences 对象
        SharedPreferences prefs = getSharedPreferences("userId", Context.MODE_PRIVATE);

        long saveKeyOfUser = prefs.getLong("saveKeyOfUser", -1);
        long saveKeyOfSetting = prefs.getLong("saveKeyOfSetting", -1);


        SugarContext.init(this);

        mainModel = new MainModel(saveKeyOfUser, saveKeyOfSetting);


        settingInfo = mainModel.getSettingInfo();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("saveKeyOfUser", mainModel.getUserSaveID());
        editor.putLong("saveKeyOfSetting", mainModel.getSettingInfoSaveID());
        editor.apply();

        APIListItems = APIListItem.listAll(APIListItem.class);
        if (APIListItems == null) {
            Log.e("APIListItems", "null");
            APIListItem item = new APIListItem("腾讯云", "https://service-hrf5csss-1318703950.gz.apigw.tencentcs.com/release/");
            item.save();
            APIListItems = APIListItem.listAll(APIListItem.class);
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
                intent.putExtra("isAdd", true);
                startActivityForResult(intent, 1);
                return true;
            }
        });

        // api列表
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FragmentManager manager = getSupportFragmentManager();

        listAdapter = new ListAdapter(APIListItems, this);

        dialog = new MenuDialogFragment(this);
        dialog.setOnDeleteItemListener(this);
        listAdapter.setDialog(dialog);
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {

            Bundle dataBundle = data.getBundleExtra("result");
            if (dataBundle.getString("mode").equals(EDIT_CODE)) {
                String name = dataBundle.getString("resultName");
                String URL = dataBundle.getString("resultURL");
                Integer num = dataBundle.getInt("resultNum");
                APIListItems.get(num).setTitle(name);
                APIListItems.get(num).setSubtitle(URL);
                APIListItems.get(num).save();
            } else {
                String name = dataBundle.getString("resultName");
                String URL = dataBundle.getString("resultURL");
                APIListItem item = new APIListItem(name, URL);
                item.save();
                APIListItems.add(item);
            }
            listAdapter = new ListAdapter(APIListItems, this);
            dialog.setOnDeleteItemListener(this);
            listAdapter.setDialog(dialog);
            recyclerView.setAdapter(listAdapter);
        }

    }

    @Override
    public void onDeleteItem(int position) {//删除list的item
        APIListItem item = APIListItem.findById(APIListItem.class, APIListItems.get(position).getId());
        if (item == null) Log.e(" API onDeleteItem", "item  is null");
        else
            item.delete();
        APIListItems.remove(position);
        listAdapter.notifyItemRemoved(position);
        listAdapter.notifyItemRangeChanged(position, listAdapter.getItemCount() - position);
        recyclerView.setAdapter(listAdapter);
    }


    @Override
    public void saveSelectId(int selectedItem) {

    }
}