package com.zncm.timepill.modules.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.malinskiy.materialicons.Iconify;
import com.zncm.timepill.R;
import com.zncm.timepill.data.base.note.NoteBookData;
import com.zncm.timepill.utils.XUtil;

import java.util.List;

public abstract class NoteBookAdapter extends BaseAdapter {

    private List<NoteBookData> items;
    private Activity ctx;

    public NoteBookAdapter(Activity ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<NoteBookData> items) {
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
        NoteBoolViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(
                    R.layout.cell_notebook_grid, null);
            holder = new NoteBoolViewHolder();
            holder.tvCreated = (TextView) convertView.findViewById(R.id.tvCreated);
            holder.ivExpired = (ImageView) convertView.findViewById(R.id.ivExpired);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            holder.ivPublic = (ImageView) convertView.findViewById(R.id.ivPublic);
            convertView.setTag(holder);
        } else {
            holder = (NoteBoolViewHolder) convertView.getTag();
        }
        setData(position, holder);
        return convertView;
    }

    public abstract void setData(int position, NoteBoolViewHolder holder);

    public class NoteBoolViewHolder {
        public TextView tvCreated;
        public ImageView ivExpired;
        public TextView tvTitle;
        public ImageView ivIcon;
        public ImageView ivPublic;
    }
}