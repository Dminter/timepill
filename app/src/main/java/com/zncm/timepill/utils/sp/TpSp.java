package com.zncm.timepill.utils.sp;

import android.content.SharedPreferences;

import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.global.TpConstants;

//对系统SharedPerference的封装
public class TpSp extends SharedPerferenceBase {

    private static final String STATE_PREFERENCE = "state_preference";
    private static SharedPreferences sharedPreferences;

    enum Key {
        default_disk_path, // 程序数据默认存储位置
        temporary_disk_path, // 缓存路径
        font_size, //字体大小
        comment_open, //注释展开
        app_version_code, // 程序版本
        is_first, // 首次打开
        install_time,
        user_email,
        theme_color,
        user_info,
        pwd_info,
        note_book_data,
        tip,
        tip_ring,
        no_pic,
        have_notify,
        theme_type,
        pic_upload,
        note_topic,
        today_date,
        tab_index,
        is_talk,
        list_animation,
        round_head,
        splash_img,
        default_notebook_id
    }

    public static void setListAnimation(Boolean list_animation) {
        putBoolean(getSharedPreferences(), Key.list_animation.toString(), list_animation);
    }

    public static Boolean getListAnimation() {
        return getBoolean(getSharedPreferences(), Key.list_animation.toString(), false);
    }

    public static void setThemeColor(Integer theme_color) {
        putInt(getSharedPreferences(), Key.theme_color.toString(), theme_color);
    }

    public static Integer getThemeColor() {
        return getInt(getSharedPreferences(), Key.theme_color.toString(), TpConstants.DEFAULT_COLOR);
    }

    public static void setTabIndex(Integer tab_index) {
        putInt(getSharedPreferences(), Key.tab_index.toString(), tab_index);
    }

    public static Integer getTabIndex() {
        return getInt(getSharedPreferences(), Key.tab_index.toString(), 0);
    }

    public static void setDefaultNotebookId(Integer default_notebook_id) {
        putInt(getSharedPreferences(), Key.default_notebook_id.toString(), default_notebook_id);
    }

    public static Integer getDefaultNotebookId() {
        return getInt(getSharedPreferences(), Key.default_notebook_id.toString(), 0);
    }


    public static void setThemeType(Integer theme_type) {
        putInt(getSharedPreferences(), Key.theme_type.toString(), theme_type);
    }

    public static Integer getThemeType() {
        return getInt(getSharedPreferences(), Key.theme_type.toString(), 1);
    }

    public static void setPicUpload(Integer pic_upload) {
        putInt(getSharedPreferences(), Key.pic_upload.toString(), pic_upload);
    }

    public static Integer getPicUpload() {
        return getInt(getSharedPreferences(), Key.pic_upload.toString(), EnumData.PicUploadEnum.COMPRESS.getValue());
    }

    public static void setPwdInfo(String pwd_info) {
        putString(getSharedPreferences(), Key.pwd_info.toString(), pwd_info);
    }

    public static String getPwdInfo() {
        return getString(getSharedPreferences(), Key.pwd_info.toString(), "");
    }


    public static void setNoteBookData(String note_book_data) {
        putString(getSharedPreferences(), Key.note_book_data.toString(), note_book_data);
    }

    public static String getNoteBookData() {
        return getString(getSharedPreferences(), Key.note_book_data.toString(), "");
    }

    public static void setUserInfo(String user_info) {
        putString(getSharedPreferences(), Key.user_info.toString(), user_info);
    }

    public static String getUserInfo() {
        return getString(getSharedPreferences(), Key.user_info.toString(), "");
    }

    public static void setSplashImg(String splash_img) {
        putString(getSharedPreferences(), Key.splash_img.toString(), splash_img);
    }

    public static String getSplashImg() {
        return getString(getSharedPreferences(), Key.splash_img.toString(), "");
    }

    public static void setNoteTopic(String note_topic) {
        putString(getSharedPreferences(), Key.note_topic.toString(), note_topic);
    }

    public static String getNoteTopic() {
        return getString(getSharedPreferences(), Key.note_topic.toString(), "");
    }

    public static void setUserEmail(String user_email) {
        putString(getSharedPreferences(), Key.user_email.toString(), user_email);
    }

    public static String getUserEmail() {
        return getString(getSharedPreferences(), Key.user_email.toString(), "");
    }

    public static void setTodayDate(String today_date) {
        putString(getSharedPreferences(), Key.today_date.toString(), today_date);
    }

    public static String getTodayDate() {
        return getString(getSharedPreferences(), Key.today_date.toString(), "");
    }


    public static void setAppVersionCode(Integer app_version_code) {
        putInt(getSharedPreferences(), Key.app_version_code.toString(), app_version_code);
    }

    public static Integer getAppVersionCode() {
        return getInt(getSharedPreferences(), Key.app_version_code.toString(), 0);
    }

    public static void setIsFirst(Boolean is_first) {
        putBoolean(getSharedPreferences(), Key.is_first.toString(), is_first);
    }


    public static Boolean getIsFirst() {
        return getBoolean(getSharedPreferences(), Key.is_first.toString(), true);
    }

    public static void setTip(Boolean tip) {
        putBoolean(getSharedPreferences(), Key.tip.toString(), tip);
    }

    public static Boolean getTip() {
        return getBoolean(getSharedPreferences(), Key.tip.toString(), true);
    }


    public static void setRoundHead(Boolean round_head) {
        putBoolean(getSharedPreferences(), Key.round_head.toString(), round_head);
    }

    public static Boolean getRoundHead() {
        return getBoolean(getSharedPreferences(), Key.round_head.toString(), false);
    }


    //is_talk
    public static void setIsTalk(Boolean is_talk) {
        putBoolean(getSharedPreferences(), Key.is_talk.toString(), is_talk);
    }

    public static Boolean getIsTalk() {
        return getBoolean(getSharedPreferences(), Key.is_talk.toString(), true);
    }

    public static void setHaveNotify(Boolean have_notify) {
        putBoolean(getSharedPreferences(), Key.have_notify.toString(), have_notify);
    }

    public static Boolean getHaveNotify() {
        return getBoolean(getSharedPreferences(), Key.have_notify.toString(), false);
    }

    public static void setTipRing(Boolean tip_ring) {
        putBoolean(getSharedPreferences(), Key.tip_ring.toString(), tip_ring);
    }

    public static Boolean getTipRing() {
        return getBoolean(getSharedPreferences(), Key.tip_ring.toString(), true);
    }

    public static void setNoPic(Boolean no_pic) {
        putBoolean(getSharedPreferences(), Key.no_pic.toString(), no_pic);
    }

    public static Boolean getNoPic() {
        return getBoolean(getSharedPreferences(), Key.no_pic.toString(), false);
    }

    public static void setCommentOpen(Boolean comment_open) {
        putBoolean(getSharedPreferences(), Key.comment_open.toString(), comment_open);
    }

    public static Boolean getCommentOpen() {
        return getBoolean(getSharedPreferences(), Key.comment_open.toString(), false);
    }


    public static void setTemporaryDiskPath(String path) {
        putString(getSharedPreferences(), Key.temporary_disk_path.toString(), path);
    }

    public static String getTemporaryDiskPath() {
        return getString(getSharedPreferences(), Key.temporary_disk_path.toString(), "");
    }

    public static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            sharedPreferences = getPreferences(STATE_PREFERENCE);
        return sharedPreferences;
    }

    public static void setDefaultDiskPath(String path) {
        putString(getSharedPreferences(), Key.default_disk_path.toString(), path);
    }

    public static String getDefaultDiskPath() {
        return getString(getSharedPreferences(), Key.default_disk_path.toString(), "");
    }

    public static void setFontSize(Float font_size) {
        putFloat(getSharedPreferences(), Key.font_size.toString(), font_size);
    }

    public static Float getFontSize() {
        return getFloat(getSharedPreferences(), Key.font_size.toString(), 15.0f);
    }


    public static void setInstallTime(Long install_time) {
        putLong(getSharedPreferences(), Key.install_time.toString(), install_time);
    }

    public static Long getInstallTime() {
        return getLong(getSharedPreferences(), Key.install_time.toString(), 0l);
    }

}
