package com.starshine.app.constant;

/**
 * 数据库常量
 *
 * Created by huyongsheng on 2014/6/11.
 */
public class DBConstant {
    public static final String DATABASE_NAME = "lexicon.db";
    public static final String DATABASE_PATH = "/data/data/com.starshine.app/databases";

    public static final String[] COLUMN_NAMES = {"id", "word", "explain", "used_times"};
    public static final String[] QUERY_COLUMN_NAMES = {"id", "word", "explain", "used_times"};
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_EXPLAIN = "explain";
    public static final String COLUMN_USED_TIMES = "used_times";

    // TODO change
    public static final int LIMIT_COUNT = 8;

    public static final int DATABASE_VERSION = 1;


}
