package com.dyhx.kdtask.db.manager;

import com.dyhx.kdtask.db.greendao.DaoSession;
import com.dyhx.kdtask.db.model.DayTaskMd;
import com.dyhx.kdtask.db.model.HourTaskMd;
import com.dyhx.kdtask.db.support.GreenTableManager;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by YeRen on 2020/7/29.
 * Describe:
 */
public class HourTaskMdGreenTableManager extends GreenTableManager<HourTaskMd, Long> {

    private DaoSession mDaoSession;

    public HourTaskMdGreenTableManager(DaoSession daoSession) {
        this.mDaoSession = daoSession;
    }

    @Override
    public AbstractDao<HourTaskMd, Long> getDao() {
        return mDaoSession.getHourTaskMdDao();
    }
}
