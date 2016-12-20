package com.jju.yuxin.supercoder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.bean.NewslistBean;
import com.jju.yuxin.supercoder.utils.Constant;

import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.util.Log.e;
import static android.util.Log.i;


/**
 * =============================================================================
 * <p>
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName OneAdapter
 * Created by yuxin.
 * Created time 12-12-2016 14:13.
 * Describe :  运动Fragment的适配器
 * History:
 * Version   1.0.
 * <p>
 * ==============================================================================
 */
public class YundongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG=YundongAdapter.class.getSimpleName();

    //上下文
    private Context context;

    private List<Integer> adlist = new ArrayList<>();


    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        NEW,
        AD
    }


    //里面的数据
    private List<NewslistBean> newslistBeen = new ArrayList<>();

    /**
     * 构造函数
     *
     * @param context
     */
    public YundongAdapter(Context context) {
        this.context = context;
    }


    /**
     * 给Recycle设置List
     *
     * @param list
     */
    public void onReference(List<NewslistBean> list, List<Integer> adlist) {
        if (list != null) {
            newslistBeen.clear();
            newslistBeen.addAll(list);
            this.adlist.clear();
            this.adlist.addAll(adlist);

            i(TAG, "HM"+"onReference" + this.adlist.toString());

            for (int adposition:adlist) {

                i(TAG, "HM"+"onReference" + adposition);

                newslistBeen.add(adposition,new NewslistBean());
            }

            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据
     *
     * @param list
     */
    public void addOnReference(List<NewslistBean> list, List<Integer> adlist) {
        if (list != null) {
            newslistBeen.addAll(list);

            this.adlist.addAll(adlist);

            i(TAG, "HM"+"addOnReference" + this.adlist.toString());

            for (int adposition:adlist) {
                i(TAG, "HM"+"addOnReference" + "adposition"+adposition);

                newslistBeen.add(adposition,new NewslistBean());
            }

            i(TAG, "onReference" + "new"+newslistBeen.toString());
            notifyDataSetChanged();
        }
    }

    //根据传入的广告的位置集合,设置相应的广告位置
    @Override
    public int getItemViewType(int position) {


        i(TAG, "HM"+"getItemViewType" + adlist.toString());

        for (int adposition : adlist) {
            if (position == adposition) {
                e(TAG, "ViewType" + "position"+position);
                return ITEM_TYPE.AD.ordinal();
            }
        }
        return ITEM_TYPE.NEW.ordinal();
    }

    @Override
    public int getItemCount() {
        return newslistBeen.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //加载Item View的时候根据不同TYPE加载不同的布局
        if (viewType == ITEM_TYPE.NEW.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_fragment_adapter, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_fragment_adapter_ad, parent, false);
            ADViewHolder adviewHolder = new ADViewHolder(view);
            return adviewHolder;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        i(TAG, "onBindViewHolder" + "position:" + position);
        if (holder instanceof ViewHolder) {
            i(TAG, "onBindViewHolder" + "ViewHolder:" + position);
            if (Constant.FAILED_LOADING.equals(newslistBeen.get(position).getPicUrl())) {
                ((ViewHolder) holder).iv_adapter.setBackgroundResource(R.mipmap.failed_loading);
            } else {
                x.image().bind(((ViewHolder) holder).iv_adapter, newslistBeen.get(position).getPicUrl());
            }
            ((ViewHolder) holder).tv_title.setText(newslistBeen.get(position).getTitle());
            ((ViewHolder) holder).tv_time.setText(newslistBeen.get(position).getCtime());
            if (mOnItemClickLitener != null) {
                ((ViewHolder) holder).ry_fragment_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(newslistBeen.get(position), position);
                    }
                });
            }

        } else if (holder instanceof ADViewHolder) {
            // 获取广告条
            View bannerView = BannerManager.getInstance(context)
                    .getBannerView(new BannerViewListener() {
                        @Override
                        public void onRequestSuccess() {
                            Log.d(TAG, "请求广告条成功");
                        }

                        @Override
                        public void onSwitchBanner() {
                            Log.d(TAG, "广告条切换");
                        }

                        @Override
                        public void onRequestFailed() {
                            Log.d(TAG, "请求广告条失败");
                        }
                    });

            // 将广告条加入到布局中
            ((ADViewHolder) holder).bannerLayout.addView(bannerView);

        }
    }


    /**
     * 视图缓存
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ry_fragment_cardview;
        ImageView iv_adapter;
        TextView tv_title;
        TextView tv_time;

        public ViewHolder(View view) {
            super(view);
            ry_fragment_cardview = (LinearLayout) view.findViewById(R.id.ry_fragment_cardview);
            iv_adapter = (ImageView) view.findViewById(R.id.im_item_title);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }

    /**
     * 视图缓存
     */
    public class ADViewHolder extends RecyclerView.ViewHolder {
        LinearLayout bannerLayout;

        public ADViewHolder(View view) {
            super(view);
            bannerLayout = (LinearLayout) view.findViewById(R.id.ll_banner);
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
