package com.zncm.timepill.data.base.note;


import com.j256.ormlite.field.DatabaseField;
import com.zncm.timepill.data.BaseData;

public class DraftData extends BaseData {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String content;
    @DatabaseField
    private int type;
    @DatabaseField
    private Long time;


    public DraftData(String content, int type) {
        this.content = content;
        this.type = type;
        this.time = System.currentTimeMillis();
    }

    public DraftData() {

    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
