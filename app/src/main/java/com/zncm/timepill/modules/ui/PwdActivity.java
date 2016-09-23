package com.zncm.timepill.modules.ui;

import android.os.Bundle;

import com.zncm.timepill.R;
import com.zncm.timepill.modules.ft.PwdFragment;


public class PwdActivity extends BaseAc {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("胶囊日记 安全锁");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new PwdFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_ft_base;
    }
}
