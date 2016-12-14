package com.jju.yuxin.supercoder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.adapter.OneAdapter;
import com.jju.yuxin.supercoder.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;



import static android.util.Log.i;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName OneFragment
 * Created by yuxin.
 * Created time 12-12-2016 14:11.
 * Describe :其中第一个fragment
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class OneFragment extends Fragment {

    private static final String TAG =OneFragment.class.getSimpleName();
    private YRecycleview recyclerview;
    private OneAdapter oneAdapter;
    private List<String> list = new ArrayList<>();

    /**
     * 获取当前类的对象
     * @return
     */
    public static OneFragment newInstance(){
        OneFragment fragment = new OneFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fragment","OneFragment");
        return fragment;
    }


    /**
     * 设置当前fragment的布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycleview,container,false);
        return view;
    }

    /**
     * 控件及数据的初始化
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取到带下拉刷新和上拉加载的recycleview
        recyclerview = (YRecycleview) this.getView().findViewById(R.id.yrecycle_view);
        
        LinearLayoutManager ll_manager = new LinearLayoutManager(getActivity());
        ll_manager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置RecycleView的方向为垂直方向
        recyclerview.setLayoutManager(ll_manager);
        //设置适配器
        oneAdapter = new OneAdapter(getActivity());

        recyclerview.setAdapter(oneAdapter);

        recyclerview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {

                i(TAG, "onRefresh" + "下拉刷新");
                //设置刷新完成
                recyclerview.setReFreshComplete();
            }

            @Override
            public void onLoadMore() {
                i(TAG, "onLoadMore" + "加载更多");
                //加载更多
                recyclerview.setloadMoreComplete();
            }
        });

        list.add("http://p3.so.qhmsg.com/sdr/720_1080_/t01d4b941b38b7cca7d.jpg");
        list.add("http://p0.so.qhmsg.com/sdr/720_1080_/t01c2345b0a461928af.jpg");
        list.add("http://p0.so.qhmsg.com/sdr/720_1080_/t0102e22db3f17f755a.jpg");
        list.add("http://p3.so.qhmsg.com/sdr/1620_1080_/t018650cc445938d97d.jpg");
        list.add("http://p3.so.qhmsg.com/sdr/719_1080_/t016027774780be365d.jpg");
        list.add("http://p1.so.qhmsg.com/sdr/719_1080_/t0100f16f9b1a9174fb.jpg");
        list.add("http://p2.so.qhmsg.com/sdr/1619_1080_/t019d150016e25a4ba2.jpg");
        list.add("http://p2.so.qhmsg.com/sdr/720_1080_/t01f1ad79da36f90057.jpg");

        //给适配器设置内容
        oneAdapter.onReference(list);

    }
}
