package com.jju.yuxin.supercoder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.jju.yuxin.supercoder.R;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName OneAdapter
 * Created by yuxin.
 * Created time 12-12-2016 14:13.
 * Describe :  第一个Fragment的适配器
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class OneAdapter extends RecyclerView.Adapter<OneAdapter.ViewHolder> {

    //上下文
    private Context context;

    //里面的数据
    private List<String> one_list = new ArrayList<>();

    /**
     * 构造函数
     * @param context
     */
    public OneAdapter(Context context){
     this.context = context;
    }


    /**
     * 给Recycle设置List
     * @param list
     */
    public void onReference(List<String> list){
       if(list != null){
           one_list.clear();
           one_list.addAll(list);
           notifyDataSetChanged();
       }
    }

    /**
     * 添加数据
     * @param list
     */
    public void addOnReference(List<String> list){
        if(list != null){
            one_list.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return one_list.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        x.image().bind(holder.iv_adapter,one_list.get(position));
    }


    /**
     * 视图缓存
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_adapter;
        public ViewHolder(View view) {
            super(view);
            iv_adapter = (ImageView) view.findViewById(R.id.iv_adapter);
        }
    }
}
