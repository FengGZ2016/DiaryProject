package com.example.administrator.diaryproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/5/3.
 * 数据库类
 */

public class DiaryHelper extends SQLiteOpenHelper{

    public DiaryHelper(Context context) {
        super(context, "day.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库
        String sql="create table diary("+"id integer primary key autoincrement,"+"title text,"+"content text,"+"time DateTime"+")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
