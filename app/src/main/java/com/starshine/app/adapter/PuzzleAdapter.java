package com.starshine.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starshine.app.R;
import com.starshine.app.model.PuzzleItem;
import com.starshine.app.utils.BitmapUtils;
import com.starshine.app.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 拼图的adapter
 *
 * @author huyongsheng 2014-05-22
 */
public class PuzzleAdapter extends BaseAdapter implements View.OnClickListener {
    private static final int COLUMNS = 3;

    private List<PuzzleItem> mList;
    private List<PuzzleItem> mOriginalList = new ArrayList<PuzzleItem>();
    private boolean isRun = false;
    private int mEmptyPosition;
    private Context mContext;
    private GameResultListener mListener;

    public PuzzleAdapter(Context context, GameResultListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(List<PuzzleItem> list) {
        for (int index = 0; index < list.size(); index++) {
            mOriginalList.add(index, list.get(index));
        }
        mList = list;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PuzzleItem getItem(int position) {
        if (mList.size() >= position) {
            return mList.get(position);
        } else {
            return null;
        }
    }

    public String getOriginalItemWord(int position) {
        if (mOriginalList.size() >= position && mOriginalList.get(position) != null) {
            return mOriginalList.get(position).getEnWord();
        } else {
            return "";
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InitViewHolder initHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_puzzle, null);
            // TODO
            int length = (DeviceUtils.getScreenWidth(mContext) - DeviceUtils.dip2px(mContext, (COLUMNS - 1) * 2 * 5f + 20)) / COLUMNS;
            convertView.setLayoutParams(new AbsListView.LayoutParams(length, length));
            initHolder = new InitViewHolder();
            initHolder.contentViewRun = (LinearLayout) convertView.findViewById(R.id.ll_puzzle_item_1);
            initHolder.contentViewPause = (LinearLayout) convertView.findViewById(R.id.ll_puzzle_item_2);
            initHolder.bgView = (ImageView) convertView.findViewById(R.id.iv_bg);
            initHolder.word0 = (TextView) convertView.findViewById(R.id.tv_word_0);
            initHolder.word1 = (TextView) convertView.findViewById(R.id.tv_word_1);
            initHolder.word2 = (TextView) convertView.findViewById(R.id.tv_word_2);
            convertView.setTag(initHolder);
        } else {
            initHolder = (InitViewHolder) convertView.getTag();
        }
        if (isRun) {
            initHolder.contentViewRun.setVisibility(View.VISIBLE);
            initHolder.contentViewPause.setVisibility(View.GONE);
            if (getItem(position).getEnWord() == null) {
                initHolder.word0.setText(getOriginalItemWord(position));
                initHolder.word0.setTextColor(mContext.getResources().getColor(R.color.black));
                initHolder.bgView.setImageBitmap(null);
            } else {
                initHolder.word0.setText(getItem(position).getCnWord());
                initHolder.word0.setTextColor(mContext.getResources().getColor(R.color.white));
                initHolder.bgView.setImageBitmap(mList.get(position).getBitmap());
            }
        } else {
            initHolder.contentViewRun.setVisibility(View.GONE);
            initHolder.contentViewPause.setVisibility(View.VISIBLE);
            if (getItem(position).getEnWord() == null) {
                initHolder.bgView.setImageBitmap(null);
            } else {
                initHolder.word1.setText(getItem(position).getEnWord());
                initHolder.word2.setText(getItem(position).getCnWord());
                initHolder.bgView.setImageBitmap(mList.get(position).getBitmap());
            }
        }
        convertView.setTag(R.string.position, position);
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag(R.string.position);
        // 如果未运行，则点击无效
        if (isRun){
        // 检测此item是否为空格
            if (position != mEmptyPosition) {
                if (canExchange(position, mEmptyPosition)) {
                    exchange(position, mEmptyPosition);
                    mEmptyPosition = position;
                    notifyDataSetChanged();
                    if (isFinish()) {
                        mListener.winGame();
                    }
                }
            }
        }
    }


    private boolean canExchange(int position, int mEmptyPosition) {
        boolean canExchange;
        int columnsResult = position % COLUMNS;
        // 先检测空格是否位于点击item的上方或下方
        canExchange = Math.abs(position - mEmptyPosition) == COLUMNS;
        if (!canExchange) {
            // 如果空格不在点击item的上方或下方，则检测空格是否位于点击item的左侧或右侧
            if (columnsResult == 0) {
                // 如果点击item位于第一列，则只判断右侧是否是空格
                canExchange = (position + 1) == mEmptyPosition;
            } else if (columnsResult == COLUMNS - 1) {
                // 如果点击item位于最后一列，则只判断左侧是否是空格
                canExchange = (position - 1) == mEmptyPosition;
            } else {
                // 如果点击item不位于两侧，这要检测点击item的两侧是否有空格
                canExchange = Math.abs(position - mEmptyPosition) == 1;
            }
        }
        return canExchange;
    }

    private void exchange(int position, int emptyPosition) {
        mList.set(emptyPosition, mList.get(position));
        mList.set(position, new PuzzleItem());
    }

    private boolean isFinish() {
        boolean isFinish = true;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i) == null) {
                continue;
            } else if (mList.get(i).getInitPosition() != i) {
                isFinish = false;
                break;
            }
        }
        return isFinish;
    }

    public void randList() {
        Collections.shuffle(mList);
    }

    public void initEmptyPosition() {
        for (int index = 0; index < mList.size(); index++) {
            if (mList.get(index).getEnWord() == null) {
                mEmptyPosition = index;
                break;
            }
        }
    }

    public void updateGameState(boolean state) {
        isRun = state;
    }

    public interface GameResultListener {
        void winGame();
    }

    private class InitViewHolder {
        private LinearLayout contentViewRun;
        private LinearLayout contentViewPause;
        private ImageView bgView;
        private TextView word0;
        private TextView word1;
        private TextView word2;
    }
}
