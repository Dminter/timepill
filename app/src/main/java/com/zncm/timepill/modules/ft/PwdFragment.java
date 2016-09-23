package com.zncm.timepill.modules.ft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.ui.HomeTabActivity;
import com.zncm.timepill.modules.ui.NoteDetailsAc;
import com.zncm.timepill.modules.ui.SplashAc;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;


public class PwdFragment extends Fragment {
    private Activity ctx;
    private View view;
    private TextView tvTitle;
    private TextView tvError;
    private EditText et1, et2, et3, et4;
    private int type;
    private int id;
    private String tmpPwd1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_pwd, null);
        ctx = getActivity();
        type = ctx.getIntent().getExtras().getInt(TpConstants.KEY_PARAM_TYPE);
        id = ctx.getIntent().getExtras().getInt(TpConstants.KEY_ID, 0);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvError = (TextView) view.findViewById(R.id.tvError);
        et1 = (EditText) view.findViewById(R.id.et1);
        et2 = (EditText) view.findViewById(R.id.et2);
        et3 = (EditText) view.findViewById(R.id.et3);
        et4 = (EditText) view.findViewById(R.id.et4);
        tvTitle.setText("输入密码");
        XUtil.autoKeyBoardShow(et1);
        et1.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int inputCount = temp.length();
                if (inputCount > 0) {
                    et2.requestFocus();
                } else {
                    et1.requestFocus();
                }
            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int inputCount = temp.length();
                if (inputCount > 0) {
                    et3.requestFocus();
                } else {
                    et2.requestFocus();
                }
            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int inputCount = temp.length();
                if (inputCount > 0) {
                    et4.requestFocus();
                } else {
                    et3.requestFocus();
                }
            }
        });
        et4.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int inputCount = temp.length();
                if (inputCount > 0) {
                    //xx
                    String p1, p2, p3, p4;
                    p1 = et1.getText().toString().trim();
                    p2 = et2.getText().toString().trim();
                    p3 = et3.getText().toString().trim();
                    p4 = et4.getText().toString().trim();
                    String tmpPwd = p1 + p2 + p3 + p4;
                    if (!StrUtil.notEmptyOrNull(tmpPwd)) {
                        return;
                    }
                    if (type == EnumData.PwdEnum.SET.getValue()) {
                        if (StrUtil.notEmptyOrNull(tmpPwd1)) {
                            if (StrUtil.notEmptyOrNull(tmpPwd) && tmpPwd.equals(tmpPwd1)) {
                                TpSp.setPwdInfo(tmpPwd);
                                ctx.finish();
                            } else {
                                initEt();
                                tmpPwd1 = "";
                                tvTitle.setText("输入密码");
                                XUtil.tShort("两次密码不一致！！");
                            }
                        } else {
                            if (StrUtil.notEmptyOrNull(tmpPwd)) {
                                tmpPwd1 = tmpPwd;
                                tvTitle.setText("确认密码");
                                initEt();
                            }
                        }
                    } else {
                        String pwd = TpSp.getPwdInfo();
                        if (StrUtil.notEmptyOrNull(pwd) && pwd.equals(tmpPwd)) {
                            if (type == EnumData.PwdEnum.SETTING_CHECK.getValue()) {
                                TpSp.setPwdInfo("");
                                XUtil.tShort("密码取消成功！");
                            } else if (type == EnumData.PwdEnum.CHECK.getValue()) {
                                Intent intent = null;
                                if (id != 0) {
                                    intent = new Intent(ctx, NoteDetailsAc.class);
                                    intent.putExtra(TpConstants.KEY_STRING, "commentList");
                                    intent.putExtra(TpConstants.KEY_ID, id);
                                    startActivity(intent);
                                } else {
                                    UserData data = TpApplication.getInstance().getUserData();
                                    if (data != null) {
                                        intent = new Intent(ctx, HomeTabActivity.class);
                                        startActivity(intent);
                                    } else {
                                        intent = new Intent(ctx, SplashAc.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                            ctx.finish();
                        } else {
                            tvError.setText("密码错误！！");
                            initEt();
                        }
                    }
                }
            }
        });
        XUtil.initViewTheme(ctx, view.findViewById(R.id.llBg));
        return view;
    }

    private void initEt() {
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et1.requestFocus();
    }


}

