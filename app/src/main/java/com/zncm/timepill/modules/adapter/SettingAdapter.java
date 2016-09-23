package com.zncm.timepill.modules.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zncm.timepill.R;
import com.zncm.timepill.data.BaseData;

import java.util.List;

public abstract class SettingAdapter extends BaseAdapter {

    private List<? extends BaseData> items;
    private Activity ctx;

    public SettingAdapter(Activity ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<? extends BaseData> items) {
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
        SettingViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(
                    R.layout.cell_setting, null);
            holder = new SettingViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            convertView.setTag(holder);
        } else {
            holder = (SettingViewHolder) convertView.getTag();
        }
        setData(position, holder);

        return convertView;
    }

    public abstract void setData(int position, SettingViewHolder holder);

    public class SettingViewHolder {
        public TextView tvTitle;
        public TextView tvStatus;
    }
}