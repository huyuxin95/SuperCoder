package com.jju.yuxin.supercoder.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.adapter.CateAndroidAdapter;
import com.jju.yuxin.supercoder.adapter.CateWinAdapter;
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
public class CateWinFragment extends ScrollAbleFragment {

    private static final String TAG =CateWinFragment.class.getSimpleName();
    private YRecycleview recyclerview;
    private CateWinAdapter winAdapter;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //给适配器设置内容
            switch (msg.what) {
                case Constant.FINISHED:
                    List<NewslistBean> newslistBeen = (List<NewslistBean>) msg.obj;
                    Log.e(TAG, "handleMessage:" + "NewslistBean"+newslistBeen);
                    winAdapter.onReference(newslistBeen);
                    break;
                case Constant.LOADMORE:
                    //数据加载更多
                    List<NewslistBean> addlistBean= (List<NewslistBean>) msg.obj;
                    e(TAG, "handleMessage:" + "NewslistBean"+addlistBean);
                    winAdapter.addOnReference(addlistBean);
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
    public static CateWinFragment newInstance(){
        CateWinFragment fragment = new CateWinFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fragment",CateWinFragment.class.getSimpleName());
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


        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        //内容的适配器
        winAdapter = new CateWinAdapter(getActivity());
        recyclerview.setAdapter(winAdapter);

        //Item点击事件
        winAdapter.setOnItemClickLitener(new CateWinAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(NewslistBean news, int position) {
                e(TAG, "onItemClick" + "news:"+news.toString()+"position"+position);
            }
        });

        //获取默认参数设置
        GetParams params_map = new GetParams();
        params_map.addToParams_map("word","win10");
        params_map.addToParams_map("num","30");
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
                params_map.addToParams_map("word","win10");
                params_map.addToParams_map("num","30");
                //获取网络数据
                HttpUtil.doGet(mhandler, Constant.URL, Constant.Keji, params_map.getParams_map());
            }

            @Override
            public void onLoadMore() {
                i(TAG, "onLoadMore" + "加载更多");
                //加载更多
                recyclerview.setloadMoreComplete();

                int page= (winAdapter.getItemCount())/30+1;

                e(TAG, "onLoadMore" + "page:"+page);
                //获取默认参数设置
                GetParams params_map = new GetParams();
                params_map.addToParams_map("word","win10");
                params_map.addToParams_map("num","30");
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
