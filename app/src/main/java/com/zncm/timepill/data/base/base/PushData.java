package com.zncm.timepill.data.base.base;


import com.zncm.timepill.data.BaseData;

public class PushData extends BaseData {

    private int id;
    private int type;
    private String msg_content;
    private String content;
    private String created;
    private String link_user_id;
    private int user_id;
    private int link_id;

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

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLink_user_id() {
        return link_user_id;
    }

    public void setLink_user_id(String link_user_id) {
        this.link_user_id = link_user_id;
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
}
