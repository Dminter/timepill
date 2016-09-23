package com.zncm.timepill.modules.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zncm.timepill.R;
import com.zncm.timepill.utils.DeviceUtil;

public class AboutTopView extends LinearLayout {
    private LayoutInflater mInflater;
    private LinearLayout llView;

    public AboutTopView(Activity context) {
        this(context, null);
    }

    public AboutTopView(Activity context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        llView = (LinearLayout) mInflater.inflate(R.layout.view_about, null);
        llView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(llView);
    }

}
