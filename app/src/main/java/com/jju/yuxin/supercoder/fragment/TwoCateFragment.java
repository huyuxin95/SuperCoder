package com.jju.yuxin.supercoder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.adapter.TwoAdapter;
import com.jju.yuxin.supercoder.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;



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
public class TwoCateFragment extends ScrollAbleFragment {

    private static final String TAG =TwoCateFragment.class.getSimpleName();
    private YRecycleview recyclerview;
    private TwoAdapter twoAdapter;
    private List<String> list = new ArrayList<>();

    /**
     * 获取当前类的对象
     * @return
     */
    public static TwoCateFragment newInstance(){
        TwoCateFragment fragment = new TwoCateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fragment",TwoCateFragment.class.getSimpleName());
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        //内容的适配器
        twoAdapter = new TwoAdapter(getActivity());
        recyclerview.setAdapter(twoAdapter);
        recyclerview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                i(TAG, "onRefresh" + "下拉刷新");
                recyclerview.setReFreshComplete();
            }

            @Override
            public void onLoadMore() {
                i(TAG, "onLoadMore" + "加载更多");
                recyclerview.setloadMoreComplete();
            }
        });

        list.add("http://p1.so.qhmsg.com/sdr/720_1080_/t01de5bf40a00aaea9b.jpg");
        list.add("http://p4.so.qhmsg.com/sdr/719_1080_/t01d7c0c03443ec2129.jpg");
        list.add("http://p4.so.qhmsg.com/sdr/720_1080_/t01d813a45c64085e3e.jpg");
        list.add("http://p1.so.qhmsg.com/sdr/720_1080_/t01aa0918403910e501.jpg");
        list.add("http://p4.so.qhmsg.com/sdr/719_1080_/t010f476c4002d7d0f9.jpg");
        list.add("http://p2.so.qhmsg.com/sdr/720_1080_/t01606b07ab9f4ee40c.jpg");
        list.add("http://p3.so.qhmsg.com/sdr/720_1080_/t01025973f4da6e16c8.jpg");
        list.add("http://p0.so.qhmsg.com/sdr/1620_1080_/t0122e43c9de3d54dc5.jpg");
        twoAdapter.onReference(list);

    }


    @Override
    public View getScrollableView() {
        return recyclerview;
    }
}
