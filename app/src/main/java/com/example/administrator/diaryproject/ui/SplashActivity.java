package com.example.administrator.diaryproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.diaryproject.MainActivity;
import com.example.administrator.diaryproject.R;
import com.example.administrator.diaryproject.model.UserBean;

import cn.bmob.v3.BmobUser;

public class SplashActivity extends AppCompatActivity {
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler=new Handler();
        isLogin();

    }

    private void isLogin() {
     UserBean userInfo=BmobUser.getCurrentUser(UserBean.class);
      //  UserBean userInfo = BmobUser.getCurrentUser( UserBean.class);
        if (userInfo!=null){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }
            }, 1000);
        }else {
            // 否则，停留1.5秒进入登陆页面
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    SplashActivity.this.finish();
                }
            }, 1500);
        }
    }
}
