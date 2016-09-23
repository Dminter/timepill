package com.zncm.timepill.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.note.NoteComment;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.List;

public abstract class CommentAdapter extends BaseAdapter {

    private List<NoteComment> items;
    private Context ctx;

    public CommentAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<NoteComment> items) {
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
                    R.layout.cell_lv_comment, null);
            holder = new NoteViewHolder();
            holder.tvContent = (TextView) convertView
                    .findViewById(R.id.tvContent);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);

            holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            convertView.setTag(holder);
        } else {
            holder = (NoteViewHolder) convertView.getTag();
        }

        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            holder.tvAuthor.setTextColor(TpSp.getThemeColor());
        }
        setData(position, holder);
        return convertView;
    }

    public abstract void setData(int position, NoteViewHolder holder);

    public class NoteViewHolder {
        public TextView tvContent;
        public TextView tvTime;
        public TextView tvAuthor;
        public ImageView ivIcon;

    }
}