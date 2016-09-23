package com.zncm.timepill.data;

public class EnumData {


    public enum MyEnum {

        PWD(0, "PWD"), PIC_MODE(1, "PIC_MODE"), NOTIFICATION_BAR(2, "NOTIFICATION_BAR"), SOUND_VIBRATE(3, "SOUND_VIBRATE"), THEME(4, "THEME"), CLEAR_TASK(5, "CLEAR_TASK"), LIST_ANIMATION(6, "LIST_ANIMATION"),
        ROUND_HEAD(7, "ROUND_HEAD"), CUSTOM_FACE(8, "CUSTOM_FACE"), PIC_UPLOAD(9, "PIC_UPLOAD"), ABOUT(10, "ABOUT"), MSG(11, "MSG"), EXIT(12, "MSG");
        private int value;
        private String strName;

        private MyEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }


    public enum SettingEnum {

        PWD(0, "PWD"), PIC_MODE(1, "PIC_MODE"), NOTIFICATION_BAR(2, "NOTIFICATION_BAR"), SOUND_VIBRATE(3, "SOUND_VIBRATE"), THEME(4, "THEME"), CLEAR_TASK(5, "CLEAR_TASK"), LIST_ANIMATION(6, "LIST_ANIMATION"),
        ROUND_HEAD(7, "ROUND_HEAD"), CUSTOM_FACE(8, "CUSTOM_FACE"), PIC_UPLOAD(9, "PIC_UPLOAD"), ABOUT(10, "ABOUT"), MSG(11, "MSG"), EXIT(12, "MSG");
        private int value;
        private String strName;

        private SettingEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum AboutEnum {

        VERSION(0, "版本"), AUTHOR(1, "作者"), SUGGEST(2, "反馈"), WX(3, "微信");
        private int value;
        private String strName;

        private AboutEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum MainEnum {

        NOTE_BOOK(0, "日记本"), ATTENTION(1, "关注"), PRIVATE_MSG(2, "私信"), DRAFT(3, "草稿"), NIGHT_MODE(4, "夜晚"), COVER_STORY(5, "封面"), SETTING(6, "设置"), CHANGE_ACCOUNT(7, "退出"), ABOUT_US(8, "关于"), DONATE(9, "捐助app"), SUGGEST(10, "意见反馈"), EXIT(11, "退出app");
        private int value;
        private String strName;

        private MainEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum DataTypeEnum {

        DRAFT(1, "DRAFT"), CUSTOM_FACE(2, "CUSTOM_FACE");
        private int value;
        private String strName;

        private DataTypeEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum NoteClassEnum {

        TOP(1, "TOP"), ATTENTION(2, "ATTENTION"), TOPIC(3, "TOPIC"), ME(4, "ME"), INBOOK(5, "INBOOK");
        private int value;
        private String strName;

        private NoteClassEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum UserTypeEnum {

        ME_ATTENTION(1, "ME_ATTENTION"), ATTENTION_ME(2, "ATTENTION_ME"), MASK(3, "MASK");
        private int value;
        private String strName;

        private UserTypeEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum NoteReadTypeEnum {

        READ(1, "TOP"), REPLY(2, "ATTENTION");
        private int value;
        private String strName;

        private NoteReadTypeEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum RefreshEnum {

        ERROR_NET(-1, "ERROR_NET"), NOTE_REFRESH(1, "NOTE_REFRESH"), NOTE_BOOK_REFRESH(2, "NOTE_BOOK_REFRESH"), PEOPLE_ATTENTION(3, "PEOPLE_ATTENTION"), NEW_MSG(4, "NEW_MSG"), NEW_MSG_TALK(5, "NEW_MSG_TALK"), USER_REFRESH(6, "USER_REFRESH"), NOTE_COMMENT_REFRESH(7, "NOTE_COMMENT_REFRESH");
        private int value;
        private String strName;

        private RefreshEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum SysEventEnum {

        ERROR_NET(1, "ERROR_NET");
        private int value;
        private String strName;

        private SysEventEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum SideMenuItem {


        NOTE_BOOK(0, "NOTE_BOOK"), RELATION(1, "RELATION"), TALK_LIST(2, "TALK_LIST"), DRAFT_LIST(3, "DRAFT_LIST"), THEME(4, "THEME"), SETTING(5, "SETTING"), LOGIN_OUT(6, "LOGIN_OUT"), ABOUT(7, "ABOUT"), DONATE(8, "DONATE"), EXIT(9, "EXIT");
        private int value;
        private String strName;

        private SideMenuItem(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }


    }

    public enum MsgType {


        TEXT(0, "TEXT");
        private int value;
        private String strName;

        private MsgType(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }


    }

    public enum MsgOwner {
        SENDER(1, "ME"), RECEIVER(2, "RECEIVER");
        private int value;
        private String strName;

        private MsgOwner(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }
    }


    public enum PushType {
        TIPS(1, "TIPS"), MESSAGE(10, "MESSAGE");
        private int value;
        private String strName;

        private PushType(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }
    }


    public enum MsgRead {
        READ(0, "READ"), UN_READ(1, "UN_READ");
        private int value;
        private String strName;

        private MsgRead(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }
    }


    public enum ThemeTypeEnum {

        WHITE(1, "白天"), BLACK(2, "夜间"), AUTO(3, "自动切换"),;
        private int value;
        private String strName;

        private ThemeTypeEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum PicUploadEnum {


        ATUO(1, "智能(仅WiFi上传原图)"), SOURCE(2, "原图上传"), COMPRESS(3, "压缩上传");
        private int value;
        private String strName;

        private PicUploadEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }


        public static PicUploadEnum valueOf(int value) {
            for (PicUploadEnum typeEnum : PicUploadEnum.values()) {
                if (typeEnum.value == value) {
                    return typeEnum;
                }
            }
            return null;
        }


    }

    public enum PwdEnum {
        SET(1, "set"), SETTING_CHECK(2, "setting_check"), CHECK(3, "check");
        private int value;
        private String strName;

        private PwdEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

}
