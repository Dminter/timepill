package com.zncm.timepill.modules.ft;

import android.app.Activity;

import com.zncm.timepill.data.base.base.MiniUserData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.modules.services.NoteService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.utils.StrUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserNoteFt extends NoteBaseFt {
    boolean isSelf = true;
    MiniUserData userData;
    Activity ctx;
    public void initData() {
        errorView.setTitle("没有日记");
        ctx = getActivity();
        isSelf = ctx.getIntent().getBooleanExtra("isSelf", true);
        Serializable dataParam = ctx.getIntent().getSerializableExtra("user_info");
        userData = (MiniUserData) dataParam;


        if (userData == null) {
            UserData fullUser = TpApplication.getInstance().getUserData();
            userData = new MiniUserData(fullUser.getId(), fullUser.getName(), fullUser.getIconUrl());
        }


        lvBase.setCanLoadMore(false);
        getData(true);
    }


    @Override
    public void getData(final boolean bFirst) {
        super.getData(bFirst);
        ServiceFactory.getService(NoteService.class).getNotesByUser(userData.getId(), new Callback<List<NoteData>>() {
                    @Override
                    public void success(List<NoteData> noteDatas, Response response) {
                        loadComplete();
                        if (!StrUtil.listNotNull(noteDatas)) {
                            if (bFirst) {
                                datas.clear();
                                mAdapter.setItems(datas);
                            }
                            return;
                        }
                        List<NoteData> list = noteDatas;
                        if (bFirst) {
                            datas = new ArrayList<NoteData>();
                        }
                        if (StrUtil.listNotNull(list)) {
                            datas.addAll(list);
                        }
                        mAdapter.setItems(datas);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        onFailure(error);
                    }
                }

        );
    }

    @Override
    public void toTop() {
        swipeLayout.setRefreshing(true);
        getData(true);
        lvBase.setSelection(0);
    }


    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
        getData(true);
    }
}
