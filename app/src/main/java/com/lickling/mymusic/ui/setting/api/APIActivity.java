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
import android.widget.Toast;

import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.APIListItem;
import com.lickling.mymusic.bean.SettingInfo;
import com.lickling.mymusic.model.MainModel;
import com.lickling.mymusic.utilty.ImmersiveStatusBarUtil;
import com.orm.SugarContext;

import java.util.List;

public class APIActivity extends AppCompatActivity implements MenuDialogFragment.OnDeleteItemListener, ListAdapter.OnItemClickListener {

    private static final String EDIT_CODE = "1";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private MenuDialogFragment dialog;
    private SettingInfo settingInfo;
    private MainModel mainModel;

    private List<APIListItem> apiListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        ImmersiveStatusBarUtil.transparentBar(this, false);


        mainModel = new MainModel(getApplication());
        settingInfo = mainModel.getSettingInfo();

        apiListItems = APIListItem.listAll(APIListItem.class);
        if (apiListItems == null) {
            Log.e("apiListItems", "null");
            APIListItem item = new APIListItem("测试", "http://192.168.31.31:3000/");
            item.save();
            apiListItems = APIListItem.listAll(APIListItem.class);
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

        listAdapter = new ListAdapter(apiListItems, this);

        dialog = new MenuDialogFragment(this);
        dialog.setOnDeleteItemListener(this);
        listAdapter.setItemClickListener(this);
        listAdapter.setDialog(dialog);
        listAdapter.setSelectedItem(settingInfo.getApiPosition());
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
                int num = dataBundle.getInt("resultNum");
                apiListItems.get(num).setTitle(name);
                apiListItems.get(num).setSubtitle(URL);
                apiListItems.get(num).save(); // 保存api
                if (settingInfo.getApiPosition() == num) {
                    settingInfo.setApiUrl(apiListItems.get(num).getSubtitle());
                    settingInfo.save(); // 保存设置修改
                }

            } else {
                String name = dataBundle.getString("resultName");
                String URL = dataBundle.getString("resultURL");
                APIListItem item = new APIListItem(name, URL);
                item.save();
                apiListItems.add(item);
            }
            listAdapter = new ListAdapter(apiListItems, this);
            dialog.setOnDeleteItemListener(this);
            listAdapter.setItemClickListener(this);
            listAdapter.setDialog(dialog);
            listAdapter.setSelectedItem(settingInfo.getApiPosition());
            recyclerView.setAdapter(listAdapter);
        }

    }

    @Override
    public void onDeleteItem(int position) {//删除list的item
        APIListItem item = APIListItem.findById(APIListItem.class, apiListItems.get(position).getId());
        if (item == null) Log.e(" API onDeleteItem", "item  is null");
        else
            item.delete();
        apiListItems.remove(position);

        if (settingInfo.getApiPosition() == position) {
            if (apiListItems.size() == 0) {
                settingInfo.setApiPosition(-1);
                settingInfo.setApiPositionId(-1);
                settingInfo.setApiUrl("");
            } else {
                settingInfo.setApiPosition(0);
                settingInfo.setApiPositionId(0);
                settingInfo.setApiUrl(apiListItems.get(0).getSubtitle());
            }
        }
        listAdapter.setSelectedItem(settingInfo.getApiPosition());
        listAdapter.notifyItemRemoved(position);
        listAdapter.notifyItemRangeChanged(position, listAdapter.getItemCount() - position);
        recyclerView.setAdapter(listAdapter);
    }


    @Override
    public void saveSelectId(int selectedItem) {
        settingInfo.setApiPosition(selectedItem);
        settingInfo.setApiPositionId(apiListItems.get(selectedItem).getId());
        settingInfo.setApiUrl(apiListItems.get(selectedItem).getSubtitle());
        mainModel.setClientAPI(settingInfo.getApiUrl());  // 更改当前的地址为选中的地址
        Toast.makeText(this, "选择" + apiListItems.get(selectedItem).getTitle(), Toast.LENGTH_SHORT).show();
    }
}