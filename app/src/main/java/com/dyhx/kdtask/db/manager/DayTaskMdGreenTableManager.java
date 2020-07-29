package com.dyhx.kdtask.db.manager;

import com.dyhx.kdtask.db.greendao.DaoSession;
import com.dyhx.kdtask.db.model.DayTaskMd;
import com.dyhx.kdtask.db.support.GreenTableManager;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by YeRen on 2020/7/29.
 * Describe:
 */
public class DayTaskMdGreenTableManager extends GreenTableManager<DayTaskMd, Long> {

    private DaoSession mDaoSession;

    public DayTaskMdGreenTableManager(DaoSession daoSession) {
        this.mDaoSession = daoSession;
    }

    @Override
    public AbstractDao<DayTaskMd, Long> getDao() {
        return mDaoSession.getDayTaskMdDao();
    }
}
