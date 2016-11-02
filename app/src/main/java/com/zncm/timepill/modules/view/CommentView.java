package com.zncm.timepill.modules.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.chat.MsgData;
import com.zncm.timepill.data.base.note.NoteComment;
import com.zncm.timepill.modules.services.NoteService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.ui.BaseAc;
import com.zncm.timepill.modules.ui.NoteCommentAc;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.NetworkUtil;
import com.zncm.timepill.utils.RefreshEvent;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jmx on 1/25 0025.
 */
public class CommentView extends LinearLayout implements View.OnClickListener {

    public EmojiconEditText etContent;
    public TextView tvSend;
    private ImageView ivFace;
    FrameLayout faceLayout;

    BaseAc ctx;
    boolean bFace = false;
    boolean bClickFirst = true;


    public CommentView(BaseAc context) {
        super(context);
        this.ctx = context;
        initView();
    }


    private void initView() {
        LayoutInflater mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) mInflater.inflate(R.layout.activity_comment, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        etContent = (EmojiconEditText) view.findViewById(R.id.etContent);
        tvSend = (TextView) view.findViewById(R.id.tvSend);
        ivFace = (ImageView) view.findViewById(R.id.ivFace);

//        tvSend.setTextColor(TpSp.getThemeColor());
//        ivFace.setTextColor(TpSp.getThemeColor());
//        etContent.setTextColor(TpSp.getThemeColor());

        faceLayout = (FrameLayout) view.findViewById(R.id.emojicons);
        tvSend.setOnClickListener(this);
        ivFace.setOnClickListener(this);
        etContent.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (bClickFirst) {
                    bFace = false;
                    faceLayout.setVisibility(View.GONE);
                    bClickFirst = false;
                }

            }
        });
        etContent.addTextChangedListener(mTextWatcher);


        etContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bFace = false;
                faceLayout.setVisibility(View.GONE);
            }
        });
        addView(view);
    }




    TextWatcher mTextWatcher = new TextWatcher() {
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
            try {
                int retainCount = temp.length();
                if (retainCount > 0) {
                    tvSend.setBackgroundResource(R.drawable.button_blue);
                } else if (retainCount == 0) {
                    tvSend.setBackgroundResource(R.drawable.button);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivFace) {
            if (bFace) {
                bFace = false;
                faceLayout.setVisibility(View.GONE);
                XUtil.autoKeyBoardShow(etContent);
            } else {
                bFace = true;
                faceLayout.setVisibility(View.VISIBLE);
                XUtil.hideKeyBoard(etContent);
            }
            setEmojiconFragment(bFace);
        }
        if (v.getId() == R.id.tvSend) {

        }
    }


    private void setEmojiconFragment(boolean useSystemDefault) {
        ctx.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }


    public void comment(NoteComment data, int _id) {
        String content = etContent.getText().toString().trim();
        if (NetworkUtil.detect(ctx)) {
            if (StrUtil.notEmptyOrNull(content)) {
                if (data != null) {
                    replyDo(content, data.getUser_id(), data.getDairy_id());
                } else {
                    replyDo(content, null, _id);
                }
                etContent.setText("");
                etContent.setHint("回应...");
                XUtil.hideKeyBoard(etContent);
                bFace = false;
                faceLayout.setVisibility(View.GONE);
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
}
