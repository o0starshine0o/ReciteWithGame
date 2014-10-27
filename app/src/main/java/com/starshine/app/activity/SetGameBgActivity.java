package com.starshine.app.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.starshine.app.R;
import com.starshine.app.adapter.SysGameBgAdapter;
import com.starshine.app.constant.RequestConstant;
import com.starshine.app.constant.SharedPreferencesConstant;
import com.starshine.app.model.SysGameBg;
import com.starshine.app.utils.LogUtils;
import com.starshine.app.utils.SharedPreferencesUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SunFenggang on 2014/10.26.
 * 本类是选择游戏背景图片的界面实现。
 */
public class SetGameBgActivity extends BaseActivity {
    private static final String LOG_TAG = SetGameBgActivity.class.getSimpleName();

    private Uri mPictureUri;

    private RelativeLayout relativeLayoutSelectFromLocal, relativeLayoutMain;
    private TextView textViewSetBgFromLocalHint, textViewClose;
    private ImageView imvSetBgFromLocalHint;
    private View popupWindowViewComment;
    private PopupWindow popupWindowComment;
    private GridView gridView;
    private SysGameBgAdapter adapter;
    private List<SysGameBg> sysGameBgList;

    /**
     * 系统内置游戏背景图
     */
    public static final int[] SYS_GAME_BGS = new int[] {
        R.drawable.bg_game_sys_1, R.drawable.bg_game_sys_2, R.drawable.bg_game_sys_3,
        R.drawable.bg_game_sys_4, R.drawable.bg_game_sys_5, R.drawable.bg_game_sys_6,
        R.drawable.bg_game_sys_7, R.drawable.bg_game_sys_8, R.drawable.bg_game_sys_9,
        R.drawable.bg_game_sys_10, R.drawable.bg_game_sys_11, R.drawable.bg_game_sys_12,
    };

    @Override
    protected void getIntentData() {

    }

    @Override
    protected int setDrawableId() {
        return R.layout.activity_set_game_bg;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText(R.string.title_activity_set_game_bg);
        mOptionButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initContentView() {
        relativeLayoutMain = (RelativeLayout) findViewById(R.id.relativeLayoutMain);
        relativeLayoutSelectFromLocal = (RelativeLayout) findViewById(R.id.relativeLayoutSelectFromLocal);
        imvSetBgFromLocalHint = (ImageView) findViewById(R.id.imvSetBgFromLocalHint);
        textViewSetBgFromLocalHint = (TextView) findViewById(R.id.textViewSetBgFromLocalHint);
        popupWindowViewComment = getLayoutInflater().inflate(R.layout.popup_window_set_game_bg_comment, null);
        textViewClose = (TextView) popupWindowViewComment.findViewById(R.id.textViewClose);
        gridView = (GridView) findViewById(R.id.gridView);

        /* 实例化提示窗口 */
        popupWindowComment = new PopupWindow(popupWindowViewComment,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindowComment.setFocusable(true);
        // 实现在popup window之外点击时隐藏popup window
        popupWindowComment.setOutsideTouchable(true);
        popupWindowComment.setBackgroundDrawable(new BitmapDrawable()); // 必不可少
        // 设定动画效果
        popupWindowComment.setAnimationStyle(R.style.popup_animation_comment);

        /* 初始化系统内置背景图GridView */
        initGridView();

        setOnClickListener(relativeLayoutSelectFromLocal, imvSetBgFromLocalHint,
                textViewSetBgFromLocalHint, textViewClose);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeLayoutSelectFromLocal:
                getPicture();
                break;
            case R.id.imvSetBgFromLocalHint:
            case R.id.textViewSetBgFromLocalHint:
                /* 打开温馨提示窗口 */
                openPopupWindow();
                break;
            case R.id.textViewClose:
                /* 关闭温馨提示窗口 */
                closePopupWindow();
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
                    // 在本地文件存储图片URI
                    SharedPreferencesUtils.save(this, SharedPreferencesConstant.APP_NAME,
                            SharedPreferencesConstant.PUZZLE_BACKGROUND_URI, mPictureUri.toString());
                    // 在本地文件存储背景图片类型
                    SharedPreferencesUtils.save(this, SharedPreferencesConstant.APP_NAME,
                            SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE,
                            SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE_CUSTOM);
                    LogUtils.d(LOG_TAG, "图片URI是：", mPictureUri.toString());
                    Toast.makeText(this, "设定成功", Toast.LENGTH_SHORT).show();
                    adapter.clearAllItemsState();
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

    /**
     * 打开温馨提示窗口
     */
    private void openPopupWindow() {
        if (popupWindowComment != null && !popupWindowComment.isShowing()) {
            popupWindowComment.showAtLocation(relativeLayoutMain, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 关闭温馨提示窗口
     */
    private void closePopupWindow() {
        if (popupWindowComment != null && popupWindowComment.isShowing()) {
            popupWindowComment.dismiss();
        }
    }

    /**
     * 初始化系统内置背景图GridView
     */
    private void initGridView() {
        if (sysGameBgList == null) {
            sysGameBgList = new ArrayList<SysGameBg>();
        }

        // 获取当前背景图类型
        int currentBgType = SharedPreferencesUtils.getInt(SetGameBgActivity.this,
                SharedPreferencesConstant.APP_NAME,
                SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE,
                SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE_SYS);  // 默认：系统内置背景
        // 获取当前设定的系统内置背景图（如果有的话）
        int currentSysBg = SharedPreferencesUtils.getInt(SetGameBgActivity.this,
                SharedPreferencesConstant.APP_NAME,
                SharedPreferencesConstant.PUZZLE_BACKGROUND_RESOURCE_ID,
                SYS_GAME_BGS[0]);                                       // 默认：系统内置背景第一张

        /* 初始化系统内置背景图数据 */
        for (int i=0; i<SYS_GAME_BGS.length; ++i) {
            SysGameBg sysGameBg = new SysGameBg();
            sysGameBg.setResId(SYS_GAME_BGS[i]);
            if (currentBgType == SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE_SYS
                    && currentSysBg == SYS_GAME_BGS[i]) {
                sysGameBg.setUsing(true);
            } else {
                sysGameBg.setUsing(false);
            }
            sysGameBgList.add(sysGameBg);
        }

        adapter = new SysGameBgAdapter(SetGameBgActivity.this, sysGameBgList);
        gridView.setAdapter(adapter);
    }
}
