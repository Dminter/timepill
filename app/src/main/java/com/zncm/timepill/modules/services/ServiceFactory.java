package com.zncm.timepill.modules.services;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.utils.NetworkUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public final class ServiceFactory {


    public static RestAdapter restAdapter;
    public static Map<Class, Object> serviceMap = new HashMap<Class, Object>();
    static Context context;




    public static boolean bReg = false;

    public static void setContext(Context context) {
        ServiceFactory.context = context;
    }

    public static RestAdapter getRestAdapter() {

        UserData tmp = TpApplication.getInstance().getUserData();
        //SharedApplication 被回收 重新实例化
        if (tmp == null) {
            String userInfo = TpSp.getUserInfo();
            if (StrUtil.notEmptyOrNull(userInfo)) {
                UserData userData = JSON.parseObject(userInfo, UserData.class);
                TpApplication.getInstance().setUserData(userData);
                XUtil.getNoteBook();
                tmp = TpApplication.getInstance().getUserData();
            }
        }

        if (restAdapter == null) {
            if (bReg) {
                restAdapter = new RestAdapter.Builder()
                        .setEndpoint(TpConstants.BASE_API_URL)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setErrorHandler(new RestServiceErrorHandler())
                        .build();
            } else {
                restAdapter = new RestAdapter.Builder()
                        .setEndpoint(TpConstants.BASE_API_URL)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setRequestInterceptor(getRequestInterceptor(tmp.getAccess_token()))
                        .setErrorHandler(new RestServiceErrorHandler())
                        .build();
            }

        }
        return restAdapter;
    }

    private static RequestInterceptor getRequestInterceptor(final String token) {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Authorization", "Basic " + token);
            }
        };

        return requestInterceptor;
    }

    public static <T> T getService(Class<T> cls) {
        if (cls != null && cls == RegService.class) {
            bReg = true;
        } else {
            bReg = false;
        }

        if (serviceMap.containsKey(cls)) {
            return (T) serviceMap.get(cls);
        } else {
            T service = getRestAdapter().create(cls);
            serviceMap.put(cls, service);
            return service;
        }
    }
}
