package com.jju.yuxin.supercoder.activity;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.adapter.FragmentViewPagerAdapter;
import com.jju.yuxin.supercoder.fragment.ChuangyeFragment;
import com.jju.yuxin.supercoder.fragment.KejiFragment;
import com.jju.yuxin.supercoder.fragment.YundongFragment;
import com.jju.yuxin.supercoder.fragment.ZixunFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.util.Log.e;
import static android.util.Log.i;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName HomeActivity
 * Created by yuxin.
 * Created time 12-12-2016 09:27.
 * Describe : 首页 ,里面两层Viewpager
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class HomeActivity extends AppCompatActivity {

    private static final String TAG =HomeActivity.class.getSimpleName();
    private LinearLayout ll_one,ll_two,ll_three,ll_four;
    private ViewPager view_pager;
    private List<Fragment> fragments = null;
    //导航栏移动的图片
    private ImageView cursor;
    // 动画图片宽度
    private int bitmap_width;
    // 当前页面编号
    private int currIndex = 0;
    // 动画图片偏移量
    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }


    /**
     * 布局和数据的初始化
     */
    private void init() {
        ll_one = (LinearLayout) findViewById(R.id.ll_one);
        ll_two = (LinearLayout) findViewById(R.id.ll_two);
        ll_three = (LinearLayout) findViewById(R.id.ll_three);
        ll_four = (LinearLayout) findViewById(R.id.ll_four);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        InitImageView();

        //加入四个fragment
        ZixunFragment zxFragment = ZixunFragment.newInstance(); //首页
        KejiFragment kjFragment = KejiFragment.newInstance(); //视频
        ChuangyeFragment cyFragment = ChuangyeFragment.newInstance(); //创业
        YundongFragment ydFragment = YundongFragment.newInstance(); //我
        //用fragment集合来管理
        fragments = new ArrayList<>();
        fragments.add(zxFragment);
        fragments.add(kjFragment);
        fragments.add(cyFragment);
        fragments.add(ydFragment);

        //获取fragment和viewpager关联的适配器
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), view_pager, fragments);

        //添加额外监听
        adapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener() {

            @Override
            public void onExtraPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                i(TAG, "onExtraPageScrolled" + "...position--------: " + position+"positionOffset:"+positionOffset+"-------------positionOffsetPixels"+positionOffsetPixels);
            }

            @Override
            public void onExtraPageSelected(int position) {
                i(TAG, "onExtraPageSelected" + "Extra...position: " + position);
            }

            @Override
            public void onExtraPageScrollStateChanged(int state) {
                i(TAG, "onExtraPageScrollStateChanged" + "Extra...state: " + state);
            }
        });

        //设置切换模式
        view_pager.setOverScrollMode(ViewPager.OVER_SCROLL_NEVER);
        //设置起始位置
        view_pager.setCurrentItem(0);
        //设置缓存界面为四个,防止fragment的destroy
        view_pager.setOffscreenPageLimit(4);
        //设置viewpager的切换监听
        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //单个页面图片需要移动的偏移量 距离
            int onedis = offset * 2 + bitmap_width;
            @Override
            public void onPageSelected(int position) {
                //设置页面改变下方导航条的移动补间动画
                Animation animation = new TranslateAnimation(onedis*currIndex, onedis*position, 0, 0);//显然这个比较简洁，只有一行代码。
                currIndex = position;
                //图片停在动画结束位置
                animation.setFillAfter(true);
                //动画持续时间
                animation.setDuration(300);
                cursor.startAnimation(animation);
            }


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        PagerChangerListener pagerchangerlistener=new PagerChangerListener();
        //给四个导航栏设置点击监听
        ll_one.setOnClickListener(pagerchangerlistener);
        ll_two.setOnClickListener(pagerchangerlistener);
        ll_three.setOnClickListener(pagerchangerlistener);
        ll_four.setOnClickListener(pagerchangerlistener);
    }

    /**
     * 图片的初始化
     */
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        // 获取图片宽度
        bitmap_width = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_pull).getWidth();
        //获取手机屏幕分辨率
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 获取手机分辨率宽度
        int screenW = dm.widthPixels;
        offset = (screenW / 4 - bitmap_width) / 2;
        //计算起始位置偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        // 设置动画初始位置
        cursor.setImageMatrix(matrix);
    }


    /**
     * 页面改变监听
     */
 private class PagerChangerListener implements View.OnClickListener{

     @Override
     public void onClick(View v) {
         //根据标题栏的点击.切换到指定页面
         switch (v.getId()){
             case R.id.ll_one:
                 view_pager.setCurrentItem(0);
                 break;
             case R.id.ll_two:
                 view_pager.setCurrentItem(1);
                 break;
             case R.id.ll_three:
                 view_pager.setCurrentItem(2);
                 break;
             case R.id.ll_four:
                 view_pager.setCurrentItem(3);
                 break;
         }
     }
 }

    /**
     * 返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

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

    /**
     * 双击退出函数
     */
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
