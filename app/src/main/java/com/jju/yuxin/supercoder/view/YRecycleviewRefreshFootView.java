package com.jju.yuxin.supercoder.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jju.yuxin.supercoder.R;


/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName YRecycleviewRefreshFootView
 * Created by yuxin.
 * Created time 13-12-2016 14:55.
 * Describe :这是一个recycleview的刷新的底部布局
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class YRecycleviewRefreshFootView extends LinearLayout {

    /**
     * 加载中
     */
    public final static int STATE_LOADING = 0;
    /**
     * 加载完成
     */
    public final static int STATE_COMPLETE = 1;
    /**
     * 正常状态
     */
    public final static int STATE_NOMORE = 2;


    public YRecycleviewRefreshFootView(Context context) {
        super(context);
        initView(context);
    }

    public YRecycleviewRefreshFootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化
     */
    private void initView(Context context) {
        //设置内部内容居中
        setGravity(Gravity.CENTER);
        //设置宽高
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //底部布局
        View mContentView = View.inflate(context, R.layout.layout_footer, null);
        addView(mContentView);
    }

    /**
     * 设置当前状态
     *
     * @param state
     */
    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                this.setVisibility(View.VISIBLE);
                break;
        }

    }
}
