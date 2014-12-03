package com.starshine.app.constant;

/**
 * intent 常量
 *
 * Created by huyongsheng on 2014/5/21.
 *
 * Modified by SunFenggang on 2014/11/10.
 * 添加了是否继续、是否放弃、返回主界面等多个参数常量值
 */
public class IntentConstant {
    public static final String LEXICON_NAME = "lexicon_name";
    public static final String LEXICON_CODE = "lexicon_code";
    public static final String LEXICON_TABLE_NAME = "lexicon_table_name";
    public static final String USER_INFO = "user_info";
    public static final String CONSUME_TIME = "consume_time";
    public static final String PERCENT = "percent";
    public static final String TIME_LIMIT = "time_limit";
    public static final String PICTURE_URI = "picture_uri";
    public static final String BEST_TIME = "best_time";

    /** 失败：是否继续 */
    public static final String ACTIVITY_RESULT_INTENT_CONTINUE = "activityResultIntentContinue";
    /** 失败：是否放弃 */
    public static final String ACTIVITY_RESULT_INTENT_GIVE_UP = "activityResultIntentGiveUp";
    /** 胜利：返回主界面 */
    public static final String ACTIVITY_RESULT_INTENT_BACK_TO_HOME = "activityResultIntentBackToHome";
    /** 选定颜色 */
    public static final String SELECT_COLOR_VALUE = "selectColorValue";
}
