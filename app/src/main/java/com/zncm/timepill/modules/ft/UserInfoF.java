package com.zncm.timepill.modules.ft;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.malinskiy.materialicons.Iconify;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.MiniUserData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.services.RelationService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.services.UserService;
import com.zncm.timepill.modules.ui.HomeTabActivity;
import com.zncm.timepill.modules.ui.PhotoShowAc;
import com.zncm.timepill.modules.ui.SettingAc;
import com.zncm.timepill.modules.ui.TalkAc;
import com.zncm.timepill.modules.ui.UserSettingAc;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.RefreshEvent;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.io.Serializable;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tr.xip.errorview.ErrorView;

public class UserInfoF extends Fragment {
    private boolean isSelf = true;
    private boolean bAttention = false;
    protected View view;
    private Activity ctx;
    ErrorView errorView;
    //    private RelativeLayout rlBg;
    private TextView tvOp;
    private TextView tvName;
    private TextView tvCreated;
    private ImageView ivIcon;
    private TextView tvIntro;
    UserData userData;
    MiniUserData miniUserData;

    private void assignViews() {
//        rlBg = (RelativeLayout) view.findViewById(R.id.rlBg);
        tvOp = (TextView) view.findViewById(R.id.tvOp);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvCreated = (TextView) view.findViewById(R.id.tvCreated);
        ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        tvIntro = (TextView) view.findViewById(R.id.tvIntro);

        tvIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = tvIntro.getText().toString().trim();
                XUtil.copyDlg(ctx, str);
            }
        });


        tvName.setTextColor(TpSp.getThemeColor());


        errorView = (ErrorView) view.findViewById(R.id.error_view);
        ErrorView.Config errorViewConfig = ErrorView.Config.create()
                .image(XUtil.initIconTheme(Iconify.IconValue.md_error, 50))
                .title("无数据")
                .retryVisible(true)
                .retryText("刷新")
                .build();
        errorView.setConfig(errorViewConfig);
        errorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                if (userData != null) {
                    getUserInfo();
                }
            }
        });
    }

    public void changeMode() {
        int themeType = TpSp.getThemeType();
        if (themeType == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            TpSp.setThemeType(EnumData.ThemeTypeEnum.BLACK.getValue());
        } else if (themeType == EnumData.ThemeTypeEnum.BLACK.getValue()) {
            TpSp.setThemeType(EnumData.ThemeTypeEnum.WHITE.getValue());
        }
        startActivity(new Intent(ctx, HomeTabActivity.class));
        ctx.finish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = getActivity();
        view = inflater.inflate(R.layout.user_index, null);
        assignViews();
        initData();
        return view;
    }

    public void initData() {

        isSelf = ctx.getIntent().getBooleanExtra("isSelf", true);
        Serializable dataParam = ctx.getIntent().getSerializableExtra("user_info");
        miniUserData = (MiniUserData) dataParam;
        if (miniUserData == null) {
            userData = TpApplication.getInstance().getUserData();
        } else {
            userData = new UserData(miniUserData);
        }
        getUserInfo();
    }


    private void attentionDlg(final String name, final boolean flag, final int user_id) {
        new MaterialDialog.Builder(getActivity())
                .content("确定要取消关注" + name + "吗?")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        attentionUser(flag, user_id);
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                    }
                })
                .show();


    }


    public void attentionUser(boolean flag, int user_id) {

        try {
            if (flag) {
                ServiceFactory.getService(RelationService.class).deleteRelation(user_id, new Callback<Response>() {
                    @Override
                    public void success(Response userData, Response response2) {
                        EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.PEOPLE_ATTENTION.getValue()));
                        XUtil.tShort("取消关注成功~");
                        bAttention = false;
                        if (tvOp != null) {
                            tvOp.setText("+关注");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        XUtil.onFailure(error);
                    }
                });

            } else {
                ServiceFactory.getService(RelationService.class).addRelation(user_id, new Callback<UserData>() {
                    @Override
                    public void success(UserData userData, Response response) {
                        EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.PEOPLE_ATTENTION.getValue()));
                        XUtil.tShort("关注成功~");
                        bAttention = true;
                        if (tvOp != null) {
                            tvOp.setText("取消关注");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        XUtil.onFailure(error);
                    }
                });
            }
        } catch (Exception e) {
        }
    }

    public void attentionUser(int user_id) {
        bAttention = false;


        ServiceFactory.getService(RelationService.class).getRelation(user_id, new Callback<UserData>() {
            @Override
            public void success(UserData userData, Response response) {

                if (userData == null && tvOp != null) {
                    bAttention = false;
                    tvOp.setText("+关注");
                } else {
                    bAttention = true;
                    tvOp.setText("已关注");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                XUtil.onFailure(error);
            }
        });
    }

    public void getUserInfo() {
        initUserInfo(userData);

        ServiceFactory.getService(UserService.class).getUserInfo(userData.getId(), new Callback<UserData>() {
            @Override
            public void success(final UserData userData, Response response) {
                if (userData == null) {
                    errorView.setVisibility(View.VISIBLE);
                    return;
                }
                initUserInfo(userData);
            }

            @Override
            public void failure(RetrofitError error) {
                XUtil.onFailure(error);
            }
        });

    }

    private void initUserInfo(final UserData userData) {
        if (userData != null) {
            errorView.setVisibility(View.GONE);
            if (StrUtil.notEmptyOrNull(userData.getName())) {
                tvName.setVisibility(View.VISIBLE);
                tvName.setText(userData.getName());

            } else {
                tvName.setVisibility(View.GONE);
            }
            if (StrUtil.notEmptyOrNull(userData.getCoverUrl()) && !TpSp.getNoPic()) {
                XUtil.getImageLoader().displayImage(userData.getCoverUrl(), ivIcon, XUtil.getRoundedOptions());
                ivIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PhotoShowAc.class);
                        intent.putExtra(TpConstants.KEY_STRING, userData.getCoverUrl());
                        startActivity(intent);
                    }
                });
            } else {
                ivIcon.setImageResource(R.drawable.head_icon);
            }

            if (StrUtil.notEmptyOrNull(userData.getCreated())) {
                tvCreated.setVisibility(View.VISIBLE);
                tvCreated.setText(StrUtil.timeYear(userData.getCreated()));
            } else {
                tvCreated.setVisibility(View.GONE);
            }

            if (StrUtil.notEmptyOrNull(userData.getIntro())) {
                tvIntro.setVisibility(View.VISIBLE);
                tvIntro.setText(userData.getIntro());
            } else {
                tvIntro.setVisibility(View.GONE);
            }

            tvOp.setVisibility(View.VISIBLE);
            if (!isSelf) {
                attentionUser(userData.getId());
            } else {
                tvOp.setText("编辑个人资料");
            }


            if (isSelf) {
                tvOp.setVisibility(View.GONE);
            } else {
                tvOp.setVisibility(View.VISIBLE);
            }

            tvOp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bAttention) {
                        attentionDlg(userData.getName(), bAttention, userData.getId());
                    } else {
                        attentionUser(bAttention, userData.getId());
                    }
                }
            });
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        SubMenu sub = menu.addSubMenu("");
        sub.setIcon(XUtil.initIconWhite(Iconify.IconValue.md_more_vert));
        if (isSelf) {
            sub.add(0, 1, 0, "夜晚/白天模式");
            sub.add(0, 2, 0, "编辑个人资料");
            sub.add(0, 3, 0, "设置");
            sub.add(0, 7, 0, "@上善【提意见】");
            sub.add(0, 4, 0, "退回旧版【需先卸载新版】");
        } else {
            sub.add(0, 5, 0, "私信");
            sub.add(0, 6, 0, "屏蔽");
        }
        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }


        switch (item.getItemId()) {
            case 1:
                changeMode();
                break;
            case 2:
                Intent intent = new Intent(ctx, UserSettingAc.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(ctx, SettingAc.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri url = Uri.parse(TpConstants.OLD_VERSION);
                intent.setData(url);
                startActivity(intent);
                break;
            case 5:
                intent = new Intent(ctx, TalkAc.class);
                intent.putExtra("user", new MiniUserData(userData.getId(), userData.getName(), userData.getIconUrl()));
                startActivity(intent);
                break;
            case 6:
                ArrayList<Integer> maskUser = TpApplication.getInstance().getMaskUser();
                boolean bMask = false;
                if (maskUser != null && maskUser.contains(userData.getId())) {
                    bMask = true;
                } else {
                    bMask = false;
                }
                EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.NOTE_REFRESH.getValue()));
                EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.PEOPLE_ATTENTION.getValue()));
                if (!bMask) {
                    DbUtils.maskUser(userData);
                    ctx.finish();
                } else {
                    DbUtils.delMaskUser(userData.getId());
                    XUtil.tShort("已取消屏蔽~");
                }
                break;
            case 7:
                intent = new Intent(ctx, TalkAc.class);
                intent.putExtra("user", TpConstants.AUTHOR_USER);
                startActivity(intent);
                break;


        }

//        if (item.getTitle().equals("md_more_vert")) {
//            opMore();
//        }
        return false;
    }

}
