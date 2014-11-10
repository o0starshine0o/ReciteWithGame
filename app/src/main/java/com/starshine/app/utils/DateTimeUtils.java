package com.starshine.app.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 时间处理类
 * Created by SunFenggang on 2014/11/10.
 */
@SuppressLint("SimpleDateFormat")
public class DateTimeUtils {

    /**
     * 生成当前系统时间对应的字符串
     * 格式为yyyy-mm-dd hh:MM:ss
     * @return String
     */
    public static String generateDateTimeString() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 生成时间对应的字符串
     * 格式为yyyy-mm-dd hh:MM:ss
     * @param date
     * @return String
     */
    public static String generateDateTimeString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 生成时间对应的字符串
     * 格式为yyyy-mm-dd hh:MM:ss
     * @param date
     * @param format
     * @return String
     */
    public static String generateDateTimeString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 生成时间
     * 格式为yyyy-mm-dd hh:MM:ss
     * @param str
     * @return Date
     */
    public static Date generateDateTime(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(str);
    }

    /**
     * 获取年份
     * @param date
     * @return String
     */
    public static String getYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    /**
     * 获取月份
     * @param date
     * @return String
     */
    public static String getMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(date);
    }

    /**
     * 生成当前系统时间对应的字符串
     * 格式为yyyy-mm-dd hh:MM:ss
     * @param format
     * @return String
     */
    public static String generateDateTimeString(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    
    /**
	 * 生成文件名称
	 * @return
	 */
	public static String generateFilenameString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");   
		return df.format(new Date()) + "_"  
				+ new Random().nextInt(1000);
	}

    /**
     * 获取当前时间
     * @param pattern
     * @return
     */
    public static String getCurrentTime(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    public static String customDateTimeString(String time) {
        String custom;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = sdf.parse(time);
            Date now = new Date();
            long diff = now.getTime() - date.getTime();
            if (diff < 0) {
                custom = sdf2.format(date);
            } else if (diff < 60000) {
                custom = ((int)diff / 1000) + "秒前";
            } else if (diff < 3600000) {
                custom = ((int)diff / 60000) + "分钟前";
            } else if (diff < 43200000) {
                custom = ((int)diff / 3600000) + "小时前";
            } else {
                custom = sdf2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            custom = "时间处理错误";
        }
        return custom;
    }

    /**
     * 将秒数转换成格式化的字符串
     *     当小于1分钟时，返回x秒，如：34秒；
     *     当小于1小时时，返回x分y秒，如：23分34秒；
     *     当大于等于1小时时，返回x时y分z秒，如：1时23分34秒
     * @param seconds 秒数
     * @return 格式化后的字符串
     */
    public static String secondsToFormattedString(int seconds) {
        if (seconds < 60) {
            return seconds + "秒";
        }
        if (seconds < 3600) {
            return seconds / 60 + "分" + seconds % 60 + "秒";
        }
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = (seconds % 3600) % 60;
        return h + "时" + m + "分" + s + "秒";
    }
}
