package com.dyhx.kdtask.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.dyhx.kdtask.db.DyhxRing;
import com.dyhx.kdtask.db.support.GreenDBManager;


/**
 * Created by zhoujun on 2019/12/17.
 * Describe:Application基类，不包含任何业务相关代码
 */
public class BaseApp extends Application {
    private static BaseApp mContext;
    private static Handler mMainThreadHandler;
    private static Looper mMainThreadLooper;
    private static Thread mMainThread;
    private static int mMainThreadId;
    GreenDBManager greenDBManager;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mMainThreadHandler = new Handler();
        mMainThreadLooper = this.getMainLooper();
        mMainThread = Thread.currentThread();
        mMainThreadId = Process.myTid();
        greenDBManager = new GreenDBManager();
        DyhxRing.configureDB(greenDBManager);
        DyhxRing.create();
    }

    public static BaseApp getApplication() {
        return mContext;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    public GreenDBManager getGreenDBManager() {
        return greenDBManager;
    }

    public void setGreenDBManager(GreenDBManager greenDBManager) {
        this.greenDBManager = greenDBManager;
    }
}

