package com.zncm.timepill.modules.ft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.tips.TipData;
import com.zncm.timepill.data.base.tips.TipUserData;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.adapter.TipsAdapter;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.services.TipService;
import com.zncm.timepill.modules.ui.NoteDetailsAc;
import com.zncm.timepill.utils.NotifyHelper;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TipsFt extends BaseListFt {
    private Activity ctx;
    protected List<TipData> datas = null;
    protected List<TipData> original_datas = null;
    protected TipsAdapter mAdapter;
    private Set<Integer> IDs;
    private HashMap<Integer, String> userInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (Activity) getActivity();
        lvBase.setCanLoadMore(false);
        datas = new ArrayList<TipData>();
        mAdapter = new TipsAdapter(ctx) {
            @Override
            public void setData(final int position, NoteViewHolder holder) {
                final TipData data = (TipData) datas.get(position);
                String content = "";
                if (data.content != null) {
                    if (data.getType() == 1) {
                        content = data.getReal_content() + " 回复了你";
                    } else if (data.getType() == 2) {//关注
                        TipData.TipContent userUserData = data.content;
                        if (userUserData != null) {
                            TipUserData userData = userUserData.user;
                            if (userData != null) {
                                content = userData.getName() + " 关注了你";
                            }
                        }

                    }
                    holder.tvContent.setVisibility(View.VISIBLE);
                    holder.tvContent.setText(content);
                } else {
                    holder.tvContent.setVisibility(View.GONE);
                }

                holder.tvDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        readByLinkIdDo(data);
                    }
                });

            }
        };

        mAdapter.setItems(datas);

        lvBase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TipData data = (TipData) parent.getItemAtPosition(position);
                if (data.getType() == 2) {
                    readDo(data);
                } else {
                    readByLinkIdDo(data);
                }
                if (data.getType() == 1) {
                    Intent intent = new Intent(ctx, NoteDetailsAc.class);
                    intent.putExtra(TpConstants.KEY_STRING, "commentList");
                    intent.putExtra(TpConstants.KEY_ID, data.getLink_id());
                    startActivity(intent);

                    NotifyHelper.clearNotificationById(data.getLink_id());

                } else if (data.getType() == 2) {//关注
                    XUtil.viewUser(ctx, new UserData(data.getLink_id()));
                }
            }
        });

        XUtil.listViewRandomAnimation(lvBase, mAdapter);
        getData(true);
        return view;
    }

    public void getData(final boolean bFirst) {
        if (!XUtil.bNetwork(ctx)) {
            errorView.setTitle("没有网络");
        } else {
            errorView.setTitle("没有提醒");
        }

        ServiceFactory.getService(TipService.class).getTips(new Callback<List<TipData>>() {
            @Override
            public void success(List<TipData> tipDatas, Response response2) {
                loadComplete();
                if (tipDatas == null) {
                    return;
                }
                List<TipData> list = tipDatas;
                if (bFirst) {
                    IDs = new HashSet<Integer>();
                    userInfo = new HashMap<Integer, String>();
                    original_datas = new ArrayList<TipData>();
                    datas = new ArrayList<TipData>();
                }
                if (StrUtil.listNotNull(list)) {
                    original_datas.addAll(list);
                    for (TipData data : list) {
                        if (data.getType() == 2) {
                            datas.add(data);
                        } else {
                            if (IDs.add(data.getLink_id())) {
                                datas.add(data);
                                TipData.TipContent commentData = data.content;
                                if (commentData != null) {
                                    TipUserData userData = commentData.author;
                                    if (userData != null) {
                                        userInfo.put(data.getLink_id(), userData.getName());
                                    }
                                }
                            } else {
                                String names = userInfo.get(data.getLink_id());
                                TipData.TipContent commentData = data.content;
                                if (commentData != null) {
                                    TipUserData userData = commentData.author;
                                    if (userData != null && !names.contains(userData.getName())) {
                                        userInfo.put(data.getLink_id(), names + "、" + userData.getName());
                                    }
                                }
                            }
                        }
                    }
                    for (TipData data : datas) {
                        if (data.getType() == 1) {
                            data.setReal_content(userInfo.get(data.getLink_id()));
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

    public void readByLinkIdDo(TipData tipData) {
        int link_id = tipData.getLink_id();
        for (TipData data : original_datas) {
            if (link_id == data.getLink_id()) {
                readDo(data);
            }
        }
    }

    public void readDo(final TipData tipData) {
        ServiceFactory.getService(TipService.class).readTips(tipData.getId(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                datas.remove(tipData);
                mAdapter.setItems(datas);
                loadComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                onFailure(error);
            }
        });

    }

    @Override
    public void onRefresh() {
        getData(true);
    }

    @Override
    public void onLoadMore() {
    }

}
