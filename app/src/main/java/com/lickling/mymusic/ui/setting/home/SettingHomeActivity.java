package com.lickling.mymusic.ui.setting.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.lickling.mymusic.R;
import com.lickling.mymusic.bean.SettingInfo;
import com.lickling.mymusic.bean.User;
import com.lickling.mymusic.model.MainModel;
import com.lickling.mymusic.ui.setting.api.APIActivity;
import com.lickling.mymusic.ui.setting.dialog.EditDialog;
import com.lickling.mymusic.ui.setting.notice.NoticeActivity;
import com.lickling.mymusic.ui.setting.password.PassWordActivity;
import com.lickling.mymusic.ui.setting.sound_quality.SoundQualityActivity;
import com.lickling.mymusic.utilty.ImmersiveStatusBarUtil;
import com.orm.SugarContext;

public class SettingHomeActivity extends AppCompatActivity  {


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
    private PopupWindow popupWindow;
    private PopupWindow popupEditWindow;
    private MainModel mainModel;
    private SettingInfo settingInfo;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_setting);
        ImmersiveStatusBarUtil.transparentBar(this, false);
        SugarContext.init(this);

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

        settingInfo = mainModel.getSettingInfo();
        user = mainModel.getUser();

        initFindView();
        toolbarBack();
        setInfo();
        setClick();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }

    @SuppressLint("SetTextI18n")
    private void setInfo() {
//        if(settingInfo.getCacheLimit() == 8) return;
        cacheSize.setText(settingInfo.getCacheLimit()+"GB");
    }

    // 使用PopupWindow实现Dialog
    private void initPopupWindowDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.setting_dialog_layout, null);

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btn_ok = popupView.findViewById(R.id.btn_ok);
        Button btn_cancel = popupView.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击确定后的处理
                Toast.makeText(SettingHomeActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击取消后的处理
                popupWindow.dismiss();
            }
        });
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

    private void toolbarBack() {
        toolbar = findViewById(R.id.setting_navigation);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                mainModel.saveSetting(settingInfo);
            }
        });

    }

    private void setClick() {
        passWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingHomeActivity.this, PassWordActivity.class));
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
//                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0); //使用弹出的popupWindow对话框
                showDialog("退出账号", user.getOurUserName());
            }
        });
        accountCancellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDialog dialog = new EditDialog();
//                dialog.show(getFragmentManager(), "MyDialogFragment");
                showEditDialog("账号注销", user.getOurUserName());
            }
        });
        cacheLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(SettingHomeActivity.this);
                View dialogView = LayoutInflater.from(SettingHomeActivity.this).inflate(R.layout.setting_edit_dialog, null);
                EditText mEditText = dialogView.findViewById(R.id.confirm_user_name_edit);
                TextView mTitle = dialogView.findViewById(R.id.title);
                TextView mSubtitle = dialogView.findViewById(R.id.subtitle);
                Button mConfirmButton = dialogView.findViewById(R.id.btn_ok);
                Button mCancelButton = dialogView.findViewById(R.id.btn_cancel);
//                mConfirmButton.setEnabled(true);
                mTitle.setText("设置缓存上限");
                mSubtitle.setText("1-8GB");
                mConfirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cacheSize.setText(mEditText.getText()+"GB");
                        settingInfo.setCacheLimit(Integer.parseInt(mEditText.getText().toString()));
                        settingInfo.save();
                        Toast.makeText(SettingHomeActivity.this, "成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                mCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.setContentView(dialogView);
                mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        // 如果 oldPassWordEdit 获得了焦点，则将下划线颜色设置为橙色
                        if (hasFocus) {
                            mEditText.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.our_orange), PorterDuff.Mode.SRC_IN);
                        }
                        // 如果 oldPassWordEdit 失去了焦点，则将下划线颜色恢复默认颜色
                        else {
                            mEditText.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hint_text), PorterDuff.Mode.SRC_IN);
                        }
                    }
                });
                mEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String text = s.toString().trim();
                        switch (text) {
                            case "1":
                            case "2":
                            case "3":
                            case "4":
                            case "5":
                            case "6":
                            case "7":
                            case "8":
                                mConfirmButton.setEnabled(true);
                                break;
                            default:
                                mConfirmButton.setEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                dialog.show();
            }
        });
        clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("清除缓存", "");
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
                Toast.makeText(SettingHomeActivity.this, "已是最新版", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void showEditDialog(String title, String subtitle) {
        Dialog dialog = new Dialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.setting_edit_dialog, null);
        EditText mEditText = dialogView.findViewById(R.id.confirm_user_name_edit);
        TextView mTitle = dialogView.findViewById(R.id.title);
        TextView mSubtitle = dialogView.findViewById(R.id.subtitle);
        Button mConfirmButton = dialogView.findViewById(R.id.btn_ok);
        Button mCancelButton = dialogView.findViewById(R.id.btn_cancel);
        mTitle.setText(title);
        mSubtitle.setText(subtitle);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //账号注销操作
                Toast.makeText(SettingHomeActivity.this, "成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.setContentView(dialogView);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 如果 oldPassWordEdit 获得了焦点，则将下划线颜色设置为橙色
                if (hasFocus) {
                    mEditText.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.our_orange), PorterDuff.Mode.SRC_IN);
                }
                // 如果 oldPassWordEdit 失去了焦点，则将下划线颜色恢复默认颜色
                else {
                    mEditText.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hint_text), PorterDuff.Mode.SRC_IN);
                }
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim();
                if (text.equals(subtitle)) {
                    mConfirmButton.setEnabled(true);
                } else {
                    mConfirmButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });


        dialog.show();
    }

    private void showDialog(String title, String subtitle) {
        Dialog dialog = new Dialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.setting_dialog_layout, null);
        dialog.setContentView(dialogView);
        TextView mTitle = dialogView.findViewById(R.id.title);
        TextView mSubtitle = dialogView.findViewById(R.id.subtitle);
        Button btn_ok = dialogView.findViewById(R.id.btn_ok);
        Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        mTitle.setText(title);
        mSubtitle.setText(subtitle);
        if (subtitle.equals(""))
            mSubtitle.setVisibility(View.GONE);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击确定后的处理
                Toast.makeText(SettingHomeActivity.this, "成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击取消后的处理
                dialog.dismiss();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        dialog.show();
    }

}