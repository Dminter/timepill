package com.zncm.timepill.modules.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.malinskiy.materialicons.Iconify;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.modules.ft.DraftListFt;
import com.zncm.timepill.modules.ft.NoteBooksFt;
import com.zncm.timepill.modules.ft.UserInfoF;
import com.zncm.timepill.modules.ft.UserNoteFt;
import com.zncm.timepill.utils.RefreshEvent;
import com.zncm.timepill.utils.XUtil;

public class UserHomeAc extends MainBarsAc {

    UserNoteFt userNoteFt;
    NoteBooksFt noteBooksFt;
    UserInfoF userInfoF;
    boolean isSelf = true;


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        isSelf = getIntent().getBooleanExtra("isSelf", true);
        return super.onCreateView(parent, name, context, attrs);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isSelf) {
            setSwipeBackOn(false);
            toolbar.setNavigationIcon(XUtil.initIconWhite(Iconify.IconValue.md_search));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, SearchPopActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public Fragment getFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                userInfoF = new UserInfoF();
                fragment = userInfoF;
                break;
            case 1:
                noteBooksFt = new NoteBooksFt();
                fragment = noteBooksFt;
                break;
            case 2:
                if (isSelf) {
                    fragment = new DraftListFt();
                } else {
                    userNoteFt = new UserNoteFt();
                    fragment = userNoteFt;
                }

                break;
        }

        return fragment;
    }

    @Override
    public String[] getTitles() {
        String[] titles = new String[]{"简介", "日记本", "今日"};
        if (isSelf) {
            titles = new String[]{"简介", "日记本", "草稿"};
        }
        return titles;
    }

    @Override
    public void onEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.NOTE_BOOK_REFRESH.getValue()) {
            noteBooksFt.onRefresh();
        } else if (type == EnumData.RefreshEnum.USER_REFRESH.getValue()) {
            userInfoF.getUserInfo();
        }
    }


}