package com.starshine.app.model;

/**
 * 词典
 *
 * Created by huyongsheng on 2014/6/4.
 *
 * Modified by SunFenggang on 2014/10/16.
 * Add three variables and relative getters and setters: total, complete, iconId.
 */
public class Lexicon {
    private String name;        // 题库名称
    private String tableName;   // 数据库表名称
    private int code;
    private int resId;
    private int total;          // 单词量总数
    private int complete;       // 已完成数
    private int iconId;         // 选择词库界面背景图

    public Lexicon(){

    }

    public Lexicon(String name, int code, int resId){
        this.name = name;
        this.code = code;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
