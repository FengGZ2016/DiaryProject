package com.example.administrator.diaryproject.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/3.
 */

public class Diary implements Serializable{
    private int id;
    private String title;//日记标题
    private String content;//日记内容
    private String time;

    public Diary(int id,String title,String content,String time){
        this.id=id;
        this.title=title;
        this.content=content;
        this.time=time;
    }

    public Diary(String title,String content,String time){
        this.title=title;
        this.content=content;
        this.time=time;
    }

    public Diary(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
