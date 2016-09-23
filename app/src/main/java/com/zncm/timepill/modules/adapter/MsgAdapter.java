package com.zncm.timepill.modules.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextVew;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.chat.MsgData;

import java.util.List;

public abstract class MsgAdapter extends BaseAdapter {

    private List<MsgData> items;
    private Activity ctx;

    public MsgAdapter(Activity ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<MsgData> items) {
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
        holder = new NoteViewHolder();
        MsgData data = items.get(position);
        int resource = 0;
        if (data != null) {
            if (data.getIs_send() == EnumData.MsgOwner.RECEIVER.getValue()) {
                resource = R.layout.cell_chatting_from;
            } else if (data.getIs_send() == EnumData.MsgOwner.SENDER.getValue()) {
                resource = R.layout.cell_chatting_to;
            }
        }
        convertView = LayoutInflater.from(ctx).inflate(
                resource, null);
        holder.tvText = (BubbleTextVew) convertView
                .findViewById(R.id.tvText);
        holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
        setData(position, holder);
        return convertView;
    }

    public abstract void setData(int position, NoteViewHolder holder);

    public class NoteViewHolder {
        public BubbleTextVew tvText;
        public ImageView ivIcon;
        public TextView tvTime;

    }
}