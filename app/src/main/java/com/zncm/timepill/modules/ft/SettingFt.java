package com.zncm.timepill.modules.ft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.SettingData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.adapter.ColorAdapter;
import com.zncm.timepill.modules.adapter.SettingAdapter;
import com.zncm.timepill.modules.ui.AboutAc;
import com.zncm.timepill.modules.ui.CustomFaceAc;
import com.zncm.timepill.modules.ui.HomeTabActivity;
import com.zncm.timepill.modules.ui.LoginAc;
import com.zncm.timepill.modules.ui.PwdActivity;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.MyPath;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


public class SettingFt extends BaseListFt {
    private SettingAdapter mAdapter;
    private Activity ctx;
    private List<SettingData> datas = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (Activity) getActivity();
        lvBase.setPadding(0, XUtil.dip2px(10), 0, 0);
        lvBase.setCanLoadMore(false);
        swipeLayout.setEnabled(false);
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

            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int curPosition = position - lvBase.getHeaderViewsCount();
                if (curPosition >= 0) {
                    SettingData data = datas.get(curPosition);
                    if (data == null) {
                        return;
                    }
                    int ITEM = data.getId();
                    if (ITEM == EnumData.SettingEnum.PWD.getValue()) {
                        boolean flag = StrUtil.notEmptyOrNull(TpSp.getPwdInfo());
                        Intent intent = new Intent(ctx, PwdActivity.class);
                        intent.putExtra(TpConstants.KEY_PARAM_TYPE, flag ? EnumData.PwdEnum.SETTING_CHECK.getValue() : EnumData.PwdEnum.SET.getValue());
                        startActivity(intent);
                    } else if (ITEM == EnumData.SettingEnum.PIC_MODE.getValue()) {
                        if (TpSp.getNoPic()) {
                            TpSp.setNoPic(false);
                        } else {
                            TpSp.setNoPic(true);
                        }
                        getData();
                    } else if (ITEM == EnumData.SettingEnum.NOTIFICATION_BAR.getValue()) {
                        if (TpSp.getTip()) {
                            TpSp.setTip(false);
                        } else {
                            TpSp.setTip(true);
                        }
                        getData();
                    } else if (ITEM == EnumData.SettingEnum.SOUND_VIBRATE.getValue()) {
                        if (TpSp.getTipRing()) {
                            TpSp.setTipRing(false);
                        } else {
                            TpSp.setTipRing(true);
                        }
                        getData();
                    } else if (ITEM == EnumData.SettingEnum.LIST_ANIMATION.getValue()) {
                        if (TpSp.getListAnimation()) {
                            TpSp.setListAnimation(false);
                        } else {
                            TpSp.setListAnimation(true);
                        }
                        System.exit(0);
                    } else if (ITEM == EnumData.SettingEnum.ROUND_HEAD.getValue()) {
                        if (TpSp.getRoundHead()) {
                            TpSp.setRoundHead(false);
                        } else {
                            TpSp.setRoundHead(true);
                        }
                        getData();
                    } else if (ITEM == EnumData.SettingEnum.THEME.getValue()) {
                        theme2();
                    } else if (ITEM == EnumData.SettingEnum.CLEAR_TASK.getValue()) {
                        ClearTask task = new ClearTask(ctx);
                        task.execute("");
                    } else if (ITEM == EnumData.SettingEnum.ABOUT.getValue()) {
                        Intent intent = new Intent(ctx, AboutAc.class);
                        startActivity(intent);
                    } else if (ITEM == EnumData.SettingEnum.EXIT.getValue()) {
                        exitDlg();
                    } else if (ITEM == EnumData.SettingEnum.CUSTOM_FACE.getValue()) {
                        Intent intent = new Intent(ctx, CustomFaceAc.class);
                        intent.putExtra("type", EnumData.DataTypeEnum.CUSTOM_FACE.getValue());
                        startActivity(intent);
                    } else if (ITEM == EnumData.SettingEnum.PIC_UPLOAD.getValue()) {

                        new MaterialDialog.Builder(ctx)
                                .items(new String[]{EnumData.PicUploadEnum.ATUO.getStrName(), EnumData.PicUploadEnum.SOURCE.getStrName(), EnumData.PicUploadEnum.COMPRESS.getStrName()})
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                        TpSp.setPicUpload(which + 1);
                                        getData();
                                    }
                                })
                                .show();
                    }
                }
            }

        });
        getData();
        return view;
    }

    public void exitDlg() {

        new MaterialDialog.Builder(ctx)
                .content("退出登录")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        changeAccount();
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                    }
                })
                .show();
    }

    public void changeAccount() {
        Intent intent;
        TpApplication.getInstance().setUserData(null);
        TpSp.setUserInfo(null);
        TpSp.setNoteBookData(null);
        intent = new Intent(ctx, LoginAc.class);
        //多帐号 退出后 依然收到
        JPushInterface.setAliasAndTags(ctx, null, null);
        startActivity(intent);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (HomeTabActivity.getInstance() != null) {
                    HomeTabActivity.getInstance().finish();
                }
                ctx.finish();
            }
        }, 200);
    }

    public void exitApp() {
        //退出不收消息
        JPushInterface.stopPush(ctx);
        ctx.finish();
    }

    private void getData() {
        datas = new ArrayList<SettingData>();
        datas.add(new SettingData(EnumData.SettingEnum.PWD.getValue(), "安全锁", StrUtil.notEmptyOrNull(TpSp.getPwdInfo()) ? "已设置" : "未设置"));
        datas.add(new SettingData(EnumData.SettingEnum.PIC_MODE.getValue(), "图片", TpSp.getNoPic() ? "不显示" : "显示"));
        datas.add(new SettingData(EnumData.SettingEnum.NOTIFICATION_BAR.getValue(), "通知栏提示", TpSp.getTip() ? "√" : "×"));
        datas.add(new SettingData(EnumData.SettingEnum.SOUND_VIBRATE.getValue(), "提醒（振动+铃声）", TpSp.getTipRing() ? "√" : "×"));
        datas.add(new SettingData(EnumData.SettingEnum.LIST_ANIMATION.getValue(), "列表动画", TpSp.getListAnimation() ? "√" : "×"));
        datas.add(new SettingData(EnumData.SettingEnum.ROUND_HEAD.getValue(), "圆形头像", TpSp.getRoundHead() ? "√" : "×"));
        datas.add(new SettingData(EnumData.SettingEnum.PIC_UPLOAD.getValue(), "图片上传", EnumData.PicUploadEnum.valueOf(TpSp.getPicUpload()).getStrName()));
        SpannableStringBuilder theme = new SpannableStringBuilder("████");
        theme.setSpan(new ForegroundColorSpan(TpSp.getThemeColor()), 0, theme.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        datas.add(new SettingData(EnumData.SettingEnum.THEME.getValue(), "主题配色", theme));
        String cacheSize = StrUtil.formatFileSize(StrUtil.getFolderSize(new File(MyPath.getPathCache())));
        datas.add(new SettingData(EnumData.SettingEnum.CLEAR_TASK.getValue(), "清除缓存", cacheSize));
//        datas.add(new SettingData(EnumData.SettingEnum.CUSTOM_FACE.getValue(), "自定义表情"));
        datas.add(new SettingData(EnumData.SettingEnum.ABOUT.getValue(), "关于"));
        datas.add(new SettingData(EnumData.SettingEnum.EXIT.getValue(), "退出登录"));
        mAdapter.setItems(datas);
    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
    }


    void theme2() {
        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 400));
        GridView gridView = new GridView(ctx);
        final ArrayList<String> colors = new ArrayList<>();
        colors.add("1699D9");
        colors.add("2889DC");
        colors.add("31A56B");
        colors.add("D9434E");
        colors.add("E46280");
        colors.add("B0639E");
        colors.add("6B51BF");
        colors.add("595C9F");
        colors.add("3074C0");
        colors.add("008199");
        colors.add("159B77");
        colors.add("4FAFAB");
        colors.add("7AC4C3");
        colors.add("9AB6D5");
        colors.add("7F4D4D");
        colors.add("EFBB59");
        colors.add("CEB9A9");
        colors.add("E6000B");
        colors.add("000001");
        colors.add("EB6301");
        ColorAdapter adapter = new ColorAdapter(getActivity()) {
            @Override
            public void setData(int position, MenuViewHolder holder) {
                String color = colors.get(position);
                holder.tvTitle.setText("        ");
                holder.tvTitle.setBackgroundColor(Color.parseColor("#" + color));
            }
        };
        gridView.setNumColumns(5);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String color = colors.get(position);
                TpSp.setThemeColor(Color.parseColor("#" + color));
                System.exit(0);
            }
        });
        adapter.setItems(colors);
        view.addView(gridView);
        new MaterialDialog.Builder(ctx)
                .customView(view, false)
                .autoDismiss(true)
                .show();
    }


    class ClearTask extends AsyncTask<String, Integer, String> {
        SuperActivityToast progressToast = new SuperActivityToast(ctx, SuperToast.Type.PROGRESS);
        Context mContext;

        public ClearTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressToast.setText("正在清理缓存数据...");
            progressToast.setIndeterminate(true);
            progressToast.setProgressIndeterminate(true);
            progressToast.show();
        }

        @Override
        protected String doInBackground(String... params) {
            XUtil.getImageLoader().clearDiskCache();
            DbUtils.deleteAll();
            XUtil.deleteFile(new File(MyPath.getPathPicture()));
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            progressToast.dismiss();
            getData();
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }


    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onLoadMore() {
    }


}
