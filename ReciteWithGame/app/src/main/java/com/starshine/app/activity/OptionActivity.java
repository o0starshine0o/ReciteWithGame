package com.starshine.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.starshine.app.R;
import com.starshine.app.constant.RequestConstant;
import com.starshine.app.constant.SharedPreferencesConstant;
import com.starshine.app.utils.LogUtils;
import com.starshine.app.utils.SharedPreferencesUtils;

public class OptionActivity extends BaseActivity {
    private static final String LOG_TAG = OptionActivity.class.getSimpleName();

    private Uri mPictureUri;

    private TextView mChangePictureButton;

    @Override
    protected void getIntentData() {

    }

    @Override
    protected int setDrawableId() {
        return R.layout.activity_option;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mOptionButton.setVisibility(View.GONE);
    }

    @Override
    protected void initContentView() {
        mChangePictureButton = (TextView) findViewById(R.id.tv_change_picture);
        setOnClickListener(mChangePictureButton);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change_picture:
                getPicture();
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    private void getPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //开启Pictures画面Type设定为image
        intent.setType("image/*");
        //取得相片后返回本画面
        startActivityForResult(intent, RequestConstant.OPTION_ACTIVITY_GET_PICTURE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestConstant.OPTION_ACTIVITY_GET_PICTURE_CODE:
                    mPictureUri = data.getData();
                    SharedPreferencesUtils.save(this, SharedPreferencesConstant.APP_NAME, SharedPreferencesConstant.PUZZLE_BACKGROUND_URI, mPictureUri.toString());
                    LogUtils.d(LOG_TAG, "图片URI是：", mPictureUri.toString());
                    finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.setData(mPictureUri);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
