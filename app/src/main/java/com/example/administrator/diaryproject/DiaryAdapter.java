package com.example.administrator.diaryproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.administrator.diaryproject.model.Diary;

import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */

public class DiaryAdapter extends RecyclerView.Adapter{

    //数据源
    private List<Diary> mDiaryList;
    private Context mContext;
    private LayoutInflater mInflater;

    public DiaryAdapter(List<Diary> mDiaryList,Context context){
        this.mDiaryList=mDiaryList;
        mContext=context;
        mInflater = LayoutInflater.from(context);
    }

//    public class DiaryViewHolder extends RecyclerView.ViewHolder {
//        LinearLayout layout;
//        TextView delete;
//        TextView mTextView_diaryTitle;
//        TextView mTextView_diaryTime;
//        TextView mTextView_diaryId;
//        View diaryView;
//        public DiaryViewHolder(View itemView) {
//            super(itemView);
//            layout= (LinearLayout) itemView.findViewById(R.id.item_layout);
//            delete= (TextView) itemView.findViewById(R.id.item_delete);
//            mTextView_diaryTitle= (TextView) itemView.findViewById(R.id.diary_title_item);
//            mTextView_diaryTime= (TextView) itemView.findViewById(R.id.diary_time_item);
//            mTextView_diaryId= (TextView) itemView.findViewById(R.id.diary_id_item);
//            diaryView=itemView;
//        }
//    }

//

    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item,parent,false);
       // final DiaryViewHolder holder=new DiaryViewHolder(view);
       return new DiaryViewHolder(mInflater.inflate(R.layout.diary_item,parent,false));
       // return new BaseViewHolder(layoutInflater.inflate(R.layout.delete_item,parent,false));
        //给item子项设置监听事件
//        holder.diaryView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //打开查看日记的活动
//                int position=holder.getAdapterPosition();
//                Diary diary=mDiaryList.get(position);
//                Intent intent=new Intent();
//                intent.setClass(mContext,ShowDiaryActivity.class);
//                intent.putExtra("DiaryTitle",diary.getTitle());
//                intent.putExtra("DiaryTime",diary.getTime());
//                intent.putExtra("DiaryContent",diary.getContent());
//                mContext.startActivity(intent);
//            }
//        });

      //  return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DiaryViewHolder viewHolder= (DiaryViewHolder) holder;
        Diary diary=mDiaryList.get(position);
       //用于对recycleView的子项进行赋值的复写方法
        viewHolder.mTextView_diaryTitle.setText(diary.getTitle());
        viewHolder.mTextView_diaryTime.setText(diary.getTime());
        viewHolder.mTextView_diaryId.setText(diary.getId()+"");
    }



    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }



}
