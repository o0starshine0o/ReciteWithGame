package com.starshine.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 处理SharePreference
 *
 * Created by huyongsheng on 2014/5/21.
 */
public class SharedPreferencesUtils {

    public static final void save(Context context, String shareName, String name, int value) {
        SharedPreferences share = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        share.edit().putInt(name, value).commit();
    }

    public static final int getInt(Context context, String shareName, String name, int defaultValue) {
        SharedPreferences share = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        return share.getInt(name, defaultValue);
    }

    public static final void save(Context context, String shareName, String name, String value){
        SharedPreferences share = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        share.edit().putString(name, value).commit();
    }

    public static final String getString(Context context, String shareName, String name, String defaultValue) {
        SharedPreferences share = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        return share.getString(name, defaultValue);
    }

    public static final void save(Context context, String shareName, String name, boolean value){
        SharedPreferences share = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        share.edit().putBoolean(name, value).commit();
    }

    public static final boolean getBoolean(Context context, String shareName, String name, boolean defaultValue){
        SharedPreferences share = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        return share.getBoolean(name, defaultValue);
    }
}
