package com.zncm.timepill.modules.ft;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.afollestad.materialdialogs.MaterialDialog;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.modules.adapter.UserAdapter;
import com.zncm.timepill.utils.DatabaseHelper;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.ArrayList;
import java.util.List;

public class MeMaskF extends BaseListFt {
    protected Activity mActivity;
    protected List<UserData> datas = null;
    protected UserAdapter mAdapter;
    public DatabaseHelper databaseHelper;

    public DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(TpApplication.getInstance().ctx, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    RuntimeExceptionDao<UserData, Integer> userDao;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mActivity = (Activity) getActivity();

        errorView.setTitle("你是好人，没有屏蔽任何人~~！！");

        userDao = getHelper().getUserDao();
        datas = new ArrayList<UserData>();
        mAdapter = new UserAdapter(mActivity) {
            @Override
            public void setData(int position, UserViewHolder holder) {
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

                holder.btnOperate.setText("取消屏蔽");
                holder.btnOperate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maskUser(data);
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
//                if (StrUtil.notEmptyOrNull(data.getCreated())) {
//                    holder.tvTitle.setVisibility(View.VISIBLE);
//                    holder.tvTitle.setText(data.getCreated());
//                } else {
//                    holder.tvTitle.setVisibility(View.GONE);
//                }
            }
        };
        mAdapter.setItems(datas);
        XUtil.listViewRandomAnimation(lvBase, mAdapter);
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

        lvBase.setCanLoadMore(false);
        initData();
        return view;
    }

    public void maskUser(final UserData userData) {
        new MaterialDialog.Builder(mActivity)
                .content("确定要把" + userData.getName() + "移屏蔽名单吗?")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        DbUtils.delMaskUser(userData.getId());
                        getData(true);
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                    }
                })
                .show();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void getData(final boolean bFirst) {
        datas = DbUtils.getUserListByType(EnumData.UserTypeEnum.MASK.getValue());
        mAdapter.setItems(datas);
        if (StrUtil.listNotNull(datas)) {
            errorView.setVisibility(View.GONE);
        }
        loadComplete();
//        try {
//            loadComplete();
//            QueryBuilder<UserData, Integer> builder = userDao.queryBuilder();
//            builder.orderBy("created", false);
//            if (bFirst) {
//                datas = new ArrayList<UserData>();
//            }
//            List<UserData> list = userDao.query(builder.prepare());
//            if (StrUtil.listNotNull(list)) {
//                datas.addAll(list);
//            }
//            mAdapter.setItems(datas);
//
//        } catch (Exception e) {
//            loadComplete();
//        }

    }


    public void initData() {
        getData(true);
    }

    public void loadComplete() {
        super.loadComplete();
    }


    @Override
    public void onLoadMore() {
    }

    @Override
    public void onRefresh() {
        getData(true);
    }

}
