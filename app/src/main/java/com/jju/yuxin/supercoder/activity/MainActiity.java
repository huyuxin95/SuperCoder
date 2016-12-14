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
 * Describe :
 * History:
 * ==============================================================================
 */

public class MainActiity extends Activity {
    Context context = null;
    LocalActivityManager manager = null;
    ViewPager pager = null;

    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private RadioGroup rg_bm_bar;
    private RadioButton rb_home;
    private RadioButton rb_video;
    private RadioButton rb_care;
    private RadioButton rb_mine;
    private static final int HomePager = 0;
    private static final int VedioPager = 1;
    private static final int CarePager = 2;
    private static final int MinePager = 3;


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
        rb_care = (RadioButton) findViewById(R.id.rb_care);
        rb_mine = (RadioButton) findViewById(R.id.rb_mine);

        final ArrayList<View> list = new ArrayList<View>();

        Intent intent = new Intent(context, HomeActivity.class);
        //将activity转换成view对象存放到List集合
        list.add(getView("Home", intent));
        Intent intent2 = new Intent(context, VedioActivity.class);
        list.add(getView("Vedio", intent2));
        Intent intent3 = new Intent(context, CareActivity.class);
        list.add(getView("Care", intent3));
        Intent intent4 = new Intent(context, MineActivity.class);
        list.add(getView("Mine", intent4));

        //适配器
        pager.setAdapter(new MainPagerAdapter(list));
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
                    case R.id.rb_care:
                        //关注
                        pager.setCurrentItem(CarePager);
                        break;
                    case R.id.rb_mine:
                        //我的
                        pager.setCurrentItem(MinePager);
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

        public MainPagerAdapter(ArrayList<View> list) {
            this.list = list;
        }

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
                case CarePager:
                    rb_care.setChecked(true);
                    break;
                case MinePager:
                    rb_mine.setChecked(true);
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
