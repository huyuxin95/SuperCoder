package com.jju.yuxin.supercoder.activity;

import android.app.Activity;
import android.content.Context;
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

import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.xutils.x;

/**
 * 详情页
 * Created by qyh on 2016/3/9.
 */
public class NewsDetilActivity extends AppCompatActivity {

    private ImageView ivImage;
    // private Toolbar toolbar;
    private CollapsingToolbarLayout collapsing_toolbar;
    private ProgressBar progress;
    private WebView htNewsContent;
    private NewslistBean newsBean;

    // private Toolbar toolbar;
    // 定义一个变量，来标识是否退出


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detil);
        init();
        initdata();
    }


    protected void init() {
        ivImage = (ImageView) findViewById(R.id.ivImage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        progress = (ProgressBar) findViewById(R.id.progress);
        htNewsContent = (WebView) findViewById(R.id.htNewsContent);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setSupportActionBar(toolbar);
        // 给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //通过 NavigationDrawer 打开关闭 抽屉---返回
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();//返回上一级
            }
        });


    }

    protected void initdata() {
        hideprogress();
        newsBean = (NewslistBean) getIntent().getParcelableExtra("news");
        collapsing_toolbar.setTitle(newsBean.getTitle());
        x.image().bind((ImageView) findViewById(R.id.ivImage), newsBean.getPicUrl());
       // htNewsContent.setHtmlFromString(newsBean.getUrl(), new HtmlTextView.LocalImageGetter());
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
    }

    public void hideprogress() {
        progress.setVisibility(View.GONE);
    }

    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }



}
