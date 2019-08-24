package com.zncm.timepill.modules.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.zncm.timepill.R;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.services.UserService;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.Base64Util;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginAc extends BaseAc {
    private TextView btnLogin, btnReg;
    private EditText etUserName, etUserPWD;
    private SuperActivityToast progressToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("登录");
        toolbar.setNavigationIcon(null);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etUserPWD = (EditText) findViewById(R.id.etUserPWD);
        btnLogin = (TextView) findViewById(R.id.btnLogin);
        btnReg = (TextView) findViewById(R.id.btnReg);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        if (StrUtil.notEmptyOrNull(TpSp.getUserEmail())) {
            etUserName.setText(TpSp.getUserEmail());
        }
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAc.this, RegAc.class);
                startActivity(intent);
            }
        });
        progressToast = new SuperActivityToast(LoginAc.this, SuperToast.Type.PROGRESS);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.ac_login;
    }

    private void login() {
        try {
            final String userName = etUserName.getText().toString().trim();
            String userPwd = etUserPWD.getText().toString().trim();
            if (StrUtil.isEmptyOrNull(userName) || StrUtil.isEmptyOrNull(userPwd)) {
                XUtil.tShort("用户名或者密码不能为空!");
                return;
            }
            final String tokenStr = Base64Util.encode((userName + ":" + userPwd).getBytes());
            UserData data = new UserData(tokenStr);
            TpApplication.getInstance().setUserData(data);
            progressToast.setText("正在登陆...");
            progressToast.setIndeterminate(true);
            progressToast.setProgressIndeterminate(true);
            progressToast.show();
            ServiceFactory.restAdapter = null;
            ServiceFactory.serviceMap.clear();
            ServiceFactory.getService(UserService.class).getMyInfo(new Callback<UserData>() {
                @Override
                public void success(UserData userData, Response response) {
                    if (userData == null) {
                        return;
                    }
                    progressToast.dismiss();
                    TpSp.setUserEmail(userName);
                    userData.setAccess_token(tokenStr);
                    TpApplication.getInstance().setUserData(userData);
                    TpSp.setUserInfo(userData.toString());
                    Intent intent = new Intent(LoginAc.this, Main.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    progressToast.dismiss();
                    XUtil.tShort("用户名或者密码错误~");
                }
            });
        } catch (Exception e) {
        }
    }

}
