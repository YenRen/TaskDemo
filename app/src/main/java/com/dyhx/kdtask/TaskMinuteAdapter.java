package com.dyhx.kdtask;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dyhx.kdtask.db.model.HourTaskMd;
import com.dyhx.kdtask.db.model.MinuteTaskMd;


/**
 * Created by YeRen on 2020/7/28.
 * Describe:
 */
public class TaskMinuteAdapter extends BaseQuickAdapter<TaskModel, BaseViewHolder> {


    public TaskMinuteAdapter() {
        super(R.layout.task_day_item_view);
    }

    @Override
    protected void convert(BaseViewHolder holder, TaskModel dayTaskMd) {

        MinuteTaskMd minuteTaskMd = dayTaskMd.getMinuteTaskMd();
        holder.setText(R.id.tv_time,dayTaskMd.getTimeMark());

        if(minuteTaskMd!=null) {
            holder.setText(R.id.spacer_view, "TaskName: "+ dayTaskMd.getMinuteTaskMd().getName());
            holder.setBackgroundColor(R.id.spacer_view,getContext().getResources().getColor(R.color.color_ebebeb));
        } else {
            holder.setText(R.id.spacer_view, "");
            holder.setBackgroundColor(R.id.spacer_view,getContext().getResources().getColor(R.color.white));
        }

    }
}
