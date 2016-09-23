package com.zncm.timepill.data.base.note;


import com.j256.ormlite.field.DatabaseField;
import com.zncm.timepill.data.BaseData;
import com.zncm.timepill.data.base.base.MiniUserData;

public class NoteComment extends BaseData {
    @DatabaseField(generatedId = true)
    private int global_id;
    @DatabaseField
    private int id;
    @DatabaseField
    private int user_id;
    @DatabaseField
    private int recipient_id;
    @DatabaseField
    private int dairy_id;
    @DatabaseField
    private String content;
    @DatabaseField
    private String created;
    private MiniUserData user;
    private MiniUserData recipient;
    @DatabaseField
    private String db_user;
    @DatabaseField
    private String db_recipient;

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

    public int getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(int recipient_id) {
        this.recipient_id = recipient_id;
    }

    public int getDairy_id() {
        return dairy_id;
    }

    public void setDairy_id(int dairy_id) {
        this.dairy_id = dairy_id;
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

    public MiniUserData getUser() {
        return user;
    }

    public void setUser(MiniUserData user) {
        this.user = user;
    }

    public MiniUserData getRecipient() {
        return recipient;
    }

    public void setRecipient(MiniUserData recipient) {
        this.recipient = recipient;
    }


    public String getDb_user() {
        return db_user;
    }

    public void setDb_user(String db_user) {
        this.db_user = db_user;
    }

    public String getDb_recipient() {
        return db_recipient;
    }

    public void setDb_recipient(String db_recipient) {
        this.db_recipient = db_recipient;
    }
}
