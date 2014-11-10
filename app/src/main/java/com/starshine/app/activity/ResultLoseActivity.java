package com.starshine.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.starshine.app.R;
import com.starshine.app.constant.IntentConstant;
import com.starshine.app.constant.RequestConstant;

/**
 * Modified by SunFenggang on 2014/11/10.
 * 添加了失败情况下的多种处理逻辑
 */
public class ResultLoseActivity extends BaseActivity {

    private Button mGiveUpButton;
    private Button mContinueButton;
    private TextView mTipTextView;


    @Override
    protected void getIntentData() {

    }

    @Override
    protected int setDrawableId() {
        return R.layout.activity_result_lose;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText("挑战失败");
        mOptionButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initContentView() {
        mGiveUpButton = (Button) findViewById(R.id.btnGiveUp);
        mContinueButton = (Button) findViewById(R.id.btnContinue);
        mTipTextView = (TextView) findViewById(R.id.tv_win_tip);
        mTipTextView.setText(R.string.lose_tip);
        setOnClickListener(mGiveUpButton, mContinueButton, mBackButton);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.btnGiveUp: // 放弃游戏
                //获取启动该Activity之间的Activity对应的Intent
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putBoolean(IntentConstant.ACTIVITY_RESULT_INTENT_GIVE_UP, true);
                intent.putExtras(bundle);
                //设置该Activity的结果码，并设置结束之后退回的Activity
                setResult(RESULT_OK, intent);
                //结束FollowListActivity
                ResultLoseActivity.this.finish();
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
                ResultLoseActivity.this.finish();
                break;
            default:
                super.onClick(v);
                break;
        }
    }
}
