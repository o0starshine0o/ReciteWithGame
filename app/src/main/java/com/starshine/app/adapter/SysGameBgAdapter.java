package com.starshine.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.starshine.app.R;
import com.starshine.app.activity.OptionActivity;
import com.starshine.app.constant.IntentConstant;
import com.starshine.app.constant.SharedPreferencesConstant;
import com.starshine.app.model.Lexicon;
import com.starshine.app.model.SysGameBg;
import com.starshine.app.utils.BitmapUtils;
import com.starshine.app.utils.SharedPreferencesUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 系统内置游戏背景图adapter
 * Created by SunFenggang on 2014/10/27.
 */
public class SysGameBgAdapter extends BaseAdapter implements View.OnClickListener {

    private List<SysGameBg> mList;
    private Context mContext;
    public SysGameBgAdapter(Context context) {
        mContext = context;
    }

    public SysGameBgAdapter(Context context, List<SysGameBg> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setData(List<SysGameBg> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SysGameBg getItem(int position) {
        if (position < mList.size()) {
            return mList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_sys_game_bg, null);
            viewHolder = new ViewHolder();
            viewHolder.imgViewBg = (ImageView) convertView.findViewById(R.id.imvBg);
            viewHolder.imgViewUsing = (ImageView) convertView.findViewById(R.id.imvUsing);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SysGameBg sysGameBg = getItem(position);

        viewHolder.imgViewBg.setImageResource(sysGameBg.getResId());
        if (sysGameBg.isUsing()) {
            viewHolder.imgViewUsing.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgViewUsing.setVisibility(View.INVISIBLE);
        }

        convertView.setTag(R.string.position, position);
        convertView.setOnClickListener(this);
        return convertView;
    }

    /**
     * 单击选择新的背景图
     * @param v
     */
    @Override
    public void onClick(View v) {
        // 获取被单击的对象对应的资源文件id
        int id = getItem((Integer)v.getTag(R.string.position)).getResId();

        /* 保存相关属性到本地文件 */
        SharedPreferencesUtils.save(mContext,
                SharedPreferencesConstant.APP_NAME,
                SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE,
                SharedPreferencesConstant.PUZZLE_BACKGROUND_TYPE_SYS);
        SharedPreferencesUtils.save(mContext,
                SharedPreferencesConstant.APP_NAME,
                SharedPreferencesConstant.PUZZLE_BACKGROUND_RESOURCE_ID,
                id);

        /* 修改GridView中的数据 */
        for (int i=0; i<mList.size(); ++i) {
            if (mList.get(i).getResId() == id) {
                mList.get(i).setUsing(true);
            } else {
                mList.get(i).setUsing(false);
            }
        }

        /* 刷新界面 */
        this.notifyDataSetChanged();
    }

    /**
     * 清空所有Item的标志位
     */
    public void clearAllItemsState() {
        /* 修改GridView中的数据 */
        for (int i=0; i<mList.size(); ++i) {
            mList.get(i).setUsing(false);
        }

        /* 刷新界面 */
        this.notifyDataSetChanged();
    }

    private class ViewHolder {
        public ImageView imgViewBg;
        public ImageView imgViewUsing;
    }

}
