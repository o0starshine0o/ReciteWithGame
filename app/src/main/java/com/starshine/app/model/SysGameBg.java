package com.starshine.app.model;

import java.io.Serializable;

/**
 * 系统内置游戏背景图
 * Created by SunFenggang on 2014/10/27.
 */
public class SysGameBg implements Serializable {
    private int resId;          // 图片 drawable id
    private boolean isUsing;    // 当前使用中标志位

    public SysGameBg(){

    }

    public SysGameBg(int resId, boolean isUsing){
        this.resId = resId;
        this.isUsing = isUsing;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isUsing() {
        return isUsing;
    }

    public void setUsing(boolean isUsing) {
        this.isUsing = isUsing;
    }
}
