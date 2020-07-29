package com.dyhx.kdtask;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.dyhx.kdtask.db.DyhxRing;
import com.dyhx.kdtask.db.model.DayTaskMd;
import com.dyhx.kdtask.db.model.HourTaskMd;
import com.dyhx.kdtask.db.model.MinuteTaskMd;
import com.hnxx.wisdombase.ui.base.BaseFragment;
import com.hnxx.wisdombase.ui.widget.CustomToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.dyhx.kdtask.MainActivity.DAY;
import static com.dyhx.kdtask.MainActivity.HOUR;


public class TaskHourFragment extends BaseFragment {
    RecyclerView mRcv;
    TaskHourAdapter mTaskDayAdapter;
    List<HourTaskMd> dayTaskMds = new ArrayList<>();
    int day;
    Handler handler =  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    setBottomDayData();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.task_day_view;
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void init() {
        mRcv =  findViewById(R.id.rcv);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRcv.setLayoutManager(manager);
        mTaskDayAdapter = new TaskHourAdapter();

        setBottomDayData();

        mTaskDayAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if(HOUR == -1){
                    Toast.makeText(getContext(),"请先新建日任务！",Toast.LENGTH_SHORT).show();
                    return;
                }
                TaskModel taskModel = (TaskModel) adapter.getItem(position);
                String ts = taskModel.getTimeMark();
                if(taskModel.getHourTaskMd() == null){
                    AddDialog dialog = new AddDialog(getContext());
                    dialog.setOnClickBottomListener(new AddDialog.OnClickBottomListener() {

                        @Override
                        public void onPositiveClick(String tName, String tMsg) {
                            HourTaskMd hourTaskMd = new HourTaskMd();
                            hourTaskMd.setDay(DAY);
                            hourTaskMd.setHour(position);
                            hourTaskMd.setName(tName);
                            hourTaskMd.setDescribe(tMsg);
                            hourTaskMd.setStartTime(ts);
                            hourTaskMd.setEndTime((position+1)+":00");
                            boolean action = DyhxRing.tableManager(HourTaskMd.class).insertOne(hourTaskMd);
                            //TODO insert task
                            if(action) {
                                MainActivity.HOUR = position;
                                Log.e("dy_db", "添加成功");
                                setBottomDayData();
                            } else {
                                Log.e("dy_db", "添加失败");
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegtiveClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    MainActivity.HOUR = position;
                    mTaskDayAdapter.notifyDataSetChanged();
                    getFirstHour(position);
                    DetailsDialog dialog = new DetailsDialog(getContext(),startTime,endTime
                            ,"任务名称： "+taskModel.getHourTaskMd().getName(),taskModel.getHourTaskMd().getDescribe());
                    dialog.show();
                }


            }
        });
        mTaskDayAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if(DAY < 0 ){
                    Toast.makeText(getContext(),"请先确认选择上时间点",Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(deleteTask(position)){
                    Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(1);
                            HOUR = -1;
                        }
                    }, 1000);

                } else {
                    Toast.makeText(getContext(),"删除操作失败",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });


    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setBottomDayData();
    }

    private void setBottomDayData(){
        int a = DAY;
        String[] whereValue = {a+""};
        StringBuffer mySql = new StringBuffer("select * from HOUR_TASK_MD where day = ?");
        dayTaskMds = DyhxRing.tableManager(HourTaskMd.class).queryBySQL(mySql.toString(),whereValue);
        List<TaskModel> mds = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            TaskModel md = new TaskModel();
            md.setTimeMark(i == 0? "00:00":i+":00");
            for (int j = 0; j < dayTaskMds.size(); j++) {
                HourTaskMd dTmd = dayTaskMds.get(j);
                if(dTmd.getHour() == i){
                    md.setHourTaskMd(dTmd);
                    break;
                }
            }
            mds.add(md);
        }

        mRcv.setAdapter(mTaskDayAdapter);
        mTaskDayAdapter.setNewData(mds);


    }

    public void setDay(int day) {
        this.day = day;
    }


    private boolean deleteTask(int position){
        //TODO 这里是需要开启事务 未处理

        StringBuffer delSql = new StringBuffer
                ("delete from HOUR_TASK_MD where hour = '");
        delSql.append(position);
        delSql.append("'");
        delSql.append(" and day = '");
        delSql.append(DAY);
        delSql.append("'");
        boolean del1 = DyhxRing.tableManager(HourTaskMd.class).execSQL(delSql.toString());

        StringBuffer delSqlMinute = new StringBuffer
                ("delete from MINUTE_TASK_MD where hour = '");
        delSqlMinute.append(position);
        delSqlMinute.append("'");
        delSqlMinute.append(" and day = '");
        delSqlMinute.append(DAY);
        delSqlMinute.append("'");
        boolean del2 = DyhxRing.tableManager(MinuteTaskMd.class).execSQL(delSqlMinute.toString());
        return  del1 && del2;
    }



    String startTime,endTime;
    private void getFirstHour(int posrtion){
        String[] whereValue = {DAY+"",posrtion+""};
        StringBuffer mySql = new StringBuffer("select * from MINUTE_TASK_MD where day = ? and hour = ? ORDER BY  minute asc");
        List<MinuteTaskMd> minuteTaskMds = DyhxRing.tableManager(MinuteTaskMd.class).queryBySQL(mySql.toString(),whereValue);

        if(minuteTaskMds==null||minuteTaskMds.size() == 0){
            startTime = "2020年7月"+DAY+"日"+posrtion+":00";
            endTime = "2020年7月"+DAY+"日"+(posrtion+1)+":00";
            return;
        } else if(minuteTaskMds.size() == 1){

            int minute = minuteTaskMds.get(0).getMinute()*5;
            startTime = "2020年7月"+DAY+"日"+posrtion+":"+(minute<10?"0"+minute:minute);
            endTime = "2020年7月"+DAY+"日"+posrtion+":"+((minute+5)<10?"0"+(minute+5):(minute+5));

        } else {

            MinuteTaskMd sminuteTaskMd = minuteTaskMds.get(0);
            MinuteTaskMd eminuteTaskMd = minuteTaskMds.get(minuteTaskMds.size()-1);

            int st = sminuteTaskMd.getMinute()*5;
            int et = eminuteTaskMd.getMinute()*5;

            startTime = "2020年7月"+DAY+"日"+posrtion+":"+(st<10?"0"+st:st);
            endTime = "2020年7月"+DAY+"日"+posrtion+":"+(et<10?"0"+et:et);
        }

    }

}
