package com.zncm.timepill.modules.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.utils.sp.TpSp;

public class CellTopicView extends LinearLayout {
    private LayoutInflater mInflater;
    private LinearLayout llView;
    private LinearLayout llContent;
    private TextView tvContent;
    private TextView tvTitle;
    private TextView tvOp;
    private ImageView ivPhoto;

    public CellTopicView(Context context) {
        this(context, null);
    }

    public CellTopicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        llView = (LinearLayout) mInflater.inflate(R.layout.view_topic, null);
        llView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(llView);
        tvContent = (TextView) llView.findViewById(R.id.tvContent);
        tvTitle = (TextView) llView.findViewById(R.id.tvTitle);
        tvOp = (TextView) llView.findViewById(R.id.tvOp);
        llContent = (LinearLayout) llView.findViewById(R.id.llContent);
        ivPhoto = (ImageView) llView.findViewById(R.id.ivPhoto);
        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            tvTitle.setTextColor(TpSp.getThemeColor());
            tvOp.setTextColor(TpSp.getThemeColor());
        }
    }

    public void hideMe() {
        llView.setVisibility(View.GONE);
    }

    public void showMe() {
        llView.setVisibility(View.VISIBLE);
    }

    public LinearLayout getLlContent() {
        return llContent;
    }

    public void setLlContent(LinearLayout llContent) {
        this.llContent = llContent;
    }

    public TextView getTvContent() {
        return tvContent;
    }

    public void setTvContent(TextView tvContent) {
        this.tvContent = tvContent;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public ImageView getIvPhoto() {
        return ivPhoto;
    }

    public void setIvPhoto(ImageView ivPhoto) {
        this.ivPhoto = ivPhoto;
    }

    public TextView getTvOp() {
        return tvOp;
    }

    public void setTvOp(TextView tvOp) {
        this.tvOp = tvOp;
    }
}
