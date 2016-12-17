package com.jju.yuxin.supercoder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.bean.VedioInfoBean;

import org.xutils.x;

import java.util.List;


/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.cinews.adapter
 * Created by yuxin.
 * Created time 2016/11/12 0012 下午 10:44.
 * Version   1.0;
 * Describe :Listview的适配器,这是一个listview的model,
 * 对于每一个模块应该有自己的adapter,在子ListView应该编写自己的ViewHolder
 * History:
 * ==============================================================================
 */

public class VedioList_Adapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<VedioInfoBean> vedioinfos;


    public VedioList_Adapter(Context context, List<VedioInfoBean> vedioinfos) {
        this.context = context;
        this.vedioinfos = vedioinfos;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return vedioinfos.size();
    }


    public void replaceList(List<VedioInfoBean> vedioinfos){
        this.vedioinfos = vedioinfos;
    }

    @Override
    public Object getItem(int position) {
        return vedioinfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.activity_vedio_item, null);
            viewHolder.iv_vedio_pic = (ImageView) convertView.findViewById(R.id.iv_vedio_pic);
            viewHolder.tv_vedio_title = (TextView) convertView.findViewById(R.id.tv_vedio_title);
            viewHolder.tv_vedio_date = (TextView) convertView.findViewById(R.id.tv_vedio_date);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        x.image().bind(viewHolder.iv_vedio_pic,vedioinfos.get(position).getImg_src());
        //设置新闻标题
        viewHolder.tv_vedio_title.setText(vedioinfos.get(position).getNews_info()+"");
        //设置新闻时间
        viewHolder.tv_vedio_date.setText(vedioinfos.get(position).getNews_date()+"");
        return convertView;
    }

    private class ViewHolder {
        ImageView iv_vedio_pic;
        TextView tv_vedio_title;
        TextView tv_vedio_date;
    }


}
