package com.jju.yuxin.supercoder.activity;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jju.yuxin.supercoder.R;

import java.util.ArrayList;
import java.util.List;


/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname viewpager.lingdian.com.viewpagerdouble
 * Created by yuxin.
 * Created time 2016/12/8 0008 下午 4:06.
 * Version   1.0;
 * Describe : 包含所有activity的activity
 * History:
 * ==============================================================================
 */

public class MainActiity extends Activity {
    private Context context = null; //上下文
    private LocalActivityManager manager = null; //activity管理器
    private ViewPager pager = null;
    private RadioGroup rg_bm_bar;
    private RadioButton rb_home;
    private RadioButton rb_video;
    private RadioButton rb_pic;
    private static final int HomePager = 0;
    private static final int VedioPager = 1;
    private static final int PicPager = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //上下文
        context = MainActiity.this;
        manager = new LocalActivityManager(this, true);
        //LocalActivityManager的初始化
        manager.dispatchCreate(savedInstanceState);
        initPagerViewer();
    }

    /**
     * 初始化PageViewer
     */
    private void initPagerViewer() {
        pager = (ViewPager) findViewById(R.id.vp_vp);

        //单选的实例化
        rb_home = (RadioButton) findViewById(R.id.rb_home);
        rb_video = (RadioButton) findViewById(R.id.rb_video);
        rb_pic = (RadioButton) findViewById(R.id.rb_pic);

        //存放界面的三个activity的View
        final ArrayList<View> list = new ArrayList<View>();

        Intent intent = new Intent(context, HomeActivity.class);
        //将activity转换成view对象存放到List集合
        list.add(getView("Home", intent));  //首页
        Intent intent2 = new Intent(context, VedioActivity.class);
        list.add(getView("Vedio", intent2));  //视频
        Intent intent3 = new Intent(context, PicActivity.class);
        list.add(getView("Pic", intent3));   //图片

        //适配器
        pager.setAdapter(new MainPagerAdapter(list));
        //设置初始加载的页面为0
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());

        //单选
        rg_bm_bar = (RadioGroup) findViewById(R.id.rg_bm_bar);

        //点击事件监听
        rg_bm_bar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        //首页
                        pager.setCurrentItem(HomePager);
                        break;
                    case R.id.rb_video:
                        //视频
                        pager.setCurrentItem(VedioPager);
                        break;
                    case R.id.rb_pic:
                        //图片
                        pager.setCurrentItem(PicPager);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    /**
     * 通过activity获取视图
     *
     * @param id
     * @param intent
     * @return
     */
    private View getView(String id, Intent intent) {
        //startActivity装载目标 Activity对象
        //getDecorView获取当前activity对应的view
        return manager.startActivity(id, intent).getDecorView();
    }

    /**
     * Pager适配器
     */
    public class MainPagerAdapter extends PagerAdapter {
        List<View> list = new ArrayList<View>();

        /**
         * 构造器
         * @param list
         */
        public MainPagerAdapter(ArrayList<View> list) {
            this.list = list;
        }

        /**
         * 销毁item
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewPager pViewPager = ((ViewPager) container);
            pViewPager.removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ViewPager pViewPager = ((ViewPager) arg0);
            pViewPager.addView(list.get(arg1));
            return list.get(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    /**
     * Viewpager Item切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            //将对应RadioButton置为checked
            switch (position) {
                case HomePager:
                    rb_home.setChecked(true);
                    break;
                case VedioPager:
                    rb_video.setChecked(true);
                    break;
                case PicPager:
                    rb_pic.setChecked(true);
                    break;
            }

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
