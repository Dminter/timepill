package com.zncm.timepill.data.base.chat;


import com.alibaba.fastjson.annotation.JSONField;
import com.j256.ormlite.field.DatabaseField;
import com.zncm.timepill.data.BaseData;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.MiniUserData;
import com.zncm.timepill.utils.TimeUtils;

public class MsgData extends BaseData {
    @JSONField(serialize = false)
    @DatabaseField(generatedId = true)
    private int id;
    @JSONField(serialize = false)
    @DatabaseField
    private int is_send;// 消息是发送1还是接收2
    @DatabaseField
    private int user_id;
    @DatabaseField
    private int friend_id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String iconUrl;
    @DatabaseField
    private int type;//0文本
    @JSONField(serialize = false)
    @DatabaseField
    private int read;//0已读,1->未读
    @DatabaseField
    private String content;
    @JSONField(serialize = false)
    @DatabaseField
    private String created;//创建时间

    public MsgData() {
    }

    public MsgData(MiniUserData user, String content, int friend_id) {
        this.user_id = user.getId();
        this.name = user.getName();
        this.iconUrl = user.getIconUrl();
        this.content = content;
        this.friend_id = friend_id;
        this.type = EnumData.MsgType.TEXT.getValue();
        this.read = EnumData.MsgRead.READ.getValue();
        this.is_send = EnumData.MsgOwner.SENDER.getValue();
        this.created = TimeUtils.getLongTime();
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }
}
