package com.starshine.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.starshine.app.R;
import com.starshine.app.constant.IntentConstant;
import com.starshine.app.constant.RequestConstant;
import com.starshine.app.constant.SharedPreferencesConstant;
import com.starshine.app.model.UserInfo;
import com.starshine.app.utils.LogUtils;
import com.starshine.app.utils.SharedPreferencesUtils;

import java.io.Serializable;
import java.net.URI;

/**
 * Modified by SunFenggang on 2014/10/26.
 * 基本重写了这个页面，添加了：
 *   1.当前选用的背景图的显示
 *   2.模式选择：正常模式-5分钟；挑战模式-2分钟
 */
public class OptionActivity extends BaseActivity {
    private static final String LOG_TAG = OptionActivity.class.getSimpleName();

    private Uri mPictureUri;

    private ImageView imgPuzzleBg;
    private RelativeLayout imgPuzzleBgWrapper;
    private Button btnNormal, btnPro;
    private String mLexiconTitle, mTableName;
    private UserInfo mUserInfo;
    private int mCode;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mLexiconTitle = intent.getStringExtra(IntentConstant.LEXICON_NAME);
        mUserInfo = (UserInfo) intent.getSerializableExtra(IntentConstant.USER_INFO);
        mTableName = intent.getStringExtra(IntentConstant.LEXICON_TABLE_NAME);
        mCode = intent.getIntExtra(IntentConstant.LEXICON_CODE, 0);
    }

    @Override
    protected int setDrawableId() {
        return R.layout.activity_option;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText(R.string.title_activity_option);
        mOptionButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initContentView() {
        imgPuzzleBg = (ImageView) findViewById(R.id.imgPuzzleBg);
        imgPuzzleBgWrapper = (RelativeLayout) findViewById(R.id.imgPuzzleBgWrapper);
        btnNormal = (Button) findViewById(R.id.btnNormal);
        btnPro = (Button) findViewById(R.id.btnPro);

        ViewTreeObserver vto = imgPuzzleBg.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float h,w;
                h = imgPuzzleBg.getDrawable().getIntrinsicHeight();
                w = imgPuzzleBg.getDrawable().getIntrinsicWidth();
                int width = imgPuzzleBg.getWidth();
                int height = (int)(width * h / w);
                imgPuzzleBg.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
                imgPuzzleBg.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        });

        setOnClickListener(imgPuzzleBgWrapper, btnNormal, btnPro);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgPuzzleBgWrapper:
                Intent intent = new Intent(OptionActivity.this, SetGameBgActivity.class);
                startActivity(intent);
                break;
            case R.id.btnNormal:
                /* 跳转到PuzzleActivity，普通模式，300s */
                startActivityToPuzzle(300);
                break;
            case R.id.btnPro:
                /* 跳转到PuzzleActivity，挑战模式，120s */
                startActivityToPuzzle(120);
                break;
            default:
                super.onClick(v);
                break;
        }
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

    @Override
    public void onResume() {
        super.onResume();

        /* 设定背景图 */
        if (SharedPreferencesUtils.getInt(OptionActivity.this,
                SharedPreferencesConstant.APP_NAME,
                SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE,
                SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE_SYS)
            == SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE_SYS) {
            int id = SharedPreferencesUtils.getInt(OptionActivity.this,
                    SharedPreferencesConstant.APP_NAME,
                    SharedPreferencesConstant.PUZZLE_BACKGROUND_RESOURCE_ID,
                    SetGameBgActivity.SYS_GAME_BGS[0]);
            imgPuzzleBg.setImageResource(id);
        } else {
            String uriStr = SharedPreferencesUtils.getString(this, SharedPreferencesConstant.APP_NAME,
                    SharedPreferencesConstant.PUZZLE_BACKGROUND_URI, "");
            if (!"".equals(uriStr)) {
                Uri uri = Uri.parse(uriStr);
                imgPuzzleBg.setImageURI(uri);
            } else {
                int id = SharedPreferencesUtils.getInt(OptionActivity.this,
                        SharedPreferencesConstant.APP_NAME,
                        SharedPreferencesConstant.PUZZLE_BACKGROUND_RESOURCE_ID,
                        SetGameBgActivity.SYS_GAME_BGS[0]);
                imgPuzzleBg.setImageResource(id);
            }
        }

    }

    private void startActivityToPuzzle(int seconds) {
        Intent intent = new Intent(this, PuzzleActivity.class);
        intent.putExtra(IntentConstant.LEXICON_CODE, mCode);
        intent.putExtra(IntentConstant.LEXICON_NAME, mLexiconTitle);
        intent.putExtra(IntentConstant.LEXICON_TABLE_NAME, mTableName);
        intent.putExtra(IntentConstant.TIME_LIMIT, seconds);
        startActivity(intent);
    }
}
