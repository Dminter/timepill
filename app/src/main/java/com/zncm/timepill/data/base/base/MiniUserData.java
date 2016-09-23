package com.zncm.timepill.data.base.base;


import com.zncm.timepill.data.BaseData;

public class MiniUserData extends BaseData {


    private int id;
    private String name;
    private String iconUrl;

    public MiniUserData(int id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public MiniUserData() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
