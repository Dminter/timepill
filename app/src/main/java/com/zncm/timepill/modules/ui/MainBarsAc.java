package com.zncm.timepill.modules.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.malinskiy.materialicons.Iconify;
import com.zncm.timepill.R;
import com.zncm.timepill.utils.DoubleClickImp;
import com.zncm.timepill.utils.RefreshEvent;
import com.zncm.timepill.utils.XUtil;

import de.greenrobot.event.EventBus;

public abstract class MainBarsAc extends BaseAc {
    private PagerSlidingTabStrip indicator;
public int curPositon = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setNavigationIcon(null);
        getSupportActionBar().setTitle("");
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(getTitles().length);
        indicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        XUtil.initIndicatorTab(indicator);
        EventBus.getDefault().register(this);
        if (getTitles().length > 0 && getTitles().length == 3) {
            if (getTitles()[2].equals("今日")) {
                toolbar.setNavigationIcon(XUtil.initIconWhite(Iconify.IconValue.md_arrow_back));
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main_tabs;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            curPositon = position;
            return getTitles()[position];
        }

        @Override
        public int getCount() {
            return getTitles().length;
        }


        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
        }

    }

    public abstract Fragment getFragment(int position);

    public abstract String[] getTitles();


    public abstract void onEvent(RefreshEvent event);


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}