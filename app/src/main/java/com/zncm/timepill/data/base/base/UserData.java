package com.zncm.timepill.data.base.base;


import com.j256.ormlite.field.DatabaseField;
import com.zncm.timepill.data.BaseData;
import com.zncm.timepill.utils.TimeUtils;

public class UserData extends BaseData {
    @DatabaseField(generatedId = true)
    private int global_id;
    @DatabaseField
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String intro;
    @DatabaseField
    private String created;
    @DatabaseField
    private String iconUrl;
    @DatabaseField
    private String coverUrl;
    @DatabaseField
    private String state;
    @DatabaseField
    private String access_token;
    @DatabaseField
    private int user_type;//1我关注，关注我，已屏蔽

    public UserData() {
    }

    public UserData(int id) {
        this.id = id;
    }

    public UserData(MiniUserData miniUserData) {
        if (miniUserData != null) {
            this.id = miniUserData.getId();
            this.name = miniUserData.getName();
            this.iconUrl = miniUserData.getIconUrl();
        }
    }

    public UserData(String iconUrl, int id, String name) {
        this.iconUrl = iconUrl;
        this.id = id;
        this.name = name;
        this.intro = "";
        this.created = TimeUtils.getLongTime();
    }

    public UserData(String access_token) {
        this.access_token = access_token;
    }

    public int getGlobal_id() {
        return global_id;
    }

    public void setGlobal_id(int global_id) {
        this.global_id = global_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }


    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }
}
