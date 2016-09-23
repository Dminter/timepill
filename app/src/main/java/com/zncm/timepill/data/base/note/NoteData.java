package com.zncm.timepill.data.base.note;


import com.j256.ormlite.field.DatabaseField;
import com.zncm.timepill.data.BaseData;
import com.zncm.timepill.data.base.base.MiniUserData;

public class NoteData extends BaseData {
    @DatabaseField(generatedId = true)
    private int global_id;
    @DatabaseField
    private int id;
    @DatabaseField
    private int user_id;
    @DatabaseField
    private int notebook_id;
    @DatabaseField
    private String notebook_subject;
    @DatabaseField
    private String content;
    @DatabaseField
    private String created;
    @DatabaseField
    private int type;
    @DatabaseField
    private int comment_count;
    @DatabaseField
    private String photoUrl;
    @DatabaseField
    private String photoThumbUrl;
    @DatabaseField
    private int note_class;
    @DatabaseField
    private int noteReadType;//浏览过 1 回复过2
    @DatabaseField
    private Long time;
    private MiniUserData user;//[id,name,iconUrl]
    @DatabaseField
    private String db_user;

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

    public int getNotebook_id() {
        return notebook_id;
    }

    public void setNotebook_id(int notebook_id) {
        this.notebook_id = notebook_id;
    }

    public String getNotebook_subject() {
        return notebook_subject;
    }

    public void setNotebook_subject(String notebook_subject) {
        this.notebook_subject = notebook_subject;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public MiniUserData getUser() {
        return user;
    }

    public void setUser(MiniUserData user) {
        this.user = user;
    }

    public String getPhotoThumbUrl() {
        return photoThumbUrl;
    }

    public void setPhotoThumbUrl(String photoThumbUrl) {
        this.photoThumbUrl = photoThumbUrl;
    }

    public int getNote_class() {
        return note_class;
    }

    public void setNote_class(int note_class) {
        this.note_class = note_class;
    }


    public int getNoteReadType() {
        return noteReadType;
    }

    public void setNoteReadType(int noteReadType) {
        this.noteReadType = noteReadType;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getGlobal_id() {
        return global_id;
    }

    public void setGlobal_id(int global_id) {
        this.global_id = global_id;
    }


    public String getDb_user() {
        return db_user;
    }

    public void setDb_user(String db_user) {
        this.db_user = db_user;
    }
}
