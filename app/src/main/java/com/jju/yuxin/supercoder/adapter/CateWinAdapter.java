package com.jju.yuxin.supercoder.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.bean.NewslistBean;
import com.jju.yuxin.supercoder.utils.Constant;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName TwoAdapter
 * Created by yuxin.
 * Created time 13-12-2016 15:13.
 * Describe :
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class CateWinAdapter extends RecyclerView.Adapter<CateWinAdapter.ViewHolder> {

    private static final String TAG =CateWinAdapter.class.getSimpleName();
    private Context context;
    //里面的数据
    private List<NewslistBean> newslistBeen=new ArrayList<>();

    public CateWinAdapter(Context context){
     this.context = context;
    }


    public void onReference(List<NewslistBean> list){
       if(list != null){
           newslistBeen.clear();
           newslistBeen.addAll(list);
           notifyDataSetChanged();
       }
    }

    public void addOnReference(List<NewslistBean> list){
        if(list != null){
            newslistBeen.addAll(list);
            notifyDataSetChanged();
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_item_win, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (Constant.FAILED_LOADING.equals(newslistBeen.get(position).getPicUrl())){
            holder.iv_adapter.setBackgroundResource(R.mipmap.failed_loading);
        }else{
        x.image().bind(holder.iv_adapter,newslistBeen.get(position).getPicUrl());
        }
        holder.tv_title.setText(newslistBeen.get(position).getTitle());

        if (mOnItemClickLitener != null) {
            holder.ry_cardview_win.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(newslistBeen.get(position), position);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return newslistBeen.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout ry_cardview_win;
        ImageView iv_adapter;
        TextView tv_title;

        public ViewHolder(View view) {
            super(view);
            ry_cardview_win=(RelativeLayout) view.findViewById(R.id.ry_cardview_win);
            iv_adapter = (ImageView) view.findViewById(R.id.im_two_img);
            tv_title=(TextView) view.findViewById(R.id.tv_title);
        }
    }


    //点击事件的回调
    private OnItemClickLitener mOnItemClickLitener;

    public interface OnItemClickLitener {
        void onItemClick(NewslistBean news, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
