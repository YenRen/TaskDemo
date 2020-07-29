package com.dyhx.kdtask.db.manager;

import com.dyhx.kdtask.db.greendao.DaoSession;
import com.dyhx.kdtask.db.model.HourTaskMd;
import com.dyhx.kdtask.db.model.MinuteTaskMd;
import com.dyhx.kdtask.db.support.GreenTableManager;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by YeRen on 2020/7/29.
 * Describe:
 */
public class MinuteTaskMdGreenTableManager extends GreenTableManager<MinuteTaskMd, Long> {

    private DaoSession mDaoSession;

    public MinuteTaskMdGreenTableManager(DaoSession daoSession) {
        this.mDaoSession = daoSession;
    }

    @Override
    public AbstractDao<MinuteTaskMd, Long> getDao() {
        return mDaoSession.getMinuteTaskMdDao();
    }
}
