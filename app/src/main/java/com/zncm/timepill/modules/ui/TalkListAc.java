//package com.zncm.timepill.modules.ui;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.j256.ormlite.dao.RuntimeExceptionDao;
//import com.j256.ormlite.stmt.QueryBuilder;
//import com.umeng.analytics.MobclickAgent;
//import com.zncm.timepill.R;
//import com.zncm.timepill.data.base.base.MiniUserData;
//import com.zncm.timepill.data.base.chat.MsgData;
//import com.zncm.timepill.data.base.chat.TalkListData;
//import com.zncm.timepill.modules.adapter.TalkListAdapter;
//import com.zncm.timepill.utils.DbUtils;
//import com.zncm.timepill.utils.NotifyHelper;
//import com.zncm.timepill.utils.StrUtil;
//import com.zncm.timepill.utils.TimeUtils;
//import com.zncm.timepill.utils.XUtil;
//import com.zncm.timepill.utils.sp.TpSp;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class TalkListAc extends BaseAc {
//    protected ListView lvBase;
//    protected Activity ctx;
//    protected List<TalkListData> datas = null;
//    protected TalkListAdapter mAdapter;
//    private RuntimeExceptionDao<TalkListData, Integer> talkListDao;
//    private RuntimeExceptionDao<MsgData, Integer> msgDao;
//    private boolean bFirst = false;
//    String empty = "1.类似微博私信 仅@上善版间有效 默认关闭 双方均开启才可互聊\n" +
//            "2.不支持Emoji表情 不支持图片发送\n" +
//            "3.真诚交流 禁止用于广告 约炮等 \n" +
//            "4.屏蔽某人后将收不到被屏蔽人的消息 \n";
//
//    public void getData() {
//        try {
//            QueryBuilder<TalkListData, Integer> builder = talkListDao.queryBuilder();
//            builder.orderBy("created", false);
//            if (bFirst) {
//                bFirst = false;
//                datas = new ArrayList<TalkListData>();
//            }
//            List<TalkListData> list = talkListDao.query(builder.prepare());
//            if (StrUtil.listNotNull(list)) {
//                datas.addAll(list);
//            }
//            mAdapter.setItems(datas);
//        } catch (Exception e) {
//        }
//    }
//
//
//    public void initData() {
//        getData();
//    }
//
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }
//
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        if (talkListDao == null) {
//            talkListDao = getHelper().getTlDao();
//        }
//        if (msgDao == null) {
//            msgDao = getHelper().getMDao();
//        }
//        ctx = this;
//        getSupportActionBar().setTitle("私信");
//        lvBase = (ListView) findViewById(R.id.lvBase);
//        TextView emptyText = (TextView) findViewById(R.id.emptyText);
//        emptyText.setText(empty);
//        lvBase.setEmptyView(emptyText);
//        datas = new ArrayList<TalkListData>();
//        mAdapter = new TalkListAdapter(ctx) {
//            @Override
//            public void setData(final int position, NoteViewHolder holder) {
//                final TalkListData data = (TalkListData) datas.get(position);
//                if (data == null) {
//                    return;
//                }
//                MiniUserData userData = new MiniUserData();
//                userData.setIconUrl(data.getIconUrl());
//                userData.setId(data.getUser_id());
//                userData.setName(data.getName());
//                if (userData != null) {
//                    if (StrUtil.notEmptyOrNull(userData.getName())) {
//                        holder.tvAuthor.setVisibility(View.VISIBLE);
//                        holder.tvAuthor.setText(userData.getName());
//                    } else {
//                        holder.tvAuthor.setVisibility(View.GONE);
//                    }
//                    if (StrUtil.notEmptyOrNull(userData.getIconUrl()) && !TpSp.getNoPic()) {
//                        holder.ivIcon.setVisibility(View.VISIBLE);
//                        XUtil.getImageLoader().displayImage(userData.getIconUrl(), holder.ivIcon, XUtil.getRoundedOptions());
//                    } else {
//                        holder.ivIcon.setVisibility(View.GONE);
//                    }
//                } else {
//                    holder.tvAuthor.setVisibility(View.GONE);
//                    holder.ivIcon.setVisibility(View.GONE);
//                }
//                try {
//                    QueryBuilder<MsgData, Integer> builder = msgDao.queryBuilder();
//                    builder.where().eq("friend_id", data.getUser_id()).and().eq("read", 1);
//                    List<MsgData> list = msgDao.query(builder.prepare());
//                    if (StrUtil.listNotNull(list)) {
//                        holder.tvCount.setVisibility(View.VISIBLE);
//                        holder.tvCount.setText(String.valueOf(list.size()));
//                    } else {
//                        holder.tvCount.setVisibility(View.GONE);
//                    }
//                } catch (Exception e) {
//                    holder.tvCount.setVisibility(View.GONE);
//                }
//                String content = "";
//                if (StrUtil.notEmptyOrNull(data.getCreated())) {
//                    holder.tvTime.setVisibility(View.VISIBLE);
//                    content = TimeUtils.getTimeHMS(new Date(Long.parseLong(data.getCreated())));
//                    if (data.getIs_send() == 1) {
//                        content += " ->";
//                    } else {
//                        content += " <-";
//                    }
//                    holder.tvTime.setText(content);
//                } else {
//                    holder.tvTime.setVisibility(View.GONE);
//                }
//
//
//            }
//        };
//        mAdapter.setItems(datas);
//        XUtil.listViewRandomAnimation(lvBase, mAdapter);
//        lvBase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                position = position - lvBase.getHeaderViewsCount();
//                final TalkListData data = (TalkListData) datas.get(position);
//                if (data == null) {
//                    return;
//                }
//                Intent newIntent = new Intent(ctx, TalkAc.class);
//                newIntent.putExtra("user", new MiniUserData(data.getUser_id(), data.getName(), data.getIconUrl()));
//                startActivity(newIntent);
//
//            }
//        });
//        lvBase.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                position = position - lvBase.getHeaderViewsCount();
//                final TalkListData data = (TalkListData) datas.get(position);
//                if (data == null) {
//                    return true;
//                }
//                final int finalPosition = position;
//
//                new MaterialDialog.Builder(ctx)
//                        .items(new String[]{"删除"})
//                        .itemsCallback(new MaterialDialog.ListCallback() {
//                            @Override
//                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
//                                switch (which) {
//                                    case 0:
//                                        DbUtils.deleteTalkList(data.getId());
//                                        datas.remove(finalPosition);
//                                        mAdapter.setItems(datas);
//                                        break;
//                                }
//                            }
//                        })
//                        .show();
//                return true;
//            }
//        });
//        XUtil.initViewTheme(ctx, lvBase);
//        initData();
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        bFirst = true;
//        initData();
//        MobclickAgent.onResume(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this);
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        boolean is_talk = TpSp.getIsTalk();
//        if (is_talk) {
//            menu.add("关闭").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        } else {
//            menu.add("打开").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        }
//        return true;
//    }
//
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        if (item == null || item.getTitle() == null) {
//            return false;
//        }
//
//        if (item.getTitle().equals("打开")) {
//            XUtil.tShort("成功开启私信~~");
//            TpSp.setIsTalk(true);
//        } else if (item.getTitle().equals("关闭")) {
//            XUtil.tShort("关闭将不会收到私信消息~~");
//            TpSp.setIsTalk(false);
//        }
//        invalidateOptionsMenu();
//        return false;
//    }
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.view_list_base;
//    }
//
//
//}
