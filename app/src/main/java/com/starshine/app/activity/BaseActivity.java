package com.starshine.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.starshine.app.R;
import com.starshine.app.constant.RequestConstant;

/**
 * 基础activity
 * 确定函数的执行顺序
 * 添加控件的监听事件
 *
 * Created by huyongsheng on 2014/5/21.
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener {
    protected ImageView mBackButton;
    protected ImageView mOptionButton;
    protected TextView mTitleTextView;

    protected abstract void getIntentData();

    protected abstract int setDrawableId();

    protected abstract void initContentView();

    protected abstract void initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        initView();
        initData();
    }

    private void initView() {
        setContentView(setDrawableId());
        initHeaderView();
        initContentView();
        initFooterView();
    }

    protected void initHeaderView() {
        mBackButton = (ImageView) findViewById(R.id.iv_back);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mOptionButton = (ImageView) findViewById(R.id.iv_option);
        setOnClickListener(mBackButton,mOptionButton);
    }

    protected void initFooterView() {
    }

    protected void setOnClickListener(View... views){
        for (View view:views){
            if (view != null){
                view.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_option:
                intent = new Intent();
                intent.setClass(this, OptionActivity.class);
                startActivityForResult(intent, RequestConstant.BASE_ACTIVITY_GET_PICTURE_CODE);
                break;
            default:
                break;
        }
    }
}
