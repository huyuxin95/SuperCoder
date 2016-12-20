package com.jju.yuxin.supercoder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.bean.NewslistBean;
import com.jju.yuxin.supercoder.bean.ResponseBean;
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
 * ClassName TwoAdapter
 * Created by yuxin.
 * Created time 13-12-2016 15:13.
 * Describe :
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class CatePicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = CatePicAdapter.class.getSimpleName();
    //上下文
    private Context context;

    private List<Integer> adlist = new ArrayList<>();


    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        NEW,
        AD
    }


    //里面的数据
    private List<ResponseBean> piclistBeen = new ArrayList<>();

    /**
     * 构造函数
     *
     * @param context
     */
    public CatePicAdapter(Context context) {
        this.context = context;
    }


    /**
     * 给Recycle设置List
     *
     * @param list
     */
    public void onReference(List<ResponseBean> list, List<Integer> adlist) {
        if (list != null) {
            piclistBeen.clear();
            piclistBeen.addAll(list);
            this.adlist.clear();
            this.adlist.addAll(adlist);

            i(TAG, "HM"+"onReference" + this.adlist.toString());

            for (int adposition:adlist) {

                i(TAG, "HM"+"onReference" + adposition);
                piclistBeen.add(adposition,new ResponseBean());
            }

            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据
     *
     * @param list
     */
    public void addOnReference(List<ResponseBean> list, List<Integer> adlist) {
        if (list != null) {
            piclistBeen.addAll(list);

            this.adlist.addAll(adlist);

            i(TAG, "HM"+"addOnReference" + this.adlist.toString());

            for (int adposition:adlist) {
                i(TAG, "HM"+"addOnReference" + "adposition"+adposition);

                piclistBeen.add(adposition,new ResponseBean());
            }

            i(TAG, "onReference" + "new"+piclistBeen.toString());
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
        //新闻条目加广告条目
        return piclistBeen.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //加载Item View的时候根据不同TYPE加载不同的布局
        if (viewType == ITEM_TYPE.NEW.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_item_pic, parent, false);
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

        if (holder instanceof ViewHolder) {

                if (piclistBeen==null||piclistBeen.get(position).getImage()==null
                        ||piclistBeen.get(position).getImage().getThumb()==null
                        ||piclistBeen.get(position).getImage().getThumb().getUrl()==null){
                    ((ViewHolder) holder).iv_pic.setBackgroundResource(R.mipmap.failed_loading);
                }else{
                    x.image().bind(((ViewHolder) holder).iv_pic, piclistBeen.get(position).getImage().getThumb().getUrl());
                }

                if (mOnItemClickLitener != null) {
                    ((ViewHolder) holder).ry_cardview_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickLitener.onItemClick(piclistBeen.get(position), position);
                        }
                    });
            }
        }else if(holder instanceof ADViewHolder){
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




    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout ry_cardview_pic;
        ImageView iv_pic;


        public ViewHolder(View view) {
            super(view);
            ry_cardview_pic=(RelativeLayout) view.findViewById(R.id.ry_cardview_pic);
            iv_pic = (ImageView) view.findViewById(R.id.im_pic_pre);
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
        void onItemClick(ResponseBean news, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
