package com.dyhx.kdtask;

import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.dyhx.kdtask.db.DyhxRing;
import com.dyhx.kdtask.db.model.DayTaskMd;
import com.hnxx.wisdombase.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


public class TaskFragment extends BaseFragment {
    RecyclerView mRcv;
    TaskDayAdapter mTaskDayAdapter;
    int type = 2;
    List<DayTaskMd> dayTaskMds = new ArrayList<>();

    TaskMinuteFragment taskMinuteFragment;
    TaskHourFragment taskHourFragment;
    TaskDayFragment taskDayFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task;
    }

    @Override
    protected void setListener() {
        RadioGroup mRgView =  findViewById(R.id.time_control);
        mRgView.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case  R.id.rb_minute:
                    type = 0;
                    switchFragment(taskMinuteFragment);
                    break;
                case  R.id.rb_hour:
                    type = 1;
                    switchFragment(taskHourFragment);
                    break;
                case  R.id.rb_day:
                    type = 2;
                    switchFragment(taskDayFragment);
                    break;
            }
        });
    }

    @Override
    protected void init() {
        taskDayFragment = new TaskDayFragment();
        taskHourFragment = new TaskHourFragment();
        taskMinuteFragment = new TaskMinuteFragment();

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();//注意！每次要执行commit()方法的时候都需要重新获取一次FragmentTransaction，否则用已经commit过的FragmentTransaction再次commit会报错！
        transaction.replace(R.id.frg_view,taskDayFragment);
        transaction.commit();
        currentFragment = taskDayFragment;


    }


    private void setBottomDayData(){

        dayTaskMds = DyhxRing.tableManager(DayTaskMd.class).loadAll();
        List<TaskModel> mds = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
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


    }

    private void setBottomHourData(){
        List<TaskModel> mds = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            TaskModel md = new TaskModel();
            mds.add(md);
        }

        mRcv.setAdapter(mTaskDayAdapter);
        mTaskDayAdapter.setNewData(mds);

    }


    Fragment currentFragment;
    //正确的做法
    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction
                    .hide(currentFragment)
                    .add(R.id.frg_view, targetFragment)
                    .commit();
            System.out.println("还没添加呢");
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment)
                    .commit();
            System.out.println("添加了( ⊙o⊙ )哇");
        }
        currentFragment = targetFragment;
    }

}
