package com.zncm.timepill.data.base.tips;


import com.zncm.timepill.data.BaseData;

public class TipCommentData extends BaseData {

    private int dairy;
    private int comment;
    private String author;

    public int getDairy() {
        return dairy;
    }

    public void setDairy(int dairy) {
        this.dairy = dairy;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
