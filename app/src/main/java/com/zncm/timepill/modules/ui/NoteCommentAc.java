package com.zncm.timepill.modules.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.malinskiy.materialicons.Iconify;
import com.umeng.analytics.MobclickAgent;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.MiniUserData;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.adapter.ColorAdapter;
import com.zncm.timepill.modules.services.NoteService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.view.FaceView;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.NetworkUtil;
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

public class NoteCommentAc extends BaseAc {
    private EditText etComment;
    private com.zncm.timepill.data.base.note.NoteComment data;
    private int _id;
    FaceView faceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackOn(false);
        Serializable dataParam = getIntent().getSerializableExtra(TpConstants.KEY_PARAM_DATA);
        data = (com.zncm.timepill.data.base.note.NoteComment) dataParam;
        getSupportActionBar().setTitle("回复");
        if (data != null) {
            MiniUserData userData = data.getUser();
            if (userData != null) {
                if (StrUtil.notEmptyOrNull(userData.getName())) {
                    getSupportActionBar().setTitle("回复 " + userData.getName());
                }
            }
        }
        _id = getIntent().getIntExtra(TpConstants.KEY_ID, 0);
        etComment = (EditText) findViewById(R.id.etComment);
        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            findViewById(R.id.llBg).setBackgroundColor(getResources().getColor(R.color.white));
            etComment.setTextColor(getResources().getColor(R.color.black));
            etComment.setHintTextColor(getResources().getColor(R.color.black));
        } else {
            findViewById(R.id.llBg).setBackgroundColor(getResources().getColor(R.color.night_bg));
            etComment.setTextColor(getResources().getColor(R.color.white));
            etComment.setHintTextColor(getResources().getColor(R.color.white));
        }
        StrUtil.etResume(etComment);
        StrUtil.etSelectionLast(etComment);
        XUtil.autoKeyBoardShow(etComment);

        faceView = new FaceView(ctx) {
            @Override
            public void PopWindowOnDismissListener(String face) {
                if (StrUtil.notEmptyOrNull(face)) {
                    etComment.append(face);
                }
            }
        };


        toolbar.setNavigationIcon(XUtil.initIconWhite(Iconify.IconValue.md_done));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("emotion").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_insert_emoticon)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }
        if (item.getTitle().equals("emotion")) {
            faceView.showPopView(toolbar);

        }
        return false;
    }



    @Override
    protected int getLayoutResource() {
        return R.layout.ac_note_comment;
    }


    private void comment() {
        String content = etComment.getText().toString().trim();
        if (NetworkUtil.detect(NoteCommentAc.this)) {
            if (StrUtil.notEmptyOrNull(content)) {
                finish();
                if (data != null) {
                    replyDo(content, data.getUser_id(), data.getDairy_id());
                } else {
                    replyDo(content, null, _id);
                }
            } else {
                XUtil.tShort("请输入回复内容~");
            }
        } else {
            XUtil.tShort("网络连接不可用!");
        }
    }
    public static void replyDo(final String content, Integer recipient_id, Integer _id) {
        try {
            ServiceFactory.getService(NoteService.class).notesComment(_id, content, recipient_id, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    XUtil.tShort("回复成功~");
                    EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.NOTE_COMMENT_REFRESH.getValue()));
                }

                @Override
                public void failure(RetrofitError error) {
                    XUtil.onFailure(error);
                    XUtil.tShort("回复失败~");
                    DbUtils.saveDraftData(content, EnumData.DataTypeEnum.DRAFT.getValue());
                }
            });
        } catch (Exception e) {
        }
    }


    public void onPause() {
        super.onPause();
        StrUtil.etSave(etComment);
    }


}