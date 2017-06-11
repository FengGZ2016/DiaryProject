package com.example.administrator.diaryproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/12.
 */

public class DiaryViewHolder extends RecyclerView.ViewHolder{

    LinearLayout layout;
    TextView delete;
    TextView mTextView_diaryTitle;
    TextView mTextView_diaryTime;
    TextView mTextView_diaryId;

    public DiaryViewHolder(View itemView) {
        super(itemView);
        layout= (LinearLayout) itemView.findViewById(R.id.item_layout);
        delete= (TextView) itemView.findViewById(R.id.item_delete);
        mTextView_diaryTitle= (TextView) itemView.findViewById(R.id.diary_title_item);
        mTextView_diaryTime= (TextView) itemView.findViewById(R.id.diary_time_item);
        mTextView_diaryId= (TextView) itemView.findViewById(R.id.diary_id_item);
    }
}
