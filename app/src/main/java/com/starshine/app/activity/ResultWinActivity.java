package com.starshine.app.activity;

import android.content.Intent;
import android.widget.TextView;

import com.starshine.app.R;
import com.starshine.app.constant.IntentConstant;

public class ResultWinActivity extends BaseActivity {

    private String mConsumeTime;
    private String mPercent;

    private TextView mGiveUpButton;
    private TextView mContinueButton;
    private TextView mTipTextView;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mConsumeTime = intent.getStringExtra(IntentConstant.CONSUME_TIME);
        mPercent = intent.getStringExtra(IntentConstant.PERCENT);
        // TODO delete mock data
        mConsumeTime = "33秒";
        mPercent = "99.9%";
    }

    @Override
    protected int setDrawableId() {
        return R.layout.activity_result_win;
    }

    @Override
    protected void initContentView() {
        mGiveUpButton = (TextView) findViewById(R.id.tv_give_up);
        mContinueButton = (TextView) findViewById(R.id.tv_continue);
        mTipTextView = (TextView) findViewById(R.id.tv_win_tip);
        mTipTextView.setText(getString(R.string.win_tip, mConsumeTime, mPercent));
        setOnClickListener(mGiveUpButton, mContinueButton);
    }

    @Override
    protected void initData() {

    }
}
