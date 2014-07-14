package com.starshine.app.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

/**
 * 处理字符串
 *
 * Created by huyongsheng on 2014/6/3.
 */
public class StringUtils {
    /**
     * 利用StringBuilder来创建字符串
     */
    public static final String getString(String... strings){
        StringBuilder result = new StringBuilder();
        for (String string:strings){
            result.append(string);
        }
        return result.toString();
    }

    /**
     * 利用StringBuilder来创建字符串
     */
    public static final String getString(Object... objects){
        StringBuilder result = new StringBuilder();
        for (Object object:objects){
            result.append(object.toString());
        }
        return result.toString();
    }

    /**
     * 对SpannableString中的某段进行颜色变换，可以对同一个Spannable进行多次变色
     *
     * @param content：原始Spannable
     * @param start：变色字段的开始index
     * @param end：变色字段的越界index
     * @param color：字体颜色
     * @return变色后的spannable
     */
    public static SpannableStringBuilder getSpannableString(SpannableStringBuilder content, int start, int end, int color) {
        if (content == null) {
            return null;
        }
        if (start < 0) {
            start = 0;
        }
        if (end > content.length()) {
            end = content.length();
        }
        content.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return content;
    }

    public static boolean isNullOrEmpty(String inputString) {
        if (null == inputString) {
            return true;
        } else if (inputString.trim().equals("")) {
            return true;
        }

        return false;
    }

    public static boolean isAllNullOrEmpty(String... strings) {
        for (String string : strings) {
            if(!isNullOrEmpty(string)) {
                return false;
            }
        }
        return true;
    }
}
