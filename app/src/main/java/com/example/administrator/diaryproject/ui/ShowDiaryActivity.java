package com.example.administrator.diaryproject.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.diaryproject.db.DiaryDao;
import com.example.administrator.diaryproject.R;
import com.example.administrator.diaryproject.model.Diary;

import jp.wasabeef.richeditor.RichEditor;

public class ShowDiaryActivity extends AppCompatActivity {
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView mTextView_time;
    private FloatingActionButton mFloatingActionButton;
    private Diary mDiary;
    private RichEditor mRichEditor;
    private DiaryDao mDiaryDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diary);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initData();
    }

    private void initData() {
        Intent intent=getIntent();
        mDiary= (Diary) intent.getSerializableExtra("diary");
        String title=mDiary.getTitle();
        String time=mDiary.getTime();
        String content=mDiary.getContent();

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }


        mCollapsingToolbarLayout.setTitle(title);
        mTextView_time.setText(time);
        mRichEditor.setHtml(content);
    }


    private void initView() {
        mDiaryDao=new DiaryDao(ShowDiaryActivity.this);
        mCollapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar_layout);
        mTextView_time= (TextView) findViewById(R.id.text_view_title);
        mRichEditor= (RichEditor) findViewById(R.id.editor);
        mRichEditor.setInputEnabled(false);
        mRichEditor.setEditorHeight(350);
        mRichEditor.setEditorFontSize(15);
        mRichEditor.setBackgroundColor(Color.TRANSPARENT);
        mRichEditor.setEditorFontColor(Color.argb(10, 0, 0, 0));
        mRichEditor.setPadding(10, 10, 10, 10);

        mFloatingActionButton= (FloatingActionButton) findViewById(R.id.floatingAction_button);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开一个对话框列表
                final String[] items={"修改","删除"};
                AlertDialog.Builder listDialog=new AlertDialog.Builder(ShowDiaryActivity.this);
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                //修改
                                Intent intent=new Intent();
                                intent.setClass(ShowDiaryActivity.this,AddDiaty.class);
                                intent.putExtra("diary",mDiary);
                                startActivity(intent);
                                break;
                            case 1:
                                //删除
                                mDiaryDao.delect(mDiary.getId());
                                Intent send=new Intent("com.example.administrator.diaryproject.MyBroadcastReceiver");
                                sendBroadcast(send);
                                Toast.makeText(ShowDiaryActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            default:
                                break;
                        }
                    }
                });
                listDialog.show();
            }
        });

    }
}
