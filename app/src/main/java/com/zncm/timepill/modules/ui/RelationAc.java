package com.zncm.timepill.modules.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;

import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.modules.ft.AttentionMeF;
import com.zncm.timepill.modules.ft.MeAttentionF;
import com.zncm.timepill.modules.ft.MeMaskF;
import com.zncm.timepill.utils.RefreshEvent;

public class RelationAc extends MainBarsAc {
    MeAttentionF meAttentionF;
    MeMaskF meMaskF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackOn(false);
    }

    @Override
    public Fragment getFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                meAttentionF = new MeAttentionF();
                fragment = meAttentionF;
                break;
            case 1:
                fragment = new AttentionMeF();
                break;
            case 2:
                meMaskF = new MeMaskF();
                fragment = meMaskF;
                break;
        }

        return fragment;
    }

    @Override
    public String[] getTitles() {
        return new String[]{"我关注", "关注我", "已屏蔽"};
    }

    @Override
    public void onEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.PEOPLE_ATTENTION.getValue()) {
            meAttentionF.onRefresh();
            meMaskF.onRefresh();
        }
    }


}