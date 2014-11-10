package com.starshine.app.asynctask;

import android.os.AsyncTask;

import com.starshine.app.utils.LogUtils;

/**
 * 拼图计时器
 *
 * Created by huyongsheng on 2014/6/3.
 *
 * Modified by SunFengggang on 2014/11/9.
 * 修改了计时时间（每一秒刷新一次界面）
 *
 */
public class CountdownTask extends AsyncTask<Integer, Integer, String> {
    private static final int SECOND = 1000;
    private String tag = CountdownTask.class.getSimpleName();

    private int mTimeLimit;
    private TimeUpdateListener mListener;

    public interface TimeUpdateListener{
        void onTimeUpdate(int time);
    }

    public CountdownTask(int timeLimit, TimeUpdateListener listener){
        mTimeLimit = timeLimit;
        mListener = listener;
    }

    @Override
    protected String doInBackground(Integer... params) {
        while(mTimeLimit > 0)
        {
            publishProgress(mTimeLimit);
            try {
                Thread.sleep(SECOND);
                mTimeLimit--;
            } catch (InterruptedException e) {
                StringBuffer log = new StringBuffer();
                log.append("Thread sleep error:");
                log.append(e.getStackTrace());
                LogUtils.e(tag,log.toString());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        mListener.onTimeUpdate(mTimeLimit);
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        mListener.onTimeUpdate(mTimeLimit);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
