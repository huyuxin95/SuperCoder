package com.jju.yuxin.supercoder.view.scrollable;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  Dimitry Ivanov rights reserved.
 * ClassName GestureListenerAdapter
 * Created by Dimitry Ivanov.
 * Created time 28.03.2015.
 * Describe :手势监听适配器
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public abstract class GestureListenerAdapter implements GestureDetector.OnGestureListener {

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
