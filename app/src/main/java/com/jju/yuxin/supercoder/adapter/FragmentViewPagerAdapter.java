package com.jju.yuxin.supercoder.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * =============================================================================
 * <p>
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName FragmentViewPagerAdapter
 * Created by yuxin.
 * Created time 12-12-2016 10:56.
 * Describe :为第二层ViewPager添加fragment
 * 绑定和处理fragments和viewpager之间的逻辑关系
 * History:
 * Version   1.0.
 * <p>
 * ==============================================================================
 */
public class FragmentViewPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private static final String TAG =FragmentViewPagerAdapter.class.getSimpleName();
    private List<Fragment> fragments; // 每个Fragment对应一个Page
    private FragmentManager fragmentManager;
    private ViewPager viewPager; // viewPager对象
    private int currentPageIndex = 0; // 当前page索引（切换之前）

    private OnExtraPageChangeListener onExtraPageChangeListener; // ViewPager切换页面时的额外功能添加接口

    /**
     * 含参构造
     *
     * @param fragmentManager
     * @param viewPager
     * @param fragments
     */
    public FragmentViewPagerAdapter(FragmentManager fragmentManager, ViewPager viewPager, List<Fragment> fragments) {
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
        this.viewPager = viewPager;
        this.viewPager.setAdapter(this);
        this.viewPager.addOnPageChangeListener(this);
    }


    //***********************PagerAdapter*****************//
    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //前面设置了Limite=4所以就不用了
        //container.removeView(fragments.get(position).getView()); // 移出viewpager两边之外的page布局
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //为View设置子布局
        Fragment fragment = fragments.get(position);
        //判断当前fragment是否已经添加到activity
        if (!fragment.isAdded()) {
            // 如果fragment还没有added..
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(fragment, fragment.getClass().getSimpleName());
            ft.commit();
            /**
             * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
             * 会在进程的主线程中，用异步的方式来执行。
             * 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
             * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
             */
            fragmentManager.executePendingTransactions();
        }
        //判断当前是否已经有了父节点
        if (fragment.getView().getParent() == null) {
            // 为viewpager增加布局
            container.addView(fragment.getView());
        }

        return fragment.getView();
    }

    //***********************OnPageChangeListener*****************//

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        //非空判断
        if (null != onExtraPageChangeListener) {
            onExtraPageChangeListener.onExtraPageScrolled(position, positionOffset, positionOffsetPixels);
            /*for(int k=0;k<fragments.size();k++){
                fragments.get(k).onPause();
            }*/
        }
    }

    @Override
    public void onPageSelected(int position) {
        fragments.get(currentPageIndex).onPause(); // 调用切换前Fargment的onPause()
        if (fragments.get(position).isAdded()) {
            fragments.get(position).onResume(); // 调用切换后Fargment的onResume()
        }
        currentPageIndex = position;
        //判断是否设置接口回调
        if (null != onExtraPageChangeListener) {
            onExtraPageChangeListener.onExtraPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //判断是否设置接口回调
        if (null != onExtraPageChangeListener) {
            onExtraPageChangeListener.onExtraPageScrollStateChanged(state);
        }
    }

    /**
     * 设置页面切换额外功能监听器
     *
     * @param onExtraPageChangeListener
     */
    public void setOnExtraPageChangeListener(OnExtraPageChangeListener onExtraPageChangeListener) {
        this.onExtraPageChangeListener = onExtraPageChangeListener;
    }

    /**
     * 接口回调
     * page切换额外功能接口
     */
    public interface OnExtraPageChangeListener {
        void onExtraPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onExtraPageSelected(int position);

        void onExtraPageScrollStateChanged(int state);
    }

    /**
     * 获取当前page索引
     *
     * @return
     */
    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    /**
     * 获取当前抛出的接口对象
     *
     * @return
     */
    public OnExtraPageChangeListener getOnExtraPageChangeListener() {
        return onExtraPageChangeListener;
    }

}
