package com.zncm.timepill.modules.ft;

import android.view.View;

import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.data.base.note.Pager;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.services.NoteService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.StrUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NoteFollowFt extends NoteBaseFt {
    private Set<Integer> IDs;
    protected int pageIndex = 1;

    public void initData() {
        errorView.setTitle("你所关注的朋友今天还没写日记呢！");
        getData(true);
    }

    @Override
    public void getData(final boolean bFirst) {
        super.getData(bFirst);
//        if (bFirst) {
//            datas = DbUtils.getNoteListByClass(EnumData.NoteClassEnum.ATTENTION.getValue());
//            mAdapter.setItems(datas);
//
//            if (StrUtil.listNotNull(datas)) {
//                errorView.setVisibility(View.GONE);
//                DbUtils.deleteNoteByClass(EnumData.NoteClassEnum.ATTENTION.getValue());
//            }
//
//
//        }
        ServiceFactory.getService(NoteService.class).getNotesFollow(pageIndex, TpConstants.PAGE_SIZE, new Callback<Pager<NoteData>>() {
            @Override
            public void success(Pager<NoteData> pagers, Response response) {
                loadComplete();
                if (pagers == null) {
                    return;
                } else if (pagers != null && !StrUtil.listNotNull(pagers.diaries)) {
                    return;
                }
                List<NoteData> list = pagers.diaries;

                if (bFirst) {
//                    DbUtils.addNoteList(list, EnumData.NoteClassEnum.ATTENTION.getValue());
                    IDs = new HashSet<Integer>();
                    datas = new ArrayList<NoteData>();
                }
                if (StrUtil.listNotNull(list)) {
                    pageIndex++;
                    for (NoteData data : list) {
                        if (IDs.add(data.getId())) {
                            if (maskUser != null && maskUser.contains(data.getUser_id())) {
                            } else {
                                datas.add(data);
                            }
                        }
                    }
                }
                mAdapter.setItems(datas);

            }

            @Override
            public void failure(RetrofitError error) {
                onFailure(error);
            }
        });
    }


    @Override
    public void toTop() {
        swipeLayout.setRefreshing(true);
        pageIndex = 1;
        getData(true);
        lvBase.setSelection(0);
    }


    @Override
    public void onLoadMore() {
        getData(false);
    }

    @Override
    public void onRefresh() {

        pageIndex = 1;
        getData(true);
    }
}
