package com.starshine.app.activity;

import android.content.Intent;

import com.starshine.app.R;
import com.starshine.app.constant.DBConstant;
import com.starshine.app.utils.DeviceUtils;
import com.starshine.app.utils.FileUtils;
import com.starshine.app.utils.LogUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by huyongsheng on 2014/5/21.
 * <p/>
 * 软件的运行入口
 * 初始化词库
 * 在词库初始化完成后且在此界面停留够3秒后跳转到词典选择界面
 */
public class LaunchActivity extends BaseActivity {
    private static final String LOG_TAG = LaunchActivity.class.getSimpleName();
    private static final int THREE_SECOND = 3000;

    private boolean mInitLexiconFinish;
    private boolean mTimeOver;

    @Override
    protected void getIntentData() {
    }

    @Override
    protected int setDrawableId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initContentView() {
    }

    @Override
    protected void initData() {
        initDeviceInfo();
        initLog();
        initDatabase();
        initTimer();
    }

    private void initDeviceInfo() {
        DeviceUtils.initScreenInfo(this);
    }

    private void initLog() {
        LogUtils.init(true);
    }

    private void initDatabase() {
        //存放数据库的目录
        if (!FileUtils.isFileExists(DBConstant.DATABASE_PATH, DBConstant.DATABASE_NAME)) {
            FileUtils.save(getApplicationContext(), DBConstant.DATABASE_PATH, DBConstant.DATABASE_NAME, R.raw.lexicon);
        }
        mInitLexiconFinish = true;
        jump();
    }

    private void initTimer() {
        Timer timer = new Timer();
        mTimeOver = false;
        StartActivityTask task = new StartActivityTask();
        timer.schedule(task, THREE_SECOND);
    }

    private void jump() {
        if (mInitLexiconFinish && mTimeOver) {
            startActivity(new Intent(getApplicationContext(), ChooseLexiconActivity.class));
            finish();
        }
    }

    private class StartActivityTask extends TimerTask {
        @Override
        public void run() {
            mTimeOver = true;
            jump();
        }
    }
}
