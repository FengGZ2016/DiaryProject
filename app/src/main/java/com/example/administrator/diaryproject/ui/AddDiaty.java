package com.example.administrator.diaryproject.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.diaryproject.R;
import com.example.administrator.diaryproject.db.DiaryDao;
import com.example.administrator.diaryproject.model.Diary;
import com.lqr.audio.AudioRecordManager;
import com.lqr.audio.IAudioRecordListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.richeditor.RichEditor;

/**
 * 添加日记的活动
 * */
public class AddDiaty extends AppCompatActivity {
    private EditText title;
    private RichEditor content;
    private TextView text_view_time;
    private DiaryDao diatyDao;
    private Diary mdiary;
    private Button mButton_auido;
    private Button mButton_camera;
    private Button mButton_photo;
    private Button mButton_video;
    private View mAudioinflate;
    private int mScreenWidth;
    private int mScreenHeight;
    private static final int TAKE_PHOTO=1;
    private static final int CHOOSE_PHOTO=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diaty);
        Toolbar toolbar= (Toolbar) findViewById(R.id.add_diary_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        Display defaultDisplay = getWindowManager().getDefaultDisplay();

        mScreenWidth = defaultDisplay.getWidth();
        mScreenHeight = defaultDisplay.getHeight();

        initData();
        initView();

    }

    private void initData() {
        Intent intent=getIntent();
        mdiary= (Diary) intent.getSerializableExtra("diary");
    }

    private void initView() {
        mButton_auido= (Button) findViewById(R.id.btn_auido);
        mButton_camera= (Button) findViewById(R.id.btn_camera);
        mButton_photo= (Button) findViewById(R.id.btn_photo);
        mButton_video= (Button) findViewById(R.id.btn_video);
        diatyDao=new DiaryDao(AddDiaty.this);
        title= (EditText) findViewById(R.id.edit_title);
      // content= (EditText) findViewById(R.id.edit_content);
        content= (RichEditor) findViewById(R.id.edit_content);
        text_view_time= (TextView) findViewById(R.id.text_view_time);
        //显示编辑日记框中的时间
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString=format.format(date);
        text_view_time.setText(dateString);

        if (mdiary!=null){
            title.setText(mdiary.getTitle());
            content.setHtml(mdiary.getContent());
        }



        /**
         * 录音
         * */
        mButton_auido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioinflate = LayoutInflater.from(AddDiaty.this).inflate(R.layout.dailog_auido, null);
                TextView tv = (TextView) mAudioinflate.findViewById(R.id.bar_title);
                tv.setText("插入音频");
                Button btn_record = (Button) mAudioinflate.findViewById(R.id.btn_record);
                AlertDialog.Builder builder=new AlertDialog.Builder(AddDiaty.this);
                final AlertDialog dialog = builder.setView(mAudioinflate).show();
                dialog.getWindow().setLayout((int) (mScreenWidth * 0.75), (int) (mScreenHeight * 0.7));

                /**
                 * 按住录音
                 * */
                btn_record.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                AudioRecordManager.getInstance(AddDiaty.this).startRecord();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (isCancelled(v, event)) {
                                    AudioRecordManager.getInstance(AddDiaty.this).willCancelRecord();
                                } else {
                                    AudioRecordManager.getInstance(AddDiaty.this).continueRecord();
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                AudioRecordManager.getInstance(AddDiaty.this).stopRecord();
                                AudioRecordManager.getInstance(AddDiaty.this).destroyRecord();

                                break;
                        }
                        return false;
                    }
                });
                AudioRecordManager.getInstance(AddDiaty.this).setAudioRecordListener(new IAudioRecordListener() {
                    public PopupWindow mRecordWindow;
                    ImageView mStateIV;
                    TextView mStateTV, mTimerTV;

                    @Override
                    public void initTipView() {
                        View view = View.inflate(AddDiaty.this, R.layout.popup_audio_wi_vo, null);
                        mStateIV = (ImageView) view.findViewById(R.id.rc_audio_state_image);
                        mStateTV = (TextView) view.findViewById(R.id.rc_audio_state_text);
                        mTimerTV = (TextView) view.findViewById(R.id.rc_audio_timer);
                        mRecordWindow = new PopupWindow(view, -1, -1);
                        mRecordWindow.showAtLocation(mAudioinflate, 17, 0, 0);
                        mRecordWindow.setFocusable(true);
                        mRecordWindow.setOutsideTouchable(false);
                        mRecordWindow.setTouchable(false);
                    }

                    @Override
                    public void setTimeoutTipView(int counter) {
                        if (this.mRecordWindow != null) {
                            this.mStateIV.setVisibility(View.GONE);
                            this.mStateTV.setVisibility(View.VISIBLE);
                            this.mStateTV.setText(R.string.voice_rec);
                            this.mTimerTV.setText(String.format("%s", new Object[]{Integer.valueOf(counter)}));
                            this.mTimerTV.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void setRecordingTipView() {
                        if (this.mRecordWindow != null) {
                            this.mStateIV.setVisibility(View.VISIBLE);
                            this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                            this.mStateTV.setVisibility(View.VISIBLE);
                            this.mStateTV.setText(R.string.voice_rec);
                            this.mTimerTV.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void setAudioShortTipView() {
                        if (this.mRecordWindow != null) {
                            mStateIV.setImageResource(R.mipmap.ic_volume_wraning);
                            mStateTV.setText(R.string.voice_short);
                        }
                    }

                    @Override
                    public void setCancelTipView() {
                        if (this.mRecordWindow != null) {
                            this.mTimerTV.setVisibility(View.GONE);
                            this.mStateIV.setVisibility(View.VISIBLE);
                            this.mStateIV.setImageResource(R.mipmap.ic_volume_cancel);
                            this.mStateTV.setVisibility(View.VISIBLE);
                            this.mStateTV.setText(R.string.voice_cancel);
                            this.mStateTV.setBackgroundResource(R.drawable.corner_voice_style);
                        }
                    }

                    @Override
                    public void destroyTipView() {
                        if (this.mRecordWindow != null) {
                            this.mRecordWindow.dismiss();
                            this.mRecordWindow = null;
                            this.mStateIV = null;
                            this.mStateTV = null;
                            this.mTimerTV = null;
                        }
                    }

                    @Override
                    public void onStartRecord() {
                        //开始录制
                    }


                    @Override
                    public void onFinish(Uri audioPath, int duration) {
                        content.setHtml((content.getHtml() == null ? "&nbsp;" : content.getHtml()) + "<audio controls='controls' height='100' width='100'>" +
                                "  <source src='" + audioPath + "' /></audio>&nbsp;");
                        dialog.dismiss();
                    }

                    @Override
                    public void onAudioDBChanged(int db) {
                        switch (db / 5) {
                            case 0:
                                this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                                break;
                            case 1:
                                this.mStateIV.setImageResource(R.mipmap.ic_volume_2);
                                break;
                            case 2:
                                this.mStateIV.setImageResource(R.mipmap.ic_volume_3);
                                break;
                            case 3:
                                this.mStateIV.setImageResource(R.mipmap.ic_volume_4);
                                break;
                            case 4:
                                this.mStateIV.setImageResource(R.mipmap.ic_volume_5);
                                break;
                            case 5:
                                this.mStateIV.setImageResource(R.mipmap.ic_volume_6);
                                break;
                            case 6:
                                this.mStateIV.setImageResource(R.mipmap.ic_volume_7);
                                break;
                            default:
                                this.mStateIV.setImageResource(R.mipmap.ic_volume_8);
                        }
                    }

                });
                //.....----------------------------------
            }
        });


        /**
         * 相机
         * */
        mButton_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建File对象，用于储存拍照后的图片
                File outputImage=new File(getExternalCacheDir(),"outputImage.jpg");
                try{
                    if (outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }

                //启动相机程序
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,outputImage);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });


        /**
         * 图库
         * */
        mButton_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        /**
         * 视频
         * */
        mButton_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    //检查权限
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(AddDiaty.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //申请权限
            ActivityCompat.requestPermissions(AddDiaty.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
           // openAlbum();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //同意
                 //   openAlbum();
                }else {
                    //不同意
                    Toast.makeText(this, "权限已被拒绝！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode==RESULT_OK){
                       // Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //将拍摄的图片显示出来
                    content.insertImage(String.valueOf(data.getData()),"image");

                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode==RESULT_OK){

                }
                break;
            default:
                break;
        }


    }

    private boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth() || event.getRawY() < location[1] - 40) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){
        case R.id.sava:
            //保存的点击事件
            String diaryTitle=title.getText().toString();//日记标题
           String diaryContent=content.getHtml();//日记内容
            //重新获取当时时间
            Date date=new Date();
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString=format.format(date);
            if (diaryTitle==null||diaryContent==null){
                Toast.makeText(this, "日记标题或内容不能为空哦！", Toast.LENGTH_SHORT).show();
            }else {
                if (mdiary!=null){
                    mdiary.setTime(dateString);
                    mdiary.setTitle(diaryTitle);
                    mdiary.setContent(diaryContent);
                    //修改数据库
                    diatyDao.update(mdiary);
                }else {
                    Diary diary=new Diary(diaryTitle,diaryContent,dateString);
                    //插入数据库
                    diatyDao.insert(diary);
                }

                Intent intent=new Intent("com.example.administrator.diaryproject.MyBroadcastReceiver");
                sendBroadcast(intent);
                Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
            }

            break;
        case android.R.id.home:
            //返回的点击事件
            finish();
            break;
    }
        return super.onOptionsItemSelected(item);
    }
}
