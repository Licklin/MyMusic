package com.lickling.mymusic.ui.home.nsh.presenter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lickling.mymusic.R;

public class LoginWangyi extends AppCompatActivity implements LoginView{
    private Toolbar toolbar;
    private EditText account;
    private EditText code;
    private Button loginButton;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginwangyi);

        // 在此处添加获取EditText和Button控件的代码

        presenter = new LoginPresenter(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.login(account.getText().toString(), code.getText().toString());
            }
        });
    }

    @Override
    public void showUsernameError() {
        account.setError("用户名错误");
    }

    @Override
    public void showPasswordError() {
        code.setError("密码错误");
    }

    @Override
    public void showLoginSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
    }
}
