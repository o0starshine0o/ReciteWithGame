package com.starshine.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.starshine.app.R;
import com.starshine.app.activity.PuzzleActivity;
import com.starshine.app.constant.IntentConstant;
import com.starshine.app.model.Lexicon;

import java.util.List;

/**
 * 选取词典的adapter
 *
 * Created by huyongsheng on 2014/6/4.
 */
public class LexiconAdapter extends BaseAdapter implements View.OnClickListener {
    private static final int CET4_LEXICON = 0;
    private static final int CET6_LEXICON = 1;
    private static final int GRE_LEXICON = 2;
    private static final int IELTS_LEXICON = 3;
    private static final int TOEFL_LEXICON = 4;

    private List<Lexicon> mList;
    private Context mContext;

    public LexiconAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Lexicon> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Lexicon getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_lexicon, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(getItem(position).getName());
        convertView.setTag(R.string.position, position);
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag(R.string.position);
        Intent intent = new Intent(mContext, PuzzleActivity.class);
        intent.putExtra(IntentConstant.LEXICON_NAME, getItem(position).getName());
        intent.putExtra(IntentConstant.LEXICON_CODE, getItem(position).getCode());
        intent.putExtra(IntentConstant.LEXICON_TABLE_NAME, getItem(position).getTableName());
        mContext.startActivity(intent);
    }

    private class ViewHolder{
        public TextView title;
    }
}
