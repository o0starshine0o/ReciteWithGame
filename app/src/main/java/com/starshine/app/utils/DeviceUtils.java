package com.starshine.app.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.starshine.app.constant.SharedPreferencesConstant;

/**
 * 处理设备相关
 *
 * Created by huyongsheng on 2014/5/21.
 */
public class DeviceUtils {

    private static final int DEFAULT_HEIGHT = 0;
    private static final int DEFAULT_WIDTH = 0;

    public static final void initScreenInfo(Context context) {
        if (getScreenHeight(context) * getScreenWidth(context) == 0) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            SharedPreferencesUtils.save(context, SharedPreferencesConstant.APP_NAME, SharedPreferencesConstant.SCREEN_HEIGHT, dm.heightPixels);
            SharedPreferencesUtils.save(context, SharedPreferencesConstant.APP_NAME, SharedPreferencesConstant.SCREEN_WIDTH, dm.widthPixels);
        }
    }

    public static final int getScreenWidth(Context context) {
        return SharedPreferencesUtils.getInt(context, SharedPreferencesConstant.APP_NAME, SharedPreferencesConstant.SCREEN_WIDTH, DEFAULT_WIDTH);
    }

    public static final int getScreenHeight(Context context) {
        return SharedPreferencesUtils.getInt(context, SharedPreferencesConstant.APP_NAME, SharedPreferencesConstant.SCREEN_HEIGHT, DEFAULT_HEIGHT);
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
