package com.starshine.app.activity;

import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

import com.starshine.app.R;
import com.starshine.app.adapter.LexiconAdapter;
import com.starshine.app.model.Lexicon;
import com.starshine.app.utils.DBUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Modified by Sunfenggang on 2014/10/16.
 * 添加了对词库单词总量、已完成量、词库背景的初始化方法以及相关变量、资源文件。
 */
public class ChooseLexiconActivity extends BaseActivity {
    private ListView mLexiconListView;
    private DBUtils dbUtils;
    private Map<String, Integer> lexiconNumMap;
    private Map<String, Integer> lexiconCompleteNumMap;
    private LexiconAdapter adapter;
    private long lastClickBackTime = 0;

    /* 词库图标id */
    private static final int[] LEXICON_ICONS_ID = {
            R.drawable.icon_lexicon_cet4,       // 四级图标
            R.drawable.icon_lexicon_cet6,       // 六级图标
            R.drawable.icon_lexicon_gre,        // GRE图标
            R.drawable.icon_lexicon_ielts,      // 雅思图标
            R.drawable.icon_lexicon_toefl       // 托福图标
    };

    @Override
    protected void getIntentData() {

    }

    @Override
    protected int setDrawableId() {
        return R.layout.activity_choose_lexicon;
    }

    @Override
    protected void initContentView() {
        mLexiconListView = (ListView) findViewById(R.id.lv_lexicon);
    }

    @Override
    public void onResume() {
        super.onResume();
        /* 更新完成情况 */
        refreshCompleteNum();
    }

    @Override
    protected void initData() {
        dbUtils = new DBUtils(ChooseLexiconActivity.this);
        adapter = new LexiconAdapter(this);
        List<Lexicon> lexiconList = new ArrayList<Lexicon>();
        String[] lexicons = getResources().getStringArray(R.array.lexicon);
        String[] tableName = getResources().getStringArray(R.array.table_name);

        /* 初始化词库数量 */
        initLexiconNum();
        /* 初始化已完成数量 */
        initLexiconCompleteNum();

        for (int i = 0;i < lexicons.length; i++){
            Lexicon lexicon = new Lexicon();
            lexicon.setCode(i);
            lexicon.setName(lexicons[i]);
            lexicon.setTableName(tableName[i]);
            lexicon.setTotal(lexiconNumMap.get(tableName[i]));
            lexicon.setComplete(lexiconCompleteNumMap.get(tableName[i]));
            lexicon.setIconId(LEXICON_ICONS_ID[i]);
            lexiconList.add(lexicon);
        }
        adapter.setData(lexiconList);
        mLexiconListView.setAdapter(adapter);
    }

    /**
     * 初始化词库数量
     */
    private void initLexiconNum() {
        lexiconNumMap = new HashMap<String, Integer>();
        dbUtils.open();
        Cursor cursor = dbUtils.rawQuery("select * from sqlite_sequence");
        while (cursor.moveToNext()) {
            lexiconNumMap.put(cursor.getString(0), cursor.getInt(1));
        }
        dbUtils.close();
    }

    /**
     * 初始化词库已完成数量
     */
    private void initLexiconCompleteNum() {
        if (lexiconCompleteNumMap == null) {
            lexiconCompleteNumMap = new HashMap<String, Integer>();
        }

        String[] tableName = getResources().getStringArray(R.array.table_name); // 数据库表名
        dbUtils.open();
        for (int i=0; i<tableName.length; ++i) {
            /* 查询已完成数量 */
            Cursor cursor = dbUtils.rawQuery("select count(*) from " + tableName[i] + " where used_times > 0");
            if (cursor.moveToNext()) {
                lexiconCompleteNumMap.put(tableName[i], cursor.getInt(0));
            }
        }
        dbUtils.close();
    }

    /**
     * 更新完成情况
     */
    private void refreshCompleteNum() {
        /* 更新词库完成数量 */
        initLexiconCompleteNum();
        /* 将新的词库完成数量以固定顺序添加到数组中 */
        String[] tableName = getResources().getStringArray(R.array.table_name);
        int[] data = new int[tableName.length];
        for (int i=0; i<data.length; ++i) {
            data[i] = lexiconCompleteNumMap.get(tableName[i]);
        }
        /* 更新到adapter */
        adapter.updateComplete(data);
    }

    /**
     * 监听按键，连续按两次返回退出程序
     * 两次按键间隔2s
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - lastClickBackTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                lastClickBackTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
