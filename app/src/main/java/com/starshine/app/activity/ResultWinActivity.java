package com.starshine.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.starshine.app.R;
import com.starshine.app.constant.IntentConstant;
import com.starshine.app.constant.SharedPreferencesConstant;
import com.starshine.app.utils.DateTimeUtils;
import com.starshine.app.utils.NetUtils;
import com.starshine.app.utils.SharedPreferencesUtils;

public class ResultWinActivity extends BaseActivity {

    private int mConsumeTime;
    private int mBestTime;
    private String mPercent;

    private Button mContinueButton;
    private Button mShowButton;
    private TextView mTipTextView;
    private SoundPool soundPool;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mConsumeTime = intent.getIntExtra(IntentConstant.CONSUME_TIME, 295);
        mBestTime = intent.getIntExtra(IntentConstant.BEST_TIME, 60);
        mPercent = intent.getStringExtra(IntentConstant.PERCENT);
        // TODO delete mock data
        mPercent = "99.9%";
    }

    @Override
    protected int setDrawableId() {
        return R.layout.activity_result_win;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText("挑战成功");
        mOptionButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initContentView() {
        mContinueButton = (Button) findViewById(R.id.btnContinue);
        mShowButton = (Button) findViewById(R.id.btnShow);
        mTipTextView = (TextView) findViewById(R.id.tv_win_tip);
        mTipTextView.setText(getString(R.string.win_tip,
                DateTimeUtils.secondsToFormattedString(mConsumeTime),
                DateTimeUtils.secondsToFormattedString(mBestTime)));
        setOnClickListener(mContinueButton, mShowButton);
        soundPool= new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(this, R.raw.win, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(1, 1, 1, 0, 0, 1);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: // 返回到词库选择界面
                //获取启动该Activity之间的Activity对应的Intent
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putBoolean(IntentConstant.ACTIVITY_RESULT_INTENT_BACK_TO_HOME, true);
                intent.putExtras(bundle);
                //设置该Activity的结果码，并设置结束之后退回的Activity
                setResult(RESULT_OK, intent);
                //结束FollowListActivity
                ResultWinActivity.this.finish();
                break;
            case R.id.btnContinue: // 继续游戏
                //获取启动该Activity之间的Activity对应的Intent
                Intent intentContinue = getIntent();
                Bundle bundleContinue = new Bundle();
                bundleContinue.putBoolean(IntentConstant.ACTIVITY_RESULT_INTENT_CONTINUE, true);
                intentContinue.putExtras(bundleContinue);
                //设置该Activity的结果码，并设置结束之后退回的Activity
                setResult(RESULT_OK, intentContinue);
                //结束FollowListActivity
                ResultWinActivity.this.finish();
                break;
            case R.id.btnShow:
                showToFriends();
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    /**
     * 炫耀
     */
    private void showToFriends() {
        if (NetUtils.isNetConnected(ResultWinActivity.this)) {
            Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("无网络连接");
            builder.setMessage("无法比较领先多少玩家");
            builder.setPositiveButton("确定", confirmListener);
            builder.setNegativeButton("取消", cancelListener);
            builder.create().show();
        }
    }

    /**
     * 监听器-确认
     */
    private DialogInterface.OnClickListener confirmListener
            = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    /**
     * 监听器-取消
     */
    private DialogInterface.OnClickListener cancelListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
}
