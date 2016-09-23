package com.zncm.timepill.data.base.base;


import com.zncm.timepill.data.BaseData;

public class PushTalkData extends BaseData {
    private int uid;
    private String content;
    private Long time;
    private String name;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
