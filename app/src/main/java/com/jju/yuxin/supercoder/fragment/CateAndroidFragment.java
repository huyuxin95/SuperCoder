package com.jju.yuxin.supercoder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.activity.NewsDetilActivity;
import com.jju.yuxin.supercoder.adapter.CateAndroidAdapter;
import com.jju.yuxin.supercoder.bean.NewslistBean;
import com.jju.yuxin.supercoder.utils.GetParams;
import com.jju.yuxin.supercoder.http.HttpNewsUtil;
import com.jju.yuxin.supercoder.utils.Constant;
import com.jju.yuxin.supercoder.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import static android.util.Log.i;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName CateAndroidFragment
 * Created by yuxin.
 * Created time 13-12-2016 10:11.
 * Describe : android 第三层Viewpager,填充的可滚动的Fragment
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class CateAndroidFragment extends ScrollAbleFragment {

    private static final String TAG =CateAndroidFragment.class.getSimpleName();
    private YRecycleview recyclerview;
    private CateAndroidAdapter androidAdapter;

    private Constant constant=new Constant();

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //给适配器设置内容
            switch (msg.what) {
                case Constant.FINISHED:
                    //数据刷新完成
                    List<NewslistBean> newslistBeen = (List<NewslistBean>) msg.obj;

                    //constant.getAdlist()初始的广告位置
                    androidAdapter.onReference(newslistBeen,constant.getThridAdlist());
                    break;
                case Constant.LOADMORE:
                    //数据加载更多
                    List<NewslistBean> addlistBean = (List<NewslistBean>) msg.obj;

                    int page = msg.arg1;

                    i(TAG, "HM" + "page:"+page);
                    //初始化一个集合用来放置即将加入的广告位置
                    List<Integer> adlists=new ArrayList<>();

                    for (int adpisition:constant.getThridAdlist()) {
                        //在原来的基础上增加
                        adlists.add(((page-1)*Constant.DEFAULT_COUNT+adpisition));
                        i(TAG, "HM"+"position" +((page-1)*Constant.DEFAULT_COUNT+adpisition));
                    }

                    androidAdapter.addOnReference(addlistBean,adlists);
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
    public static CateAndroidFragment newInstance(){
        CateAndroidFragment fragment = new CateAndroidFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fragment",CateAndroidFragment.class.getSimpleName());
        return fragment;
    }


    /**
     * 设置当前fragment的布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //当前fragment绑定的视图
        View view = inflater.inflate(R.layout.fragment_recycleview,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerview = (YRecycleview) this.getView().findViewById(R.id.yrecycle_view);

        //线性垂直布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        //内容的适配器
        androidAdapter = new CateAndroidAdapter(getActivity());
        recyclerview.setAdapter(androidAdapter);

        //Item点击事件
        androidAdapter.setOnItemClickLitener(new CateAndroidAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(NewslistBean news, int position) {
                i(TAG, "onItemClick" + "news:"+news.toString()+"position"+position);

                Intent intent = new Intent(getActivity(), NewsDetilActivity.class);
                intent.putExtra("news", news);
                startActivity(intent);
            }
        });

        //获取默认参数设置
        GetParams params_map = new GetParams();
        params_map.addToParams_map("word","Android");
        //获取网络数据
        HttpNewsUtil.doGet(mhandler, Constant.URL, Constant.Keji, params_map.getParams_map());


        recyclerview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {

                //设置刷新完成
                recyclerview.setReFreshComplete();
                //获取默认参数设置
                GetParams params_map = new GetParams();
                params_map.addToParams_map("word","Android");
                //获取网络数据
                HttpNewsUtil.doGet(mhandler, Constant.URL, Constant.Keji, params_map.getParams_map());
            }

            @Override
            public void onLoadMore() {

                //加载更多
                recyclerview.setloadMoreComplete();

                int page= (androidAdapter.getItemCount())/Constant.DEFAULT_COUNT+1;


                //获取默认参数设置
                GetParams params_map = new GetParams();
                params_map.addToParams_map("word","Android");
                params_map.addToParams_map("page",page+"");
                //获取网络数据
                HttpNewsUtil.doGet(mhandler, Constant.URL, Constant.Keji, params_map.getParams_map(),page);
            }
        });

    }


    @Override
    public View getScrollableView() {
        return recyclerview;
    }
}
