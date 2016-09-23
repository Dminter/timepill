package com.zncm.timepill.modules.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.ui.PhotoShowAc;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

public class NoteHeadView extends LinearLayout {
    public TextView tvContent;
    public TextView tvTime;
    public TextView tvTitle;
    public TextView tvAuthor;
    public TextView tvReply;
    public ImageView ivIcon;
    public ImageView ivPhoto;
    private NoteData data;
    private Activity ctx;
    private boolean bOpShow = true;
    private LinearLayout view;

    public NoteHeadView(Activity context) {
        this(context, null);
    }

    public NoteHeadView(Activity context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        initViews();
    }


    private void initViews() {
        LayoutInflater mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) mInflater.inflate(R.layout.cell_lv_note, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        tvContent = (TextView) view.findViewById(R.id.tvContent);


//        tvContent.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                XUtil.copyDlg(ctx, tvContent.getText().toString().trim());
//            }
//        });

        View viewDiv = (View) view.findViewById(R.id.viewDiv);
        viewDiv.setVisibility(GONE);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
        tvReply = (TextView) view.findViewById(R.id.Reply);
        ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);

        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            tvAuthor.setTextColor(TpSp.getThemeColor());
            tvReply.setTextColor(TpSp.getThemeColor());
        }
        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            view.setBackgroundColor(getResources().getColor(R.color.white));
        } else {
            view.setBackgroundColor(getResources().getColor(R.color.night_bg));
        }
        addView(view);
    }


    public void initData(boolean bOpShow, NoteData data) {
        this.bOpShow = bOpShow;
        this.data = data;
        initNoteData();
    }


    private void initNoteData() {
        boolean isSelf = false;
        com.zncm.timepill.data.base.base.UserData user = TpApplication.getInstance().getUserData();
        if (user != null) {
            if (data.getUser_id() == user.getId()) {
                isSelf = true;
            }
        }
        if (StrUtil.notEmptyOrNull(data.getContent())) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(Html.fromHtml(data.getContent()));
        } else {
            tvContent.setVisibility(View.GONE);
        }
        if (StrUtil.notEmptyOrNull(data.getContent())) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(data.getContent());
            XUtil.doubleTextPre(ctx, tvContent, data.getContent());

        } else {
            tvContent.setVisibility(View.GONE);
        }
        ivIcon.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        tvReply.setVisibility(View.GONE);
        if (StrUtil.notEmptyOrNull(data.getNotebook_subject())) {
            tvAuthor.setVisibility(View.VISIBLE);
            tvAuthor.setText(data.getNotebook_subject());
            tvAuthor.setTextColor(getResources().getColor(R.color.gray));
        } else {
            tvAuthor.setVisibility(View.GONE);
        }

        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XUtil.viewUser(ctx, new UserData(data.getUser()));
            }
        });


        tvAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XUtil.viewUser(ctx, new UserData(data.getUser()));
            }
        });


        if (StrUtil.notEmptyOrNull(data.getPhotoThumbUrl()) && data.getType() == 2 && !TpSp.getNoPic()) {
            ivPhoto.setVisibility(View.VISIBLE);
            XUtil.getImageLoader().displayImage(data.getPhotoThumbUrl(), ivPhoto, XUtil.getOptions());
            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, PhotoShowAc.class);
                    intent.putExtra(TpConstants.KEY_STRING, data.getPhotoUrl());
                    ctx.startActivity(intent);
                }
            });
        } else {
            ivPhoto.setVisibility(View.GONE);
        }


        if (StrUtil.notEmptyOrNull(data.getCreated())) {
            tvTime.setVisibility(View.VISIBLE);
            if (bOpShow) {
                tvTime.setText(StrUtil.timeDay(data.getCreated()));
            } else {
                tvTime.setText(data.getCreated());
            }

        } else {
            tvTime.setVisibility(View.GONE);
        }


    }


}