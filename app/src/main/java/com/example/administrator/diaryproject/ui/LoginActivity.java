package com.example.administrator.diaryproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.diaryproject.MainActivity;
import com.example.administrator.diaryproject.R;
import com.example.administrator.diaryproject.model.UserBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditText_phone;
    private EditText mEditText_pass;
    private Button mButton_login;
    private Button mButton_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    private void initView() {
        mEditText_phone= (EditText) findViewById(R.id.edit_phoneNum);
        mEditText_pass= (EditText) findViewById(R.id.edit_password);
        mButton_login= (Button) findViewById(R.id.btn_login);
        mButton_register= (Button) findViewById(R.id.btn_goreg);

        //登录
        mButton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mPhoneStr = mEditText_phone.getText().toString().trim()
                        .replaceAll(" ", "");
                String mPasswordStr = mEditText_pass.getText().toString().trim();

                if (isBlank(mPhoneStr)) {
                    Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else if (!isMobileNO(mPhoneStr)) {
                    Toast.makeText(LoginActivity.this, "手机号输入有误", Toast.LENGTH_SHORT).show();
                } else if (isBlank(mPasswordStr)) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "正在登录...",Toast.LENGTH_SHORT).show();
                    UserBean bu2 = new UserBean();
                    bu2.setUsername(mPhoneStr);
                    bu2.setPassword(mPasswordStr);
                    bu2.login(new SaveListener<UserBean>() {
                        @Override
                        public void done(UserBean userBean, BmobException e) {
                            if(e == null){
                                //登录成功
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                LoginActivity.this.finish();
                            }else{
                              //登录失败
                                Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });


        //注册
        mButton_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                LoginActivity.this.finish();
            }
        });
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isBlank(String str) {
        boolean isBlank = false;
        if (null == str || str.equals("") || str.equals("null")
                || str.trim().length() == 0) {
            isBlank = true;
        }
        return isBlank;
    }
}
