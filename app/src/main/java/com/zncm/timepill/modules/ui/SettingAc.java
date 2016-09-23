package com.zncm.timepill.modules.ui;

import android.os.Bundle;

import com.zncm.timepill.R;
import com.zncm.timepill.modules.ft.SettingFt;


public class SettingAc extends BaseAc {
    SettingFt settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("设置");
        settingFragment = new SettingFt();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, settingFragment)
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_ft_base;
    }
}
