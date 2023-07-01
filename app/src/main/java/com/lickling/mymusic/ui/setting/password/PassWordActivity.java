package com.lickling.mymusic.ui.setting.password;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.User;
import com.lickling.mymusic.model.MainModel;
import com.lickling.mymusic.ui.home.nsh.LoginWangyi;
import com.lickling.mymusic.ui.home.nsh.dao.UserDao;
import com.lickling.mymusic.ui.setting.home.SettingHomeActivity;
import com.orm.SugarContext;

public class PassWordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private User user;
    private MainModel mainModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_setting_password);
        EditText oldPassWordEdit = findViewById(R.id.old_password);
        EditText newPassWordEdit1 = findViewById(R.id.new_password1);
        EditText newPassWordEdit2 = findViewById(R.id.new_password2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        toolbar = findViewById(R.id.setting_navigation_password);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 获取 SharedPreferences 对象
        SharedPreferences prefs = getSharedPreferences("userId", Context.MODE_PRIVATE);

        long saveKeyOfUser = prefs.getLong("saveKeyOfUser", 1);
        long saveKeyOfSetting = prefs.getLong("saveKeyOfSetting", 1);

        SugarContext.init(this);

        mainModel = new MainModel(saveKeyOfUser,saveKeyOfSetting);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("saveKeyOfUser", mainModel.getUserSaveID());
        editor.putLong("saveKeyOfSetting", mainModel.getSettingInfoSaveID());
        editor.apply();

        user = mainModel.getUser();


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

        ImageView UpdatePass = findViewById(R.id.update_password);
        UpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (judge_newpass(newPassWordEdit1.getText().toString(),newPassWordEdit2.getText().toString())) {
                    updatepass();
                    SugarContext.init(PassWordActivity.this);
                    Intent intent = new Intent(PassWordActivity.this, LoginWangyi.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(PassWordActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }

    public void updatepass(){
        new Thread(){
            @Override
            public void run() {
                UserDao userDao = new UserDao();
                EditText newPassWordEdit1 = findViewById(R.id.new_password1);
                String newpass = newPassWordEdit1.getText().toString();
                System.out.println("被修改账号的账户名:::"+user.getOurUserID());
                System.out.println("需要修改的密码:::"+user.getOurUserPWD());
                System.out.println("新的密码:::"+newpass);
                System.out.println("——————————参数已经传入——————————");
                boolean flag=userDao.modifyPassword(user.getOurUserID(),user.getOurUserPWD(),newpass);
            }
        }.start();

    }
    public boolean judge_newpass(String newpass1,String newpass2){
        if(newpass1.equals(newpass2)){
            return true;
        }
        else {

            return false;
        }
    }

}