package com.starshine.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.starshine.app.R;
import com.starshine.app.constant.SharedPreferencesConstant;
import com.starshine.app.model.PuzzleItem;
import com.starshine.app.utils.DeviceUtils;
import com.starshine.app.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 拼图的adapter
 *
 * @author huyongsheng 2014-05-22
 *
 * Modified by SunFenggang on 2014/11/12.
 * 修改了游戏结束判断函数，消除了判断错误的bug
 */
public class PuzzleAdapter extends BaseAdapter implements View.OnClickListener {
    private static final int COLUMNS = 3;

    private List<PuzzleItem> mList;
    private List<PuzzleItem> mOriginalList = new ArrayList<PuzzleItem>();
    private boolean isRun = false;
    private int mEmptyPosition;
    private Context mContext;
    private GameResultListener mListener;
    private int color;
    private boolean[] locked;
    private SoundPool soundPool;

    public PuzzleAdapter(Context context, GameResultListener listener) {
        mContext = context;
        mListener = listener;
        color = SharedPreferencesUtils.getInt(context, SharedPreferencesConstant.APP_NAME,
                SharedPreferencesConstant.PUZZLE_TEXT_COLOR, Color.BLACK);
        soundPool= new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(mContext, R.raw.good, 1);
    }

    public void setData(List<PuzzleItem> list) {
        for (int index = 0; index < list.size(); index++) {
            mOriginalList.add(index, list.get(index));
        }
        mList = list;
        // 设定最后一个小块的initPosition为-1，否则在isFinish判断中会造成误判
        mList.get(mList.size()-1).setInitPosition(-1);
        // 初始化locked
        locked = new boolean[list.size()];
        for (int i=0; i<list.size(); ++i) {
            locked[i] = false;
        }
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
            initHolder.imvLock = (ImageView) convertView.findViewById(R.id.imvLock);
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
                initHolder.word0.setTextColor(color);
                initHolder.bgView.setImageBitmap(mList.get(position).getBitmap());
                if (mList.get(position).getInitPosition() == position) {
                    initHolder.word0.setText(getItem(position).getEnWord()+" : "+getItem(position).getCnWord());
                    initHolder.imvLock.setVisibility(View.VISIBLE);
                    locked[position] = true;
                } else {
                    initHolder.imvLock.setVisibility(View.GONE);
                }
            }
        } else {
            initHolder.contentViewRun.setVisibility(View.GONE);
            initHolder.contentViewPause.setVisibility(View.VISIBLE);
            if (getItem(position).getEnWord() == null) {
                initHolder.bgView.setImageBitmap(null);
            } else {
                initHolder.word1.setText(getItem(position).getEnWord());
                initHolder.word2.setText(getItem(position).getCnWord());
                initHolder.word1.setTextColor(color);
                initHolder.word2.setTextColor(color);
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
//                if (canExchange(position, mEmptyPosition)) {
                if (!locked[position]){
                    exchange(position, mEmptyPosition);
                    mEmptyPosition = position;
                    notifyDataSetChanged();
                    if (isFinish()) {
                        mListener.winGame();
                    }
                }
//                }
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
        mList.get(position).setInitPosition(-1); // 设定其初始值为-1，否则会影响isFinish的判定结果
        if (mList.get(emptyPosition).getInitPosition() == emptyPosition) {
            locked[emptyPosition] = true;
            soundPool.play(1, 1, 1, 0, 0, 1);
        }
    }

    private boolean isFinish() {
        boolean isFinish = true;
        int len = mList.size() - 1; // 只检测1-8小块
        for (int i = 0; i < len; i++) {
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
        List<PuzzleItem> tmpList;
        tmpList = new ArrayList<PuzzleItem>();
        for(int i=0;i<=8;i++) {
            tmpList.add(i, mList.get(i));
        }
        int serialnum[] = { 2,3,5,1,8,7,6,4,0};
        serialnum = changegame(serialnum);
        for(int i=0;i<=8;i++) {
            mList.set(i, tmpList.get(serialnum[i]));
        }
    }

    int[] changegame(int[] serialnum) {
		/*
		 * { int i=0; int sum=0, tmpnum,tmpindex; for(i=0;i<=8;i++){
		 * if(serialnum[i]!=0){ for(int j=i+1;j<=8;j++){
		 * if(serialnum[j]<serialnum[i]&&serialnum[j]!=0){ sum++; } } } }
		 * if(sum%2!=0){ tmpnum = serialnum[0]; tmpindex=0; for(int
		 * j=1;j<=8;j++){ if(tmpnum<serialnum[j]){
		 * serialnum[tmpindex]=serialnum[j]; serialnum[j]=tmpnum; break; } else
		 * if(tmpnum<serialnum[j]){ tmpnum=serialnum[j]; tmpindex = j; } } }}
		 */
        int count = 0;
        int direction[] = new int[20];
        int zeroposition = 8;
        int tmp;
        int movenum = 0;
        for (int i = 0; i <= 8; i++) {
            if (serialnum[i] == 0) {
                zeroposition = i;
                break;
            }
        }
        Random rdm = new Random(System.currentTimeMillis());

        while (count < 20) {
            direction[count++] = Math.abs(rdm.nextInt()) % 4;
        }
        count = 0;
        while (movenum < 30) {
            if (count == 20) {
                count = 0;
            }
            int cd = direction[count];
            if (cd == 0) {
                if (zeroposition - 3 >= 0) {
                    serialnum[zeroposition] = serialnum[zeroposition - 3];
                    serialnum[zeroposition - 3] = 0;
                    zeroposition = zeroposition - 3;
                    movenum++;
                }
            } else if (cd == 1) {
                if (zeroposition + 3 <= 8) {
                    serialnum[zeroposition] = serialnum[zeroposition + 3];
                    serialnum[zeroposition + 3] = 0;
                    zeroposition = zeroposition + 3;
                    movenum++;
                }
            } else if (cd == 2) {
                if (zeroposition != 0 && zeroposition != 3 && zeroposition != 6) {
                    serialnum[zeroposition] = serialnum[zeroposition - 1];
                    serialnum[zeroposition - 1] = 0;
                    zeroposition = zeroposition - 1;
                    movenum++;
                }
            } else if (cd == 3) {
                if (zeroposition != 2 && zeroposition != 5 && zeroposition != 8) {
                    serialnum[zeroposition] = serialnum[zeroposition + 1];
                    serialnum[zeroposition + 1] = 0;
                    zeroposition = zeroposition + 1;
                    movenum++;
                }
            }
            count++;
        }
        return serialnum;
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
        private ImageView imvLock;
    }
}
