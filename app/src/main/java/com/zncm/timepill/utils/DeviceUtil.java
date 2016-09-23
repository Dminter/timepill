package com.zncm.timepill.utils;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.zncm.timepill.global.TpApplication;


//设备工具类
public class DeviceUtil {
    // 网络
    public static boolean isNetWorkAvailable(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }

    // 得到Imsi
    @SuppressWarnings("unused")
    public static String getImsi(Context ctx) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        // return mTelephonyMgr.getSubscriberId();
        return "460029903033840";
    }

    // 得到Imei
    public static String getImei(Context ctx) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getDeviceId();
    }

    /**
     * @return 获取屏幕宽度
     */
    public int getScreenWidth(Activity context) {
        WindowManager windowManager = context.getWindowManager();
        // Display display = windowManager.getDefaultDisplay();

        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        // int height = dm.heightPixels;
        int width = dm.widthPixels;
        // int ca = display.getWidth();

        return width;
    }

    public static int getDialogWidth(Activity context) {
        WindowManager windowManager = context.getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return (width * 4 / 5);
    }

    /**
     * 检查SD卡是否可用
     *
     * @return
     * @Description
     * @author Dminter
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     */

    public static final WindowManager wm = (WindowManager) TpApplication
            .getInstance().ctx.getSystemService(Context.WINDOW_SERVICE);

    /**
     * @return DisplayMetrics
     * @throws
     * @Title: getDeviceMetrics
     */
    public static DisplayMetrics getDeviceMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * @return float
     * @throws
     * @Title: getDeviceDensity
     */
    public static float getDeviceDensity() {
        return getDeviceMetrics().density;
    }

    /**
     * @return int
     * @throws
     * @Title: getDeviceDesityDpi
     */
    public static int getDeviceDesityDpi() {
        return getDeviceMetrics().densityDpi;
    }

    /**
     * @return int
     * @throws
     * @Title: getDeviceHeight
     */
    public static int getDeviceHeight() {
        return getDeviceMetrics().heightPixels;
    }

    /**
     * @return int
     * @throws
     * @Title: getDeviceWidth
     */
    public static int getDeviceWidth() {
        return getDeviceMetrics().widthPixels;
    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title: getDeviceModel
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * @param @return
     * @return int
     * @throws
     * @Title: getOSSdkVersion
     */
    public static int getOSSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title: getOSVersion
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getVersionName(Activity ctx) {

        // 获取packagemanager的实例
        PackageManager packageManager = ctx.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 通过反射获取手机硬件信息
     *
     * @return String
     * @throws
     * @Title: getDeviceInfoByReflection
     */
    public static String getDeviceInfoByReflection() {
        StringBuffer sb = new StringBuffer();
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static Integer getVersionCode(Activity ctx) {

        // 获取packagemanager的实例
        PackageManager packageManager = ctx.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Integer versionCode = packInfo.versionCode;
        return versionCode;
    }
}
