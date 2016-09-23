package com.zncm.timepill.modules.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;

import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.modules.ft.TipsFt;
import com.zncm.timepill.utils.RefreshEvent;


public class MsgActivity extends MainBarsAc {

    private TipsFt tipsFt;
    private TalkListFt talkListFt;

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
                tipsFt = new TipsFt();
                fragment = tipsFt;
                break;
            case 1:
                talkListFt = new TalkListFt();
                fragment = talkListFt;
                break;
        }
        return fragment;
    }

    @Override
    public String[] getTitles() {
        return new String[]{"消息", "私信"};
    }

    @Override
    public void onEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.NEW_MSG.getValue()) {
            tipsFt.onRefresh();
        } else if (type == EnumData.RefreshEnum.NEW_MSG_TALK.getValue()) {
            talkListFt.onRefresh();
        }
    }

}