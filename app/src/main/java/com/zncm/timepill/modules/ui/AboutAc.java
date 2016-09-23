package com.zncm.timepill.modules.ui;

import android.os.Bundle;

import com.zncm.timepill.R;
import com.zncm.timepill.modules.ft.AboutFt;


public class AboutAc extends BaseAc {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("关于");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new AboutFt())
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_ft_base;
    }
}
