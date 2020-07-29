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
import com.dyhx.kdtask.db.model.MinuteTaskMd;
import com.hnxx.wisdombase.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.dyhx.kdtask.MainActivity.DAY;
import static com.dyhx.kdtask.MainActivity.HOUR;


public class TaskMinuteFragment extends BaseFragment {
    RecyclerView mRcv;
    TaskMinuteAdapter mTaskMinuteAdapter;
    List<MinuteTaskMd> dayTaskMds = new ArrayList<>();
    int day,hour;
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
        mTaskMinuteAdapter = new TaskMinuteAdapter();

        mTaskMinuteAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if(HOUR < 0){
                    Toast.makeText(getContext(),"请先新建时任务！",Toast.LENGTH_SHORT).show();
                    return;
                }

                TaskModel taskModel = (TaskModel) adapter.getItem(position);
                String ts = taskModel.getTimeMark();
                if(taskModel.getMinuteTaskMd() == null){
                    AddDialog dialog = new AddDialog(getContext());
                    dialog.setOnClickBottomListener(new AddDialog.OnClickBottomListener() {

                        @Override
                        public void onPositiveClick(String tName, String tMsg) {
                            MinuteTaskMd minuteTaskMd = new MinuteTaskMd();
                            minuteTaskMd.setDay(DAY);
                            minuteTaskMd.setHour(HOUR);
                            minuteTaskMd.setMinute(position);
                            minuteTaskMd.setName(tName);
                            minuteTaskMd.setDescribe(tMsg);
                            minuteTaskMd.setStartTime(ts);
                            boolean action = DyhxRing.tableManager(MinuteTaskMd.class).insertOne(minuteTaskMd);
                            //TODO insert task
                            if(action) {
                                setBottomDayData();
                                Log.e("dy_db", "添加成功");
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

                }


            }
        });


        mTaskMinuteAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                if(DAY < 0 && HOUR<0){
                    Toast.makeText(getContext(),"请先确认选择上时间点",Toast.LENGTH_SHORT).show();
                    return false;
                }

                if(deleteTask(position)){
                    Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(1);
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
        int b = HOUR;
        String[] whereValue = {a+"",b+""};
        StringBuffer mySql = new StringBuffer("select * from MINUTE_TASK_MD where day = ? and hour = ?");
        dayTaskMds = DyhxRing.tableManager(MinuteTaskMd.class).queryBySQL(mySql.toString(),whereValue);
        List<TaskModel> mds = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            TaskModel md = new TaskModel();
            md.setTimeMark(i == 0? hour+":00":hour+":"+((i*5)<10?("0"+(i*5)):(i*5)));
            for (int j = 0; j < dayTaskMds.size(); j++) {
                MinuteTaskMd dTmd = dayTaskMds.get(j);
                if(dTmd.getMinute() == i){
                    md.setMinuteTaskMd(dTmd);
                    break;
                }
            }
            mds.add(md);
        }

        mRcv.setAdapter(mTaskMinuteAdapter);
        mTaskMinuteAdapter.setNewData(mds);

        if(!isBottomAdd) {
            View headView = getLayoutInflater().inflate(R.layout.task_day_item_view, null);
            TextView tvTime = headView.findViewById(R.id.tv_time);
            tvTime.setText("0:60");
            mTaskMinuteAdapter.addFooterView(headView);
            isBottomAdd = true;
        }


    }

    public void setDay(int day) {
        this.day = day;
    }


    private boolean deleteTask(int position){
        StringBuffer delSql = new StringBuffer
                ("delete from MINUTE_TASK_MD where minute = '");
        delSql.append(position);
        delSql.append("'");
        delSql.append(" and day = '");
        delSql.append(DAY);
        delSql.append("'");
        delSql.append(" and hour = '");
        delSql.append(HOUR);
        delSql.append("'");
        boolean delBool = DyhxRing.tableManager(MinuteTaskMd.class).execSQL(delSql.toString());
        return  delBool;
    }
}
