package com.jju.yuxin.supercoder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class CatePadAdapter extends RecyclerView.Adapter<CatePadAdapter.ViewHolder> {

    private static final String TAG = CatePadAdapter.class.getSimpleName();

    private Context context;
    //里面的数据
    private List<NewslistBean> newslistBeen = new ArrayList<>();

    public CatePadAdapter(Context context) {
        this.context = context;
    }


    public void onReference(List<NewslistBean> list) {
        if (list != null) {
            newslistBeen.clear();
            newslistBeen.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addOnReference(List<NewslistBean> list) {
        if (list != null) {
            newslistBeen.addAll(list);
            notifyDataSetChanged();
        }
    }


    @Override
    public CatePadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_item_two, parent, false);
        CatePadAdapter.ViewHolder vh = new CatePadAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(CatePadAdapter.ViewHolder holder, final int position) {
        if (Constant.FAILED_LOADING.equals(newslistBeen.get(position).getPicUrl())){
            holder.iv_adapter.setBackgroundResource(R.mipmap.failed_loading);
        }else{
            x.image().bind(holder.iv_adapter,newslistBeen.get(position).getPicUrl());
        }
        holder.tv_title.setText(newslistBeen.get(position).getTitle());
        holder.tv_time.setText(newslistBeen.get(position).getCtime());

        if (mOnItemClickLitener != null) {
            holder.ry_cardview.setOnClickListener(new View.OnClickListener() {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ry_cardview;
        ImageView iv_adapter;
        TextView tv_title;
        TextView tv_time;

        public ViewHolder(View view) {
            super(view);
            ry_cardview = (LinearLayout) view.findViewById(R.id.ry_cardview);
            iv_adapter = (ImageView) view.findViewById(R.id.im_two_img);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
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
