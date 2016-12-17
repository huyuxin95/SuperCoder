package com.jju.yuxin.supercoder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.adapter.CateAndroidAdapter;
import com.jju.yuxin.supercoder.adapter.VedioAdapter;
import com.jju.yuxin.supercoder.bean.NewslistBean;
import com.jju.yuxin.supercoder.bean.VedioInfoBean;
import com.jju.yuxin.supercoder.http.GetParams;
import com.jju.yuxin.supercoder.http.HttpUtil;
import com.jju.yuxin.supercoder.utils.Constant;
import com.jju.yuxin.supercoder.utils.JsoupUtils;
import com.jju.yuxin.supercoder.view.YRecycleview;

import java.util.List;

import javax.security.auth.login.LoginException;

import static android.util.Log.e;
import static android.util.Log.i;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname viewpager.lingdian.com.viewpagerdouble.activity
 * Created by yuxin.
 * Created time 2016/12/8 0008 下午 9:38.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */
public class VedioActivity extends Activity {

    private static final String TAG = VedioActivity.class.getSimpleName();

    //视频新闻爬取地址
    private static final String path = "http://www.jxntv.cn/";

    //加载完成的新闻集合
    private List<VedioInfoBean> vedioinfos;

    private YRecycleview lv_vedio_news;

    private VedioAdapter vedioAdapter;


    Handler mhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //加载成功
                case Constant.SUCCESS:
                    vedioinfos = (List<VedioInfoBean>) msg.obj;
                    vedioAdapter.onReference(vedioinfos);
                    break;
                //加载失败
                case Constant.ERROR:
                    Exception e = (Exception) msg.obj;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio);
        e(TAG, "onCreate" + "");
        //新闻列表
        lv_vedio_news = (YRecycleview) findViewById(R.id.yrecycle_view_vedio);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_vedio_news.setLayoutManager(layoutManager);
        lv_vedio_news.setItemAnimator(new DefaultItemAnimator());

        //内容的适配器
        vedioAdapter = new VedioAdapter(this);
        lv_vedio_news.setAdapter(vedioAdapter);

        //Item点击事件
        vedioAdapter.setOnItemClickLitener(new VedioAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(VedioInfoBean news, int position) {
                e(TAG, "onItemClick" + "news:" + news.toString() + "position" + position);

                Log.e(TAG, "onItemClick" + "position:" + position);
                Intent intent = new Intent(VedioActivity.this, VedioNewsDetailsActivity.class);
                intent.putExtra("vedio_news", vedioinfos.get(position));
                startActivity(intent);
            }
        });

        //获取新闻
        JsoupUtils.getNewPaper(path, mhandler);

        lv_vedio_news.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                i(TAG, "onRefresh" + "下拉刷新");
                //设置刷新完成
                lv_vedio_news.setReFreshComplete();
                JsoupUtils.getNewPaper(path, mhandler);

            }

            @Override
            public void onLoadMore() {
                i(TAG, "onLoadMore" + "加载更多");
                //加载更多
                lv_vedio_news.setloadMoreComplete();
            }
        });
    }
}
