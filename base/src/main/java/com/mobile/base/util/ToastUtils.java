package com.mobile.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast工具类
 */
public class ToastUtils {

//    private static Toast sToast = null;

    /**
     * 显示一个Toast
     *
     * @param context  Context
     * @param strResId 字符串资源ID
     */
    public static void toast(Context context, int strResId) {
        toast(context, strResId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示一个Toast
     *
     * @param context Context
     * @param msg     信息
     */
    public static void toast(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示一个Toast
     *
     * @param context  Context
     * @param strResId 字符串资源ID
     * @param time     显示时间
     */
    public static void toast(Context context, int strResId, int time) {
        try{
            toast(context, context.getString(strResId), time);
        }catch (Exception e){
            e.printStackTrace();
            if (e instanceof Resources.NotFoundException){
//                ((BaseActivity)context).isPlug = true;
                toast(context, context.getString(strResId), time);
//                ((BaseActivity)context).isPlug = false;
            }
        }

    }

    /**
     * 显示一个Toast
     *
     * @param context Context
     * @param msg     信息
     * @param time    显示时间
     */
    public static void toast(Context context, String msg, int time) {
        if (context == null ||
                ((context instanceof Activity) &&
                        ((Activity) context).isFinishing())) {
            return;
        }
        Toast sToast = Toast.makeText(context, msg, time);
        sToast.setText(msg);
        sToast.setDuration(time);
        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.show();

    }
}
