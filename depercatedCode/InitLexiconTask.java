package com.starshine.app.asynctask;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.starshine.app.constant.DBConstant;
import com.starshine.app.constant.LexiconConstant;
import com.starshine.app.model.InitLexiconProgress;
import com.starshine.app.model.Lexicon;
import com.starshine.app.utils.DBUtils;
import com.starshine.app.utils.LogUtils;
import com.starshine.app.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 初始化词典
 *
 * Created by huyongsheng on 2014/6/10.
 * @deprecated
 */
public class InitLexiconTask extends AsyncTask<Lexicon, Void, Boolean> {

    private static final String LOG_TAG = InitLexiconTask.class.getSimpleName();

    private final Context mContext;
    private final InitLexiconListener mListener;

    private InitLexiconProgress mProgress;
    private DBUtils mDbUtils;

    public InitLexiconTask(Context context, InitLexiconListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // 建立数据库
        mDbUtils = new DBUtils(mContext);
        // 通知导入词库
        mProgress = new InitLexiconProgress();
        mListener.onInitLexiconUpdate(mProgress);
    }

    @Override
    protected Boolean doInBackground(Lexicon... lexicons) {
        if (lexicons != null) {
            // 建立连接
            mDbUtils.open();
            // 设置进度
            mProgress.setTotalLexicon(lexicons.length);
            // 遍历所有表
            for (int i = 0; i < lexicons.length; i++) {
                Lexicon lexicon = lexicons[i];
                mProgress.setNowLexicon(i + 1);
                // 获取inputStream
                InputStream inputStream = mContext.getResources().openRawResource(lexicon.getResId());
                if (inputStream != null) {
                    // 建立表
                    try {
                        createTable(lexicon.getName());
                    } catch (Exception e) {
                        LogUtils.e(LOG_TAG, "建立表{0}出错", lexicon.getName(), e);
                    }
                    try {
                        // 获取数据
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        int nowWord = 0;
                        while (bufferedReader.ready()) {
                            // 拆分数据
                            String data = bufferedReader.readLine();
                            String word[];
                            // 设置进度
                            if (nowWord == 0) {
                                mProgress.setTotalWord(Integer.valueOf(data));
                                nowWord++;
                                continue;
                            } else {
                                mProgress.setNowWord(nowWord);
                                nowWord++;
                                word = data.split(LexiconConstant.SPLIT);
                            }
                            // 插入数据库
                            if (word.length >= 2 && mDbUtils.isTableExist(lexicon.getName())) {
                                ContentValues contentValue = new ContentValues();
                                contentValue.put(DBConstant.COLUMN_WORD, word[0]);
                                for (int j = 1; j < word.length; j++){
                                    if (!word[j].equals("")){
                                        contentValue.put(DBConstant.COLUMN_EXPLAIN, word[j]);
                                        break;
                                    }
                                }
                                contentValue.put(DBConstant.COLUMN_USED_TIMES, 0);
                                mDbUtils.insert(lexicon.getName(), contentValue);
                            }
                            // 更新进度
                            publishProgress();
                        }
                    } catch (UnsupportedEncodingException e) {
                        LogUtils.e(LOG_TAG, "不支持的编码方式", e);
                    } catch (IOException e) {
                        LogUtils.e(LOG_TAG, "BufferedReader获取失败", e);
                    }
                }
            }
            // 关闭连接
            mDbUtils.close();
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        mListener.onInitLexiconUpdate(mProgress);
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
        mListener.onInitLexiconUpdate(mProgress);
    }

    /**
     * 创建表
     */
    private void createTable(String name) throws Exception {
        // 如果已经有表则删除
        String deleteSql = StringUtils.getString("DROP TABLE IF EXISTS ",name);
        mDbUtils.execSQL(deleteSql);
        //id是自动增长的主键，其他都是字符串文本类型
//        String createTableSql = StringUtils.getString("CREATE TABLE ", name,
//                " (? INTEGER PRIMARY KEY AUTOINCREMENT, ? TEXT, ? TEXT, ? INTEGER)");
//        mDbUtils.execSQL(createTableSql, new Object[]{"id","word","explain","used_times"});
        String createTableSql = StringUtils.getString("CREATE TABLE ", name, " (? INTEGER PRIMARY KEY AUTOINCREMENT, ? TEXT, ? TEXT, ? INTEGER)");
//        mDbUtils.execSQL(String.format(createTableSql,DBConstant.COLUMN_NAMES));
        mDbUtils.execSQL(createTableSql, DBConstant.COLUMN_NAMES);
    }

    public interface InitLexiconListener {
        void onInitLexiconUpdate(InitLexiconProgress process);
    }
}
