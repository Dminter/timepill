package com.zncm.timepill.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zncm.timepill.R;

import java.util.List;

public abstract class ColorAdapter extends BaseAdapter {

    private List<String> items;
    private Context ctx;

    public ColorAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<String> items) {
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
        MenuViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(
                    R.layout.cell_color, null);
            holder = new MenuViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(holder);
        } else {
            holder = (MenuViewHolder) convertView.getTag();
        }
        setData(position, holder);

        return convertView;
    }

    public abstract void setData(int position, MenuViewHolder holder);

    public class MenuViewHolder {
        public TextView tvTitle;
    }
}