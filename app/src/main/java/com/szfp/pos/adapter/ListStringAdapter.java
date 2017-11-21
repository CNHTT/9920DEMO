package com.szfp.pos.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szfp.adapter.BaseListAdapter;
import com.szfp.pos.R;
import com.szfp.utils.ContextUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author：ct on 2017/10/23 17:08
 * email：cnhttt@163.com
 */

public class ListStringAdapter extends BaseListAdapter<String> {
    public ListStringAdapter(Context mContext, List<String> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView ==null){
            convertView = ContextUtils.inflate(mContext, R.layout.layout_text);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else  viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.setData(getItem(position));
        return convertView;
    }

      static class ViewHolder {
        @BindView(R.id.lay_tv)
        TextView layTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void setData(String data) {
            layTv.setText(data);
        }
    }
}
