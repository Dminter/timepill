package com.zncm.timepill.modules.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.FaceData;
import com.zncm.timepill.data.base.note.DraftData;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;


public class FaceAdapter extends BaseAdapter {
    private List<FaceData> items;
    private Activity ctx;

    public FaceAdapter(Activity ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<FaceData> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    @Override
    public FaceData getItem(int position) {
        if (items != null) {
            return items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (items != null) {
            return position;
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FaceData faceData = items.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(
                    R.layout.cell_face, null);
            holder = new Holder();
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.textView.setText(faceData.getContent());
        return convertView;
    }


    private class Holder {
        public TextView textView;
    }


    public static List<FaceData> initFace() {
        List<FaceData> faceDatas = new ArrayList<FaceData>();
        ArrayList<DraftData> datas = DbUtils.initDraftData(EnumData.DataTypeEnum.CUSTOM_FACE.getValue());
        if (StrUtil.listNotNull(datas)) {
            for (DraftData data : datas) {
                FaceData faceData = new FaceData(data.getContent());
                faceDatas.add(faceData);
            }
        }
        faceDatas.addAll(TpConstants.getFaces());
        return faceDatas;
    }


}
