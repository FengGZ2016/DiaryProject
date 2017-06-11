package com.example.administrator.diaryproject;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/5/14.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
       Bmob.initialize(this,"32572705a751878820d67f65d30a12cb");

    }
}
