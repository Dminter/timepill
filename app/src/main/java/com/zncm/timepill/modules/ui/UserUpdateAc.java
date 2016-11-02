package com.zncm.timepill.modules.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.malinskiy.materialicons.Iconify;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.services.UserService;
import com.zncm.timepill.utils.NetworkUtil;
import com.zncm.timepill.utils.RefreshEvent;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.io.Serializable;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserUpdateAc extends BaseAc {
    private EditText etContent;
    private String key;
    private UserData userData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key = getIntent().getStringExtra(TpConstants.KEY_ID);
        if (StrUtil.notEmptyOrNull(key)) {
            getSupportActionBar().setTitle(key);
        }

        Serializable dataParam = getIntent().getSerializableExtra("user");
        userData = (UserData) dataParam;
        if (userData == null) {
            userData = JSON.parseObject(TpSp.getUserInfo(), UserData.class);
            TpApplication.getInstance().setUserData(userData);
        }
        etContent = (EditText) findViewById(R.id.etComment);
        if (userData != null) {
            if (key.equals("修改名字")) {
                if (StrUtil.notEmptyOrNull(userData.getName())) {
                    etContent.setText(userData.getName());
                    etContent.setSelection(userData.getName().length());
                }
            }
            if (key.equals("修改介绍")) {
                if (StrUtil.notEmptyOrNull(userData.getIntro())) {
                    etContent.setText(userData.getIntro());
                    etContent.setSelection(userData.getIntro().length());
                }

            }
        }
        etContent.setHint(key);
        XUtil.autoKeyBoardShow(etContent);


        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            findViewById(R.id.llBg).setBackgroundColor(getResources().getColor(R.color.white));
            etContent.setTextColor(getResources().getColor(R.color.black));
            etContent.setHintTextColor(getResources().getColor(R.color.black));
        } else {
            findViewById(R.id.llBg).setBackgroundColor(getResources().getColor(R.color.night_bg));
            etContent.setTextColor(getResources().getColor(R.color.white));
            etContent.setHintTextColor(getResources().getColor(R.color.white));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("comment");
        item.setIcon(XUtil.initIconWhite(Iconify.IconValue.md_save));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }
        if (item.getTitle().equals("comment")) {
            String content = etContent.getText().toString().trim();
            if (NetworkUtil.detect(UserUpdateAc.this)) {
                if (StrUtil.notEmptyOrNull(content)) {
                    if (StrUtil.notEmptyOrNull(content)) {
                        updateUserDo(content);
                    }
                } else {
                    XUtil.tShort("请输入修改内容~");
                }
            } else {
                XUtil.tShort("网络连接不可用!");
            }

        }
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.ac_note_comment;
    }

    public void updateUserDo(String content) {
        final String name, intro;
        if (key.equals("修改名字")) {
            name = content;
        } else {
            name = userData.getName();
        }
        if (key.equals("修改介绍")) {
            intro = content;
        } else {
            intro = userData.getIntro();
        }

        ServiceFactory.getService(UserService.class).updateUser(name, intro, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.USER_REFRESH.getValue()));
                XUtil.tShort("修改成功~");
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                XUtil.onFailure(error);
            }
        });
    }


}