/*
 * Copyright (C) 2011-2016 Markus Junginger, greenrobot (http://greenrobot.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dyhx.kdtask.db.support;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;

/**
 * SQLiteOpenHelper to allow working with greenDAO's {@link Database} abstraction to create and update database schemas.
 *
 * 修复在部分高版本系统手机上运行时报错崩溃
 * Rejecting re-init on previously-failed class java.lang.Class<org.greenrobot.greendao.database.DatabaseOpenHelper$EncryptedHelper>:
 * java.lang.NoClassDefFoundError: Failed resolution of: Lnet/sqlcipher/database/SQLiteOpenHelper;
 * 具体原因及解决方案请查看issue <a>https://github.com/greenrobot/greenDAO/issues/428</a>
 */
public abstract class DatabaseOpenHelperFixed extends SQLiteOpenHelper {

    private final Context context;
    private final String name;
    private final int version;


    public DatabaseOpenHelperFixed(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public DatabaseOpenHelperFixed(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
        this.name = name;
        this.version = version;
    }

    /**
     * Like {@link #getWritableDatabase()}, but returns a greenDAO abstraction of the database.
     * The backing DB is an standard {@link SQLiteDatabase}.
     */
    public Database getWritableDb() {
        return wrap(getWritableDatabase());
    }

    /**
     * Like {@link #getReadableDatabase()}, but returns a greenDAO abstraction of the database.
     * The backing DB is an standard {@link SQLiteDatabase}.
     */
    public Database getReadableDb() {
        return wrap(getReadableDatabase());
    }

    protected Database wrap(SQLiteDatabase sqLiteDatabase) {
        return new StandardDatabase(sqLiteDatabase);
    }

    /**
     * Delegates to {@link #onCreate(Database)}, which uses greenDAO's database abstraction.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        onCreate(wrap(db));
    }

    /**
     * Override this if you do not want to depend on {@link SQLiteDatabase}.
     */
    public void onCreate(Database db) {
        // Do nothing by default
    }

    /**
     * Delegates to {@link #onUpgrade(Database, int, int)}, which uses greenDAO's database abstraction.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(wrap(db), oldVersion, newVersion);
    }

    /**
     * Override this if you do not want to depend on {@link SQLiteDatabase}.
     */
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        // Do nothing by default
    }

    /**
     * Delegates to {@link #onOpen(Database)}, which uses greenDAO's database abstraction.
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        onOpen(wrap(db));
    }

    /**
     * Override this if you do not want to depend on {@link SQLiteDatabase}.
     */
    public void onOpen(Database db) {
        // Do nothing by default
    }

}