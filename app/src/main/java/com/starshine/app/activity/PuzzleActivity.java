package com.starshine.app.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.starshine.app.R;
import com.starshine.app.adapter.PuzzleAdapter;
import com.starshine.app.asynctask.CountdownTask;
import com.starshine.app.constant.DBConstant;
import com.starshine.app.constant.IntentConstant;
import com.starshine.app.constant.RequestConstant;
import com.starshine.app.constant.SharedPreferencesConstant;
import com.starshine.app.model.PuzzleItem;
import com.starshine.app.model.UserInfo;
import com.starshine.app.utils.BitmapUtils;
import com.starshine.app.utils.DBUtils;
import com.starshine.app.utils.DateTimeUtils;
import com.starshine.app.utils.SharedPreferencesUtils;
import com.starshine.app.utils.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Modified by SunFenggang on 2014/10/26.
 * 修改了设定拼图背景的逻辑方法，添加了对系统内置图片的支持。
 *
 * Modified by SunFenggang on 2014/11/08.
 * 修改了左上角的图片展示。
 *
 * Modified by SunFenggang on 2014/11/10.
 * 修改了进度显示逻辑，以及对游戏失败和胜利等多种情况下的处理逻辑
 */
public class PuzzleActivity extends BaseActivity implements PuzzleAdapter.GameResultListener, CountdownTask.TimeUpdateListener {
    private static final String LOG_TAG = PuzzleActivity.class.getSimpleName();

    private static final int GAME_STATE_INIT = 0;
    private static final int GAME_STATE_RUN = 1;
    private static final int GAME_STATE_FAIL = 2;
    private static final int GAME_STATE_WIN = 3;
    private static final int DEFAULT_TIME_LIMIT = 300; // 默认模式：普通-300s

    private String mLexiconTitle;
    private String mTableName;
    private int mGameState;
    private UserInfo mUserInfo;
    private PuzzleAdapter mPuzzleAdapter;
    private int mTimeLimit, mTimeRest;
    private CountdownTask mCountdownTask;
    private Uri mPictureUri;
    private List<PuzzleItem> mList;

    private ImageView mHeaderPortraitImageView;
    private TextView mScoreTextView;
    private TextView mRateTextView;
    private ProgressBar progressBar;
    private Button mUpdateButton;
    private Button mControlButton;
    private GridView mPuzzleGridView;
    private RelativeLayout rlGameInfo;
    private LinearLayout llGameControl;
    private TextView txvBest;
    private TextView txvRestTime;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mLexiconTitle = intent.getStringExtra(IntentConstant.LEXICON_NAME);
        mUserInfo = (UserInfo) intent.getSerializableExtra(IntentConstant.USER_INFO);
        mTimeLimit = intent.getIntExtra(IntentConstant.TIME_LIMIT, DEFAULT_TIME_LIMIT);
        mTableName = intent.getStringExtra(IntentConstant.LEXICON_TABLE_NAME);
        // TODO delete mock data
        mUserInfo = new UserInfo();
        mUserInfo.setScore(12332);
        mUserInfo.setRate(400);
        mUserInfo.setTotal(4231);
        mUserInfo.setHeadPortraitUrl("");
    }

    @Override
    protected int setDrawableId() {
        return R.layout.activity_puzzle;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText(mLexiconTitle);
        mOptionButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initContentView() {
        mHeaderPortraitImageView = (ImageView) findViewById(R.id.iv_head_portrait);
        mScoreTextView = (TextView) findViewById(R.id.tv_score);
        mRateTextView = (TextView) findViewById(R.id.tv_rate);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(mTimeLimit);
        mGameState = GAME_STATE_INIT;
        mUpdateButton = (Button) findViewById(R.id.btnUpdate);
        mControlButton = (Button) findViewById(R.id.btnControl);
        mPuzzleGridView = (GridView) findViewById(R.id.gv_puzzle);
        rlGameInfo = (RelativeLayout) findViewById(R.id.rlGameInfo);
        txvBest = (TextView) findViewById(R.id.txvBest);
        txvRestTime = (TextView) findViewById(R.id.txvRestTime);
        llGameControl = (LinearLayout) findViewById(R.id.ll_control);

        setOnClickListener(mUpdateButton, mControlButton);
    }

    @Override
    protected void initData() {
        mPuzzleAdapter = new PuzzleAdapter(this, this);
        // 获取单词
        mList = getWordList();
        mPuzzleAdapter.setData(mList);
        mPuzzleGridView.setAdapter(mPuzzleAdapter);

        /* 根据背景图片的类型，调用不同的方法来设定拼图背景 */
        switch (SharedPreferencesUtils.getInt(this, SharedPreferencesConstant.APP_NAME,
                SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE,           // 背景图片类型
                SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE_SYS)) {    // 默认值为系统图片
            case SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE_SYS:
                /* 背景图为系统内置图片 */
                int resId = SharedPreferencesUtils.getInt(this, SharedPreferencesConstant.APP_NAME,
                        SharedPreferencesConstant.PUZZLE_BACKGROUND_RESOURCE_ID,
                        SetGameBgActivity.SYS_GAME_BGS[0]);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                updateItemBitmap(bitmap); // 设定游戏背景图
                mHeaderPortraitImageView.setImageBitmap(bitmap); // 设定左上角的图片
                break;
            case SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE_CUSTOM:
                /* 背景图为用户自定义图片 */
                String uri = SharedPreferencesUtils.getString(this, SharedPreferencesConstant.APP_NAME,
                        SharedPreferencesConstant.PUZZLE_BACKGROUND_URI, "");
                updateItemUri(Uri.parse(uri)); // 设定游戏背景
                if (!StringUtils.isNullOrEmpty(uri)) { // 设定左上角的图片
                    /* 存在本地图片，使用本地图片 */
                    String path = BitmapUtils.getPathFromUri(PuzzleActivity.this, Uri.parse(uri));
                    mHeaderPortraitImageView.setImageBitmap( // 缩放后设置
                            BitmapUtils.getScaledImage(path, 200, 200));
//                    mHeaderPortraitImageView.setImageBitmap(
//                            BitmapUtils.getBitmapByUri(PuzzleActivity.this, Uri.parse(uri)));
                } else {
                    /* 不存在本地图片，使用系统默认的第一幅图 */
                    mHeaderPortraitImageView.setImageBitmap(
                            BitmapFactory.decodeResource(getResources(), R.drawable.bg_game_sys_1));
                }
                break;
        }

    }

    private List<PuzzleItem> getWordList() {
        // 构建单词列表
        List<PuzzleItem> list = new ArrayList<PuzzleItem>();
        // 打开数据库
        DBUtils mDbUtils = new DBUtils(this);
        mDbUtils.open();
        // 创建cursor,获取8个单词
        String querySql = StringUtils.getString("select {0},{1},{2},{3} from ", mTableName, " order by ", DBConstant.COLUMN_USED_TIMES, " limit ", DBConstant.LIMIT_COUNT);
        querySql = MessageFormat.format(querySql, DBConstant.QUERY_COLUMN_NAMES);
        Cursor cursor = mDbUtils.rawQuery(querySql);
        // 插入空数据到列表
        int initPosition = 0;
        list.add(initPosition, new PuzzleItem());
        while (cursor.moveToNext()) {
            // 插入查询结果到列表
            PuzzleItem item = new PuzzleItem(cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_WORD)),
                    cursor.getString(cursor.getColumnIndex(DBConstant.COLUMN_EXPLAIN)), initPosition);
            list.add(initPosition++, item);
            // 更新数据库的used_times字段
            String updateSql = StringUtils.getString("update ", mTableName, " set ", DBConstant.COLUMN_USED_TIMES,"=", cursor.getInt(cursor.getColumnIndex(DBConstant.COLUMN_USED_TIMES)) + 1,
                    " where ", DBConstant.COLUMN_ID,"=",cursor.getInt(cursor.getColumnIndex(DBConstant.COLUMN_ID)));
            mDbUtils.execSQL(updateSql);
        }
        // 关闭cursor，关闭数据库
        cursor.close();
        mDbUtils.close();
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                initData();
                break;
            case R.id.btnControl:
                if (mGameState == GAME_STATE_INIT) {
                    startGame();
                }
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    private void startGame() {
        // 显示出游戏信息面板
        rlGameInfo.setVisibility(View.VISIBLE);
        llGameControl.setVisibility(View.GONE);
        progressBar.setMax(mTimeLimit);
        String sharedBest = SharedPreferencesConstant.BEST_CET_4;
        if (mTableName.equals("lexicon_cet_4")) {
            sharedBest = SharedPreferencesConstant.BEST_CET_4;
        } else if (mTableName.equals("lexicon_cet_6")) {
            sharedBest = SharedPreferencesConstant.BEST_CET_6;
        } else if (mTableName.equals("lexicon_gre")) {
            sharedBest = SharedPreferencesConstant.BEST_GRE;
        } else if (mTableName.equals("lexicon_ielts")) {
            sharedBest = SharedPreferencesConstant.BEST_IELTS;
        } else if (mTableName.equals("lexicon_toefl")) {
            sharedBest = SharedPreferencesConstant.BEST_TOEFL;
        }
        int bestInfo = SharedPreferencesUtils.getInt(PuzzleActivity.this,
                SharedPreferencesConstant.APP_NAME, sharedBest, -1);
        if (bestInfo < 0) { // 没有最佳纪录
            txvBest.setText("最佳：暂无");
        } else {
            txvBest.setText("最佳：" + DateTimeUtils.secondsToFormattedString(bestInfo));
        }

        mGameState = GAME_STATE_RUN;
        mPuzzleAdapter.randList();
        mPuzzleAdapter.initEmptyPosition();
        mPuzzleAdapter.updateGameState(true);
        mPuzzleAdapter.notifyDataSetChanged();
        startCountdown();
    }

    private void startCountdown() {
        mCountdownTask = new CountdownTask(mTimeLimit, this);
        mCountdownTask.execute(mTimeLimit);
    }

    @Override
    public void winGame() {
        int usedTime = mTimeLimit - mTimeRest; // 计算用时

        Intent intent = new Intent();
        intent.setClass(this, ResultWinActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(IntentConstant.CONSUME_TIME, usedTime);
        intent.putExtras(bundle);

        String sharedBest = SharedPreferencesConstant.BEST_CET_4;
        if (mTableName.equals("lexicon_cet_4")) {
            sharedBest = SharedPreferencesConstant.BEST_CET_4;
        } else if (mTableName.equals("lexicon_cet_6")) {
            sharedBest = SharedPreferencesConstant.BEST_CET_6;
        } else if (mTableName.equals("lexicon_gre")) {
            sharedBest = SharedPreferencesConstant.BEST_GRE;
        } else if (mTableName.equals("lexicon_ielts")) {
            sharedBest = SharedPreferencesConstant.BEST_IELTS;
        } else if (mTableName.equals("lexicon_toefl")) {
            sharedBest = SharedPreferencesConstant.BEST_TOEFL;
        }
        // 获取本地保存的最佳纪录
        int bestInfo = SharedPreferencesUtils.getInt(PuzzleActivity.this,
                SharedPreferencesConstant.APP_NAME, sharedBest, -1);
        // 比较并重新对最佳纪录进行赋值
        bestInfo = bestInfo < 0 ? usedTime : bestInfo <= usedTime ? bestInfo : usedTime;
        // 保存到本地
        SharedPreferencesUtils.save(PuzzleActivity.this, SharedPreferencesConstant.APP_NAME,
                sharedBest, bestInfo);
        // 更新界面：
        txvBest.setText("最佳：" + DateTimeUtils.secondsToFormattedString(bestInfo));

        startActivityForResult(intent, RequestConstant.START_TO_PUZZLE_WIN_ACTIVITY_REQUEST);
    }

    /**
     * 游戏失败，跳转至失败界面
     */
    private void loseGame() {
        Intent intent = new Intent();
        intent.setClass(this, ResultLoseActivity.class);
        startActivityForResult(intent, RequestConstant.START_TO_PUZZLE_LOSE_ACTIVITY_REQUEST);
    }

    @Override
    public void onTimeUpdate(int time) {
        mTimeRest = time; // 记录剩余时间
        if (time > 0) {
            progressBar.setProgress(time);
            // 显示剩余时间
            txvRestTime.setText(DateTimeUtils.secondsToFormattedString(time));
        } else { // 游戏失败
            progressBar.setProgress(0);
            txvRestTime.setText(DateTimeUtils.secondsToFormattedString(0));
            loseGame();
        }
    }

    @Override
    protected void onPause() {
        if (mCountdownTask != null) {
            mCountdownTask.cancel(true);
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestConstant.BASE_ACTIVITY_GET_PICTURE_CODE:
                if (resultCode == RESULT_OK) {
                    mPictureUri = data.getData();
                    updateItemUri(mPictureUri);
                }
                break;
            case RequestConstant.START_TO_PUZZLE_LOSE_ACTIVITY_REQUEST: // 从失败界面返回
                if (resultCode == RESULT_OK) {
                    boolean isContinue = data.getBooleanExtra(
                            IntentConstant.ACTIVITY_RESULT_INTENT_CONTINUE, false);
                    if (isContinue) { // 如果继续，则隐藏当前进度信息，让用户可以点击“开始”
                        rlGameInfo.setVisibility(View.GONE);
                        llGameControl.setVisibility(View.VISIBLE);
                        mGameState = GAME_STATE_INIT; // 重置游戏状态
                        break;
                    }
                    boolean isGiveUp = data.getBooleanExtra(
                            IntentConstant.ACTIVITY_RESULT_INTENT_GIVE_UP, false);
                    if (isGiveUp) { // 如果放弃，则关闭当前页面，并向上级activity发送intent数据
                        //获取启动该Activity之间的Activity对应的Intent
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(IntentConstant.ACTIVITY_RESULT_INTENT_GIVE_UP, true);
                        intent.putExtras(bundle);
                        //设置该Activity的结果码，并设置结束之后退回的Activity
                        setResult(RESULT_OK, intent);
                        //结束FollowListActivity
                        PuzzleActivity.this.finish();
                    }
                }
                break;
            case RequestConstant.START_TO_PUZZLE_WIN_ACTIVITY_REQUEST: // 从胜利界面返回
                if (resultCode == RESULT_OK) {
                    boolean isContinue = data.getBooleanExtra(
                            IntentConstant.ACTIVITY_RESULT_INTENT_CONTINUE, false);
                    if (isContinue) { // 如果继续，则隐藏当前进度信息，让用户可以点击“开始”
                        rlGameInfo.setVisibility(View.GONE);
                        llGameControl.setVisibility(View.VISIBLE);
                        mGameState = GAME_STATE_INIT; // 重置游戏状态
                        initData(); // 重置游戏数据
                        break;
                    }
                    boolean isBackToHome = data.getBooleanExtra(
                            IntentConstant.ACTIVITY_RESULT_INTENT_BACK_TO_HOME, false);
                    if (isBackToHome) { // 返回到主界面
                        //获取启动该Activity之间的Activity对应的Intent
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(IntentConstant.ACTIVITY_RESULT_INTENT_BACK_TO_HOME, true);
                        intent.putExtras(bundle);
                        //设置该Activity的结果码，并设置结束之后退回的Activity
                        setResult(RESULT_OK, intent);
                        //结束FollowListActivity
                        PuzzleActivity.this.finish();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateItemUri(Uri mPictureUri) {
        if (!StringUtils.isNullOrEmpty(mPictureUri.toString())) {
            /* 存在本地图片 */
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setBitmap(BitmapUtils.getCutBitmapByUri(this, mPictureUri, 3, i));
            }
        } else {
            /* 不存在本地图片，使用系统默认的第一幅图 */
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_game_sys_1);
            updateItemBitmap(bitmap);
        }
        mPuzzleAdapter.notifyDataSetChanged();
    }

    /**
     * 设定拼图背景
     * @author SunFenggang
     * @desc 如果选用的是系统内置的图片，则使用本方法
     *       如果选用的是用户自定义的图片，则在设定拼图背景时使用updateItemUri(Uri uri)方法
     * @param bitmap 原始图片
     */
    private void updateItemBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            for (int i=0; i<mList.size(); ++i) {
                mList.get(i).setBitmap(BitmapUtils.setCutBitmap(bitmap, 3, i));
            }
        }
    }
}
