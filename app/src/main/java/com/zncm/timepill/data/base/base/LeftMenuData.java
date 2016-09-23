package com.zncm.timepill.data.base.base;

import android.graphics.drawable.Drawable;

import com.zncm.timepill.data.BaseData;

import java.io.Serializable;

public class LeftMenuData extends BaseData implements Serializable {
    private static final long serialVersionUID = 8838725426885988959L;
    private Drawable icon;
    private String title;
    private int id;

    public LeftMenuData() {
    }

    public LeftMenuData(String title) {
        this.title = title;
    }

    public LeftMenuData(Drawable icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public LeftMenuData(int id, Drawable icon, String title) {
        this.id = id;
        this.icon = icon;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
