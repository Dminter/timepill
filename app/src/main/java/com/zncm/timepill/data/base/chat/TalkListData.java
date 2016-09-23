package com.zncm.timepill.data.base.chat;


import com.j256.ormlite.field.DatabaseField;
import com.zncm.timepill.data.BaseData;
import com.zncm.timepill.data.base.base.MiniUserData;
import com.zncm.timepill.utils.TimeUtils;

public class TalkListData extends BaseData {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int is_send;// 消息是发送1还是接收2
    @DatabaseField
    private int user_id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String iconUrl;
    @DatabaseField
    private String content;
    @DatabaseField
    private String created;//创建时间

    public TalkListData() {
    }

    public TalkListData(MiniUserData user, String content) {
        this.user_id = user.getId();
        this.name = user.getName();
        this.iconUrl = user.getIconUrl();
        this.content = content;
        this.is_send = 1;
        this.created = TimeUtils.getLongTime();
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getIs_send() {
        return is_send;
    }

    public void setIs_send(int is_send) {
        this.is_send = is_send;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
