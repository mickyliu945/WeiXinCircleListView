package com.micky.weixinxlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @Project WeiXinXListView
 * @Packate com.micky.weixinxlistview.xlistview
 *
 * @Description
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2015-12-01 12:30
 * @Version 1.0
 */
public class UserAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<String> mDataList;

    public UserAdapter(Context context, List<String> list) {
        mLayoutInflater = LayoutInflater.from(context);
        mDataList = list;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public String getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_user, null);
            holder = new Holder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }
        holder.tvName.setText(getItem(position));
        return convertView;
    }

    class Holder {
        TextView tvName;
    }

}
