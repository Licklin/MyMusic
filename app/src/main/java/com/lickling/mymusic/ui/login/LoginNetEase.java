package com.lickling.mymusic.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.NetEaseUser;
import com.lickling.mymusic.bean.User;
import com.lickling.mymusic.bean.networkBean.QrCodeCheckResponse;
import com.lickling.mymusic.model.MainModel;
import com.lickling.mymusic.network.NetEase.NetEaseApiHandler;
import com.lickling.mymusic.ui.home.MainActivity;
import com.lickling.mymusic.ui.home.nsh.dao.UserDao;
import com.lickling.mymusic.utilty.ImmersiveStatusBarUtil;
import com.lickling.mymusic.utilty.PictureUtil;
import com.lickling.mymusic.viewmodel.MusicViewModel;
import com.lickling.mymusic.viewmodel.UserViewModel;
import com.orm.SugarContext;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginNetEase extends AppCompatActivity {
    private Toolbar toolbar;
    private MainModel mainModel;
    private EditText EditTextAccount;
    private EditText EditTextPassword;
    boolean flag = true;
    private NetEaseUser netUser;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginwangyi);
        ImmersiveStatusBarUtil.transparentBar(this, false);
        //输入光标

        SugarContext.init(this);

        mainModel = new MainModel(this);
        netUser = mainModel.getNetEaseUser();

// 在主线程中创建一个 Handler 对象
        handler = new Handler();

        EditTextAccount = findViewById(R.id.account);
        EditTextPassword = findViewById(R.id.password);
        EditTextAccount.setText(netUser.getUserID());
        EditTextPassword.setText(netUser.getUserPWD());
        toolbar = findViewById(R.id.wangyi);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(LoginNetEase.this, R.anim.alpha);
                toolbar.startAnimation(animation);
                finish();
            }
        });
        TextView account = findViewById(R.id.account);
        EditText code = findViewById(R.id.password);

        ImageView imageView = findViewById(R.id.qr_code_btn);
//        if(!netUser.getCookie().equals("")) imageView.setClickable(false);
//        // 二维码
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginQd();
            }
        });
        account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 如果 oldPassWordEdit 获得了焦点，则将下划线颜色设置为红色
                if (hasFocus) {
                    account.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.wy_red), PorterDuff.Mode.SRC_IN);
                }
                // 如果 oldPassWordEdit 失去了焦点，则将下划线颜色恢复默认颜色
                else {
                    account.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hint_text), PorterDuff.Mode.SRC_IN);
                }
            }
        });

        code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 如果 newPassWordEdit1 获得了焦点，则将下划线颜色设置为红色
                if (hasFocus) {
                    code.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.wy_red), PorterDuff.Mode.SRC_IN);
                }
                // 如果 newPassWordEdit1 失去了焦点，则将下划线颜色恢复默认颜色
                else {
                    code.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hint_text), PorterDuff.Mode.SRC_IN);
                }
            }
        });


//注册账号按钮点击反应提示
        //注册账号
        TextView register = findViewById(R.id.register);
        String text1 = register.getText().toString();
        SpannableString spannableString1 = new SpannableString(text1);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 点击时设置文字下划线和背景色
                spannableString1.setSpan(new UnderlineSpan(), 0, spannableString1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString1.setSpan(new BackgroundColorSpan(getColor(R.color.tianlan)), 0, spannableString1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString1.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                register.setText(spannableString1);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                // 设置文字颜色和下划线颜色
                super.updateDrawState(ds);
                ds.setColor(Color.GRAY);
                ds.setUnderlineText(false);
            }
        };

// 将 clickableSpan 应用到 spannableString 中的指定位置
        int start = text1.indexOf(register.getText().toString());
        int end = start + register.getText().toString().length();
        spannableString1.setSpan(clickableSpan1, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// 将 spannableString 应用到 TextView 控件中
        register.setText(spannableString1);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        register.setHighlightColor(Color.TRANSPARENT);

        //忘记密码
        TextView forgetcode = findViewById(R.id.forgetcode);
        forgetcode.setClickable(true);
        // 设置点击事件监听器
        forgetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取 TextView 中的文字
                String text = forgetcode.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginNetEase.this);
                builder.setIcon(R.drawable.baseline_lightbulb_24);
                builder.setTitle("功能待完善");
                builder.setMessage("敬请期待");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();

                // 创建 SpannableString 对象
                SpannableString spannableString = new SpannableString(text);

                // 设置下划线
                spannableString.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                // 设置高亮
                spannableString.setSpan(new BackgroundColorSpan(getColor(R.color.tianlan)), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                // 将修改后的 SpannableString 对象设置给 TextView 控件
                forgetcode.setText(spannableString);

                // 定时器，200ms后将下划线和高亮样式移除
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // 创建一个新的 SpannableString 对象
                        SpannableString newSpannableString = new SpannableString(text);

                        // 移除下划线和高亮样式
                        newSpannableString.removeSpan(new UnderlineSpan());
                        newSpannableString.removeSpan(new BackgroundColorSpan(Color.YELLOW));

                        // 将修改后的 SpannableString 对象重新设置给 TextView 控件
                        forgetcode.post(new Runnable() {
                            @Override
                            public void run() {
                                forgetcode.setText(newSpannableString);
                            }
                        });
                    }
                }, 200); // 200ms后执行定时器任务
            }
        });

//注册按钮跳转注册界面
        TextView register_buttom = (TextView) findViewById(R.id.register);
        register_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(LoginNetEase.this, R.anim.alpha);
                register_buttom.startAnimation(animation);

                String text = register_buttom.getText().toString();
                // 创建 SpannableString 对象
                SpannableString spannableString = new SpannableString(text);

                // 设置下划线
                spannableString.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                // 设置高亮
                spannableString.setSpan(new BackgroundColorSpan(getColor(R.color.tianlan)), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                // 将修改后的 SpannableString 对象设置给 TextView 控件
                register_buttom.setText(spannableString);
                // 定时器，3秒后将下划线和高亮样式移除
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // 创建一个新的 SpannableString 对象
                        SpannableString newSpannableString = new SpannableString(text);

                        // 移除下划线和高亮样式
                        newSpannableString.removeSpan(new UnderlineSpan());
                        newSpannableString.removeSpan(new BackgroundColorSpan(Color.YELLOW));

                        // 将修改后的 SpannableString 对象重新设置给 TextView 控件
                        register_buttom.post(new Runnable() {
                            @Override
                            public void run() {
                                register_buttom.setText(newSpannableString);
                            }
                        });
                    }
                }, 200); // 3秒后执行定时器任务
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }

    //登录验证

    public void login(View view) {

        EditText EditTextAccount = findViewById(R.id.account);
        EditText EditTextPassword = findViewById(R.id.password);

        new Thread() {
            @Override
            public void run() {
                UserDao userDao = new UserDao();
                int msg = userDao.login(EditTextAccount.getText().toString(), EditTextPassword.getText().toString());
                hand1.sendEmptyMessage(msg);
            }
        }.start();

    }

    public void login() {
        new Thread() {
            @Override
            public void run() {
                UserDao userDao = new UserDao();
                int msg = userDao.login(EditTextAccount.getText().toString(), EditTextPassword.getText().toString());
                hand1.sendEmptyMessage(msg);
            }
        }.start();

    }

    @SuppressLint("HandlerLeak")
    final Handler hand1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_LONG).show();
            } else if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
            } else if (msg.what == 2) {
                Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_LONG).show();
            } else if (msg.what == 3) {
                Toast.makeText(getApplicationContext(), "账号不存在", Toast.LENGTH_LONG).show();
            }
        }
    };

    @SuppressLint("CheckResult")
    private void showLoginQd() {
        Dialog dialog = new Dialog(this);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                handler.removeCallbacks(runnable);
            }
        });
        dialog.setContentView(R.layout.qd_dailog);
        ImageView imageView = dialog.findViewById(R.id.qd_imageview);
        dialog.show();
        if (imageView == null) {
            return;
        }
        mainModel.setQd2ImageView(imageView);
        checkQdState();

    }

    @SuppressLint("CheckResult")
    private void checkQdState() {
        NetEaseApiHandler client = new NetEaseApiHandler();

// 一个 Runnable 对象，用于执行定时任务
        runnable = new Runnable() {
            @Override
            public void run() {
                // 在这里执行定时任务的操作
                // ...
                client.checkQrCodeStatus()
                        .observeOn(AndroidSchedulers.mainThread()) // 将结果切换回主线程
                        .subscribe(qrCodeCheckResponse -> {
                            System.out.println("[checkQrCodeStatus] " + qrCodeCheckResponse.toString());
                            if (qrCodeCheckResponse.code == 803) {

                                netUser.setCookie(qrCodeCheckResponse.cookie);
                                netUser.setUserName(qrCodeCheckResponse.nickname);
                                netUser.setAvatarURL(qrCodeCheckResponse.avatarUrl);
                                netUser.save();
                                handler.removeCallbacks(this);
                                startActivity(new Intent(LoginNetEase.this, MainActivity.class));
                                finish();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                    }
//                                }, 300);

                            }
                            if (qrCodeCheckResponse.code == 800) {
                                System.out.println("[checkQrCodeStatus] Cookie被偷了！");

                            }
                        });
                // 完成任务后，再次将该任务发送到主线程的消息队列中，以实现循环定时器的效果
                handler.postDelayed(this, 1000); // 1000 毫秒后再次执行该任务
            }
        };
// 将该任务发送到主线程的消息队列中，以实现定时器的效果
        handler.postDelayed(runnable, 1000); // 1000 毫秒后执行该任务
    }
}