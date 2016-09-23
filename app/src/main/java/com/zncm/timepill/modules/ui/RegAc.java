package com.zncm.timepill.modules.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.umeng.analytics.MobclickAgent;
import com.zncm.timepill.R;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.services.RegService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.services.UserService;
import com.zncm.timepill.utils.Base64Util;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegAc extends BaseAc {
    private TextView btnReg;
    private EditText etUserName, etUserPWD, etUserNickName;
    private SuperActivityToast progressToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("胶囊-注册");
        etUserName = (EditText) findViewById(R.id.etUserName);
        etUserNickName = (EditText) findViewById(R.id.etUserNickName);
        etUserPWD = (EditText) findViewById(R.id.etUserPWD);
        btnReg = (TextView) findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg();
            }
        });
        progressToast = new SuperActivityToast(RegAc.this, SuperToast.Type.PROGRESS);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.ac_reg;
    }

    private void reg() {
        try {
            final String userName = etUserName.getText().toString().trim();
            String userPwd = etUserPWD.getText().toString().trim();
            String userNickName = etUserNickName.getText().toString().trim();
            if (StrUtil.isEmptyOrNull(userName) || StrUtil.isEmptyOrNull(userPwd) || StrUtil.isEmptyOrNull(userNickName)) {
                XUtil.tShort("用户名，密码，昵称均不能为空!");
                return;
            }
            progressToast.setText("注册中...");
            progressToast.setIndeterminate(true);
            progressToast.setProgressIndeterminate(true);
            progressToast.show();
            ServiceFactory.restAdapter = null;
            ServiceFactory.serviceMap.clear();

            ServiceFactory.getService(RegService.class).reg(userNickName, userName, userPwd, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    progressToast.dismiss();
                    XUtil.tShort("注册成功，开始登录吧~");
                    TpSp.setUserEmail(userName);
                    finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    progressToast.dismiss();
                    XUtil.onFailure(error);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
