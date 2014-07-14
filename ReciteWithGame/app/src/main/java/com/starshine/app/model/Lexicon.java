package com.starshine.app.model;

/**
 * 词典
 *
 * Created by huyongsheng on 2014/6/4.
 */
public class Lexicon {
    private String name;
    private String tableName;
    private int code;
    private int resId;

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
}
