package com.zncm.timepill.modules.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.malinskiy.materialicons.Iconify;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.MiniUserData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.note.NoteComment;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.adapter.CommentAdapter;
import com.zncm.timepill.modules.services.NoteService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.view.CommentView;
import com.zncm.timepill.modules.view.LoadMoreListView;
import com.zncm.timepill.modules.view.NoteHeadView;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.RefreshEvent;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class NoteDetailsAc extends BaseAc implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    private boolean bOpShow = true;
    private Integer _id;
    private NoteData data;
    private SwipeRefreshLayout swipeLayout;
    private LoadMoreListView lvBase;
    private List<NoteComment> datas = null;
    private CommentAdapter mAdapter;
    private int curPosition;
    private ArrayList<Integer> maskUser;
    private Activity ctx;
    private NoteHeadView headView;
    private boolean onLoading = false;
    private boolean isSelf = false;
    LinearLayout llComment;
    CommentView commentView;
    NoteComment curComment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(TpSp.getThemeColor());
        lvBase = (LoadMoreListView) findViewById(R.id.listView);
        lvBase.setOnLoadMoreListener(this);
        lvBase.setCanLoadMore(false);
        headView = new NoteHeadView(this);
        lvBase.addHeaderView(headView);

//        tvComment = (TextView) findViewById(R.id.tvComment);
//        tvFace = (TextView) findViewById(R.id.tvFace);
        llComment = (LinearLayout) findViewById(R.id.llComment);
//        tvComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                commentDo();
//            }
//        });
//        tvFace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                faceView.showPopView(toolbar);
//            }
//        });
        commentView = new CommentView(this);
        llComment.addView(commentView);
        commentView.tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentView.comment(curComment, _id);
                curComment = null;
            }
        });


        String to = getIntent().getExtras().getString(TpConstants.KEY_STRING);
        if (StrUtil.notEmptyOrNull(to) && to.equals("details_noOp")) {
            bOpShow = false;
        }
        Serializable dataParam = getIntent().getSerializableExtra(TpConstants.KEY_PARAM_DATA);
        data = (NoteData) dataParam;
        _id = getIntent().getIntExtra(TpConstants.KEY_ID, 0);
        getSupportActionBar().setTitle("");
        if (data != null) {
            MiniUserData userData = data.getUser();
            if (userData != null) {
                if (StrUtil.notEmptyOrNull(userData.getName())) {
                    getSupportActionBar().setTitle(userData.getName());
                }
            }
            initButton(data);
            headView.initData(bOpShow, data);
        } else {
            getNoteDetails();
        }

        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            swipeLayout.setBackgroundColor(getResources().getColor(R.color.day_bg));

        } else {
            swipeLayout.setBackgroundColor(getResources().getColor(R.color.night_bg));
//            tvComment.setBackgroundColor(getResources().getColor(R.color.night_bg));
//            tvFace.setBackgroundColor(getResources().getColor(R.color.night_bg));
        }
        initData();
        EventBus.getDefault().register(this);
    }

    public void onEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.NOTE_REFRESH.getValue()) {
            getNoteDetails();
        } else if (type == EnumData.RefreshEnum.NOTE_COMMENT_REFRESH.getValue()) {
            getData();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getNoteDetails() {
        try {
            if (_id == null || _id == 0) {
                return;
            }

            ServiceFactory.getService(NoteService.class).getNote(_id, new Callback<NoteData>() {
                @Override
                public void success(NoteData noteData, Response response2) {
                    if (noteData == null) {
                        return;
                    }
                    data = noteData;
                    initButton(noteData);
                    headView.initData(bOpShow, data);
                }

                @Override
                public void failure(RetrofitError error) {
                    XUtil.onFailure(error);
                }
            });
        } catch (Exception e) {
        }
    }

    private void initButton(NoteData noteData) {
        try {

            if (!bOpShow) {
                llComment.setVisibility(View.GONE);
                return;
            }

            UserData userData = TpApplication.getInstance().getUserData();
            if (userData == null) {
                userData = JSON.parseObject(TpSp.getUserInfo(), UserData.class);
                TpApplication.getInstance().setUserData(userData);
            }
            if (userData != null) {
                if (userData.getId() == noteData.getUser_id()) {
                    isSelf = true;
                }
            }
        } catch (Exception e) {
        }
    }


    private void initData() {
        maskUser = TpApplication.getInstance().getMaskUser();
        mAdapter = new CommentAdapter(ctx) {
            @Override
            public void setData(int position, NoteViewHolder holder) {
                final NoteComment data = (NoteComment) datas.get(position);
                if (data == null) {
                    return;
                }
                MiniUserData userData = data.getUser();
                if (userData != null) {
                    if (StrUtil.notEmptyOrNull(userData.getName())) {
                        holder.tvAuthor.setVisibility(View.VISIBLE);
                        holder.tvAuthor.setText(userData.getName());
                    } else {
                        holder.tvAuthor.setVisibility(View.GONE);
                    }
                    if (StrUtil.notEmptyOrNull(userData.getIconUrl()) && !TpSp.getNoPic()) {
                        holder.ivIcon.setVisibility(View.VISIBLE);
                        XUtil.getImageLoader().displayImage(userData.getIconUrl(), holder.ivIcon, XUtil.getRoundedOptions());
                    } else {
                        holder.ivIcon.setVisibility(View.GONE);
                    }
                } else {
                    holder.tvAuthor.setVisibility(View.GONE);
                    holder.ivIcon.setVisibility(View.GONE);
                }
                holder.ivIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        XUtil.viewUser(ctx, new UserData(data.getUser()));
                    }
                });
                holder.tvAuthor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        XUtil.viewUser(ctx, new UserData(data.getUser()));
                    }
                });
                StringBuilder sbContent = new StringBuilder();
                MiniUserData recipient = data.getRecipient();
                if (recipient != null) {
                    sbContent.append("@").append(recipient.getName()).append(" ");
                }
                if (StrUtil.notEmptyOrNull(data.getContent())) {
                    sbContent.append(data.getContent());
                    holder.tvContent.setVisibility(View.VISIBLE);
                    holder.tvContent.setText(sbContent.toString());
                } else {
                    holder.tvContent.setVisibility(View.GONE);
                }
                if (StrUtil.notEmptyOrNull(data.getCreated())) {
                    holder.tvTime.setVisibility(View.VISIBLE);
                    holder.tvTime.setText(StrUtil.timeDay(data.getCreated()));
                } else {
                    holder.tvTime.setVisibility(View.GONE);
                }
            }
        };
        mAdapter.setItems(datas);
        XUtil.listViewRandomAnimation(lvBase, mAdapter);

        lvBase.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curPosition = position - lvBase.getHeaderViewsCount();
                if (curPosition < 0) {
                    return;
                }
                final NoteComment tmp = datas.get(curPosition);
                if (tmp == null) {
                    return;
                }
//                com.zncm.timepill.data.base.base.UserData userData = TpApplication.getInstance().getUserData();
//                if (userData != null && tmp.getUser_id() == userData.getId()) {
//                    XUtil.tShort("不能回复自己~");
//                    return;
//                }

                if (!bOpShow) {
//                    XUtil.tShort("过期日记,不能回复~");
                    return;
                }
//                Intent intent = new Intent(ctx, NoteCommentAc.class);
//                intent.putExtra(TpConstants.KEY_PARAM_DATA, tmp);
//                intent.putExtra(TpConstants.KEY_DATA, data);
//                startActivityForResult(intent, TpConstants.REQUESTCODE_ADD);
                curComment = tmp;
                XUtil.autoKeyBoardShow(commentView.etContent);
                commentView.etContent.setHint("@" + tmp.getUser().getName());
//                commentView.tvSend.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        commentView.comment(tmp, _id);
//                    }
//                });

            }
        });

        lvBase.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                curPosition = position - lvBase.getHeaderViewsCount();
                if (curPosition == -1) {
                    //headview
                    XUtil.copyDlg(ctx, data.getContent());
                }
                if (curPosition >= 0 && StrUtil.listNotNull(datas)) {

                    final com.zncm.timepill.data.base.note.NoteComment tmp = datas.get(curPosition);
                    if (tmp == null) {
                        return true;
                    }
                    opDialog(tmp);
                }
                return true;
            }
        });
        refresh();
    }


    public void getData() {
        datas = DbUtils.getNoteCommentListByClass(_id);
        mAdapter.setItems(datas);
        if (StrUtil.listNotNull(datas)) {
            DbUtils.deleteNoteCommentByNoteId(_id);
        }
        ServiceFactory.getService(NoteService.class).getNotesComments(_id, new Callback<List<NoteComment>>() {
            @Override
            public void success(List<NoteComment> comments, Response response) {
                loadComplete();
                if (!StrUtil.listNotNull(comments)) {
                    return;
                }
                List<NoteComment> list = comments;
                Collections.reverse(list);
                datas = new ArrayList<NoteComment>();
                if (StrUtil.listNotNull(list)) {
                    for (NoteComment data : list) {
                        if (maskUser != null && maskUser.contains(data.getUser_id())) {
                        } else {
                            datas.add(data);
                        }
                    }
                    DbUtils.addNoteCommentList(datas);
                }
                mAdapter.setItems(datas);

            }

            @Override
            public void failure(RetrofitError error) {
                XUtil.onFailure(error);
            }
        });
    }


    public void loadComplete() {
//        .dismiss();
        onLoading = false;
        swipeLayout.setRefreshing(false);
    }

    private void opDialog(final NoteComment commentData) {
        boolean isOwner = false;
        com.zncm.timepill.data.base.base.UserData userData = TpApplication.getInstance().getUserData();
        if (userData != null) {
            if (data.getUser_id() == userData.getId()) {
                isOwner = true;
            }
        }
        boolean isSelf = false;
        if (userData != null) {
            if (commentData.getUser_id() == userData.getId()) {
                isSelf = true;
            }
        }
        if (!bOpShow) {
            isSelf = false;
            isOwner = false;
        }
        new MaterialDialog.Builder(ctx)
                .items(isSelf || isOwner ? new String[]{"复制", "删除"} : new String[]{"复制"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                StrUtil.copyText(ctx, commentData.getContent());
                                break;
                            case 1:
                                new MaterialDialog.Builder(ctx)
                                        .content(commentData.getContent())
                                        .positiveText("删除")
                                        .negativeText("取消")
                                        .callback(new MaterialDialog.ButtonCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog materialDialog) {
                                                delReplyDo(commentData);
                                            }

                                            @Override
                                            public void onNeutral(MaterialDialog dialog) {
                                            }
                                        })
                                        .show();
                                break;
                        }
                    }
                })
                .show();
    }

    private void copyComment(NoteComment commentData) {
        StrUtil.copyText(this, commentData.getContent());
    }


    public void delReplyDo(final NoteComment commentData) {
        ServiceFactory.getService(NoteService.class).deleteComments(commentData.getId(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                XUtil.tShort("删除成功!");
                datas.remove(commentData);
                mAdapter.setItems(datas);
                loadComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                XUtil.onFailure(error);
            }
        });


    }

    private void refresh() {
        onLoading = true;
        swipeLayout.setRefreshing(true);
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    private void editNote(NoteData noteData) {
        Intent newIntent = new Intent(ctx, NoteNewAc.class);
        newIntent.putExtra("note", noteData);
        startActivity(newIntent);
    }

    public void delNote(int _id) {
        ServiceFactory.getService(NoteService.class).delete(_id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                XUtil.tShort("删除成功!");
                EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.NOTE_REFRESH.getValue()));
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                XUtil.onFailure(error);
            }
        });


    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_newdetails;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isSelf && bOpShow) {
            SubMenu sub = menu.addSubMenu("");
            sub.setIcon(XUtil.initIconWhite(Iconify.IconValue.md_more_vert));
            sub.add(0, 1, 0, "编辑");
            sub.add(0, 2, 0, "删除");
            sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }
        switch (item.getItemId()) {
            case 1:
                editNote(data);
                break;
            case 2:
                new MaterialDialog.Builder(ctx)
                        .content("删除日记!!!")
                        .positiveText("删除")
                        .negativeText("取消")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog materialDialog) {
                                delNote(data.getId());
                            }

                            @Override
                            public void onNeutral(MaterialDialog dialog) {
                            }
                        })
                        .show();
                break;
        }
//        if (item.getTitle().equals("md_more_vert")) {
//            new BottomSheet.Builder(ctx).sheet(isSelf && bOpShow ? R.menu.menu_edit : R.menu.menu_copy).listener(new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    switch (which) {
//                        case R.id.edit:
//                            editNote(data);
//                            break;
//                        case R.id.del:
//                            new MaterialDialog.Builder(ctx)
//                                    .content("删除日记!!!")
//                                    .positiveText("删除")
//                                    .negativeText("取消")
//                                    .callback(new MaterialDialog.ButtonCallback() {
//                                        @Override
//                                        public void onPositive(MaterialDialog materialDialog) {
//                                            delNote(data.getId());
//                                        }
//
//                                        @Override
//                                        public void onNeutral(MaterialDialog dialog) {
//                                        }
//                                    })
//                                    .show();
//                            break;
//                        case R.id.copy:
//                            StrUtil.copyText(ctx, data.getContent());
//                            break;
//
//                    }
//                }
//            }).show();
//        }
        return super.onOptionsItemSelected(item);
    }

    public void commentDo() {
        Intent intent = new Intent(this, NoteCommentAc.class);
        intent.putExtra(TpConstants.KEY_ID, _id);
        intent.putExtra(TpConstants.KEY_DATA, data);
        startActivityForResult(intent, TpConstants.REQUESTCODE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == TpConstants.REQUESTCODE_ADD) {
            refresh();
        }
    }

    @Override
    public void onRefresh() {
        if (onLoading) {
            return;
        }
        refresh();
    }

    @Override
    public void onLoadMore() {

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
