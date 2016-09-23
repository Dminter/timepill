package com.zncm.timepill.modules.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.malinskiy.materialicons.Iconify;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.utils.DatabaseHelper;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

public abstract class BaseAc extends ActionBarActivity {
    public Toolbar toolbar;
    protected Context ctx;
    public DatabaseHelper databaseHelper;
    public boolean swipeBackOn = true;

    public DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(TpApplication.getInstance().ctx, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (swipeBackOn) {
            SwipeBackHelper.onCreate(this);
        }
        if (getLayoutResource() != -1) {
            setContentView(getLayoutResource());
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(XUtil.initIconWhite(Iconify.IconValue.md_arrow_back));
            if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
                setTheme(R.style.Theme_light);
                toolbar.setBackgroundDrawable(new ColorDrawable(TpSp.getThemeColor()));
//                toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            } else if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.BLACK.getValue()) {
                setTheme(R.style.Theme_dark);
                toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.night_bar)));
            }
        }
        ctx = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (swipeBackOn) {

            SwipeBackHelper.onPostCreate(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (swipeBackOn) {

            SwipeBackHelper.onDestroy(this);
        }
    }

    protected abstract int getLayoutResource();

    public boolean isSwipeBackOn() {
        return swipeBackOn;
    }

    public void setSwipeBackOn(boolean swipeBackOn) {
        this.swipeBackOn = swipeBackOn;
    }


}
