package com.dyhx.kdtask.db;


import androidx.collection.SimpleArrayMap;
import androidx.core.util.Preconditions;

import com.dyhx.kdtask.db.greendao.DaoSession;
import com.dyhx.kdtask.db.support.GreenDBManager;
import com.dyhx.kdtask.db.support.IDBManager;
import com.dyhx.kdtask.db.support.ITableManger;

/**
 * Created by  zhoujun
 * on 2020/3/11
 * Desc:
 */
public class DyhxRing {


    private static IDBManager mDBManager;
    private static SimpleArrayMap<Object, ITableManger> mapTableManager = new SimpleArrayMap();
    private static DaoSession mDaoSession;
    /**
     * 开始构建
     */
    public static void create() {
        //数据库模块的构建工作
        if (mDBManager != null) {
            mDBManager.init();
            mDBManager.putTableManager(mapTableManager);
        }
    }


    /**
     * 配置数据库模块
     */
    public static void configureDB(IDBManager dbManager) {
        mDBManager = dbManager;
    }

    public static DaoSession getDaoSession(GreenDBManager greenDBManager) {
        return greenDBManager.getmDaoSession();
    }


    /**
     * 获取数据库管理者
     */
    public static <T extends IDBManager> T dbManager() {
        return (T) Preconditions.checkNotNull(mDBManager, "请先在Application中调用DevRing.configureDB(IDBManager)方法设置数据库管理类");
    }

    /**
     * 获取数据表管理者
     */
    public static <T extends ITableManger> T tableManager(Object key) {
        return (T) Preconditions.checkNotNull(mapTableManager.get(key), "没找到该Key值对应的数据表管理者，请检查IDBManager实现类中的putTableManager(Map<Object,ITableManager>)方法");
    }


}
