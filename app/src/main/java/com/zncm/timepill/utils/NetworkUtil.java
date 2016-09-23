package com.zncm.timepill.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


public class NetworkUtil {

    /**
     * 判断网络是否可用
     *
     * @param ctx
     * @return
     */
    public static boolean detect(Context ctx) {
        ConnectivityManager manager =
                (ConnectivityManager) ctx.getApplicationContext().getSystemService(
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


    public static boolean isWifiOr4g(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                ) {
            if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE) {
                return true;
            }
        }
        return false;
    }


}
