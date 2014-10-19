package com.starshine.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
import com.starshine.app.activity.PuzzleActivity;
import com.starshine.app.constant.IntentConstant;
import com.starshine.app.model.Lexicon;
import com.starshine.app.utils.BitmapUtils;

import java.text.DecimalFormat;
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
            viewHolder.title = (TextView) convertView.findViewById(R.id.textViewTitle);
            viewHolder.progress = (TextView) convertView.findViewById(R.id.textViewProgress);
            viewHolder.complete = (TextView) convertView.findViewById(R.id.textViewComplete);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imgViewIcon);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Lexicon lexicon = getItem(position);

        viewHolder.title.setText(lexicon.getName());
        viewHolder.complete.setText(lexicon.getComplete() + "/" + lexicon.getTotal());
        viewHolder.progress.setText(new DecimalFormat("###.##").format(
                100*(float)lexicon.getComplete()/lexicon.getTotal())+"%");
        setLexiconIcon(viewHolder.imageView, lexicon.getIconId());
        viewHolder.progressBar.setMax(lexicon.getTotal());
        viewHolder.progressBar.setProgress(lexicon.getComplete());
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

    /**
     * 设定词库图标背景
     * @param imageView
     * @param resourceId
     */
    private void setLexiconIcon(final ImageView imageView, int resourceId) {
        /* 处理图片，绘制圆角 */
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resourceId);
        bitmap = BitmapUtils.setRoundedBitmap(bitmap, 30, 1, 3);
        imageView.setImageBitmap(bitmap);

        /* 自适配图片，使图片宽高比不变 */
        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float h,w;
                h = imageView.getDrawable().getIntrinsicHeight();
                w = imageView.getDrawable().getIntrinsicWidth();
                int width = imageView.getWidth();
                int height = (int)(width * h / w);
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        });
    }

    /**
     * 更新词库完成数量
     * @param data
     */
    public void updateComplete(int[] data) {
        for (int i=0; i<data.length; ++i) {
            getItem(i).setComplete(data[i]);
        }
        this.notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView title;          // 词库名称
        public TextView complete;       // 完成信息
        public TextView progress;       // 进度信息
        public ProgressBar progressBar; // 进度条
        public ImageView imageView;     // 词库图标
    }

}
