package com.zncm.timepill.modules.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malinskiy.materialicons.widget.IconTextView;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.List;

public abstract class NoteScanAdapter extends BaseAdapter {

    private List<NoteData> items;
    private Activity ctx;

    public NoteScanAdapter(Activity ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<NoteData> items) {
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
                    R.layout.cell_lv_scan_note, null);
            holder = new NoteViewHolder();
            holder.tvContent = (TextView) convertView
                    .findViewById(R.id.tvContent);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.tvTimeMonmth = (TextView) convertView.findViewById(R.id.tvTimeMonth);
            holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            holder.viewDiv = (View) convertView.findViewById(R.id.viewDiv);
            holder.llTime = (LinearLayout) convertView.findViewById(R.id.llTime);
            convertView.setTag(holder);
        } else {
            holder = (NoteViewHolder) convertView.getTag();
        }
        setData(position, holder);
        return convertView;
    }

    public abstract void setData(int position, NoteViewHolder holder);

    public class NoteViewHolder {
        public TextView tvContent;
        public TextView tvTimeMonmth;
        public TextView tvTime;
        public LinearLayout llTime;
        public ImageView ivPhoto;
        public View viewDiv;

    }
}