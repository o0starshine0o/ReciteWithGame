package com.starshine.app.activity;

import android.widget.TextView;

import com.starshine.app.R;

public class ResultLoseActivity extends BaseActivity {

    private TextView mGiveUpButton;
    private TextView mContinueButton;
    private TextView mTipTextView;


    @Override
    protected void getIntentData() {

    }

    @Override
    protected int setDrawableId() {
        return R.layout.activity_result_lose;
    }

    @Override
    protected void initContentView() {
        mGiveUpButton = (TextView) findViewById(R.id.tv_give_up);
        mContinueButton = (TextView) findViewById(R.id.tv_continue);
        mTipTextView = (TextView) findViewById(R.id.tv_win_tip);
        mTipTextView.setText(R.string.lose_tip);
        setOnClickListener(mGiveUpButton, mContinueButton);
    }

    @Override
    protected void initData() {

    }
}
