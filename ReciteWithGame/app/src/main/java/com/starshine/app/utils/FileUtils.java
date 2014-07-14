package com.starshine.app.utils;

import android.content.Context;

import com.starshine.app.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 处理文件相关
 *
 * Created by huyongsheng on 2014/6/13.
 */
public class FileUtils {
    private static final String LOG_TAG = FileUtils.class.getSimpleName();

    /**
     * 把资源文件复制到指定位置
     */
    public static final void save(Context context, String path, String name, int resId){
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, name);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            InputStream inputStream = context.getResources().openRawResource(resId);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bytes=new byte[inputStream.available()];
            inputStream.read(bytes);
            fileOutputStream.write(bytes);
            inputStream.close();
            fileOutputStream.close();
        }catch(FileNotFoundException e){
            LogUtils.e(LOG_TAG,"文件不存在",e);
        }catch(IOException e) {
            LogUtils.e(LOG_TAG,"IO错误",e);
        }
    }

    public static final boolean isFileExists(String path, String name){
        boolean result = false;
        File dir = new File(path);
        if(dir.exists()) {
            File file = new File(dir, name);
            if(file.exists()) {
                result = true;
            }
        }
        return result;
    }
}
