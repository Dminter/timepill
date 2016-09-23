package com.zncm.timepill.data.base.tips;


import com.zncm.timepill.data.BaseData;

public class TipUserData extends BaseData {

    private int id;
    private String name;

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
}
