package com.zncm.timepill.modules.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zncm.timepill.R;
import com.zncm.timepill.data.base.tips.TipData;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.List;

public abstract class TipsAdapter extends BaseAdapter {

    private List<TipData> items;
    private Activity ctx;

    public TipsAdapter(Activity ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<TipData> items) {
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
    public Object getItem(int position) {
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
        NoteViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(
                    R.layout.cell_lv_tips, null);
            holder = new NoteViewHolder();
            holder.tvContent = (TextView) convertView
                    .findViewById(R.id.tvContent);
            holder.tvDel = (TextView) convertView.findViewById(R.id.tvDel);
            holder.rlTips = (RelativeLayout) convertView.findViewById(R.id.rlTips);
            convertView.setTag(holder);
        } else {
            holder = (NoteViewHolder) convertView.getTag();
        }
        holder.tvDel.setTextColor(TpSp.getThemeColor());
        setData(position, holder);
        return convertView;
    }

    public abstract void setData(int position, NoteViewHolder holder);

    public class NoteViewHolder {
        public TextView tvContent;
        public TextView tvDel;
        public RelativeLayout rlTips;

    }
}