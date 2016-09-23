package com.zncm.timepill.modules.ui;

import android.os.Bundle;

import com.zncm.timepill.R;
import com.zncm.timepill.modules.ft.AboutFt;
import com.zncm.timepill.modules.ft.DraftListFt;


public class CustomFaceAc extends BaseAc {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("自定义表情");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new DraftListFt())
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_ft_base;
    }
}
