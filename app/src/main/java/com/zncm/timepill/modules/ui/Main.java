package com.zncm.timepill.modules.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.utils.DeviceUtil;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

public class Main extends Activity {

    private Activity ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {

            int color = Color.parseColor("#2DBD60");
            XUtil.debug("color:" + color);

        } catch (Exception e) {

        }


        ctx = this;
        if (TpSp.getAppVersionCode() != null && DeviceUtil.getVersionCode(ctx) != null
                && DeviceUtil.getVersionCode(ctx) > TpSp.getAppVersionCode()) {
            // 记录最新版本号,下次进入不再显示引导页
            TpSp.setAppVersionCode(DeviceUtil.getVersionCode(ctx));
            if (TpConstants.betaVersion) {
                TpSp.setUserInfo(null);
            }
        }
        String pwd = TpSp.getPwdInfo();
        if (StrUtil.isEmptyOrNull(pwd)) {
            Intent intent = new Intent(this, SplashAc.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, PwdActivity.class);
            intent.putExtra(TpConstants.KEY_PARAM_TYPE, EnumData.PwdEnum.CHECK.getValue());
            startActivity(intent);
            finish();
        }
    }
}