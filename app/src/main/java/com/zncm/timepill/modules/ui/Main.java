package com.zncm.timepill.modules.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.utils.DeviceUtil;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import cn.jpush.android.api.JPushInterface;

public class Main extends Activity {

    private Activity ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        String pwd = TpSp.getPwdInfo();
        if (StrUtil.isEmptyOrNull(pwd)) {
            String userInfo = TpSp.getUserInfo();
            if (StrUtil.notEmptyOrNull(userInfo)) {
                JPushInterface.init(getApplicationContext());
                JPushInterface.resumePush(getApplicationContext());
                UserData userData = JSON.parseObject(userInfo, UserData.class);
                JPushInterface.setAliasAndTags(getApplicationContext(), String.valueOf(userData.getId()), null, null);
                TpApplication.getInstance().setUserData(userData);
                XUtil.getNoteBook();
                startActivity(new Intent(Main.this, HomeTabActivity.class));
            } else {
                startActivity(new Intent(Main.this, LoginAc.class));
            }
            finish();
        } else {
            Intent intent = new Intent(this, PwdActivity.class);
            intent.putExtra(TpConstants.KEY_PARAM_TYPE, EnumData.PwdEnum.CHECK.getValue());
            startActivity(intent);
            finish();
        }
    }
}