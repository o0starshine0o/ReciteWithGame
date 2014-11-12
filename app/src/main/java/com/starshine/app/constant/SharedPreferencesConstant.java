package com.starshine.app.constant;

/**
 * SharePreference常量
 *
 * Created by huyongsheng on 2014/5/21.
 *
 * Modified by SunFenggang on 2014/10/26.
 * 添加游戏背景类型及其相关的变量
 *
 * Modified by SunFenggang on 2014/11/10.
 * 添加了最佳成绩相关常量名称
 */
public class SharedPreferencesConstant {
    public static final String APP_NAME = "recite_with_game";
    public static final String INIT_LEXICON = "init_lexicon";
    public static final String SCREEN_HEIGHT = "screen_height";
    public static final String SCREEN_WIDTH = "screen_width";
    public static final String PUZZLE_BACKGROUND_URI = "puzzle_background_uri";

    public static final boolean LEXICON_NOT_EXIST = false;
    public static final boolean LEXICON_EXIST = true;

    /**
     * 游戏背景类型
     * PUZZLE_BACKGROUND_TYPE_SYS-0：系统图片（默认值）
     * PUZZLE_BACKGROUND_TYPE_CUSTOM-1：自定义图片
     */
    public static final String PUZZLE_BACKGROUND_TYPE = "puzzle_background_type";

    /**
     * 游戏背景类型：系统图片（默认值）
     */
    public static final int PUZZLE_BACKGROUND_TYPE_SYS = 0;

    /**
     * 游戏背景类型：自定义图片
     */
    public static final int PUZZLE_BACKGROUND_TYPE_CUSTOM = 1;

    /**
     * 作为游戏背景的系统内置图片的资源id在本地文件中存储时使用的变量名称
     */
    public static final String PUZZLE_BACKGROUND_RESOURCE_ID = "puzzle_background_resource_id";

    /** 最佳成绩 */
    public static final String BEST_CET_4 = "bestCet4";
    public static final String BEST_CET_6 = "bestCet6";
    public static final String BEST_GRE = "bestGre";
    public static final String BEST_IELTS = "bestIelts";
    public static final String BEST_TOEFL = "bestToefl";

    /** 游戏字体颜色 */
    public static final String PUZZLE_TEXT_COLOR = "puzzleTextColor";
}
