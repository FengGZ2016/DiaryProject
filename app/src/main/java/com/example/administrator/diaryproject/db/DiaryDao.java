package com.example.administrator.diaryproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.diaryproject.model.Diary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 * 数据库控制类:执行数据库的增删查改
 */

public class DiaryDao {
    private DiaryHelper mDiaryHelper;
    public DiaryDao(Context context){
        mDiaryHelper=new DiaryHelper(context);
    }

    /**
     * 增加数据
     * */
    public void insert(Diary diary){
        SQLiteDatabase db=mDiaryHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("title",diary.getTitle());
        values.put("content",diary.getContent());
        values.put("time",diary.getTime());
        int id= (int) db.insert("diary",null,values);
        diary.setId(id);
        db.close();
    }

    /**
     * 查询所有的数据
     * */
    public List<Diary> queryAllDiary(){
        SQLiteDatabase db=mDiaryHelper.getReadableDatabase();
        Cursor cursor=db.query("diary",null,null,null,null,null,null);
        List<Diary> diaryList=new ArrayList<>();
        while (cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String title=cursor.getString(cursor.getColumnIndex("title"));
            String content=cursor.getString(cursor.getColumnIndex("content"));
            String time=cursor.getString(cursor.getColumnIndex("time"));
            Diary diary=new Diary(id,title,content,time);
            diaryList.add(diary);
        }
        cursor.close();
        db.close();

        return diaryList;
    }

    /**
     * 删除一条数据
     * */
    public int delect(Integer id) {
        SQLiteDatabase db=mDiaryHelper.getWritableDatabase();
        //根据id删除数据
        int count=db.delete("diary", "id=?", new String[]{id+""});
        //关闭数据库
        db.close();
        //返回受影响的行数
        return count;

    }


    /**
     * 查找数据
     * */
    public  List<Diary> dimSearch(String editText){
        List<Diary> diaryList=new ArrayList<>();
        SQLiteDatabase db=mDiaryHelper.getReadableDatabase();
        Cursor cursor = db.query("diary", null, "content like '%" + editText + "%'", null, null, null, "id desc");
        diaryList.clear();
        while(cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String title=cursor.getString(cursor.getColumnIndex("title"));
            String content=cursor.getString(cursor.getColumnIndex("content"));
            String time=cursor.getString(cursor.getColumnIndex("time"));
            Diary diary=new Diary(id,title,content,time);
            diaryList.add(diary);
        }
        cursor.close();
        db.close();
        return diaryList;
    }


    /**
     * 修改数据
     * */
    public int update(Diary diary) {
        ContentValues values=new ContentValues();
        values.put("title",diary.getTitle());
        values.put("content",diary.getContent());
        values.put("time",diary.getTime());
        SQLiteDatabase db=mDiaryHelper.getWritableDatabase();
        int count=db.update("diary", values, "id=?", new String[]{diary.getId()+""});
        db.close();
        return count;

    }

}
