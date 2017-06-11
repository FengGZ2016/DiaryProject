package com.example.administrator.diaryproject.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.diaryproject.DiaryAdapter;
import com.example.administrator.diaryproject.db.DiaryDao;
import com.example.administrator.diaryproject.ItemRemoveRecyclerView;
import com.example.administrator.diaryproject.listener.OnItemClickListener;
import com.example.administrator.diaryproject.R;
import com.example.administrator.diaryproject.model.Diary;

import java.util.List;

public class ActivitySearth extends AppCompatActivity {
    private ImageView mImageViewBack;
    private EditText mEditTextSearth;
    private ItemRemoveRecyclerView searchRecyclerview;
    private DiaryDao mDiaryDao;
    private List<Diary> mDiaryList;
    private SharedPreferences mSharePreferences;
    private Handler myHandler=new Handler();
    private DiaryAdapter mDiaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searth);
       initView();
        initRecycler();
    }

    private void initRecycler() {
        LinearLayoutManager manager=new LinearLayoutManager(ActivitySearth.this);
        mDiaryAdapter=new DiaryAdapter(mDiaryList,ActivitySearth.this);
        searchRecyclerview.setLayoutManager(manager);
        searchRecyclerview.setAdapter(mDiaryAdapter);
        searchRecyclerview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Diary diary=mDiaryList.get(position);
                Intent intent=new Intent();
                intent.setClass(ActivitySearth.this,ShowDiaryActivity.class);
                intent.putExtra("diary",diary);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                //根据当前位置获取Diary对象
                final Diary diary=mDiaryList.get(position);
                //从列表中删除
                mDiaryList.remove(diary);
                //从数据库中删除
                mDiaryDao.delect(diary.getId());
                mDiaryAdapter.notifyDataSetChanged();
                Intent send=new Intent("com.example.administrator.diaryproject.MyBroadcastReceiver");
                sendBroadcast(send);
                Toast.makeText(ActivitySearth.this, "删除成功！", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initView() {
        mDiaryDao=new DiaryDao(this);
        mDiaryList=mDiaryDao.queryAllDiary();
        mSharePreferences=getSharedPreferences("image",MODE_PRIVATE);
        mImageViewBack= (ImageView) findViewById(R.id.back_search_diary);
        mEditTextSearth= (EditText) findViewById(R.id.search_edit);
        searchRecyclerview= (ItemRemoveRecyclerView) findViewById(R.id.search_recycler_view);

        /**
         * 返回
         * */
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /**
         * 编辑框添加监听事件
         * */
        mEditTextSearth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()!=0){
                    //编辑框不为空时
                    myHandler.post(eChanged);
                }

            }
        });
        setBackground();
    }

    Runnable eChanged=new Runnable() {
        @Override
        public void run() {
            String editStr=mEditTextSearth.getText().toString().trim();
            mDiaryList.clear();
            getmDataSub(mDiaryList,editStr);
            mDiaryAdapter.notifyDataSetChanged();
        }
    };

    private void getmDataSub(List<Diary> diaryList, String editStr) {
        List<Diary> list=mDiaryDao.queryAllDiary();
        int length=list.size();
        for (int i=0;i<length;i++){
            if (list.get(i).getTitle().contains(editStr)||list.get(i).getContent().contains(editStr)){
                diaryList.add(list.get(i));
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground();
    }


    private void setBackground() {
        LinearLayout layout = (LinearLayout) this.findViewById(R.id.activity_searth);
        int id = mSharePreferences.getInt("id", 0);
        if (id == 0) {
            layout.setBackgroundResource(R.drawable.diary_view_bg);
        } else if (id == 1) {// id=1ËµÃ÷ÓÃ»§Ñ¡ÔñÁËµÚÒ»·ùÍ¼Æ¬
            layout.setBackgroundResource(R.drawable.diary_view_bg);
        } else if (id == 2) {// id=2ËµÃ÷ÓÃ»§Ñ¡ÔñÁËµÚ¶þ·ùÍ¼Æ¬
            layout.setBackgroundResource(R.drawable.spring);
        } else if (id == 3) {// id=3ËµÃ÷ÓÃ»§Ñ¡ÔñÁËµÚÈý·ùÍ¼Æ¬
            layout.setBackgroundResource(R.drawable.summer);
        } else if (id == 4) {// id=4ËµÃ÷ÓÃ»§Ñ¡ÔñÁËµÚËÄ·ùÍ¼Æ¬
            layout.setBackgroundResource(R.drawable.autumn);
        } else if (id == 5) {// id=4ËµÃ÷ÓÃ»§Ñ¡ÔñÁËµÚËÄ·ùÍ¼Æ¬
            layout.setBackgroundResource(R.drawable.winter);
        }
    }



}
