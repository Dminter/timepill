package com.zncm.timepill.modules.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zncm.timepill.R;
import com.zncm.timepill.data.base.note.DraftData;

import java.util.List;

public abstract class DraftAdapter extends BaseAdapter {

    private List<DraftData> items;
    private Activity ctx;

    public DraftAdapter(Activity ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<DraftData> items) {
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
        DraftViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(
                    R.layout.cell_lv_draft, null);
            holder = new DraftViewHolder();
            holder.tvText = (TextView) convertView
                    .findViewById(R.id.tvText);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            convertView.setTag(holder);
        } else {
            holder = (DraftViewHolder) convertView.getTag();
        }
        setData(position, holder);
        return convertView;
    }

    public abstract void setData(int position, DraftViewHolder holder);

    public class DraftViewHolder {
        public TextView tvText;
        public TextView tvTime;

    }
}