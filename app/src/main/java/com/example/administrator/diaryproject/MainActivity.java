package com.example.administrator.diaryproject;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.diaryproject.db.DiaryDao;
import com.example.administrator.diaryproject.listener.Listener;
import com.example.administrator.diaryproject.listener.OnItemClickListener;
import com.example.administrator.diaryproject.model.Diary;
import com.example.administrator.diaryproject.ui.ActivitySearth;
import com.example.administrator.diaryproject.ui.AddDiaty;
import com.example.administrator.diaryproject.ui.ShowDiaryActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Listener {
    private EditText mEditText;
    private ItemRemoveRecyclerView mRecyclerView;
    private DiaryDao mDiaryDao;
    public static  List<Diary> mDiaryList;
    public static DiaryAdapter mDiaryAdapter;
    private MyBroadcastReceiver mMyBroadcastReceiver;
    private Button mButton_searth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       initReceiver();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册广播接收者
        unregisterReceiver(mMyBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // mDiaryAdapter.notifyDataSetChanged();
    }

    /**
     * 注册广播接收者
     * */
    private void initReceiver() {
        mMyBroadcastReceiver=new MyBroadcastReceiver(MainActivity.this);
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.example.administrator.diaryproject.MyBroadcastReceiver");
        registerReceiver(mMyBroadcastReceiver,filter);

    }


    private void initView() {
        mEditText= (EditText) findViewById(R.id.eidt_text);
        mRecyclerView= (ItemRemoveRecyclerView) findViewById(R.id.recycler_view);
        mButton_searth= (Button) findViewById(R.id.button_search);

        /**
         * 搜索功能
         * */
        mButton_searth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开搜索的活动
   Intent intent=new Intent(MainActivity.this,ActivitySearth.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "打开搜索", Toast.LENGTH_SHORT).show();
            }
        });



        //编辑框设置点击事件
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //在这里开启一个活动，执行编写日记的具体操作
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,AddDiaty.class);
                startActivity(intent);
                //返回值 false运行，只能监听到“按下”事件，“移动”和“抬起”都不能够监听到
                return true;
            }
        });

        mDiaryDao=new DiaryDao(MainActivity.this);
        //查询所有的数据
        mDiaryList=mDiaryDao.queryAllDiary();
        LinearLayoutManager manager=new LinearLayoutManager(MainActivity.this);
        mDiaryAdapter=new DiaryAdapter(mDiaryList,MainActivity.this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mDiaryAdapter);
        mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Diary diary=mDiaryList.get(position);
               Intent intent=new Intent();
                intent.setClass(MainActivity.this,ShowDiaryActivity.class);
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
                Toast.makeText(MainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 当数据改变的时候调用
     * */
    @Override
    public void changData() {
        //查询所有的数据
        mDiaryList=mDiaryDao.queryAllDiary();
        LinearLayoutManager manager=new LinearLayoutManager(MainActivity.this);
        mDiaryAdapter=new DiaryAdapter(mDiaryList,MainActivity.this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mDiaryAdapter);
    }
}
