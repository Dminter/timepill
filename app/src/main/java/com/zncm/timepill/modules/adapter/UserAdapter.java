package com.zncm.timepill.modules.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.List;

public abstract class UserAdapter extends BaseAdapter {

    private List<UserData> items;
    private Activity ctx;

    public UserAdapter(Activity ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<UserData> items) {
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
        UserViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(
                    R.layout.cell_user_mini, null);
            holder = new UserViewHolder();
            holder.tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
            holder.btnOperate = (TextView) convertView.findViewById(R.id.btnOperate);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            convertView.setTag(holder);
        } else {
            holder = (UserViewHolder) convertView.getTag();
        }
        holder.tvAuthor.setTextColor(TpSp.getThemeColor());
        setData(position, holder);
        return convertView;
    }

    public abstract void setData(int position, UserViewHolder holder);

    public class UserViewHolder {
        public TextView tvAuthor;
        public TextView btnOperate;
        public ImageView ivIcon;

    }
}