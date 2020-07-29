package com.dyhx.kdtask;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.dyhx.kdtask.MainActivity.DAY;
import static com.dyhx.kdtask.MainActivity.HOUR;


public class TaskDayFragment extends BaseFragment {
    RecyclerView mRcv;
    TaskDayAdapter mTaskDayAdapter;
    List<DayTaskMd> dayTaskMds = new ArrayList<>();
    boolean isBottomAdd;

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
        mTaskDayAdapter = new TaskDayAdapter();

        setBottomDayData();

        mTaskDayAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TaskModel taskModel = (TaskModel) adapter.getItem(position);
                if(position+1 != DAY){
                    HOUR = -2;
                }
                if(taskModel.getDayTaskMd() == null){
                    AddDialog dialog = new AddDialog(getContext());
                    dialog.setOnClickBottomListener(new AddDialog.OnClickBottomListener() {

                        @Override
                        public void onPositiveClick(String tName, String tMsg) {
                            DayTaskMd dayTaskMd = new DayTaskMd();
                            dayTaskMd.setDay(position+1);
                            dayTaskMd.setName(tName);
                            dayTaskMd.setDescribe(tMsg);
                            dayTaskMd.setStartTime("2020年7月"+(position+1)+"日");
                            dayTaskMd.setEndTime(position == 30? "2020年8月1日":"2020年7月"+(position+2)+"日");
                            boolean action = DyhxRing.tableManager(DayTaskMd.class).insertOne(dayTaskMd);
                            //TODO insert task
                            if(action) {
                                DAY = position +1;
                                Log.e("dy_db", "添加成功");
                                setBottomDayData();
                                dialog.dismiss();
                            } else {
                                Log.e("dy_db", "添加失败");
                            }
                        }

                        @Override
                        public void onNegtiveClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    DAY = position +1;
                    mTaskDayAdapter.notifyDataSetChanged();
                    getFirstHour(position);
                    DetailsDialog dialog = new DetailsDialog(getContext(),startTime,endTime
                            ,"任务名称： "+taskModel.getDayTaskMd().getName(),taskModel.getDayTaskMd().getDescribe());
                    dialog.show();
                }


            }
        });


        mTaskDayAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if(deleteTask(position)){
                    Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(1);
                            DAY = -1;
                            HOUR = -1;
                        }
                    }, 2000);

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

        dayTaskMds = DyhxRing.tableManager(DayTaskMd.class).loadAll();
        List<TaskModel> mds = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            TaskModel md = new TaskModel();
            for (int j = 0; j < dayTaskMds.size(); j++) {
                DayTaskMd dTmd = dayTaskMds.get(j);
                if(dTmd.getDay() == i+1){
                    md.setDayTaskMd(dTmd);
                    break;
                }
            }
            mds.add(md);
        }

        mRcv.setAdapter(mTaskDayAdapter);
        mTaskDayAdapter.setNewData(mds);

        if(!isBottomAdd){
            View headView = getLayoutInflater().inflate(R.layout.task_day_item_view,null);
            TextView tvTime = headView.findViewById(R.id.tv_time);
            tvTime.setText("8月1日");
            mTaskDayAdapter.addFooterView(headView);
            isBottomAdd = true;
        }

    }


    private boolean deleteTask(int position){
        //TODO 这里是需要开启事务 未处理
        StringBuffer delSql0 = new StringBuffer
                ("delete from DAY_TASK_MD where day = '");
        delSql0.append((position+1));
        delSql0.append("'");
        boolean del0 = DyhxRing.tableManager(DayTaskMd.class).execSQL(delSql0.toString());

        StringBuffer delSql = new StringBuffer
                ("delete from HOUR_TASK_MD where day = '");
        delSql.append((position+1));
        delSql.append("'");
        boolean del1 = DyhxRing.tableManager(HourTaskMd.class).execSQL(delSql.toString());

        StringBuffer delSqlMinute = new StringBuffer
                ("delete from MINUTE_TASK_MD where day = '");
        delSqlMinute.append((position+1));
        delSqlMinute.append("'");
        boolean del2 = DyhxRing.tableManager(MinuteTaskMd.class).execSQL(delSqlMinute.toString());
        return  del0 && del1 && del2;
    }


    String startTime,endTime;
    private void getFirstHour(int posrtion){
        String[] whereValue = {(posrtion+1)+""};
        StringBuffer mySql = new StringBuffer("select * from HOUR_TASK_MD where day = ? ORDER BY  hour asc");
        List<HourTaskMd> hourTaskMds = DyhxRing.tableManager(HourTaskMd.class).queryBySQL(mySql.toString(),whereValue);


        if(hourTaskMds==null || hourTaskMds.size() == 0){
            startTime = "2020年7月"+(posrtion+1)+"日";
            endTime = posrtion == 30?"2020年8月1日":"2020年7月"+(posrtion+1+1)+"日";
            return;
        } else if(hourTaskMds.size() == 1){

            HourTaskMd hourTaskMd = hourTaskMds.get(0);
            int hour = hourTaskMd.getHour();

            String[] whereValue1 = {hour+""};
            StringBuffer mySql1 = new StringBuffer("select * from MINUTE_TASK_MD where day = ? ORDER BY  minute asc");
            List<MinuteTaskMd> minuteTaskMds = DyhxRing.tableManager(MinuteTaskMd.class).queryBySQL(mySql1.toString(),whereValue1);

            if(minuteTaskMds.size() == 0){
                startTime = "2020年7月"+(posrtion+1)+"日" + hour +":00";
                endTime = (posrtion == 30?"2020年7月31日":"2020年7月"+(posrtion+1)+"日") + (hour+1)+":00";
            } else if(minuteTaskMds.size() == 1){

                MinuteTaskMd minuteTaskMd = minuteTaskMds.get(0);
                int minute = minuteTaskMd.getMinute()*5;
                startTime = "2020年7月"+(posrtion+1)+"日" + hour + ":"+(minute<10?"0"+minute:minute);
                endTime = (posrtion == 30?"2020年7月31日":("2020年7月"+(posrtion+1)+"日")) + hour + ":"+(minute+5<10?"0"+(minute+5):(minute+5));
            } else {
                MinuteTaskMd sminuteTaskMd = minuteTaskMds.get(0);
                MinuteTaskMd eminuteTaskMd = minuteTaskMds.get(minuteTaskMds.size()-1);
                int st = sminuteTaskMd.getMinute()*5;
                int et = eminuteTaskMd.getMinute()*5;
                startTime = "2020年7月"+(posrtion+1)+"日" + hour + ":"+(st<10?"0"+st:st);
                endTime = (posrtion == 30?"2020年7月31日":("2020年7月"+(posrtion+1)+"日")) + hour + ":"+(et<10?"0"+et:et);
            }

            return;
        } else {

            HourTaskMd shourTaskMd = hourTaskMds.get(0);
            HourTaskMd ehourTaskMd = hourTaskMds.get(hourTaskMds.size()-1);

            int shour = shourTaskMd.getHour();


            String[] whereValue1 = {shour+""};
            StringBuffer mySql1 = new StringBuffer("select * from MINUTE_TASK_MD where day = ? ORDER BY  minute asc");
            List<MinuteTaskMd> minuteTaskMds = DyhxRing.tableManager(MinuteTaskMd.class).queryBySQL(mySql1.toString(),whereValue1);

            if(minuteTaskMds!=null && !minuteTaskMds.isEmpty()){
                int st =  minuteTaskMds.get(0).getMinute()*5;
                startTime = "2020年7月"+(posrtion+1)+"日" + shour + ":"+(st<10?"0"+st:st);
            } else {
                startTime = "2020年7月"+(posrtion+1)+"日" + (shour==0?"00":shour) + ":"+"00";
            }

            int ehour = ehourTaskMd.getHour();
            String[] whereValue2 = {ehour+""};
            StringBuffer mySql2 = new StringBuffer("select * from MINUTE_TASK_MD where day = ? ORDER BY  minute asc");
            List<MinuteTaskMd> minuteTaskMds2 = DyhxRing.tableManager(MinuteTaskMd.class).queryBySQL(mySql2.toString(),whereValue2);

            if(minuteTaskMds2!=null && !minuteTaskMds2.isEmpty()){
                int st =  minuteTaskMds.get(minuteTaskMds2.size()-1).getMinute()*5;
                endTime = "2020年7月"+(posrtion+1)+"日" + ehour + ":"+(st<10?"0"+st:st);
            } else {
                endTime = "2020年7月"+(posrtion+1)+"日" + ehour + ":"+"00";
            }

        }

    }

}
