package com.micky.weixinxlistview.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2015/12/1.
 */
public class ViewUtils {

    public static DisplayMetrics getScreenInfo(Context context) {
        // 获取屏幕大小
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm;
    }
}
