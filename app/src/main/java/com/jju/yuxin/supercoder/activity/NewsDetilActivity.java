package com.jju.yuxin.supercoder.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.bean.NewslistBean;

import org.xutils.x;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName NewsDetilActivity
 * Created by yuxin.
 * Created time 20-12-2016 22:17.
 * Describe :新闻详情页
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class NewsDetilActivity extends AppCompatActivity {

    private ImageView ivImage;
    // ToolbarLayout用来放置简介图片和标题
    private CollapsingToolbarLayout collapsing_toolbar;
    private ProgressBar progress;
    private WebView htNewsContent;
    private NewslistBean newsBean;
    private Toolbar toolbar;

    // private Toolbar toolbar;
    // 定义一个变量，来标识是否退出


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detil);
        init();
        initdata();
    }


    /**
     * 视图的初始化
     */
    protected void init() {
        ivImage = (ImageView) findViewById(R.id.ivImage);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        progress = (ProgressBar) findViewById(R.id.progress);
        htNewsContent = (WebView) findViewById(R.id.htNewsContent);

        //当build版本大于21
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //设置状态栏透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setSupportActionBar(toolbar);
        // 给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---返回.finish当前activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回上一级
                onBackPressed();
            }
        });


    }

    /**
     * 数据的初始化
     */
    protected void initdata() {
        //获取传送过来的对象
        newsBean = (NewslistBean) getIntent().getParcelableExtra("news");

        //设置文章标题
        collapsing_toolbar.setTitle(newsBean.getTitle());

        //绑定简介图片
        x.image().bind((ImageView) findViewById(R.id.ivImage), newsBean.getPicUrl());

        //设置webview的url
        htNewsContent.loadUrl(newsBean.getUrl());
        htNewsContent.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }
        });
        //支持js
        WebSettings settings = htNewsContent.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
    }





}
