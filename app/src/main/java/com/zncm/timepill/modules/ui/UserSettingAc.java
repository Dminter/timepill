package com.zncm.timepill.modules.ui;

import android.content.Intent;
import android.os.Bundle;

import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.modules.ft.UserSettingFt;
import com.zncm.timepill.utils.RefreshEvent;

import de.greenrobot.event.EventBus;


public class UserSettingAc extends BaseAc {
    UserSettingFt userSettingFt = new UserSettingFt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("编辑个人资料");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, userSettingFt)
                .commit();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_ft_base;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
    }


    public void onEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.USER_REFRESH.getValue()) {
            userSettingFt.getUserInfo();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.USER_REFRESH.getValue()));
    }
}
