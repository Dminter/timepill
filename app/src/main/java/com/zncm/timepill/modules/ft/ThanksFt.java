package com.zncm.timepill.modules.ft;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zncm.timepill.R;
import com.zncm.timepill.global.TpConstants;


public class ThanksFt extends BaseFt {
    private TextView tvTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = getActivity();
        view = inflater.inflate(R.layout.ft_thanks, null);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(TpConstants.USER_NAMES);
        return view;
    }
}
