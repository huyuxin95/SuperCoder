package com.jju.yuxin.supercoder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.activity.NewsDetilActivity;
import com.jju.yuxin.supercoder.adapter.OneAdapter;
import com.jju.yuxin.supercoder.adapter.YundongAdapter;
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
 * =============================================================================
 * <p>
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName OneFragment
 * Created by yuxin.
 * Created time 12-12-2016 14:11.
 * Describe :运动fragment
 * History:
 * Version   1.0.
 * <p>
 * ==============================================================================
 */
public class YundongFragment extends Fragment {

    private static final String TAG = YundongFragment.class.getSimpleName();
    private YRecycleview recyclerview;
    private YundongAdapter ydAdapter;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //给适配器设置内容
            switch (msg.what) {
                case Constant.FINISHED:
                    List<NewslistBean> newslistBeen = (List<NewslistBean>) msg.obj;
                    e(TAG, "handleMessage:" + "NewslistBean" + newslistBeen);
                    ydAdapter.onReference(newslistBeen);
                    break;
                case Constant.LOADMORE:
                    //数据加载更多
                    List<NewslistBean> addlistBean= (List<NewslistBean>) msg.obj;
                    e(TAG, "handleMessage:" + "NewslistBean"+addlistBean);
                    ydAdapter.addOnReference(addlistBean);
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
     *
     * @return
     */
    public static YundongFragment newInstance() {
        YundongFragment fragment = new YundongFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fragment", YundongFragment.class.getSimpleName());
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
        View view = inflater.inflate(R.layout.fragment_recycleview, container, false);
        return view;
    }

    /**
     * 控件及数据的初始化
     *
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

        recyclerview.setItemAnimator(new DefaultItemAnimator());
        //设置适配器
        ydAdapter = new YundongAdapter(getActivity());

        recyclerview.setAdapter(ydAdapter);

        ydAdapter.setOnItemClickLitener(new YundongAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(NewslistBean news, int position) {
                e(TAG, "onItemClick" + "news:" + news + "position" + position);

                Intent intent = new Intent(getActivity(), NewsDetilActivity.class);
                intent.putExtra("news", news);
                startActivity(intent);
            }
        });

        //获取默认参数设置
        GetParams params_map = new GetParams();
        //获取网络数据
        HttpUtil.doGet(mhandler, Constant.URL, Constant.Tiyu, params_map.getParams_map());

        recyclerview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                i(TAG, "onRefresh" + "下拉刷新");
                //设置刷新完成
                recyclerview.setReFreshComplete();
                //获取默认参数设置
                GetParams params_map = new GetParams();
                //获取网络数据
                HttpUtil.doGet(mhandler, Constant.URL, Constant.Tiyu, params_map.getParams_map());
            }

            @Override
            public void onLoadMore() {
                i(TAG, "onLoadMore" + "加载更多");
                //加载更多
                recyclerview.setloadMoreComplete();

                int page= (ydAdapter.getItemCount())/Constant.DEFAULT_COUNT+1;

                e(TAG, "onLoadMore" + "page:"+page);
                //获取默认参数设置
                GetParams params_map = new GetParams();
                params_map.addToParams_map("page",page+"");
                //获取网络数据
                HttpUtil.doGet(mhandler, Constant.URL, Constant.Tiyu, params_map.getParams_map(),false);
            }
        });
    }
}
