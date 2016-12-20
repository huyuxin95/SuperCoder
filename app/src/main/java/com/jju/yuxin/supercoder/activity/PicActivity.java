package com.jju.yuxin.supercoder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.adapter.CatePicAdapter;
import com.jju.yuxin.supercoder.bean.NewslistBean;
import com.jju.yuxin.supercoder.bean.ResponseBean;
import com.jju.yuxin.supercoder.http.HttpNewsUtil;
import com.jju.yuxin.supercoder.http.HttpPicUtil;
import com.jju.yuxin.supercoder.utils.Constant;
import com.jju.yuxin.supercoder.utils.GetParams;
import com.jju.yuxin.supercoder.view.YRecycleview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.util.Log.e;
import static android.util.Log.i;


/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname viewpager.lingdian.com.viewpagerdouble.activity
 * Created by yuxin.
 * Created time 2016/12/8 0008 下午 9:40.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */
public class PicActivity extends Activity {

    private static final String TAG = PicActivity.class.getSimpleName();

    private CatePicAdapter picAdapter;

    private YRecycleview lv_pic;

    private Constant constant=new Constant();


    Handler mhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //加载成功
                case Constant.FINISHED:
                    List<ResponseBean> responseBeen= (List<ResponseBean>) msg.obj;
                    e(TAG, "handleMessage" + responseBeen.toString());
                    picAdapter.onReference(responseBeen,constant.getThridAdlist());
                    break;
                //加载失败
                case Constant.LOADMORE:
                    //数据加载更多
                    List<ResponseBean> addlistBean = (List<ResponseBean>) msg.obj;

                    int page = msg.arg1;

                    i(TAG, "HM" + "page:"+page);
                    //初始化一个集合用来放置即将加入的广告位置
                    List<Integer> adlists=new ArrayList<>();

                    for (int adpisition:constant.getThridAdlist()) {
                        //在原来的基础上增加
                        adlists.add(((page-1)*(Constant.DEFAULT_COUNT*2)+adpisition));
                        i(TAG, "HM"+"position" +((page-1)*Constant.DEFAULT_COUNT+adpisition));
                    }

                    picAdapter.addOnReference(addlistBean,adlists);

                    break;
                //加载失败
                case Constant.ERROR:

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        lv_pic = (YRecycleview) findViewById(R.id.yrecycle_view_pic);


        lv_pic.setLayoutManager(new StaggeredGridLayoutManager(2
                ,StaggeredGridLayoutManager.VERTICAL));

        lv_pic.setItemAnimator(new DefaultItemAnimator());

        //内容的适配器
        picAdapter = new CatePicAdapter(this);

        lv_pic.setAdapter(picAdapter);

        HashMap<String, String> pic_params = new HashMap<>();
        pic_params.put("page","1");
        HttpPicUtil.doGet(mhandler,Constant.PIC_URL,pic_params);

        //Item点击事件
        picAdapter.setOnItemClickLitener(new CatePicAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(ResponseBean news, int position) {
                e(TAG, "onItemClick" + news.toString()+"position"+position);


            }
        });

        //获取图片

        lv_pic.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                i(TAG, "onRefresh" + "下拉刷新");
                //设置刷新完成
                HashMap<String, String> pic_params = new HashMap<>();
                pic_params.put("page","1");
                HttpPicUtil.doGet(mhandler,Constant.PIC_URL,pic_params);
            }

            @Override
            public void onLoadMore() {
                i(TAG, "onLoadMore" + "加载更多");
                //加载更多
                lv_pic.setloadMoreComplete();

                int page= (picAdapter.getItemCount())/20+1;

                e(TAG, "onLoadMore" + "page:"+page);
                //获取默认参数设置

                HashMap<String, String> pic_params = new HashMap<>();
                pic_params.put("page",page+"");
                //获取网络数据
                HttpPicUtil.doGet(mhandler,Constant.PIC_URL,pic_params);

            }
        });
    }
}
