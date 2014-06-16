package com.starshine.app.model;

import android.graphics.Bitmap;

/**
 * 拼图中每一项
 *
 * Created by huyongsheng on 2014/5/21.
 */
public class PuzzleItem {
    private String enWord;
    private String cnWord;
    private String bgUrl;
    private Bitmap bitmap;
    private int initPosition;

    // TODO delete
    public PuzzleItem(String enWord, String cnWord, int position) {
        this.enWord = enWord;
        this.cnWord = cnWord;
        this.initPosition = position;
    }
    public PuzzleItem(String enWord, String cnWord, int position, Bitmap bit) {
        this.enWord = enWord;
        this.cnWord = cnWord;
        this.initPosition = position;
        this.bitmap = bit;
    }

    public PuzzleItem() {
    }

    public String getEnWord() {
        return enWord;
    }

    public void setEnWord(String enWord) {
        this.enWord = enWord;
    }

    public String getCnWord() {
        return cnWord;
    }

    public void setCnWord(String cnWord) {
        this.cnWord = cnWord;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

    public int getInitPosition() {
        return initPosition;
    }

    public void setInitPosition(int initPosition) {
        this.initPosition = initPosition;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
