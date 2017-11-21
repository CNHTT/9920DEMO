package com.szfp.pos.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szfp.adapter.BaseListAdapter;
import com.szfp.pos.R;
import com.szfp.pos.model.Item;
import com.szfp.utils.ContextUtils;

import java.util.List;

/**
 * Created by 戴尔 on 2017/11/20.
 */

public class ItemAdapter extends BaseListAdapter<Item> {


    public ItemAdapter(Context mContext, List<Item> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView==null){
            convertView = ContextUtils.inflate(mContext, R.layout.item_layout);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else  holder = (ViewHolder) convertView.getTag();
        holder.setData(getItem(position));

        return convertView;
    }

    private  class ViewHolder{

        private TextView mTv_title;
        private TextView mTv_result;


        public ViewHolder(View view) {
            mTv_title = (TextView) view.findViewById(R.id.tv_title);
            mTv_result = (TextView)view.findViewById(R.id.tv_result);

        }

        public void setData(Item data) {

            mTv_title.setText(data.getGameType()+" "+data.getGameOption()+" "+data.getOldType()  + "   ＄ " +data.getAmount());
            mTv_result.setText(data.getList().toString());
        }
    }
}
