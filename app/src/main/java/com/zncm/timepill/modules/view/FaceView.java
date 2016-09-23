package com.zncm.timepill.modules.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.modules.adapter.ColorAdapter;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.ArrayList;

public abstract class FaceView extends PopupWindow {
    private PopupWindow faceView;
    private Context ctx;

    public FaceView(Context context) {
        super(context);
        this.ctx = context;
        init();
    }

    private void init() {
        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            view.setBackgroundColor(ctx.getResources().getColor(R.color.day_bg));
        } else {
            view.setBackgroundColor(ctx.getResources().getColor(R.color.night_bg));
        }
        GridView gridView = new GridView(ctx);
        gridView.setPadding(10, 10, 10, 10);
        final ArrayList<String> faceStr = XUtil.getFaceStr();
        if (!StrUtil.listNotNull(faceStr)) {
            return;
        }
        ColorAdapter adapter = new ColorAdapter(ctx) {
            @Override
            public void setData(int position, MenuViewHolder holder) {
                String face = faceStr.get(position);
                if (StrUtil.notEmptyOrNull(face)) {
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    holder.tvTitle.setText(face);
                } else {
                    holder.tvTitle.setVisibility(View.GONE);
                }
            }
        };
        gridView.setNumColumns(3);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String face = faceStr.get(position);
                if (StrUtil.notEmptyOrNull(face)) {
                    PopWindowOnDismissListener(face);
                    faceView.dismiss();
                }
            }
        });
        adapter.setItems(faceStr);
        view.addView(gridView);
        faceView = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
    }

    public void showPopView(View parent) {
        faceView.setBackgroundDrawable(new BitmapDrawable());
        faceView.showAsDropDown(parent);
        faceView.update();
    }


    public abstract void PopWindowOnDismissListener(String face);

}
