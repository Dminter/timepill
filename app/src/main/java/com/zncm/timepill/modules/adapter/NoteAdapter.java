package com.zncm.timepill.modules.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.malinskiy.materialicons.widget.IconTextView;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.List;

public abstract class NoteAdapter extends BaseAdapter {

    private List<NoteData> items;
    private Activity ctx;

    public NoteAdapter(Activity ctx) {
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
                    R.layout.cell_lv_note, null);
            holder = new NoteViewHolder();
            holder.tvContent = (TextView) convertView
                    .findViewById(R.id.tvContent);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
            holder.tvReply = (TextView) convertView.findViewById(R.id.Reply);
            holder.replyIcon = (IconTextView) convertView.findViewById(R.id.replyIcon);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            convertView.setTag(holder);
        } else {
            holder = (NoteViewHolder) convertView.getTag();
        }
//        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
//        }
        holder.tvAuthor.setTextColor(TpSp.getThemeColor());
        holder.replyIcon.setVisibility(View.VISIBLE);
        setData(position, holder);
        return convertView;
    }

    public abstract void setData(int position, NoteViewHolder holder);

    public class NoteViewHolder {
        public TextView tvContent;
        public TextView tvTime;
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvReply;
        public IconTextView replyIcon;
        public ImageView ivIcon;
        public ImageView ivPhoto;
    }
}