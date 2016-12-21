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
import com.jju.yuxin.supercoder.bean.VedioInfoBean;
import com.jju.yuxin.supercoder.utils.Constant;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName VedioAdapter
 * Created by yuxin.
 * Created time 13-12-2016 15:13.
 * Describe : 视频适配器
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class VedioAdapter extends RecyclerView.Adapter<VedioAdapter.ViewHolder> {

    private static final String TAG = VedioAdapter.class.getSimpleName();

    private Context context;
    //里面的数据
    private List<VedioInfoBean> vedioinfos = new ArrayList<>();

    public VedioAdapter(Context context) {
        this.context = context;
    }


    /**
     * 添加数据引用
     * @param list
     */
    public void onReference(List<VedioInfoBean> list) {
        if (list != null) {
            vedioinfos.clear();
            vedioinfos.addAll(list);
            notifyDataSetChanged();
        }
    }

  /**
     * 追加数据
     * @param list
     */
    public void addOnReference(List<VedioInfoBean> list) {
        if (list != null) {
            vedioinfos.addAll(list);
            notifyDataSetChanged();
        }
    }


    @Override
    public VedioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     //返回ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_vedio_item, parent, false);
        VedioAdapter.ViewHolder vh = new VedioAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(VedioAdapter.ViewHolder holder, final int position) {
           //图片加载失败
        if (Constant.FAILED_LOADING.equals(vedioinfos.get(position).getImg_src())){
            holder.iv_vedio_pic.setBackgroundResource(R.mipmap.failed_loading);
        }else{
         //正常加载图片
            x.image().bind(holder.iv_vedio_pic,vedioinfos.get(position).getImg_src());
        }
        //设置标题
        holder.tv_vedio_title.setText(vedioinfos.get(position).getNews_info());
          //设置时间
        holder.tv_vedio_date.setText(vedioinfos.get(position).getNews_date());

        if (mOnItemClickLitener != null) {
         //点击事件监听
            holder.ll_vedio_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(vedioinfos.get(position), position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return vedioinfos.size();
    }

/**
     * ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_vedio_item;
        ImageView iv_vedio_pic;
        TextView tv_vedio_title;
        TextView tv_vedio_date;

        public ViewHolder(View view) {
            super(view);
            ll_vedio_item=(LinearLayout) view.findViewById(R.id.ll_vedio_item);
            iv_vedio_pic = (ImageView) view.findViewById(R.id.iv_vedio_pic);
            tv_vedio_title = (TextView) view.findViewById(R.id.tv_vedio_title);
            tv_vedio_date = (TextView) view.findViewById(R.id.tv_vedio_date);
        }
    }

    //点击事件的回调
    private OnItemClickLitener mOnItemClickLitener;

    public interface OnItemClickLitener {
        void onItemClick(VedioInfoBean news, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
