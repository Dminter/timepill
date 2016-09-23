package com.zncm.timepill.data.base.base;

import com.zncm.timepill.data.BaseData;


public class FaceData extends BaseData {
    private String content;
    private String meaning;

    public FaceData() {
    }

    public FaceData(String mText) {
        this.content = mText;
    }

    public FaceData(String text, String meaning) {
        this.meaning = meaning;
        content = text;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
