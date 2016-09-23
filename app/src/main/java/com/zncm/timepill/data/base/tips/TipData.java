package com.zncm.timepill.data.base.tips;


import com.zncm.timepill.data.BaseData;

public class TipData extends BaseData {

    private int id;
    private int user_id;
    private int link_id;
    private int link_user_id;
    private int type;//  "type": 2, 关注  "type": 1, 回复
    private String created;
    private int read;
    private String real_content;
    public TipContent content;

    public class TipContent {
        public int comment_id;
        public int dairy_id;
        public TipUserData author;
        public TipUserData user;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getLink_id() {
        return link_id;
    }

    public void setLink_id(int link_id) {
        this.link_id = link_id;
    }

    public int getLink_user_id() {
        return link_user_id;
    }

    public void setLink_user_id(int link_user_id) {
        this.link_user_id = link_user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }


    public String getReal_content() {
        return real_content;
    }

    public void setReal_content(String real_content) {
        this.real_content = real_content;
    }
}
