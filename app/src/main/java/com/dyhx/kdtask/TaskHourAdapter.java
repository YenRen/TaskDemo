package com.dyhx.kdtask;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dyhx.kdtask.db.model.HourTaskMd;


/**
 * Created by YeRen on 2020/7/28.
 * Describe:
 */
public class TaskHourAdapter extends BaseQuickAdapter<TaskModel, BaseViewHolder> {


    public TaskHourAdapter() {
        super(R.layout.task_day_item_view);
    }

    @Override
    protected void convert(BaseViewHolder holder, TaskModel dayTaskMd) {

        HourTaskMd hourTaskMd = dayTaskMd.getHourTaskMd();
        holder.setText(R.id.tv_time,dayTaskMd.getTimeMark());

        if(hourTaskMd!=null) {
            holder.setText(R.id.spacer_view, "TaskName: "+ dayTaskMd.getHourTaskMd().getName());
            holder.setBackgroundColor(R.id.spacer_view,getContext().getResources().getColor(R.color.color_ebebeb));
        } else {
            holder.setText(R.id.spacer_view, "");
            holder.setBackgroundColor(R.id.spacer_view,getContext().getResources().getColor(R.color.white));
        }

        if(holder.getPosition() == MainActivity.HOUR){
            holder.setBackgroundColor(R.id.spacer_view,getContext().getResources().getColor(R.color.blue));
        }

    }
}
