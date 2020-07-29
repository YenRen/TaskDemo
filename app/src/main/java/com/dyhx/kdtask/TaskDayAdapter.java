package com.dyhx.kdtask;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;


/**
 * Created by YeRen on 2020/7/28.
 * Describe:
 */
public class TaskDayAdapter extends BaseQuickAdapter<TaskModel, BaseViewHolder> {


    public TaskDayAdapter() {
        super(R.layout.task_day_item_view);
    }

    @Override
    protected void convert(BaseViewHolder holder, TaskModel dayTaskMd) {


        if(holder.getPosition() == 31){
            holder.setText(R.id.tv_time,"8月"+1+"日");
            holder.getView(R.id.spacer_view).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.spacer_view).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_time,"7月"+(holder.getLayoutPosition()+1)+"日");
        }
        if(dayTaskMd.getDayTaskMd()!=null) {
            holder.setText(R.id.spacer_view, "TaskName: "+ dayTaskMd.getDayTaskMd().getName());
            holder.setBackgroundColor(R.id.spacer_view,getContext().getResources().getColor(R.color.color_ebebeb));
        } else {
            holder.setText(R.id.spacer_view, "");
            holder.setBackgroundColor(R.id.spacer_view,getContext().getResources().getColor(R.color.white));
        }

        if(holder.getPosition() == MainActivity.DAY -1){
            holder.setBackgroundColor(R.id.spacer_view,getContext().getResources().getColor(R.color.blue));
        }

    }
}
