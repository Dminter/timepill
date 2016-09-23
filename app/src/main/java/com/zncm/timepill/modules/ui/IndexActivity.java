package com.zncm.timepill.modules.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.malinskiy.materialicons.Iconify;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.modules.ft.NoteFollowFt;
import com.zncm.timepill.modules.ft.NoteNewFt;
import com.zncm.timepill.modules.ft.NoteTopicFt;
import com.zncm.timepill.modules.ft.UserNoteFt;
import com.zncm.timepill.utils.DoubleClickImp;
import com.zncm.timepill.utils.RefreshEvent;
import com.zncm.timepill.utils.XUtil;


public class IndexActivity extends MainBarsAc {
    private NoteNewFt noteNewFt;
    private NoteFollowFt followFt;
    private NoteTopicFt topicFt;
    UserNoteFt userNoteFt;
    private Fragment fragment = null;

    @Override
    public Fragment getFragment(int position) {
        switch (position) {
            case 0:
                noteNewFt = new NoteNewFt();
                fragment = noteNewFt;
                break;
            case 1:
                followFt = new NoteFollowFt();
                fragment = followFt;
                break;
            case 2:
                userNoteFt = new UserNoteFt();
                fragment = userNoteFt;
                break;
            case 3:
                topicFt = new NoteTopicFt();
                fragment = topicFt;
                break;

        }
        return fragment;
    }

    @Override
    public String[] getTitles() {
        return new String[]{"最新", "关注", "今日", "话题"};
    }


    @Override
    public void onEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.NOTE_REFRESH.getValue()) {
            noteNewFt.onRefresh();
            userNoteFt.onRefresh();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackOn(false);
        DoubleClickImp.registerDoubleClickListener(toolbar,
                new DoubleClickImp.OnDoubleClickListener() {
                    @Override
                    public void OnSingleClick(View v) {

                    }

                    @Override
                    public void OnDoubleClick(View v) {
                        toTop();
                    }
                });
        toolbar.setNavigationIcon(XUtil.initIconWhite(Iconify.IconValue.md_refresh));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTop();
            }
        });
    }

    private void toTop() {
        try {
            noteNewFt.toTop();
            followFt.toTop();
            topicFt.toTop();
        } catch (Exception e) {
            Intent newIntent = new Intent(ctx, Main.class);
            startActivity(newIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("md_add").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_add)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == null || item.getTitle() == null) {
            return false;
        }
        if (item.getTitle().equals("md_add")) {
            Intent newIntent = new Intent(ctx, NoteNewAc.class);
            startActivity(newIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}