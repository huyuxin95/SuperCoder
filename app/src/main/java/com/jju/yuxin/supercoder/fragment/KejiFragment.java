package com.jju.yuxin.supercoder.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.view.SlidingTabLayout;
import com.jju.yuxin.supercoder.view.scrollable.ScrollableLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * =============================================================================
 * <p>
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName KejiFragment
 * Created by yuxin.
 * Created time 13-12-2016 09:22.
 * Describe :含有第三层Viewpager的---科技Fragment
 * History:
 * Version   1.0.
 * <p>
 * ==============================================================================
 */
public class KejiFragment extends Fragment {

    private ImageView iv_top;
    private ViewPager view_pager;
    private ScrollableLayout mScrollLayout;
    private SlidingTabLayout mSlidingTabLayout;
    private List<String> titles = new ArrayList<>();

    /**
     * 获取当前类的对象
     *
     * @return
     */
    public static KejiFragment newInstance() {
        KejiFragment fragment = new KejiFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fragment", KejiFragment.class.getSimpleName());
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
        View view = inflater.inflate(R.layout.fragment_two, container, false);
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

        mScrollLayout = (ScrollableLayout) this.getView().findViewById(R.id.scrollableLayout);
        //顶部填充图片,因为为双重Viewpager的直接嵌套,需要留出一块区域用于完成Viewpager的tab切换
        iv_top = (ImageView) this.getView().findViewById(R.id.iv_top);
        //顶部可滑动的tablayout
        mSlidingTabLayout = (SlidingTabLayout) this.getView().findViewById(R.id.tab_layout);
        //第三层的ViewPager
        view_pager = (ViewPager) this.getView().findViewById(R.id.two_view_pager);

        //给顶部填充图片设置layoutparams
        RelativeLayout.LayoutParams linearParams =
                (RelativeLayout.LayoutParams) iv_top.getLayoutParams();
        //设置图片宽度填充屏幕大小
        linearParams.width = getScreenMaxWidth(0);
        //高度为屏幕大小的1/3
        int maxHeight = (int) (getScreenMaxWidth(0) / 3);
        linearParams.height = maxHeight;

        iv_top.setLayoutParams(linearParams);

        //设置图片填充颜色为白色
        iv_top.setBackgroundColor(Color.parseColor("#fefefe"));
        mScrollLayout.setClickHeadExpand(maxHeight);
        init();
    }

    /**
     * 数据初始化
     */
    public void init() {
        //添加二级子标题
        titles.add("Android");
        titles.add("IOS");
        titles.add("Win10");
        titles.add("手机");
        titles.add("平板");
        titles.add("电脑");
        titles.add("游戏");
        titles.add("VR");

        //用集合管理可滚动的Fragment.用于填充三级Viewpager
        final ArrayList<ScrollAbleFragment> fragmentList = new ArrayList<>();

        //循环的添加第三层fragment
        fragmentList.add(CateAndroidFragment.newInstance());//andropid
        fragmentList.add(CateIOSFragment.newInstance()); //IOS
        fragmentList.add(CateWinFragment.newInstance());  //WIN
        fragmentList.add(CateShoujiFragment.newInstance());//手机
        fragmentList.add(CatePadFragment.newInstance()); //平板
        fragmentList.add(CatePCFragment.newInstance()); //电脑
        fragmentList.add(CateGameFragment.newInstance()); // 游戏
        fragmentList.add(CateVRFragment.newInstance());  //VR


        //获取第三层ViewPager的标题的适配器
        final MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        view_pager.setAdapter(adapter);
        //滚动模式
        view_pager.setOverScrollMode(ViewPager.OVER_SCROLL_NEVER);
        //设置Viewpager的缓存大小
        view_pager.setOffscreenPageLimit(titles.size());
        //设置初始化的第一个item位置
        view_pager.setCurrentItem(0);
        //可滚动的Tablayout关联Viewpager
        mSlidingTabLayout.setViewPager(view_pager);
        //设置当前可滚动的容器填充第一个fragment
        mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(0));

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //设置当前可滚动的容器填充指定fragment
                mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(position));
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 第三层tablayout的适配器
     */
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<ScrollAbleFragment> fragmentList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<ScrollAbleFragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        //根据Viewpager的位置返回对应的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


    /**
     * 获取到屏幕的宽度.返回值单位是像素
     *
     * @param paramInt
     * @return
     */
    public int getScreenMaxWidth(int paramInt) {
        Object localObject = new DisplayMetrics();
        try {
            //获取屏幕参数
            DisplayMetrics localDisplayMetrics =
                    getActivity().getApplicationContext().getResources().getDisplayMetrics();
            localObject = localDisplayMetrics;
            //返回屏幕宽度
            return ((DisplayMetrics) localObject).widthPixels - dip2px(getActivity(), paramInt);
        } catch (Exception localException) {
            while (true) {
                localException.printStackTrace();
                //默认宽度是640px
                ((DisplayMetrics) localObject).widthPixels = 640;
            }
        }
    }

    /**
     * dp转换为像素
     *
     * @param context
     * @param dipValue
     * @return
     */
    public int dip2px(Context context, int dipValue) {
        if (context != null) {
            float reSize = context.getResources().getDisplayMetrics().density;
            return (int) ((dipValue * reSize) + 0.5);
        }
        return dipValue;
    }
}
