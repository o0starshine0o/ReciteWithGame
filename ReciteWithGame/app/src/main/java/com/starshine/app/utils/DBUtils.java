package com.starshine.app.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.starshine.app.constant.DBConstant;

/**
 * 处理数据库
 * <p/>
 * Created by huyongsheng on 2014/6/3.
 */
public class DBUtils {
    private static final String LOG_TAG = DBUtils.class.getSimpleName();

    static private DatabaseHelper mDbHelper;
    static private SQLiteDatabase mDb;

    private final Context mContext;

    public DBUtils(Context context) {
        this.mContext = context;
    }

    public DBUtils open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDb.close();
        mDbHelper.close();
    }

    /**
     * 执行sql
     */
    public void execSQL(String sql) {
        LogUtils.d(LOG_TAG, StringUtils.getString("execSql :", sql));
        mDb.execSQL(sql);
    }

    /**
     * 执行sql
     */
    public Cursor rawQuery(String sql) {
        LogUtils.d(LOG_TAG, StringUtils.getString("rawQuery :", sql));
        return mDb.rawQuery(sql, null);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DBConstant.DATABASE_NAME, null, DBConstant.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}