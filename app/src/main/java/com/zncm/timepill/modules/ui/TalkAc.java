package com.zncm.timepill.modules.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.MiniUserData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.chat.MsgData;
import com.zncm.timepill.data.base.chat.TalkListData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.adapter.MsgAdapter;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.services.TalkService;
import com.zncm.timepill.modules.view.CommentView;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.NetworkUtil;
import com.zncm.timepill.utils.RefreshEvent;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.TimeUtils;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TalkAc extends BaseAc implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    private ListView lvBase;
    private Activity ctx;
    private List<MsgData> datas = null;
    private MsgAdapter mAdapter;
    private RuntimeExceptionDao<MsgData, Integer> msgDao;
    private RuntimeExceptionDao<TalkListData, Integer> talkListDao;
    //    private EditText etInput;
//    private TextView ivSend;
    private int user_id;
    private MiniUserData friend;
    private MiniUserData me;
    //    private InputMethodManager imm;
    private MessageReceiver msgReceiver;


    LinearLayout llComment;
    CommentView commentView;

    // 收到消息
    private void registerMsgReceiver() {
        msgReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(TpConstants.MESSAGE_RECEIVED_ACTION);
        registerReceiver(msgReceiver, filter);
    }  // 消息接收

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TpConstants.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                lvBase.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);// 自动滚动到底部
                String message = intent.getStringExtra(TpConstants.KEY_MESSAGE);
                MsgData msgData = JSON.parseObject(message, MsgData.class);
                if (msgData != null && msgData.getFriend_id() == friend.getId()) {
                    msgData.setIs_send(EnumData.MsgOwner.RECEIVER.getValue());
                    msgData.setRead(EnumData.MsgRead.READ.getValue());
                    msgData.setCreated(String.valueOf(System.currentTimeMillis()));
                    datas.add(msgData);
                    mAdapter.setItems(datas);
                }
            }
        }
    }

    public void getData() {
        try {
            QueryBuilder<MsgData, Integer> builder = msgDao.queryBuilder();
            builder.where().eq("friend_id", friend.getId());
            builder.orderBy("created", true);
            List<MsgData> list = msgDao.query(builder.prepare());
            if (StrUtil.listNotNull(list)) {
                datas.addAll(list);
                for (MsgData msgData : list) {
                    if (msgData.getRead() == 1) {
                        msgData.setRead(0);
                        msgDao.update(msgData);
                    }
                }
            }
            mAdapter.setItems(datas);
        } catch (Exception e) {
        }
    }

    public void sendMsg(String msg) {
        try {

            ServiceFactory.getService(TalkService.class).sendMsg(friend.getId(), msg, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {

                }

                @Override
                public void failure(RetrofitError error) {
                    XUtil.onFailure(error);
                }
            });
        } catch (Exception e) {
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        ctx = this;

        EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.NEW_MSG_TALK.getValue()));

        Serializable dataParam = getIntent().getSerializableExtra("user");
        friend = (MiniUserData) dataParam;


        if (talkListDao == null) {
            talkListDao = getHelper().getTlDao();
        }
        if (msgDao == null) {
            msgDao = getHelper().getMDao();
        }


        UserData userData = TpApplication.getInstance().getUserData();
        if (userData != null) {
            me = new MiniUserData(userData.getId(), userData.getName(), userData.getIconUrl());
        } else {
            userData = JSON.parseObject(TpSp.getUserInfo(), UserData.class);
            TpApplication.getInstance().setUserData(userData);
        }

        if (friend == null || userData == null) {
            finish();
            return;
        }
        DbUtils.addTalkList(friend);


        if (StrUtil.notEmptyOrNull(friend.getName())) {
            getSupportActionBar().setTitle(friend.getName());
        }
        lvBase = (ListView) findViewById(R.id.lvMsg);

        llComment = (LinearLayout) findViewById(R.id.llComment);
        commentView = new CommentView(this);
        llComment.addView(commentView);
        commentView.tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = commentView.etContent.getText().toString().trim();
                if (StrUtil.isEmptyOrNull(content)) {
                    XUtil.tShort("请输入内容!");
                    return;
                }
                if (friend == null) {
                    XUtil.tShort("出错了!");
                    return;
                }
                if (!NetworkUtil.detect(ctx)) {
                    XUtil.tShort("没有网络");
                    return;
                }
                MsgData msg = new MsgData(me, content, friend.getId());
                datas.add(msg);
                mAdapter.notifyDataSetChanged();
                msgDao.create(msg);
                msg.setFriend_id(me.getId());
                msg.setUser_id(friend.getId());
                sendMsg(msg.toString());
                commentView.etContent.setText("");
            }
        });

//        etInput = (EditText) findViewById(R.id.etInput);
//        imm.showSoftInput(etInput, 0);
//        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
//            etInput.setTextColor(getResources().getColor(R.color.black));
//            etInput.setHintTextColor(getResources().getColor(R.color.black));
//        } else {
//            etInput.setTextColor(getResources().getColor(R.color.white));
//            etInput.setHintTextColor(getResources().getColor(R.color.white));
//        }
//        ivSend = (TextView) findViewById(R.id.ivSend);
//        ivSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String content = etInput.getText().toString();
//                if (StrUtil.isEmptyOrNull(content)) {
//                    XUtil.tShort("请输入内容!");
//                    return;
//                }
//                if (friend == null) {
//                    XUtil.tShort("出错了!");
//                    return;
//                }
//
//                if (!NetworkUtil.detect(ctx)) {
//                    XUtil.tShort("没有网络");
//                    return;
//                }
//                MsgData msg = new MsgData(me, content, friend.getId());
//                datas.add(msg);
//                mAdapter.notifyDataSetChanged();
//                msgDao.create(msg);
//                msg.setFriend_id(me.getId());
//                msg.setUser_id(friend.getId());
//                sendMsg(msg.toString());
//                MobclickAgent.onEvent(ctx, "talk_send");
//                etInput.setText("");
//            }
//        });
        datas = new ArrayList<MsgData>();
        mAdapter = new MsgAdapter(ctx) {
            @Override
            public void setData(final int position, NoteViewHolder holder) {
                final MsgData data = (MsgData) datas.get(position);
                if (data == null) {
                    return;
                }
                if (StrUtil.notEmptyOrNull(data.getContent())) {
                    holder.tvText.setVisibility(View.VISIBLE);
                    holder.tvText.setText(data.getContent());
                } else {
                    holder.tvText.setVisibility(View.GONE);
                }
                holder.tvTime.setVisibility(View.GONE);
//                if (StrUtil.notEmptyOrNull(data.getCreated())) {
//                    holder.tvTime.setVisibility(View.VISIBLE);
//                    holder.tvTime.setText(TimeUtils.getTimeHMS(new Date(Long.parseLong(data.getCreated()))));
//                } else {
//                    holder.tvTime.setVisibility(View.GONE);
//                }
                if (StrUtil.notEmptyOrNull(data.getIconUrl())) {
                    holder.ivIcon.setVisibility(View.VISIBLE);
                    XUtil.getImageLoader().displayImage(data.getIconUrl(), holder.ivIcon, XUtil.getRoundedOptions());
                } else {
                    holder.ivIcon.setVisibility(View.GONE);
                }
                holder.ivIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent newIntent = new Intent(ctx, ZoneAc.class);
//                        newIntent.putExtra(TpConstants.KEY_ID, data.getFriend_id());
//                        newIntent.putExtra(TpConstants.KEY_STRING, new MiniUserData(data.getFriend_id(), data.getName(), data.getIconUrl()));
//                        startActivity(newIntent);

                        XUtil.viewUser(ctx, new UserData(new MiniUserData(data.getFriend_id(), data.getName(), data.getIconUrl())));
                    }
                });
//                holder.tvText.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        StrUtil.copyText(ctx, data.getContent());
//                    }
//                });
            }
        };
        mAdapter.setItems(datas);
        lvBase.setAdapter(mAdapter);
        lvBase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MsgData msgData = (MsgData) parent.getItemAtPosition(position);
                if (msgData != null && StrUtil.notEmptyOrNull(msgData.getContent())) {
                    XUtil.copyDlg(TalkAc.this, msgData.getContent());
                }

            }
        });
        XUtil.initViewTheme(ctx, lvBase);
//        XUtil.initViewTheme(ctx, lvBase);
//        XUtil.initViewTheme(ctx, findViewById(R.id.llBottom));
        getData();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.lv_msg;
    }


    @Override
    protected void onResume() {
        super.onResume();
        TpApplication.getInstance().setTalkBackground(false);
        registerMsgReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TpApplication.getInstance().setTalkBackground(true);
        if (msgReceiver != null) {
            unregisterReceiver(msgReceiver);
        }
    }


    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(commentView.etContent, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(commentView.etContent);
    }
}
