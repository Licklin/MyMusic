package com.lickling.mymusic.ui.home.nsh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lickling.mymusic.R;
import com.lickling.mymusic.ui.home.nsh.dao.UserDao;
import com.lickling.mymusic.ui.home.nsh.entity.User;

public class Register extends AppCompatActivity {
    private Toolbar page_head;
    EditText userAccount = null;
    EditText userPassword = null;
    EditText userName = null;

    RadioGroup sex = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userAccount = findViewById(R.id.account);
        userName = findViewById(R.id.username);
        userPassword = findViewById(R.id.password);
        sex=(RadioGroup) findViewById(R.id.radioGroup);

        EditText username = findViewById(R.id.username);
        EditText account = findViewById(R.id.account);
        EditText password = findViewById(R.id.password);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 如果 oldPassWordEdit 获得了焦点，则将下划线颜色设置为红色
                if (hasFocus) {
                    username.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.our_orange), PorterDuff.Mode.SRC_IN);
                }
                // 如果 oldPassWordEdit 失去了焦点，则将下划线颜色恢复默认颜色
                else {
                    username.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hint_text), PorterDuff.Mode.SRC_IN);
                }
            }
        });
        account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 如果 oldPassWordEdit 获得了焦点，则将下划线颜色设置为红色
                if (hasFocus) {
                    account.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.our_orange), PorterDuff.Mode.SRC_IN);
                }
                // 如果 oldPassWordEdit 失去了焦点，则将下划线颜色恢复默认颜色
                else {
                    account.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hint_text), PorterDuff.Mode.SRC_IN);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 如果 oldPassWordEdit 获得了焦点，则将下划线颜色设置为红色
                if (hasFocus) {
                    password.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.our_orange), PorterDuff.Mode.SRC_IN);
                }
                // 如果 oldPassWordEdit 失去了焦点，则将下划线颜色恢复默认颜色
                else {
                    password.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hint_text), PorterDuff.Mode.SRC_IN);
                }
            }
        });


        //返回登录界面
        page_head = findViewById(R.id.register_page);
        page_head.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(Register.this, LoginWangyi.class);
//                startActivity(intent);
                finish();
            }
        });
    }
    //注册点击
    public void register(View view) {

        String userAccount1 = userAccount.getText().toString();
        String userName1 = userName.getText().toString();
        String userPassword1 = userPassword.getText().toString();
        String usersex1=((RadioButton)Register.this.findViewById(sex.getCheckedRadioButtonId())).getText().toString();

        if (TextUtils.isEmpty(userAccount1) || TextUtils.isEmpty(userPassword1) || TextUtils.isEmpty(userName1)) {
            Toast.makeText(getApplicationContext(), "账号、密码、用户名不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        User user = new User();

        user.setUserAccount(userAccount1);
        user.setUserName(userName1);
        user.setUserPassword(userPassword1);
        user.setUsersex(usersex1);

        new Thread() {
            @Override
            public void run() {

                int msg = 0;

                UserDao userDao = new UserDao();

                User uu = userDao.findUser(user.getUserAccount());
                if (uu != null) {
                    msg = 1;
                } else {
                    boolean flag = userDao.register(user);
                    if (flag) {
                        msg = 2;
                    }
                }
                hand.sendEmptyMessage(msg);

            }
        }.start();
    }
    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler()
    {
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_LONG).show();
            } else if(msg.what == 1) {
                Toast.makeText(getApplicationContext(),"该账号已经存在，请换一个账号",Toast.LENGTH_LONG).show();
            } else if(msg.what == 2) {
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                //将想要传递的数据用putExtra封装在intent中
                intent.putExtra("a","注册");
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        }
    };
}