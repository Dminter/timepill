package com.zncm.timepill.modules.ft;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.SettingData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.adapter.SettingAdapter;
import com.zncm.timepill.modules.view.AboutTopView;
import com.zncm.timepill.utils.DeviceUtil;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.TimeUtils;
import com.zncm.timepill.utils.XUtil;

import java.util.ArrayList;
import java.util.List;


public class AboutFt extends BaseListFt {
    private SettingAdapter mAdapter;
    private Activity ctx;
    private List<SettingData> datas = null;
    private AboutTopView aboutTopView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (Activity) getActivity();
        lvBase.setCanLoadMore(false);
        swipeLayout.setEnabled(false);
        aboutTopView = new AboutTopView(ctx);
        lvBase.addHeaderView(aboutTopView);
        datas = new ArrayList<SettingData>();
        mAdapter = new SettingAdapter(ctx) {
            @Override
            public void setData(int position, SettingViewHolder holder) {
                final SettingData data = datas.get(position);
                if (data == null) {
                    return;
                }
                if (data.getTitle() != null && StrUtil.notEmptyOrNull(data.getTitle().toString())) {
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    holder.tvTitle.setText(data.getTitle());
                } else {
                    holder.tvTitle.setVisibility(View.GONE);
                }
                if (data.getStatus() != null && StrUtil.notEmptyOrNull(data.getStatus().toString())) {
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    holder.tvStatus.setText(data.getStatus());
                } else {
                    holder.tvStatus.setVisibility(View.GONE);
                }
            }
        };
        XUtil.listViewRandomAnimation(lvBase, mAdapter);
        lvBase.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int curPosition = position - lvBase.getHeaderViewsCount();
                if (curPosition >= 0) {
                    SettingData data = datas.get(curPosition);
                    if (data == null) {
                        return;
                    }
                    int ITEM = data.getId();
                    if (ITEM == EnumData.AboutEnum.VERSION.getValue()) {

                    } else if (ITEM == EnumData.AboutEnum.AUTHOR.getValue()) {
                        XUtil.viewUser(ctx, new UserData(TpConstants.AUTHOR_ID));

                    }
                }
            }
        });
        getData();
        return view;
    }


    private void getData() {
        datas = new ArrayList<SettingData>();
        datas.add(new SettingData(EnumData.AboutEnum.VERSION.getValue(), EnumData.AboutEnum.VERSION.getStrName(), DeviceUtil.getVersionName(ctx)));
        datas.add(new SettingData(EnumData.AboutEnum.AUTHOR.getValue(), EnumData.AboutEnum.AUTHOR.getStrName(), "@上善"));
        mAdapter.setItems(datas);
    }



    @Override
    public void onResume() {
        super.onResume();
        getData();
    }


    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onLoadMore() {
    }


}
