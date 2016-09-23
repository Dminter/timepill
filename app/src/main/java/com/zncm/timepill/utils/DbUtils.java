package com.zncm.timepill.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.MiniUserData;
import com.zncm.timepill.data.base.base.SearchHistoryData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.chat.MsgData;
import com.zncm.timepill.data.base.chat.TalkListData;
import com.zncm.timepill.data.base.note.DraftData;
import com.zncm.timepill.data.base.note.NoteBookData;
import com.zncm.timepill.data.base.note.NoteComment;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.global.TpApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MX on 11/18 0018.
 */
public class DbUtils {
    static DatabaseHelper databaseHelper = null;
    static RuntimeExceptionDao<MsgData, Integer> msgDao = null;
    static RuntimeExceptionDao<TalkListData, Integer> tlDao = null;
    static RuntimeExceptionDao<UserData, Integer> userDao;
    static RuntimeExceptionDao<NoteData, Integer> noteDao;
    static RuntimeExceptionDao<NoteComment, Integer> noteCommentDao;
    static RuntimeExceptionDao<NoteBookData, Integer> noteBookDao = null;
    static RuntimeExceptionDao<DraftData, Integer> draftDao;
    static RuntimeExceptionDao<SearchHistoryData, Integer> shDao;

    public static void initDb() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(TpApplication.getInstance().ctx, DatabaseHelper.class);
        }
        if (msgDao == null) {
            msgDao = databaseHelper.getMDao();
        }
        if (tlDao == null) {
            tlDao = databaseHelper.getTlDao();
        }
        if (userDao == null) {
            userDao = databaseHelper.getUserDao();
        }
        if (noteDao == null) {
            noteDao = databaseHelper.getNoteDao();
        }
        if (noteCommentDao == null) {
            noteCommentDao = databaseHelper.getNoteCommentDao();
        }
        if (noteBookDao == null) {
            noteBookDao = databaseHelper.getNoteBookDao();
        }
        if (draftDao == null) {
            draftDao = databaseHelper.getDraftDao();
        }
        if (shDao == null) {
            shDao = databaseHelper.getShDao();
        }
    }


    public static void delAllSearchHistory() {
        initDb();
        try {
            DeleteBuilder deleteBuilder = shDao.deleteBuilder();
            deleteBuilder.where().ge("id", 0);
            deleteBuilder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertSearchHistory(SearchHistoryData searchHistoryData) {
        initDb();
        try {
            if (searchHistoryData != null) {
                List<SearchHistoryData> list = shDao.queryForEq("key", searchHistoryData.getKey());
                if (!StrUtil.listNotNull(list)) {
                    shDao.create(searchHistoryData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<SearchHistoryData> getSearchHistory(Context ctx) {
        initDb();
        ArrayList<SearchHistoryData> datas = new ArrayList<>();
        try {
            QueryBuilder<SearchHistoryData, Integer> builder = shDao.queryBuilder();
            builder.orderBy("time", false);
            List<SearchHistoryData> list = shDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }


    public static int unReadMsg() {
        int count = 0;
        initDb();
        try {
            QueryBuilder<MsgData, Integer> builder = msgDao.queryBuilder();
            builder.where().eq("read", 1);
            List<MsgData> list = msgDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                count = list.size();
            }
        } catch (Exception e) {
        }
        return count;
    }

    public static void initMaskUser() {
        initDb();
        try {
//            QueryBuilder<UserData, Integer> builder = userDao.queryBuilder();
//            builder.orderBy("created", false);
            List<UserData> list = getUserListByType(EnumData.UserTypeEnum.MASK.getValue());
            TpApplication.getInstance().setMaskUser(null);
            if (StrUtil.listNotNull(list)) {
                ArrayList<Integer> userIds = new ArrayList<Integer>();
                for (UserData data : list) {
                    userIds.add(data.getId());
                }
                TpApplication.getInstance().setMaskUser(userIds);
            }
        } catch (Exception e) {

        }
    }


    public static ArrayList<TalkListData> getTalkLists() {
        ArrayList<TalkListData> datas = new ArrayList<TalkListData>();
        initDb();
        try {
            QueryBuilder<TalkListData, Integer> builder = tlDao.queryBuilder();
            builder.orderBy("created", false);
            List<TalkListData> list = tlDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
        }
        return datas;
    }

    public static Integer getMsgCountByFriend(int friend_id) {
        int count = 0;
        initDb();
        try {
            QueryBuilder<MsgData, Integer> builder = msgDao.queryBuilder();
            builder.where().eq("friend_id", friend_id).and().eq("read", 1);
            List<MsgData> list = msgDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                count = list.size();
            }
        } catch (Exception e) {
        }
        return count;
    }


    public static ArrayList<DraftData> initDraftData(int type) {
        ArrayList<DraftData> datas = new ArrayList<DraftData>();
        initDb();
        try {
            QueryBuilder<DraftData, Integer> builder = draftDao.queryBuilder();
            builder.where().eq("type", type);
            builder.orderBy("time", false);
            List<DraftData> list = draftDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
        }
        return datas;
    }

    public static ArrayList<NoteData> getNoteListByClass(int note_class) {
        return getNoteListByClass(note_class, 0, false);
    }

    public static ArrayList<NoteData> getNoteListByClass(int note_class, int notebook_id, boolean asc) {
        ArrayList<NoteData> datas = new ArrayList<NoteData>();
        initDb();
        try {
            QueryBuilder<NoteData, Integer> builder = noteDao.queryBuilder();
            if (notebook_id != 0) {
                builder.where().eq("note_class", note_class).and().eq("notebook_id", notebook_id);
            } else {
                builder.where().eq("note_class", note_class);
            }
            builder.orderBy("created", asc);
            List<NoteData> list = noteDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                for (NoteData tmp : list) {
                    MiniUserData user = JSON.parseObject(tmp.getDb_user(), MiniUserData.class);
                    tmp.setUser(user);
                    datas.add(tmp);
                }
            }
        } catch (Exception e) {
        }
        return datas;
    }

    public static ArrayList<NoteComment> getNoteCommentListByClass(int dairy_id) {
        ArrayList<NoteComment> datas = new ArrayList<NoteComment>();
        initDb();
        try {
            QueryBuilder<NoteComment, Integer> builder = noteCommentDao.queryBuilder();
            builder.where().eq("dairy_id", dairy_id);
            builder.orderBy("created", true);
            List<NoteComment> list = noteCommentDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                for (NoteComment tmp : list) {
                    MiniUserData user = JSON.parseObject(tmp.getDb_user(), MiniUserData.class);
                    MiniUserData recipient = JSON.parseObject(tmp.getDb_recipient(), MiniUserData.class);
                    tmp.setUser(user);
                    tmp.setRecipient(recipient);
                    datas.add(tmp);
                }
            }
        } catch (Exception e) {
        }
        return datas;
    }

    public static ArrayList<NoteBookData> getNoteBookListByClass(int user_id) {
        ArrayList<NoteBookData> datas = new ArrayList<NoteBookData>();
        initDb();
        try {
            QueryBuilder<NoteBookData, Integer> builder = noteBookDao.queryBuilder();
            builder.where().eq("user_id", user_id);
            builder.orderBy("created", false);
            List<NoteBookData> list = noteBookDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
        }
        return datas;
    }

    public static ArrayList<UserData> getUserListByType(int user_type) {
        ArrayList<UserData> datas = new ArrayList<UserData>();
        initDb();
        try {
            QueryBuilder<UserData, Integer> builder = userDao.queryBuilder();
            builder.where().eq("user_type", user_type);
            builder.orderBy("global_id", true);
            List<UserData> list = userDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
        }
        return datas;
    }

    public static ArrayList<NoteData> getNoteListByReadType(int readType) {
        ArrayList<NoteData> datas = new ArrayList<NoteData>();
        initDb();
        try {
            QueryBuilder<NoteData, Integer> builder = noteDao.queryBuilder();
            builder.where().eq("noteReadType", readType);
            builder.orderBy("time", false);
            List<NoteData> list = noteDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
        }
        return datas;
    }


    public static void saveDraftData(String content, int type) {
        initDb();
        try {
            List<DraftData> list = draftDao.queryForEq("content", content);
            if (!StrUtil.listNotNull(list)) {
                XUtil.tShort("已存为草稿");
                DraftData draftData = new DraftData(content, type);
                draftDao.create(draftData);
            }
        } catch (Exception e) {

        }
    }

    public static void deleteDraftData(int _id) {
        initDb();
        try {
            draftDao.deleteById(_id);
        } catch (Exception e) {
        }
    }

    public static void deleteNoteByReadType(int readType) {
        initDb();
        try {
            DeleteBuilder deleteBuilder = noteDao.deleteBuilder();
            deleteBuilder.where().eq("noteReadType", readType);
            deleteBuilder.delete();
        } catch (Exception e) {
        }
    }

    public static void deleteUserByType(int user_type) {
        initDb();
        try {
            DeleteBuilder deleteBuilder = userDao.deleteBuilder();
            deleteBuilder.where().eq("user_type", user_type);
            deleteBuilder.delete();
        } catch (Exception e) {
        }
    }

    public static void deleteAll() {
        try {
            deleteNoteByClass(0);
            deleteNoteBookByUserId(0);
            deleteNoteCommentByNoteId(0);
            delAllSearchHistory();
        } catch (Exception e) {
        }
    }

    public static void deleteNoteByClass(int note_class) {
        initDb();
        try {
            DeleteBuilder deleteBuilder = noteDao.deleteBuilder();
            if (note_class != 0) {
                deleteBuilder.where().eq("note_class", note_class);
            }
            deleteBuilder.delete();
        } catch (Exception e) {
        }
    }

    public static void deleteNoteBookByUserId(int user_id) {
        initDb();
        try {
            DeleteBuilder deleteBuilder = noteBookDao.deleteBuilder();
            if (user_id != 0) {
                deleteBuilder.where().eq("user_id", user_id);
            }
            deleteBuilder.delete();
        } catch (Exception e) {
        }
    }

    public static void deleteNoteCommentByNoteId(int dairy_id) {
        initDb();
        try {
            DeleteBuilder deleteBuilder = noteCommentDao.deleteBuilder();
            if (dairy_id != 0) {
                deleteBuilder.where().eq("dairy_id", dairy_id);
            }
            deleteBuilder.delete();
        } catch (Exception e) {
        }
    }

    public static void deleteTalkList(int _id) {
        initDb();
        try {
            tlDao.deleteById(_id);
        } catch (Exception e) {
        }
    }


    public static void maskUser(UserData userData) {
        initDb();
        try {
            if (!isUserSaved(userData.getId(), EnumData.UserTypeEnum.MASK.getValue())) {
                userData.setUser_type(EnumData.UserTypeEnum.MASK.getValue());
                userDao.create(userData);
                initMaskUser();
                XUtil.tShort("屏蔽成功!");
            }
        } catch (Exception e) {
        }
    }

    public static void delMaskUser(int user_id) {
        initDb();
        try {
            DeleteBuilder deleteBuilder = userDao.deleteBuilder();
            deleteBuilder.where().eq("id", user_id).and().eq("user_type", EnumData.UserTypeEnum.MASK.getValue());
            deleteBuilder.delete();
            XUtil.tShort("成功移除屏蔽!");
            initMaskUser();
        } catch (Exception e) {
        }
    }


    public static boolean isNoteSaved(int note_id, int note_class) {
        initDb();
        try {
            QueryBuilder<NoteData, Integer> builder = noteDao.queryBuilder();
            builder.where().eq("id", note_id).and().eq("note_class", note_class);
            List<NoteData> list = noteDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isUserSaved(int user_id, int user_type) {
        initDb();
        try {
            QueryBuilder<UserData, Integer> builder = userDao.queryBuilder();
            builder.where().eq("id", user_id).and().eq("user_type", user_type);
            List<UserData> list = userDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isNoteBookSaved(int id) {
        initDb();
        try {
            QueryBuilder<NoteBookData, Integer> builder = noteBookDao.queryBuilder();
            builder.where().eq("id", id);
            List<NoteBookData> list = noteBookDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isNoteCommentSaved(int id) {
        initDb();
        try {
            QueryBuilder<NoteComment, Integer> builder = noteCommentDao.queryBuilder();
            builder.where().eq("id", id);
            List<NoteComment> list = noteCommentDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static void addNoteList(List<NoteData> list, int note_class) {
        initDb();
        try {
            if (StrUtil.listNotNull(list)) {
                for (NoteData data : list) {
                    if (!isNoteSaved(data.getId(), note_class)) {
                        if (data.getUser() != null) {
                            data.setDb_user(data.getUser().toString());
                        }
                        data.setNote_class(note_class);
                        noteDao.create(data);
                    } else {
                        noteDao.update(data);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public static void addNoteCommentList(List<NoteComment> list) {
        initDb();
        try {
            if (StrUtil.listNotNull(list)) {
                for (NoteComment data : list) {
                    if (data == null) {
                        return;
                    }
                    if (!isNoteCommentSaved(data.getId())) {
                        if (data.getUser() != null) {
                            data.setDb_user(data.getUser().toString());
                        }
                        if (data.getRecipient() != null) {
                            data.setDb_recipient(data.getRecipient().toString());
                        }
                        noteCommentDao.create(data);
                    } else {
                        noteCommentDao.update(data);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUserList(List<UserData> list, int user_type) {
        initDb();
        try {
            if (StrUtil.listNotNull(list)) {
                for (UserData data : list) {
                    if (data == null) {
                        return;
                    }
                    if (!isUserSaved(data.getId(), user_type)) {
                        data.setUser_type(user_type);
                        userDao.create(data);
                    } else {
                        userDao.update(data);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNoteBookList(List<NoteBookData> list) {
        initDb();
        try {
            if (StrUtil.listNotNull(list)) {
                for (NoteBookData data : list) {
                    if (data == null) {
                        return;
                    }

                    if (!isNoteBookSaved(data.getId())) {
                        noteBookDao.create(data);
                    } else {
                        noteBookDao.update(data);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addTalkList(MiniUserData friend) {
        initDb();
        try {
            TalkListData talkListData = new TalkListData(friend, "");
            List<TalkListData> list = tlDao.queryForEq("user_id", friend.getId());
            if (!StrUtil.listNotNull(list)) {
                talkListData.setIs_send(1);
                tlDao.create(talkListData);
            }
        } catch (Exception e) {
        }
    }


//    public static void addNoteReadType(NoteData noteData) {
//        initDb();
//        try {
//            noteData.setTime(System.currentTimeMillis());
//            QueryBuilder<NoteData, Integer> builder = noteDao.queryBuilder();
//            builder.where().eq("id", noteData.getId()).and().eq("noteReadType", noteData.getNoteReadType());
//            List<NoteData> list = noteDao.query(builder.prepare());
//            if (StrUtil.listNotNull(list)) {
//                NoteData tmp = list.get(0);
//                noteDao.update(tmp);
//            } else {
//                noteDao.create(noteData);
//            }
//        } catch (Exception e) {
//        }
//    }


    public static NoteData getNoteById(int note_id) {
        NoteData noteData = null;
        initDb();
        try {
            noteData = noteDao.queryForId(note_id);
        } catch (Exception e) {
        }
        return noteData;
    }

    public static void initTalkList(MiniUserData friend) {
        initDb();
        try {
            TalkListData talkListData = new TalkListData(friend, "");
            List<TalkListData> list = tlDao.queryForEq("user_id", friend.getId());
            if (!StrUtil.listNotNull(list)) {
                talkListData.setIs_send(EnumData.MsgOwner.RECEIVER.getValue());
                tlDao.create(talkListData);
            }
        } catch (Exception e) {

        }
    }


    public static void saveTalkData(MsgData msgData) {
        initDb();
        try {
            msgDao.create(msgData);
        } catch (Exception e) {
        }
    }

    public static boolean canTalk() {
//        boolean flag = false;
//        initDb();
//        try {
//            GenericRawResults<String[]> talkCount = tlDao.queryRaw("SELECT count(*) from talklistdata WHERE  user_id <> 100178315 AND is_send = 1");
//            Integer count = Integer.parseInt(talkCount.getFirstResult()[0]);
//            if (count != null && count <= 4) {
//                return true;
//            }
//        } catch (Exception e) {
//            CheckedExceptionHandler.handleException(e);
//        }
//        return flag;
        return true;
    }


    public static void clearTable() {
        initDb();
        try {
            //聊天记录不必删除
//            msgDao.deleteBuilder().delete();
//            tlDao.deleteBuilder().delete();
            noteDao.deleteBuilder().delete();
        } catch (Exception e) {
        }
    }


}
