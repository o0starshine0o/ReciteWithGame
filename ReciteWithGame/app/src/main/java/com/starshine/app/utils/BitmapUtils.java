package com.starshine.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 处理bitmap
 *
 * Created by huyongsheng on 2014/6/3.
 */
public class BitmapUtils {
    /**
     * 图片圆角处理
     *
     * @param bitmap  原始图片
     * @param roundPx 圆角半径
     * @return 带圆角的bitmap图片
     */
    public static Bitmap setRoundedBitmap(Bitmap bitmap, float roundPx) {
        //创建新的位图
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //把创建的位图作为画板
        Canvas canvas = new Canvas(output);
        //设置画笔
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        //打开抗锯齿
        paint.setAntiAlias(true);
        //先绘制圆角矩形
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        //设置图像的叠加模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //绘制图像
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 图片圆角处理,支持对一个或多个角进行处理
     *
     * @param bitmap   The picture intent to add round corner
     * @param radius   The radius of the corner
     * @param location Which corner to add round corner:
     *                 1-left_top;2-left_bottom;3-right_top;4-right_bottom
     * @return The picture with round corner
     */
    public static Bitmap setRoundedBitmap(Bitmap bitmap, int radius, int... location) {
        if (bitmap == null) {
            return bitmap;
        }
        final int LEFT_TOP = 1;
        final int LEFT_BOTTOM = 2;
        final int RIGHT_TOP = 3;
        final int RIGHT_BOTTOM = 4;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Rect completeRect = new Rect(0, 0, width, height);
        Rect leftTopRect = new Rect(0, 0, radius, radius);
        Rect leftBottomRect = new Rect(0, height - radius, radius, height);
        Rect rightTopRect = new Rect(width - radius, 0, width, radius);
        Rect rightBottomRect = new Rect(width - radius, height - radius, width, height);
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        canvas.drawRect(completeRect, paint);
        if (location != null) {
            for (int corner : location)
                switch (corner) {
                    case LEFT_TOP:
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawRect(leftTopRect, paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                        canvas.drawCircle(radius, radius, radius, paint);
                        break;
                    case LEFT_BOTTOM:
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawRect(leftBottomRect, paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                        canvas.drawCircle(radius, height - radius, radius, paint);
                        break;
                    case RIGHT_TOP:
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawRect(rightTopRect, paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                        canvas.drawCircle(width - radius, radius, radius, paint);
                        break;
                    case RIGHT_BOTTOM:
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawRect(rightBottomRect, paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                        canvas.drawCircle(width - radius, height - radius, radius, paint);
                        break;
                    default:
                        break;
                }
        }
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return output;
    }

    /**
     * 图片的灰化处理
     *
     * @param bitmap 原始图片
     * @return 灰化的图片
     */
    public static Bitmap setGrayBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();

        //创建颜色变换矩阵
        ColorMatrix colorMatrix = new ColorMatrix();
        //设置灰度影响范围
        colorMatrix.setSaturation(0);
        //创建颜色过滤矩阵
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        //设置画笔的颜色过滤矩阵
        paint.setColorFilter(colorMatrixColorFilter);
        //使用处理后的画笔绘制图像
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return output;
    }

    /**
     * 获取裁剪的图片
     *
     * @param bitmap   原始图片
     * @param rows     总共列数
     * @param location 位置（顺序排列,从0开始）
     * @return 裁切的图片
     */
    public static Bitmap setCutBitmap(Bitmap bitmap, int rows, int location) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        // 裁切后所取的正方形区域边长
        int length = (bitmapWidth > bitmapHeight ? bitmapHeight : bitmapWidth) / rows;
        // 基于原图，取正方形左上角x坐标
        int retX = (location % rows) * length;
        int retY = (location / rows) * length;
        //下面这句是关键
        Bitmap result = Bitmap.createBitmap(bitmap, retX, retY, length, length);
        return result;
    }

    public static Bitmap getBitmapByUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        InputStream stream = null;
        try {
            stream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            // TODO
            Log.e("Exception", e.getMessage(), e);
        }
        if (stream == null) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        return bitmap;
    }

    public static Bitmap getCutBitmapByUri(Context context, Uri uri, int rows, int location){
        return setCutBitmap(getBitmapByUri(context, uri), rows, location);
    }

    public static Bitmap getBitmapByResourceId(Context context, int resId){
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }
}
