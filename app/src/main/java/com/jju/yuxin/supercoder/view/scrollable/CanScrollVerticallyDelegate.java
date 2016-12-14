package com.jju.yuxin.supercoder.view.scrollable;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName CanScrollVerticallyDelegate
 * Created by Dimitry Ivanov .
 * Created time 28.03.2015.
 * Describe :
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public interface CanScrollVerticallyDelegate {

    /**
     * @see android.view.View#canScrollVertically(int)
     */
    boolean canScrollVertically(int direction);
}
