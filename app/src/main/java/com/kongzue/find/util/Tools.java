package com.kongzue.find.util;

import android.content.Context;

/**
 * Created by chao on 2016/3/4.
 */
public class Tools {

    public static String StartFindWords="";

    //用于进行dip和px转换
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //用于进行px和dip转换
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //检测一段文字中是否含有某个词汇
    public static boolean isHaveWord(String original,String haveWord){
        boolean flag=false;
        String replaceStr=original;
        if ((replaceStr.replaceAll(haveWord,"")).length()!=original.length()){
            flag=true;
        }
        return flag;
    }
}
