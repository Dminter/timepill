package com.zncm.timepill.modules.ft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.malinskiy.materialicons.Iconify;
import com.zncm.timepill.R;
import com.zncm.timepill.data.base.base.MiniUserData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.note.NoteBookData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.adapter.NoteBookAdapter;
import com.zncm.timepill.modules.services.NotebooksService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.ui.NoteBookAc;
import com.zncm.timepill.modules.ui.NoteInBookAc;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tr.xip.errorview.ErrorView;

public class NoteBooksFt extends BaseFt implements SwipeRefreshLayout.OnRefreshListener {
    protected GridView gvBase;
    protected Activity ctx;
    protected List<NoteBookData> datas = null;
    protected NoteBookAdapter mAdapter;
    private int curPosition = 0;
    private boolean isSelf = true;
    private MiniUserData userData;
    protected SwipeRefreshLayout swipeLayout;
    View view;
    ErrorView errorView;

    public void getData(final boolean bFlag) {
        if (userData != null && bFlag) {
            datas = DbUtils.getNoteBookListByClass(userData.getId());
            mAdapter.setItems(datas);

            if (StrUtil.listNotNull(datas)) {
                errorView.setVisibility(View.GONE);
                if (userData != null) {
                    DbUtils.deleteNoteBookByUserId(userData.getId());
                }
            }


        }
        if (isSelf) {
            ServiceFactory.getService(NotebooksService.class).getNotebooks(new Callback<List<NoteBookData>>() {
                @Override
                public void success(List<NoteBookData> noteBookDatas, Response response) {
                    loadComplete();
                    if (!StrUtil.listNotNull(noteBookDatas)) {
                        errorView.setVisibility(View.VISIBLE);
                        return;
                    }
                    DbUtils.addNoteBookList(noteBookDatas);
                    errorView.setVisibility(View.GONE);
                    List<NoteBookData> notExpired = new ArrayList<NoteBookData>();
                    for (NoteBookData noteBookData : noteBookDatas) {
                        if (noteBookData.getIsExpired().equals("false")) {
                            //逆序 后建的日记本在前面
                            notExpired.add(noteBookData);
                            TpApplication.getInstance().setNoteBookDatas(notExpired);
                            TpSp.setNoteBookData(notExpired.toString());
                        }
                    }
                    if (bFlag) {
                        datas = new ArrayList<NoteBookData>();
                    }
                    datas.addAll(noteBookDatas);
                    mAdapter.setItems(datas);
                }

                @Override
                public void failure(RetrofitError error) {
                    loadComplete();
                    XUtil.onFailure(error);
                }
            });
        } else {
            ServiceFactory.getService(NotebooksService.class).getNotebooksByUser(userData.getId(), new Callback<List<NoteBookData>>() {
                @Override
                public void success(List<NoteBookData> noteBookDatas, Response response) {
                    loadComplete();
                    if (!StrUtil.listNotNull(noteBookDatas)) {
                        return;
                    }
                    DbUtils.addNoteBookList(noteBookDatas);
                    if (bFlag) {
                        datas = new ArrayList<NoteBookData>();
                    }
                    datas.addAll(noteBookDatas);
                    mAdapter.setItems(datas);
                }

                @Override
                public void failure(RetrofitError error) {
                    loadComplete();
                    XUtil.onFailure(error);
                }
            });
        }


    }


    public void initData() {
        getData(true);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_grid_base, null);
        ctx = getActivity();
        gvBase = (GridView) view.findViewById(R.id.gvBase);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(TpSp.getThemeColor());


        errorView = (ErrorView) view.findViewById(R.id.error_view);
        ErrorView.Config errorViewConfig = ErrorView.Config.create()
                .image(XUtil.initIconTheme(Iconify.IconValue.md_error, 50))
                .title("没有日记本，新建一个吧！！")
                .retryVisible(true)
                .retryText("刷新")
                .build();
        errorView.setConfig(errorViewConfig);
        errorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                onRefresh();
            }
        });


        XUtil.initViewTheme(ctx, swipeLayout);
        datas = new ArrayList<NoteBookData>();
        mAdapter = new NoteBookAdapter(ctx) {
            @Override
            public void setData(final int position, NoteBoolViewHolder holder) {
                final NoteBookData data = (NoteBookData) datas.get(position);
                if (data == null) {
                    return;
                }
                if (data != null) {
                    if (StrUtil.notEmptyOrNull(data.getSubject())) {
                        holder.tvTitle.setVisibility(View.VISIBLE);
                        holder.tvTitle.setText(data.getSubject());
                    } else {
                        holder.tvTitle.setVisibility(View.GONE);
                    }
                    holder.ivIcon.setVisibility(View.VISIBLE);
                    if (StrUtil.notEmptyOrNull(data.getCoverUrl()) && !TpConstants.BOOK_COVER_DEFAULT.equals(data.getCoverUrl()) && !TpSp.getNoPic()) {
                        XUtil.getImageLoader().displayImage(data.getCoverUrl(), holder.ivIcon, XUtil.getOptions());
                    } else {
                        holder.ivIcon.setImageResource(R.drawable.default_notebook);
                    }
//                    if (StrUtil.notEmptyOrNull(data.getCoverUrl()) && !TpConstants.BOOK_COVER_DEFAULT.equals(data.getCoverUrl()) && !TpSp.getNoPic()) {
//                        XUtil.getImageLoader().displayImage(data.getCoverUrl(), holder.ivIcon, XUtil.getOptions());
//                    } else {
//                        holder.ivIcon.setImageDrawable(new ColorDrawable(mColorGenerator.getColor(data.getSubject())));
//                    }
                } else {
                    holder.ivIcon.setVisibility(View.GONE);
                    holder.tvTitle.setVisibility(View.GONE);
                }

                if (StrUtil.notEmptyOrNull(data.getIsExpired()) && data.getIsExpired().equals("true")) {
                    holder.ivExpired.setVisibility(View.VISIBLE);
                } else {
                    holder.ivExpired.setVisibility(View.GONE);
                }
                if (StrUtil.notEmptyOrNull(data.getIsPublic()) && data.getIsPublic().equals("true")) {
                    holder.ivPublic.setVisibility(View.GONE);
                } else {
                    holder.ivPublic.setVisibility(View.VISIBLE);
                }
                if (StrUtil.notEmptyOrNull(data.getCreated())) {
                    holder.tvCreated.setVisibility(View.VISIBLE);
                    holder.tvCreated.setText(data.getCreated() + "-" + data.getExpired());
                } else {
                    holder.tvCreated.setVisibility(View.GONE);
                }
            }
        };
        mAdapter.setItems(datas);
        gvBase.setAdapter(mAdapter);
        gvBase.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                curPosition = position;
                if (curPosition >= 0) {
                    NoteBookData noteBookData = datas.get(curPosition);
                    if (noteBookData == null || !isSelf) {
                        return false;
                    }
                    editBook(noteBookData);
                }
                return true;
            }
        });
        gvBase.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    Intent intent = new Intent(ctx, NoteInBookAc.class);
                    intent.putExtra(TpConstants.KEY_PARAM_DATA, datas.get(position));
                    startActivity(intent);
                }
            }
        });

        isSelf = ctx.getIntent().getBooleanExtra("isSelf", true);
        Serializable dataParam = ctx.getIntent().getSerializableExtra("user_info");
        userData = (MiniUserData) dataParam;

        if (userData == null) {
            UserData fullUser = TpApplication.getInstance().getUserData();
            userData = new MiniUserData(fullUser.getId(), fullUser.getName(), fullUser.getIconUrl());
        }
        initData();
        return view;
    }


    private void editBook(NoteBookData noteBookData) {
        Intent intent = new Intent(ctx, NoteBookAc.class);
        intent.putExtra("note_book", noteBookData);
        startActivity(intent);
    }


    public void loadComplete() {
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onRefresh() {

        getData(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isSelf) {
            menu.add("md_add").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_add)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }
        if (item.getTitle().equals("md_add")) {
            Intent intent = new Intent(ctx, NoteBookAc.class);
            startActivity(intent);
        }
        return false;
    }


}
