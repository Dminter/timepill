package com.zncm.timepill.utils;

import android.content.Context;

import com.zncm.timepill.BuildConfig;
import com.zncm.timepill.global.TpApplication;

//LOG输出
public class L {

    public static final boolean D = BuildConfig.DEBUG;
    private static Context ctx = TpApplication.getInstance().ctx;

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: i
     */
    public static void i(String msg) {
        new LogContent(LogContent.Type.Information, msg).flush();
    }

    /**
     * @param msg
     * @param tr
     * @return void
     * @throws
     * @Title: i
     */
    public static void i(String msg, Throwable tr) {
        new LogContent(LogContent.Type.Information, msg, tr).flush();
    }

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: e
     */
    public static void e(String msg) {
        new LogContent(LogContent.Type.Error, msg).flush();
    }

    /**
     * @param msg
     * @param tr
     * @return void
     * @throws
     * @Title: e
     */
    public static void e(String msg, Throwable tr) {
        new LogContent(LogContent.Type.Error, msg, tr).flush();
    }

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: d
     */
    public static void d(String msg) {
        new LogContent(LogContent.Type.Debug, msg).flush();
    }

    /**
     * @param msg
     * @param tr
     * @return void
     * @throws
     * @Title: d
     */
    public static void d(String msg, Throwable tr) {
        new LogContent(LogContent.Type.Debug, msg, tr).flush();
    }

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: v
     */
    public static void v(String msg) {
        new LogContent(LogContent.Type.Verbose, msg).flush();
    }

    /**
     * @param msg
     * @param tr
     * @return void
     * @throws
     * @Title: v
     */
    public static void v(String msg, Throwable tr) {
        new LogContent(LogContent.Type.Verbose, msg, tr).flush();
    }

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: w
     */
    public static void w(String msg) {
        new LogContent(LogContent.Type.Warning, msg).flush();
    }

    /**
     * @param msg
     * @param tr
     * @return void
     * @throws
     * @Title: w
     */
    public static void w(String msg, Throwable tr) {
        new LogContent(LogContent.Type.Warning, msg, tr).flush();
    }

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: toastShart
     */
//    public static void toastShort(String msg) {
//        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
//    }

    // public static void toastShortTop(Activity ctx, String msg) {
    // AppMsg appMsg = AppMsg.makeText(ctx, msg, AppMsg.STYLE_INFO);
    // appMsg.setLayoutGravity(Gravity.TOP);
    // appMsg.show();
    // }
    //
    // public static void toastShort(Activity ctx, String msg) {
    // AppMsg appMsg = AppMsg.makeText(ctx, msg, AppMsg.STYLE_INFO);
    // appMsg.setLayoutGravity(Gravity.BOTTOM);
    // appMsg.show();
    // }
    //
    // public static void toastShort(Activity ctx, String msg, Style style) {
    // AppMsg appMsg = AppMsg.makeText(ctx, msg, style);
    // appMsg.setLayoutGravity(Gravity.BOTTOM);
    // appMsg.show();
    // }

    /**
     * @param msgRes
     * @return void
     * @throws
     * @Title: toastShart
     */
//    public static void toastShort(int msgRes) {
//        Toast.makeText(ctx, msgRes, Toast.LENGTH_SHORT).show();
//    }

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: toastLong
     */
//    public static void toastLong(String msg) {
//        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
//    }

    /**
     * @param msgRes
     * @return void
     * @throws
     * @Title: toastLong
     */
//    public static void toastLong(int msgRes) {
//        Toast.makeText(ctx, msgRes, Toast.LENGTH_LONG).show();
//    }
}
