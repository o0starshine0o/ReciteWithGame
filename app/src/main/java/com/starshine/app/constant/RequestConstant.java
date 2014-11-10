package com.starshine.app.constant;

/**
 * RequestCode 常量
 *
 * Created by huyongsheng on 2014/6/2.
 *
 * Modified by SunFenggang on 2014/11/10.
 * 添加了101、102、103
 */
public class RequestConstant {
    public static final int PUZZLE_ACTIVITY_CODE = 0;
    public static final int OPTION_ACTIVITY_GET_PICTURE_CODE = 1;
    public static final int BASE_ACTIVITY_GET_PICTURE_CODE = 2;

    /** 跳转到游戏界面 */
    public static final int START_TO_PUZZLE_ACTIVITY_REQUEST = 101;

    /** 跳转到游戏失败界面 */
    public static final int START_TO_PUZZLE_LOSE_ACTIVITY_REQUEST = 102;

    /** 跳转到游戏胜利界面 */
    public static final int START_TO_PUZZLE_WIN_ACTIVITY_REQUEST = 103;

}
