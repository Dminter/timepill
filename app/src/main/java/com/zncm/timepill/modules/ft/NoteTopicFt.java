package com.zncm.timepill.modules.ft;

import android.content.Intent;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.data.base.note.NoteTopicData;
import com.zncm.timepill.data.base.note.Pager;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.modules.ui.PhotoShowAc;
import com.zncm.timepill.modules.services.NoteService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.TimeUtils;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NoteTopicFt extends NoteBaseFt {
    private NoteTopicData topicData;
    private int pageIndex = 1;
    private Set<Integer> IDs;
    private boolean bShowTopicConent = false;
    private boolean bTopic = false;
    private String topicInfo;

    public void initData() {
        errorView.setTitle("今天没有话题");
        boolean needTopic = false;
        topicInfo = TpSp.getNoteTopic();
        if (StrUtil.notEmptyOrNull(topicInfo)) {
            topicData = JSON.parseObject(topicInfo, NoteTopicData.class);
            if (topicData != null && StrUtil.notEmptyOrNull(topicData.getCreated())) {
                String date = StrUtil.timeDate(topicData.getCreated());
                String today = TimeUtils.getDateYDM();
                if (date.equals(today)) {
                    TpApplication.getInstance().setTopic(topicData);
                    topicView.showMe();
                    initTopicView();
                } else {
                    needTopic = true;
                }
            } else {
                needTopic = true;
            }
        } else {
            needTopic = true;
        }
        if (needTopic) {
            getTopic();
        }
        getData(true);
    }

    private void getTopic() {
        ServiceFactory.getService(NoteService.class).getTodayTopic(new Callback<NoteTopicData>() {
            @Override
            public void success(NoteTopicData topicData, Response response) {
                if (topicData != null) {
                    NoteTopicFt.this.topicData = topicData;
                    TpApplication.getInstance().setTopic(topicData);
                    TpSp.setNoteTopic(topicData.toString());
                    topicView.showMe();
                    initTopicView();
                } else {
                    topicView.hideMe();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                onFailure(error);
            }
        });


    }

    private void initTopicView() {
        if (topicData == null) {
            return;
        }
        bTopic = true;
        if (StrUtil.notEmptyOrNull(topicData.getTitle())) {
            topicView.getTvTitle().setVisibility(View.VISIBLE);
            topicView.getTvTitle().setText("话题: " + topicData.getTitle());
        } else {
            topicView.getTvTitle().setVisibility(View.GONE);
        }
        if (StrUtil.notEmptyOrNull(topicData.getIntro())) {
            topicView.getTvContent().setVisibility(View.VISIBLE);
            topicView.getTvContent().setText(StrUtil.replaceBlank(topicData.getIntro()));
        } else {
            topicView.getTvContent().setVisibility(View.GONE);
        }

        if (StrUtil.notEmptyOrNull(topicData.getImageUrl()) && !TpSp.getNoPic()) {
            topicView.getIvPhoto().setVisibility(View.VISIBLE);
            XUtil.getImageLoader().displayImage(topicData.getImageUrl(), topicView.getIvPhoto(), XUtil.getOptions());
            topicView.getIvPhoto().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, PhotoShowAc.class);
                    intent.putExtra(TpConstants.KEY_STRING, topicData.getImageUrl());
                    startActivity(intent);
                }
            });
        } else {
            topicView.getIvPhoto().setVisibility(View.GONE);
        }

        topicView.getLlContent().setVisibility(View.GONE);
        topicView.getTvOp().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bShowTopicConent) {
                    bShowTopicConent = false;
                    topicView.getTvOp().setText("展开");
                    topicView.getLlContent().setVisibility(View.GONE);
                } else {
                    bShowTopicConent = true;
                    topicView.getTvOp().setText("收起");
                    topicView.getLlContent().setVisibility(View.VISIBLE);
                }
            }
        });


    }


    @Override
    public void getData(final boolean bFirst) {
        super.getData(bFirst);
//        if (bFirst) {
//            datas = DbUtils.getNoteListByClass(EnumData.NoteClassEnum.TOPIC.getValue());
//            mAdapter.setItems(datas);
//            if (StrUtil.listNotNull(datas)) {
//                errorView.setVisibility(View.GONE);
//                DbUtils.deleteNoteByClass(EnumData.NoteClassEnum.TOPIC.getValue());
//            }
//        }
        ServiceFactory.getService(NoteService.class).getNotesTopic(pageIndex, TpConstants.PAGE_SIZE, new Callback<Pager<NoteData>>() {
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
//                    DbUtils.addNoteList(list, EnumData.NoteClassEnum.TOPIC.getValue());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            pageIndex = 1;
            getData(true);
        }
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
