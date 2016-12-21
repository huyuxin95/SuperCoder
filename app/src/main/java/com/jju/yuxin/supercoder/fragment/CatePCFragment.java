package com.jju.yuxin.supercoder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.activity.NewsDetilActivity;
import com.jju.yuxin.supercoder.adapter.CatePCAdapter;
import com.jju.yuxin.supercoder.bean.NewslistBean;
import com.jju.yuxin.supercoder.utils.GetParams;
import com.jju.yuxin.supercoder.http.HttpNewsUtil;
import com.jju.yuxin.supercoder.utils.Constant;
import com.jju.yuxin.supercoder.view.YRecycleview;

import java.util.List;

import static android.util.Log.e;
import static android.util.Log.i;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName CatePCFragment
 * Created by yuxin.
 * Created time 13-12-2016 10:11.
 * Describe : 第三层Viewpager,填充的可滚动的Fragment
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class CatePCFragment extends ScrollAbleFragment {

    private static final String TAG =CatePCFragment.class.getSimpleName();
    private YRecycleview recyclerview;
    private CatePCAdapter pcAdapter;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //给适配器设置内容
            switch (msg.what) {
                case Constant.FINISHED:
                    //数据请求完成
                    List<NewslistBean> newslistBeen = (List<NewslistBean>) msg.obj;
                    e(TAG, "handleMessage:" + "NewslistBean"+newslistBeen);
                    pcAdapter.onReference(newslistBeen);
                    break;
                case Constant.LOADMORE:
                    //数据加载更多
                    List<NewslistBean> addlistBean= (List<NewslistBean>) msg.obj;
                    e(TAG, "handleMessage:" + "NewslistBean"+addlistBean);
                    pcAdapter.addOnReference(addlistBean);
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
    public static CatePCFragment newInstance(){
        CatePCFragment fragment = new CatePCFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fragment",CatePCFragment.class.getSimpleName());
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
        View view = inflater.inflate(R.layout.fragment_recycleview,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerview = (YRecycleview) this.getView().findViewById(R.id.yrecycle_view);

        //设置当前Recycle的样式为线性垂直
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        //内容的适配器
        pcAdapter = new CatePCAdapter(getActivity());
        recyclerview.setAdapter(pcAdapter);

        //item点击事件
        pcAdapter.setOnItemClickLitener(new CatePCAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(NewslistBean news, int position) {
                e(TAG, "onItemClick" + "news:"+news.toString()+"position"+position);

                Intent intent = new Intent(getActivity(), NewsDetilActivity.class);
                intent.putExtra("news", news);
                startActivity(intent);

            }
        });

        //获取默认参数设置
        GetParams params_map = new GetParams();
        params_map.addToParams_map("word","电脑");
        //获取网络数据
        HttpNewsUtil.doGet(mhandler, Constant.URL, Constant.Keji, params_map.getParams_map());


        recyclerview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                i(TAG, "onRefresh" + "下拉刷新");
                //设置刷新完成
                recyclerview.setReFreshComplete();
                //获取默认参数设置
                GetParams params_map = new GetParams();
                params_map.addToParams_map("word","电脑");
                //获取网络数据
                HttpNewsUtil.doGet(mhandler, Constant.URL, Constant.Keji, params_map.getParams_map());
            }

            @Override
            public void onLoadMore() {
                i(TAG, "onLoadMore" + "加载更多");
                //加载更多
                recyclerview.setloadMoreComplete();

                int page= (pcAdapter.getItemCount())/Constant.DEFAULT_COUNT+1;

                e(TAG, "onLoadMore" + "page:"+page);
                //获取默认参数设置
                GetParams params_map = new GetParams();
                params_map.addToParams_map("word","电脑");
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
