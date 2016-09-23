package com.zncm.timepill.modules.ft;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.materialicons.Iconify;
import com.zncm.timepill.R;
import com.zncm.timepill.modules.view.LoadMoreListView;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import retrofit.RetrofitError;
import tr.xip.errorview.ErrorView;


public abstract class BaseListFt extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener {
    protected SwipeRefreshLayout swipeLayout;
    protected LoadMoreListView lvBase;
    protected View view;
    protected LayoutInflater mInflater;
    protected ErrorView errorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        view = inflater.inflate(R.layout.fragment_base, null);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(TpSp.getThemeColor());
        lvBase = (LoadMoreListView) view.findViewById(R.id.listView);
        lvBase.setOnLoadMoreListener(this);
        errorView = (ErrorView) view.findViewById(R.id.error_view);
        ErrorView.Config errorViewConfig = ErrorView.Config.create()
                .image(XUtil.initIconTheme(Iconify.IconValue.md_error, 50))
                .title("无数据")
                .retryVisible(true)
                .retryText("刷新")
                .build();
        errorView.setConfig(errorViewConfig);
        lvBase.setEmptyView(errorView);
        errorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                retry();
            }
        });
        XUtil.initViewTheme(getActivity(), swipeLayout);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void loadComplete() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    getActivity().invalidateOptionsMenu();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                swipeLayout.setRefreshing(false);
                lvBase.onLoadMoreComplete();
            }
        });
    }


    public void onFailure(RetrofitError error) {
        try {
            loadComplete();
            if (StrUtil.notEmptyOrNull(error.getMessage())) {
                XUtil.tShort(error.getMessage());
            }
        } catch (Exception e) {

        }
    }

    public void retry() {
        onRefresh();
    }

}
