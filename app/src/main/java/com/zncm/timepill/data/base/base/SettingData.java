package com.zncm.timepill.data.base.base;

import com.zncm.timepill.data.BaseData;

import java.io.Serializable;

public class SettingData extends BaseData implements Serializable {
    private static final long serialVersionUID = 8838725426885988959L;
    private int id;
    private int type;
    private CharSequence title;
    private CharSequence status;
    private BaseData data;


    public SettingData() {
    }


    public SettingData(int id, CharSequence title) {
        this.id = id;
        this.title = title;
    }

    public SettingData(int id, CharSequence title,CharSequence status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }


    public SettingData(int id, int type, BaseData data, CharSequence title, CharSequence status) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.title = title;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }


    public CharSequence getStatus() {
        return status;
    }

    public void setStatus(CharSequence status) {
        this.status = status;
    }

    public BaseData getData() {
        return data;
    }

    public void setData(BaseData data) {
        this.data = data;
    }
}
