package com.jju.yuxin.supercoder.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.adapter.CateIOSAdapter;
import com.jju.yuxin.supercoder.adapter.TwoAdapter;
import com.jju.yuxin.supercoder.bean.NewslistBean;
import com.jju.yuxin.supercoder.http.GetParams;
import com.jju.yuxin.supercoder.http.HttpUtil;
import com.jju.yuxin.supercoder.utils.Constant;
import com.jju.yuxin.supercoder.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import static android.util.Log.e;
import static android.util.Log.i;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName TwoCateFragment
 * Created by yuxin.
 * Created time 13-12-2016 10:11.
 * Describe : 第三层Viewpager,填充的可滚动的Fragment
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class CateIOSFragment extends ScrollAbleFragment {

    private static final String TAG =CateIOSFragment.class.getSimpleName();
    private YRecycleview recyclerview;
    private CateIOSAdapter iosAdapter;
    private List<String> list = new ArrayList<>();


    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //给适配器设置内容
            switch (msg.what) {
                case Constant.FINISHED:
                    List<NewslistBean> newslistBeen = (List<NewslistBean>) msg.obj;
                    e(TAG, "handleMessage:" + "NewslistBean"+newslistBeen);
                    iosAdapter.onReference(newslistBeen);
                    break;
                case Constant.LOADMORE:
                    //数据加载更多
                    List<NewslistBean> addlistBean= (List<NewslistBean>) msg.obj;
                    e(TAG, "handleMessage:" + "NewslistBean"+addlistBean);
                    iosAdapter.addOnReference(addlistBean);
                    break;
                case Constant.ERROR:
                    break;
                default:
                    break;
            }

        }
    };

    /**
     * 获取当前类的对象
     * @return
     */
    public static CateIOSFragment newInstance(){
        CateIOSFragment fragment = new CateIOSFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fragment",CateIOSFragment.class.getSimpleName());
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycleview,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerview = (YRecycleview) this.getView().findViewById(R.id.yrecycle_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置默认动画
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(),2));

        //内容的适配器
        iosAdapter = new CateIOSAdapter(getActivity());
        recyclerview.setAdapter(iosAdapter);

        //设置点击事件
        iosAdapter.setOnItemClickLitener(new CateIOSAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(NewslistBean news, int position) {
                e(TAG, "onItemClick" + "news:"+news.toString()+"position:"+position);
            }
        });

        //获取默认参数设置
        GetParams params_map = new GetParams();
        params_map.addToParams_map("word","ios");
        params_map.addToParams_map("num","20");
        //获取网络数据
        HttpUtil.doGet(mhandler, Constant.URL, Constant.Keji, params_map.getParams_map());

        recyclerview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                i(TAG, "onRefresh" + "下拉刷新");
                //设置刷新完成
                recyclerview.setReFreshComplete();
                //获取默认参数设置
                GetParams params_map = new GetParams();
                params_map.addToParams_map("word","ios");
                params_map.addToParams_map("num","20");
                //获取网络数据
                HttpUtil.doGet(mhandler, Constant.URL, Constant.Keji, params_map.getParams_map());
            }

            @Override
            public void onLoadMore() {
                i(TAG, "onLoadMore" + "加载更多");
                //加载更多
                recyclerview.setloadMoreComplete();

                int page= (iosAdapter.getItemCount())/20+1;

                e(TAG, "onLoadMore" + "page:"+page);
                //获取默认参数设置
                GetParams params_map = new GetParams();
                params_map.addToParams_map("word","ios");
                params_map.addToParams_map("num","20");
                params_map.addToParams_map("page",page+"");
                //获取网络数据
                HttpUtil.doGet(mhandler, Constant.URL, Constant.Keji, params_map.getParams_map(),false);
            }
        });
    }


    @Override
    public View getScrollableView() {
        return recyclerview;
    }
}