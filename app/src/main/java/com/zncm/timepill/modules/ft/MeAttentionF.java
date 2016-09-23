package com.zncm.timepill.modules.ft;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.afollestad.materialdialogs.MaterialDialog;
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

public class MeAttentionF extends BaseListFt {
    protected Activity mActivity;
    protected List<UserData> datas = null;
    protected UserAdapter mAdapter;
    protected int pageIndex = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivity = (Activity) getActivity();
        datas = new ArrayList<UserData>();
        mAdapter = new UserAdapter(mActivity) {
            @Override
            public void setData(final int position, UserViewHolder holder) {
                final UserData data = (UserData) datas.get(position);
                if (data == null) {
                    return;
                }
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

                holder.btnOperate.setText("取消关注");
                holder.btnOperate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attentionDlg(data.getName(), position, data.getId());
                    }
                });

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


//                if (StrUtil.notEmptyOrNull(data.getIntro())) {
//                    holder.tvTitle.setVisibility(View.VISIBLE);
//                    holder.tvTitle.setText(data.getIntro().trim());
//                } else {
//                    holder.tvTitle.setVisibility(View.GONE);
//                }
            }
        };
        mAdapter.setItems(datas);
        XUtil.listViewRandomAnimation(lvBase, mAdapter);
        lvBase.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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


    private void attentionDlg(final String name, final int curPosition, final int user_id) {
        new MaterialDialog.Builder(mActivity)
                .content("确定要取消关注" + name + "吗?")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        attentionUser(curPosition, user_id);
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                    }
                })
                .show();


    }

    public void attentionUser(final int curPosition, int user_id) {

        ServiceFactory.getService(RelationService.class).deleteRelation(user_id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                datas.remove(curPosition);
                mAdapter.setItems(datas);
                loadComplete();
                XUtil.tShort("取消关注成功~");
            }

            @Override
            public void failure(RetrofitError error) {
                onFailure(error);
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void getData(final boolean bFirst) {
//        if (bFirst) {
//            datas = DbUtils.getUserListByType(EnumData.UserTypeEnum.ME_ATTENTION.getValue());
//            mAdapter.setItems(datas);
//            if (StrUtil.listNotNull(datas)) {
//                errorView.setVisibility(View.GONE);
//                DbUtils.deleteUserByType(EnumData.UserTypeEnum.ME_ATTENTION.getValue());
//            }
//        }

        ServiceFactory.getService(RelationService.class).getMeAttention(pageIndex, TpConstants.PAGE_SIZE, new Callback<RelationPager<UserData>>() {
            @Override
            public void success(RelationPager<UserData> relationPager, Response response) {
                loadComplete();
                if (relationPager == null) {
                    return;
                } else if (relationPager != null && !StrUtil.listNotNull(relationPager.users)) {
                    return;
                }
                List<UserData> list = relationPager.users;
//                DbUtils.addUserList(list, EnumData.UserTypeEnum.ME_ATTENTION.getValue());
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
