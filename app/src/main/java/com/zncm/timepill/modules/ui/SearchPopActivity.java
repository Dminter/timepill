package com.zncm.timepill.modules.ui;

import android.os.Bundle;

import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.modules.ft.BaseFt;
import com.zncm.timepill.modules.ft.SearchHistoryFragment;
import com.zncm.timepill.utils.sp.TpSp;


public class SearchPopActivity extends BaseAc {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();

//        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
//            swipeLayout.setBackgroundColor(getResources().getColor(R.color.day_bg));
//
//        } else {
//            swipeLayout.setBackgroundColor(getResources().getColor(R.color.night_bg));
//        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new SearchHistoryFragment())
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
        return R.layout.activity_search;
    }
}
