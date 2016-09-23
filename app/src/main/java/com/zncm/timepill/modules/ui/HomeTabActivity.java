package com.zncm.timepill.modules.ui;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.malinskiy.materialicons.Iconify;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.RefreshEvent;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.TimeUtils;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class HomeTabActivity extends TabActivity {

    private static HomeTabActivity instance;

    public static HomeTabActivity getInstance() {
        return instance;
    }

    private Drawable norDrawable[] = new Drawable[]{
            XUtil.initIconThemeUnSel(Iconify.IconValue.md_menu),
            XUtil.initIconThemeUnSel(Iconify.IconValue.md_people_outline),
            XUtil.initIconThemeUnSel(Iconify.IconValue.md_notifications_none),
            XUtil.initIconThemeUnSel(Iconify.IconValue.md_account_box)
    };
    private Drawable norDrawableSel[] = new Drawable[]{
            XUtil.initIconTheme(Iconify.IconValue.md_menu),
            XUtil.initIconTheme(Iconify.IconValue.md_people_outline),
            XUtil.initIconTheme(Iconify.IconValue.md_notifications_none),
            XUtil.initIconTheme(Iconify.IconValue.md_account_box)
    };
    private ArrayList<ImageView> imgs = new ArrayList<>();
    private ArrayList<TextView> titles = new ArrayList<>();
    private Activity ctx;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            setTheme(R.style.Theme_light);
        } else if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.BLACK.getValue()) {
            setTheme(R.style.Theme_dark);
        }
        XUtil.debug("theme:" + TpSp.getThemeColor());
        setContentView(R.layout.tab_main);
        ctx = this;
        instance = this;
        setTabs();
        for (int i = 0; i < norDrawable.length; i++) {
            setIcon(i, false);
        }
        setIcon(0, true);
        TabHost tabHost = getTabHost();
        try {
            tabHost.setCurrentTab(TpSp.getTabIndex());
        } catch (Exception e) {

        }
        EventBus.getDefault().register(this);
    }

    private void setIcon(int i, boolean bSel) {
        Drawable drawable = norDrawable[i];
        if (bSel) {
            drawable = norDrawableSel[i];
        }

        if (drawable != null) {
            imgs.get(i).setVisibility(View.VISIBLE);
            imgs.get(i).setImageDrawable(drawable);
        } else {
            imgs.get(i).setVisibility(View.GONE);
        }
        TextView textView = titles.get(i);
        if (textView != null) {
            if (bSel) {
                textView.setTextColor(TpSp.getThemeColor());
            } else {
                textView.setTextColor(getResources().getColor(R.color.mi_text));
            }
        }
    }

    private void setTabs() {
        addTab(0, "胶囊", IndexActivity.class);
        addTab(1, "关注", RelationAc.class);
        addTab(2, "消息", MsgActivity.class);
        addTab(3, "我", UserHomeAc.class);
    }

    private void addTab(int id, String labelId, Class<?> c) {
        final TabHost tabHost = getTabHost();
        Intent intent = new Intent(this, c);
        TabHost.TabSpec spec = tabHost.newTabSpec(String.valueOf(id));
        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        if (XUtil.notEmptyOrNull(labelId)) {
            title.setVisibility(View.VISIBLE);
            title.setText(labelId);
        } else {
            title.setVisibility(View.GONE);
        }
        titles.add(title);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        imgs.add(icon);
        spec.setIndicator(tabIndicator);
        spec.setContent(intent);

//        tabHost.setBackgroundColor(TpSp.getThemeColor());

        tabHost.addTab(spec);
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                int id = Integer.parseInt(tabId);
                if (id == 2) {
                    TpApplication.msgNew = false;
                    initMsgCount();
                }
                for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
                    if (id == i) {
                        TpSp.setTabIndex(id);
                        setIcon(i, true);
                    } else {
                        setIcon(i, false);
                    }
                }

            }
        });
    }


    private void initMsgCount() {
        TextView tvMsg = titles.get(2);
        if (StrUtil.listNotNull(titles) && tvMsg != null) {
            if (TpApplication.msgNew) {
                tvMsg.setText(Html.fromHtml("<font color='#FF0033'>" + "消息" + "</font>"));
            } else {
                tvMsg.setText("消息");
            }
        }
    }

    public void onEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.NEW_MSG.getValue()) {
            initMsgCount();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            String today = TimeUtils.getDateYDM();
            String todayDate = TpSp.getTodayDate();
            if (StrUtil.notEmptyOrNull(todayDate)) {
                if (today.equals(todayDate)) {
                    String pwd = TpSp.getPwdInfo();
                    if (StrUtil.isEmptyOrNull(pwd)) {
                        this.moveTaskToBack(true);
                    } else {
                        finish();
                    }
                } else {
                    DbUtils.deleteNoteByClass(EnumData.NoteClassEnum.TOP.getValue());
                    DbUtils.deleteNoteByClass(EnumData.NoteClassEnum.ATTENTION.getValue());
                    DbUtils.deleteNoteByClass(EnumData.NoteClassEnum.TOPIC.getValue());
                    TpSp.setTodayDate(today);
                    finish();
                }
            } else {
                TpSp.setTodayDate(today);
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}