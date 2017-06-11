package com.example.administrator.diaryproject.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.diaryproject.R;
import com.example.administrator.diaryproject.model.UserBean;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEditText_phone;
    private EditText mEditText_code;
    private EditText mEditText_pass;
    private Button mButton_getCode;
    private Button mButton_register;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        mEditText_phone= (EditText) findViewById(R.id.edit_phone);
        mEditText_code= (EditText) findViewById(R.id.edit_code);
        mEditText_pass= (EditText) findViewById(R.id.edit_pass);
        mButton_getCode= (Button) findViewById(R.id.btn_getcode);
        mButton_register= (Button) findViewById(R.id.btn_reg);
        time = new TimeCount(60000, 1000);// 构造CountDownr对象

        /**
         * 获取验证码
         * */
        mButton_getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mPhoneStr = mEditText_phone.getText().toString().trim()
                        .replaceAll(" ", "");
                if (isBlank(mPhoneStr)) {
                    Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else if (!isMobileNO(mPhoneStr)) {
                    Toast.makeText(RegisterActivity.this, "手机号输入有误", Toast.LENGTH_SHORT).show();
                }
                else {
                    //判断手机号是否已经注册
                    BmobQuery<UserBean> bmobQuery = new BmobQuery<UserBean>();
                    //查询mobile叫mPhoneStr的数据
                    bmobQuery.addWhereEqualTo("username", mPhoneStr);
                    bmobQuery.findObjects(new FindListener<UserBean>() {
                        @Override
                        public void done(List<UserBean> list, cn.bmob.v3.exception.BmobException e) {
                            if (e == null) {
                                if (list.size() > 0) {
                                    Toast.makeText(RegisterActivity.this, "该手机号已注册，请直接登录", Toast.LENGTH_SHORT).show();
                                } else {
                                    mButton_getCode.setText("获取中...");
                                    BmobSMS.requestSMSCode(mEditText_phone.getText().toString().trim().replaceAll(" ", ""), getResources().getString(R.string.app_name), new QueryListener<Integer>() {
                                        @Override
                                        public void done(Integer smsId, BmobException ex) {
                                            if (ex == null) {//验证码发送成功
                                                Log.d("bmob","短信id="+smsId);   //用于查询本次短信发送详情
                                                time.start();// 开始倒计时
                                                Toast.makeText(RegisterActivity.this, "验证码已经发送", Toast.LENGTH_SHORT).show();
                                                mEditText_phone.setTextColor(Color.RED);
                                                mEditText_phone.setEnabled(false);
                                            }
                                        }
                                    });

                                }

                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败，请稍后重试"+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });


        /**
         * 确定注册
         * */
        mButton_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone=mEditText_phone.getText().toString().trim();
                final String code=mEditText_code.getText().toString().trim();
                final String pass=mEditText_pass.getText().toString().trim();

//                BmobUser.signOrLoginByMobilePhone(phone,code, new LogInListener<UserBean>() {
//
//                    @Override
//                    public void done(UserBean user, BmobException e) {
//                        if(user!=null){
//                            Log.i("smile","用户登陆成功");
//                        }
//                    }
//                });
                if (isBlank(phone)) {
                    Toast.makeText(RegisterActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
                } else if (!isMobileNO(phone)) {
                    Toast.makeText(RegisterActivity.this, "手机号输入格式有误！", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(pass)) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(code)) {
                    Toast.makeText(RegisterActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
                }else {
                    UserBean userBean=new UserBean();
                    userBean.setPassword(pass);
                    userBean.setUsername(phone);
                    userBean.signUp(new SaveListener<UserBean>() {
                        @Override
                        public void done(UserBean userBean, cn.bmob.v3.exception.BmobException e) {
                            if (e == null) {
                                Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                                Intent login=new Intent();
                                login.setClass(RegisterActivity.this,LoginActivity.class);
                                startActivity(login);
                            } else {
                                if (e.getErrorCode() == 202) {
                                    Toast.makeText(RegisterActivity.this, "该用户已注册", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }

            }
        });


    }


    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mButton_getCode.setClickable(true);
            mButton_getCode.setText("重新获取");
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            mButton_getCode.setClickable(false);
            mButton_getCode.setText(millisUntilFinished / 1000 + "s");
        }
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isBlank(String str) {
        boolean isBlank = false;
        if (null == str || str.equals("") || str.equals("null")
                || str.trim().length() == 0) {
            isBlank = true;
        }
        return isBlank;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        boolean isEmpty = false;
        if (null == str || str.equals("") || str.equals("null") || str.trim().length() == 0) {
            isEmpty = true;
        }
        return isEmpty;
    }

}
