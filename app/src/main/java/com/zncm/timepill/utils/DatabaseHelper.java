package com.zncm.timepill.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zncm.timepill.data.base.base.SearchHistoryData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.note.DraftData;
import com.zncm.timepill.data.base.note.NoteBookData;
import com.zncm.timepill.data.base.note.NoteComment;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.data.base.chat.MsgData;
import com.zncm.timepill.data.base.chat.TalkListData;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "tp.db";
    private static final int DATABASE_VERSION = 20;
    private RuntimeExceptionDao<MsgData, Integer> mDao = null;
    private RuntimeExceptionDao<TalkListData, Integer> tlDao = null;
    private RuntimeExceptionDao<UserData, Integer> userDao = null;
    private RuntimeExceptionDao<NoteData, Integer> noteDao = null;
    private RuntimeExceptionDao<NoteComment, Integer> noteCommentDao = null;
    private RuntimeExceptionDao<NoteBookData, Integer> noteBookDao = null;
    private RuntimeExceptionDao<DraftData, Integer> draftDao = null;
    private RuntimeExceptionDao<SearchHistoryData, Integer> shDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTableIfNotExists(connectionSource, MsgData.class);
            TableUtils.createTableIfNotExists(connectionSource, TalkListData.class);
            TableUtils.createTableIfNotExists(connectionSource, UserData.class);
            TableUtils.createTableIfNotExists(connectionSource, NoteData.class);
            TableUtils.createTableIfNotExists(connectionSource, NoteComment.class);
            TableUtils.createTableIfNotExists(connectionSource, NoteBookData.class);
            TableUtils.createTableIfNotExists(connectionSource, DraftData.class);
            TableUtils.createTableIfNotExists(connectionSource, SearchHistoryData.class);
        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

    }

    public RuntimeExceptionDao<SearchHistoryData, Integer> getShDao() {
        if (shDao == null) {
            shDao = getRuntimeExceptionDao(SearchHistoryData.class);
        }
        return shDao;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, MsgData.class, true);
            TableUtils.dropTable(connectionSource, TalkListData.class, true);
            TableUtils.dropTable(connectionSource, NoteData.class, true);
            if (oldVersion < 20) {
//                TableUtils.dropTable(connectionSource, DraftData.class, true);
                TableUtils.dropTable(connectionSource, UserData.class, true);
                TableUtils.dropTable(connectionSource, NoteData.class, true);
            }
//            TableUtils.dropTable(connectionSource, UserData.class, true);
            onCreate(db, connectionSource);
        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }


    public RuntimeExceptionDao<MsgData, Integer> getMDao() {
        if (mDao == null) {
            mDao = getRuntimeExceptionDao(MsgData.class);
        }
        return mDao;
    }

    public RuntimeExceptionDao<TalkListData, Integer> getTlDao() {
        if (tlDao == null) {
            tlDao = getRuntimeExceptionDao(TalkListData.class);
        }
        return tlDao;
    }

    public RuntimeExceptionDao<UserData, Integer> getUserDao() {
        if (userDao == null) {
            userDao = getRuntimeExceptionDao(UserData.class);
        }
        return userDao;
    }

    public RuntimeExceptionDao<NoteData, Integer> getNoteDao() {
        if (noteDao == null) {
            noteDao = getRuntimeExceptionDao(NoteData.class);
        }
        return noteDao;
    }

    public RuntimeExceptionDao<DraftData, Integer> getDraftDao() {
        if (draftDao == null) {
            draftDao = getRuntimeExceptionDao(DraftData.class);
        }
        return draftDao;
    }

    public RuntimeExceptionDao<NoteComment, Integer> getNoteCommentDao() {
        if (noteCommentDao == null) {
            noteCommentDao = getRuntimeExceptionDao(NoteComment.class);
        }
        return noteCommentDao;
    }

    public RuntimeExceptionDao<NoteBookData, Integer> getNoteBookDao() {
        if (noteBookDao == null) {
            noteBookDao = getRuntimeExceptionDao(NoteBookData.class);
        }
        return noteBookDao;
    }


    @Override
    public void close() {
        super.close();
        mDao = null;
        tlDao = null;
        userDao = null;
        noteDao = null;
        noteCommentDao = null;
        draftDao = null;
        noteBookDao = null;
    }
}
