package com.zncm.timepill.data.base.base;

import com.j256.ormlite.field.DatabaseField;
import com.zncm.timepill.data.BaseData;

import java.io.Serializable;

public class SearchHistoryData extends BaseData implements Serializable {

    private static final long serialVersionUID = 8838725426885988959L;
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String key;
    @DatabaseField
    private Long time;


    public SearchHistoryData() {
    }


    public SearchHistoryData(String key) {
        this.key = key;
        this.time = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
