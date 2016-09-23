package com.zncm.timepill.data.base.base;


import com.zncm.timepill.data.BaseData;

public class CommentData extends BaseData {

    private String dairy_id;
    private String author;
    private String comment_id;


    public CommentData() {
    }


    public String getDairy_id() {
        return dairy_id;
    }

    public void setDairy_id(String dairy_id) {
        this.dairy_id = dairy_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }
}
