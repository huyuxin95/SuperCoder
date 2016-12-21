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
import com.jju.yuxin.supercoder.http.AsyncTaskUtils;
import com.jju.yuxin.supercoder.utils.Constant;

import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static android.util.Log.e;
import static android.util.Log.i;


/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName CateAndroidAdapter
 * Created by yuxin.
 * Created time 13-12-2016 15:13.
 * Describe : android fragment 适配器
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class CateAndroidAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = CateAndroidAdapter.class.getSimpleName();

    private Context context;

    private List<Integer> adlist = new ArrayList<>();


    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        NEW,  //新闻
        AD     //广告
    }


    //里面的数据
    private List<NewslistBean> newslistBeen = new ArrayList<>();

    /**
     * 构造函数
     *
     * @param context
     */
    public CateAndroidAdapter(Context context) {
        this.context = context;
    }


    /**
     * 给RecycleView重新赋值List
     *
     * @param list
     */
    public void onReference(List<NewslistBean> list, List<Integer> adlist) {
        if (list != null) {
            //清空原有数据
            newslistBeen.clear();
            //添加新的数据
            newslistBeen.addAll(list);
            //清空广告位置设置
            this.adlist.clear();
            this.adlist.addAll(adlist);

            i(TAG, "HM"+"onReference" + this.adlist.toString());

            //遍历广告位置,在相应的位置插入一个为空的数据
            for (int adposition:adlist) {
                i(TAG, "HM"+"onReference" + adposition);
                newslistBeen.add(adposition,new NewslistBean());
            }
            //通知adapter数据发生改变
            notifyDataSetChanged();
        }
    }

    /**
     * 追加数据
     * @param list
     */
    public void addOnReference(List<NewslistBean> list, List<Integer> adlist) {
        if (list != null) {
            //向新闻集合级添加新的集合
            newslistBeen.addAll(list);
            //向广告集合里添加新的广告位置
            this.adlist.addAll(adlist);

            i(TAG, "HM"+"addOnReference" + this.adlist.toString());

            //遍历广告集合
            for (int adposition:adlist) {
                i(TAG, "HM"+"addOnReference" + "adposition"+adposition);
                //向新闻的集合广告位置插入空的对象
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
        //遍历广告位置集合
        for (int adposition : adlist) {
            //当前位置是广告
            if (position == adposition) {
                e(TAG, "ViewType" + "position"+position);
                //广告ViewType返回
                return ITEM_TYPE.AD.ordinal();
            }
        }
        return ITEM_TYPE.NEW.ordinal();
    }

    @Override
    public int getItemCount() {
        //新闻条目加广告条目
        return newslistBeen.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //加载Item View的时候根据不同TYPE加载不同的布局
        if (viewType == ITEM_TYPE.NEW.ordinal()) {
            //新闻布局
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_item_two, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        } else {
            //广告布局
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_fragment_adapter_ad, parent, false);
            ADViewHolder adviewHolder = new ADViewHolder(view);
            return adviewHolder;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        i(TAG, "onBindViewHolder" + "position:" + position);
        //当前是新闻布局
        if (holder instanceof ViewHolder) {
            i(TAG, "onBindViewHolder" + "ViewHolder:" + position);
            //图片加载失败处理
            if (Constant.FAILED_LOADING.equals(newslistBeen.get(position).getPicUrl())) {
                ((ViewHolder) holder).iv_adapter.setBackgroundResource(R.mipmap.failed_loading);
            } else {
                //图片正常加载
                x.image().bind(((ViewHolder) holder).iv_adapter, newslistBeen.get(position).getPicUrl());
            }
            //设置新闻标题
            ((ViewHolder) holder).tv_title.setText(newslistBeen.get(position).getTitle());
            //新闻时间
            ((ViewHolder) holder).tv_time.setText(newslistBeen.get(position).getCtime());
            //判断调用设置监听
            if (mOnItemClickLitener != null) {
                //设置点击监听
                ((ViewHolder) holder).ry_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(newslistBeen.get(position), position);
                    }
                });
            }

            //广告布局
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
        LinearLayout ry_cardview;  //点击事件视图
        ImageView iv_adapter;  //新闻图片
        TextView tv_title;  //新闻标题
        TextView tv_time;  //新闻时间

        public ViewHolder(View view) {
            super(view);
            ry_cardview = (LinearLayout) view.findViewById(R.id.ry_cardview);
            iv_adapter = (ImageView) view.findViewById(R.id.im_two_img);
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
