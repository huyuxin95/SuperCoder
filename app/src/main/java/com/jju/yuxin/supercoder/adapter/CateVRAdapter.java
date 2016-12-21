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
 * ClassName CateVRAdapter
 * Created by yuxin.
 * Created time 13-12-2016 15:13.
 * Describe : VR fragment 没有添加广告控件
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class CateVRAdapter extends RecyclerView.Adapter<CateVRAdapter.ViewHolder> {


    private static final String TAG = CateVRAdapter.class.getSimpleName();

    private Context context;
    //里面的数据
    private List<NewslistBean> newslistBeen = new ArrayList<>();

    public CateVRAdapter(Context context) {
        this.context = context;
    }


    /**
     * 添加数据引用
     * @param list
     */
    public void onReference(List<NewslistBean> list) {
        if (list != null) {
            newslistBeen.clear();
            newslistBeen.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 追加数据
     * @param list
     */
    public void addOnReference(List<NewslistBean> list) {
        if (list != null) {
            newslistBeen.addAll(list);
            notifyDataSetChanged();
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    //返回ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_item_two, parent, false);
        CateVRAdapter.ViewHolder vh = new CateVRAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
     //图片加载失败
        if (Constant.FAILED_LOADING.equals(newslistBeen.get(position).getPicUrl())){
            holder.iv_adapter.setBackgroundResource(R.mipmap.failed_loading);
        }else{
            //正常加载图片
            x.image().bind(holder.iv_adapter,newslistBeen.get(position).getPicUrl());
        }
        //设置标题
        holder.tv_title.setText(newslistBeen.get(position).getTitle());
        //设置时间
        holder.tv_time.setText(newslistBeen.get(position).getCtime());

        //判断是否有调动监听
        if (mOnItemClickLitener != null) {
            //点击事件监听
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

    /**
     * ViewHolder
     */
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
