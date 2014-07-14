package com.starshine.app.utils;

import android.util.Log;

import java.text.MessageFormat;

/**
 * 写日志工具，支持模板化日志字符串
 *
 * Created by huyongsheng on 2014/6/3.
 */
public class LogUtils {

    private static boolean IS_PRINTABLE = false;

    public static void init(boolean isPrintable) {
        IS_PRINTABLE = isPrintable;
    }

    public static void v(String tag, String msg) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Object... args) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.v(tag, format(msg, args));
    }

    public static void d(String tag, String msg) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Object... args) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.d(tag, format(msg, args));
    }

    public static void i(String tag, String msg) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Object... args) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.i(tag, format(msg, args));
    }

    public static void w(String tag, String msg) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Object... args) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.w(tag, format(msg, args));
    }

    public static void e(String tag, String msg) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Object... args) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.e(tag, format(msg, args));
    }

    private static String format(String format, Object... args) {
        return MessageFormat.format(format, args);
    }

}
