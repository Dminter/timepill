package com.zncm.timepill.modules.ui;

import android.os.Bundle;

import com.zncm.timepill.R;
import com.zncm.timepill.modules.ft.ShowInfoFragement;


public class ShowInfoActivity extends BaseAc {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new ShowInfoFragement())
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.ac_showinfo;
    }



}
