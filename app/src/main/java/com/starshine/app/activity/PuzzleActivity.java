package com.starshine.app.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.starshine.app.utils.SharedPreferencesUtils;
import com.starshine.app.utils.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


public class PuzzleActivity extends BaseActivity implements PuzzleAdapter.GameResultListener, CountdownTask.TimeUpdateListener {
    private static final String LOG_TAG = PuzzleActivity.class.getSimpleName();

    private static final int GAME_STATE_INIT = 0;
    private static final int GAME_STATE_RUN = 1;
    private static final int GAME_STATE_FAIL = 2;
    private static final int GAME_STATE_WIN = 3;
    private static final int DEFAULT_TIME_LIMIT = 30;

    private String mLexiconTitle;
    private String mTableName;
    private int mGameState;
    private UserInfo mUserInfo;
    private PuzzleAdapter mPuzzleAdapter;
    private int mTimeLimit;
    private CountdownTask mCountdownTask;
    private Uri mPictureUri;
    private List<PuzzleItem> mList;

    private ImageView mHeaderPortraitImageView;
    private TextView mScoreTextView;
    private TextView mRateTextView;
    private ProgressBar mTimeRateBar;
    private TextView mUpdateButton;
    private TextView mControlButton;
    private GridView mPuzzleGridView;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mLexiconTitle = intent.getStringExtra(IntentConstant.LEXICON_NAME);
        mUserInfo = (UserInfo) intent.getSerializableExtra(IntentConstant.USER_INFO);
        mTimeLimit = intent.getIntExtra(IntentConstant.TIME_LIMIT, DEFAULT_TIME_LIMIT) * 100;
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
    }

    @Override
    protected void initContentView() {
        mHeaderPortraitImageView = (ImageView) findViewById(R.id.iv_head_portrait);
        mScoreTextView = (TextView) findViewById(R.id.tv_score);
        mRateTextView = (TextView) findViewById(R.id.tv_rate);
        mTimeRateBar = (ProgressBar) findViewById(R.id.pb_time);
        mTimeRateBar.setMax(mTimeLimit);
        mUpdateButton = (TextView) findViewById(R.id.tv_update);
        mControlButton = (TextView) findViewById(R.id.tv_control);
        mPuzzleGridView = (GridView) findViewById(R.id.gv_puzzle);
        setOnClickListener(mUpdateButton, mControlButton);
    }

    @Override
    protected void initData() {
        mPuzzleAdapter = new PuzzleAdapter(this, this);
        // 获取单词
        mList = getWordList();
        mPuzzleAdapter.setData(mList);
        mPuzzleGridView.setAdapter(mPuzzleAdapter);
        updateItemUri(Uri.parse(SharedPreferencesUtils.getString(this, SharedPreferencesConstant.APP_NAME,
                SharedPreferencesConstant.PUZZLE_BACKGROUND_URI, "")));
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
            case R.id.tv_update:
                break;
            case R.id.tv_control:
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
//        mGameState = GAME_STATE_RUN;
        mPuzzleAdapter.randList();
        mPuzzleAdapter.initEmptyPosition();
        mPuzzleAdapter.updateGameState();
        mPuzzleAdapter.notifyDataSetChanged();
        startCountdown();
    }

    private void startCountdown() {
        mCountdownTask = new CountdownTask(mTimeLimit, this);
        mCountdownTask.execute(mTimeLimit);
    }

    @Override
    public void winGame() {
        Intent intent = new Intent();
        intent.setClass(this, ResultWinActivity.class);
        startActivityForResult(intent, RequestConstant.PUZZLE_ACTIVITY_CODE);
    }

    private void loseGame() {
        Intent intent = new Intent();
        intent.setClass(this, ResultLoseActivity.class);
        startActivityForResult(intent, RequestConstant.PUZZLE_ACTIVITY_CODE);
    }

    @Override
    public void onTimeUpdate(int time) {
        if (time > 0) {
            mTimeRateBar.setProgress(time);
        } else {
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
        if (requestCode == RequestConstant.BASE_ACTIVITY_GET_PICTURE_CODE) {
            if (resultCode == RESULT_OK) {
                mPictureUri = data.getData();
                updateItemUri(mPictureUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateItemUri(Uri mPictureUri) {
        if (mPictureUri != null) {
            for (int i = 0; i < mList.size(); i++) {
                if (!StringUtils.isNullOrEmpty(mPictureUri.toString())) {
                    mList.get(i).setBitmap(BitmapUtils.getCutBitmapByUri(this, mPictureUri, 3, i));
                } else {
                    mList.get(i).setBitmap(BitmapUtils.getBitmapByResourceId(this, R.drawable.puzzle_item_default_bg));
                }
            }
            mPuzzleAdapter.notifyDataSetChanged();
        }
    }
}
