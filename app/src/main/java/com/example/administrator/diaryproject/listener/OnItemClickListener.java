package com.example.administrator.diaryproject.listener;

import android.view.View;

/**
 * Created by Administrator on 2017/5/11.
 */

public interface OnItemClickListener {
    /**
     * item点击回调
     *
     * @param view
     * @param position
     */
    void onItemClick(View view, int position);

    /**
     * 删除按钮回调
     *
     * @param position
     */
    void onDeleteClick(int position);

}
