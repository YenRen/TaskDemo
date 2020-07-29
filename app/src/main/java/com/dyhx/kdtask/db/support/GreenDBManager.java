package com.dyhx.kdtask.db.support;

import androidx.collection.SimpleArrayMap;

import com.dyhx.kdtask.base.BaseApp;
import com.dyhx.kdtask.db.greendao.DaoMaster;
import com.dyhx.kdtask.db.greendao.DaoSession;
import com.dyhx.kdtask.db.greendao.DayTaskMdDao;
import com.dyhx.kdtask.db.greendao.HourTaskMdDao;
import com.dyhx.kdtask.db.greendao.MinuteTaskMdDao;
import com.dyhx.kdtask.db.manager.DayTaskMdGreenTableManager;
import com.dyhx.kdtask.db.manager.HourTaskMdGreenTableManager;
import com.dyhx.kdtask.db.manager.MinuteTaskMdGreenTableManager;
import com.dyhx.kdtask.db.model.DayTaskMd;
import com.dyhx.kdtask.db.model.HourTaskMd;
import com.dyhx.kdtask.db.model.MinuteTaskMd;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

/**
 * description:
 * 由于GreenDao的特殊性以及具体数据表的不确定，无法很好地集成到DevRing当中。
 * 所以需要实现IDBManager接口，并通过DevRing.configureDB()方法传入。
 * 1.在init()中对数据库进行初始化操作，如建库建表。
 * 2.在putTableManager()方法中将数据表管理者存进参数map中，请记清楚key值
 *   后面对数据表的操作是通过DevRing.tableManager(key)方法得到数据表管理者，然后进行相关增删改查。
 * 3.可在本类中添加IDBManager接口以外的方法
 * ，然后通过DevRing.<GreenDBManager>dbManager()来调用。
 *
 * https://www.jianshu.com/p/11bdd9d761e6
 */

public class GreenDBManager implements IDBManager {

    DaoSession mDaoSession;
    DayTaskMdGreenTableManager dayTaskMdGreenTableManager;
    HourTaskMdGreenTableManager hourTaskMdGreenTableManager;
    MinuteTaskMdGreenTableManager minuteTaskMdGreenTableManager;

    @Override
    public void init() {

        String dbName = "dyhx_green.db";
        Integer dbVersion = DaoMaster.SCHEMA_VERSION;
        Class<? extends AbstractDao<?, ?>>[] classes = new Class[]{
                DayTaskMdDao.class,
                HourTaskMdDao.class,
                MinuteTaskMdDao.class
           };
        //这里使用DevRing提供的GreenOpenHelper对DaoMaster进行初始化，这样就可以实现数据库升级时的数据迁移
        //默认的DaoMaster.OpenHelper不具备数据迁移功能，它会在数据库升级时将数据删除。
        GreenOpenHelper openHelper = new GreenOpenHelper(BaseApp.getApplication(), dbName, dbVersion, classes);
        DaoMaster daoMaster = new DaoMaster(openHelper.getWritableDatabase());
//      DaoMaster daoMaster = new DaoMaster(greenOpenHelper.getEncryptedWritableDb("your_secret"));//加密
        mDaoSession = daoMaster.newSession();
        dayTaskMdGreenTableManager = new DayTaskMdGreenTableManager(mDaoSession);
        hourTaskMdGreenTableManager = new HourTaskMdGreenTableManager(mDaoSession);
        minuteTaskMdGreenTableManager = new MinuteTaskMdGreenTableManager(mDaoSession);

        //查看数据库更新版本时数据迁移的log
        MigrationHelper.DEBUG = true;
        //数据库增删改查时的log
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        //清空缓存
        mDaoSession.clear();
    }

    @Override
    public void putTableManager(SimpleArrayMap<Object, ITableManger> mapTables) {
        mapTables.put(DayTaskMd.class, dayTaskMdGreenTableManager);
        mapTables.put(HourTaskMd.class, hourTaskMdGreenTableManager);
        mapTables.put(MinuteTaskMd.class, minuteTaskMdGreenTableManager);
    }


    public DaoSession getmDaoSession() {
        return mDaoSession;
    }
}
