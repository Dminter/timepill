package com.zncm.timepill.modules.ft;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.relation.RelationPager;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.adapter.UserAdapter;
import com.zncm.timepill.modules.services.RelationService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AttentionMeF extends BaseListFt {
    protected Activity mActivity;
    protected List<UserData> datas = null;
    protected UserAdapter mAdapter;
    protected int pageIndex = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mActivity = (Activity) getActivity();
        if (!XUtil.bNetwork(mActivity)) {
            errorView.setTitle("没有网络");
        } else {
            errorView.setTitle("还没有人关注你呀~~");
        }
        datas = new ArrayList<UserData>();
        mAdapter = new UserAdapter(mActivity) {
            @Override
            public void setData(int position, UserViewHolder holder) {
                final UserData data = (UserData) datas.get(position);
                if (data == null) {
                    return;
                }
                holder.btnOperate.setVisibility(View.GONE);

                holder.ivIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        XUtil.viewUser(mActivity, data);
                    }
                });
                holder.tvAuthor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        XUtil.viewUser(mActivity, data);
                    }
                });
                if (data != null) {
                    if (StrUtil.notEmptyOrNull(data.getName())) {
                        holder.tvAuthor.setVisibility(View.VISIBLE);
                        holder.tvAuthor.setText(data.getName());
                    } else {
                        holder.tvAuthor.setVisibility(View.GONE);
                    }
                    if (StrUtil.notEmptyOrNull(data.getIconUrl()) && !TpSp.getNoPic()) {
                        holder.ivIcon.setVisibility(View.VISIBLE);
                        XUtil.getImageLoader().displayImage(data.getIconUrl(), holder.ivIcon, XUtil.getRoundedOptions());
                    } else {
                        holder.ivIcon.setVisibility(View.GONE);
                    }
                } else {
                    holder.ivIcon.setVisibility(View.GONE);
                    holder.tvAuthor.setVisibility(View.GONE);
                }
//                if (StrUtil.notEmptyOrNull(data.getIntro())) {
//                    holder.tvTitle.setVisibility(View.VISIBLE);
//                    holder.tvTitle.setText(data.getIntro().trim());
//                } else {
//                    holder.tvTitle.setVisibility(View.GONE);
//                }
            }
        };
        mAdapter.setItems(datas);
        lvBase.setAdapter(mAdapter);
        lvBase.setOnItemClickListener(new OnItemClickListener() {

            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - lvBase.getHeaderViewsCount();
                if (position >= 0) {
                    UserData data = datas.get(position);
                    XUtil.viewUser(mActivity, data);
                }
            }
        });
        initData();
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void getData(final boolean bFirst) {
//        if (bFirst) {
//            datas = DbUtils.getUserListByType(EnumData.UserTypeEnum.ATTENTION_ME.getValue());
//            mAdapter.setItems(datas);
//            if (StrUtil.listNotNull(datas)) {
//                errorView.setVisibility(View.GONE);
//                DbUtils.deleteUserByType(EnumData.UserTypeEnum.ATTENTION_ME.getValue());
//
//            }
//        }

        ServiceFactory.getService(RelationService.class).getAttentionMe(pageIndex, TpConstants.PAGE_SIZE, new Callback<RelationPager<UserData>>() {
            @Override
            public void success(RelationPager<UserData> relationPager, Response response) {
                loadComplete();
                if (relationPager == null) {
                    return;
                } else if (relationPager != null && !StrUtil.listNotNull(relationPager.users)) {
                    return;
                }
                List<UserData> list = relationPager.users;
//                DbUtils.addUserList(list, EnumData.UserTypeEnum.ATTENTION_ME.getValue());
                if (bFirst) {
                    datas = new ArrayList<UserData>();
                }
                if (StrUtil.listNotNull(list)) {
                    pageIndex++;
                    for (UserData data : list) {
                        datas.add(data);
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


    public void initData() {
        getData(true);
    }

    public void loadComplete() {
        super.loadComplete();
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
