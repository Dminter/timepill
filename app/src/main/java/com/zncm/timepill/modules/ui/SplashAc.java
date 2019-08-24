

package com.zncm.timepill.modules.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zncm.timepill.R;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.utils.DeviceUtil;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import cn.jpush.android.api.JPushInterface;

public class SplashAc extends Activity {
    private TextView tvAppInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);
        ImageView  imageView = (ImageView) findViewById(R.id.imageView);
        tvAppInfo = (TextView) findViewById(R.id.tvAppInfo);
        tvAppInfo.setText(getResources().getString(R.string.app_name) + " @上善版 " + DeviceUtil.getVersionName(SplashAc.this));
    }

}
