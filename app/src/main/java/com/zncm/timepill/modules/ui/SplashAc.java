

package com.zncm.timepill.modules.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.utils.DeviceUtil;
import com.zncm.timepill.utils.NetworkUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.sp.TpSp;

import cn.jpush.android.api.JPushInterface;

public class SplashAc extends Activity {
    private Handler handler;
    private boolean bIncome = false;
    private String splash_img;
    private ImageView imageView;
    private TextView tvAppInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);
        try {
            goUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        splash_img = MobclickAgent.getConfigParams(this, "splash_img");
//        if (StrUtil.notEmptyOrNull(splash_img)) {
//            TpSp.setSplashImg(splash_img);
//        } else {
//            splash_img = TpSp.getSplashImg();
//        }
        imageView = (ImageView) findViewById(R.id.imageView);
//        XUtil.getImageLoader().displayImage(splash_img, imageView, XUtil.getOptions());
        tvAppInfo = (TextView) findViewById(R.id.tvAppInfo);
        tvAppInfo.setText(getResources().getString(R.string.app_name) + " @上善版 " + DeviceUtil.getVersionName(SplashAc.this));
        String userInfo = TpSp.getUserInfo();
        if (StrUtil.notEmptyOrNull(userInfo)) {
            JPushInterface.init(getApplicationContext());
            JPushInterface.resumePush(getApplicationContext());
            bIncome = true;
            UserData userData = JSON.parseObject(userInfo, UserData.class);
            JPushInterface.setAliasAndTags(getApplicationContext(), String.valueOf(userData.getId()), null, null);
            TpApplication.getInstance().setUserData(userData);
            XUtil.getNoteBook();
        } else {
            bIncome = false;
        }
        handler = new Handler();
//        if (!NetworkUtil.detect(this)) {
//            netError();
//        } else {
//        }
        handler.postDelayed(startAct, 3000);
    }

    private void netError() {
        new MaterialDialog.Builder(this)
                .content("没有网络!!!")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        handler.postDelayed(startAct, 500);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        finish();

                    }


                })
                .show();
    }

    Runnable startAct = new Runnable() {

        @Override
        public void run() {
            if (bIncome) {
                startActivity(new Intent(SplashAc.this, HomeTabActivity.class));
            } else {
                startActivity(new Intent(SplashAc.this, LoginAc.class));
            }
            finish();
        }

    };

    // 友盟自动更新监听
    private void goUpdate() {
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case 0:
                        UmengUpdateAgent.showUpdateDialog(SplashAc.this, updateInfo);
                        break;
                }
            }
        });
    }
}
