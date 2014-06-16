package com.starshine.app.model;

/**
 * 初始化词库的进度
 *
 * Created by huyongsheng on 2014/6/11.
 */
public class InitLexiconProgress {

    private int totalLexicon;
    private int nowLexicon;
    private int totalWord;
    private int nowWord;

    public int getTotalLexicon() {
        return totalLexicon;
    }

    public void setTotalLexicon(int totalLexicon) {
        this.totalLexicon = totalLexicon;
    }

    public int getTotalWord() {
        return totalWord;
    }

    public void setTotalWord(int totalWord) {
        this.totalWord = totalWord;
    }

    public int getNowLexicon() {
        return nowLexicon;
    }

    public void setNowLexicon(int nowLexicon) {
        this.nowLexicon = nowLexicon;
    }

    public int getNowWord() {
        return nowWord;
    }

    public void setNowWord(int nowWord) {
        this.nowWord = nowWord;
    }

    public int getPercent() {
        return nowWord / totalWord;
    }

    public boolean isFinish(){
        return (totalLexicon & totalWord) != 0 && nowLexicon == totalLexicon && nowWord == totalWord;
    }
}
