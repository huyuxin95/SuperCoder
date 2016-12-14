package com.jju.yuxin.supercoder.view;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jju.yuxin.supercoder.R;


/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName WaitingView
 * Created by yuxin.
 * Created time 13-12-2016 17:09.
 * Describe 加载动画
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class WaitingView extends RelativeLayout {

	public WaitingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.waiting_view, this);
		AnimationDrawable mSpalshDrawable;
		ImageView loading_load_ImageView;
		loading_load_ImageView=(ImageView) findViewById(R.id.waiting_imageview);
		loading_load_ImageView.setBackgroundResource(R.drawable.refresh_animation);
		mSpalshDrawable=(AnimationDrawable) loading_load_ImageView.getBackground();
		mSpalshDrawable.start();
	}

	public WaitingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.waiting_view, this);
		AnimationDrawable mSpalshDrawable;
		ImageView loading_load_ImageView;
		loading_load_ImageView=(ImageView) findViewById(R.id.waiting_imageview);
		loading_load_ImageView.setBackgroundResource(R.drawable.refresh_animation);
		mSpalshDrawable=(AnimationDrawable) loading_load_ImageView.getBackground();
		mSpalshDrawable.start();
	}

	public WaitingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.waiting_view, this);
		AnimationDrawable mSpalshDrawable;
		ImageView loading_load_ImageView;
		loading_load_ImageView=(ImageView) findViewById(R.id.waiting_imageview);
		loading_load_ImageView.setBackgroundResource(R.drawable.refresh_animation);
		mSpalshDrawable=(AnimationDrawable) loading_load_ImageView.getBackground();
		mSpalshDrawable.start();
	}
	
	
	
}

