package com.example.administrator.diaryproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.administrator.diaryproject.listener.Listener;

/**
 * Created by Administrator on 2017/5/10.
 * 广播接收者
 */

public class MyBroadcastReceiver extends BroadcastReceiver{

    private Listener listener;

    public MyBroadcastReceiver(Listener listener) {
        this.listener=listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("com.example.administrator.diaryproject.MyBroadcastReceiver")){
            listener.changData();
            Log.d("调试","接收到广播了");
        }

    }
}
