package com.jju.yuxin.supercoder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.adapter.VedioAdapter;
import com.jju.yuxin.supercoder.bean.VedioInfoBean;
import com.jju.yuxin.supercoder.utils.Constant;
import com.jju.yuxin.supercoder.utils.JsoupUtils;
import com.jju.yuxin.supercoder.view.YRecycleview;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.util.Log.e;
import static android.util.Log.i;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname viewpager.lingdian.com.viewpagerdouble.activity
 * Created by yuxin.
 * Created time 2016/12/8 0008 下午 9:38.
 * Version   1.0;
 * Describe : 视频activity
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


    /**
     * 用来获取子线程发送来的信息
     */
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

        //视频新闻列表
        lv_vedio_news = (YRecycleview) findViewById(R.id.yrecycle_view_vedio);

        //设置为普通的线性垂直布局
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
                //查看新闻详细内容
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
                //获取最新新闻
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

    /**
     * 返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        //当按下返回键
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            //调用双击退出函数
            exitBy2Click();
            //当点击的是返回按键,那么返回true,拦截事件的传递
            return true;
        }
        //拦截按键事件,
        return false;
    }
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

}
