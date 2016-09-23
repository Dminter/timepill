package com.zncm.timepill.data.base.note;


import com.j256.ormlite.field.DatabaseField;
import com.zncm.timepill.data.BaseData;

public class NoteBookData extends BaseData {
    @DatabaseField(generatedId = true)
    private int global_id;
    @DatabaseField
    private int id;
    @DatabaseField
    private int user_id;
    @DatabaseField
    private String subject;
    @DatabaseField
    private String description;
    @DatabaseField
    private String created;
    @DatabaseField
    private String expired;
    @DatabaseField
    private String privacy;
    @DatabaseField
    private String cover;
    @DatabaseField
    private String isExpired;
    @DatabaseField
    private String coverUrl;
    @DatabaseField
    private String isPublic;

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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;

    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(String isExpired) {
        this.isExpired = isExpired;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }
}
