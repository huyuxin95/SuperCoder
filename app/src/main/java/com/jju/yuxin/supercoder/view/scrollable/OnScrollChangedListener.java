package com.jju.yuxin.supercoder.view.scrollable;

/**
 *=============================================================================
 *
 * Copyright (c) 2016 Dimitry Ivanov rights reserved.
 * ClassName OnScrollChangedListener
 * Created by Dimitry Ivanov.
 * Created time 28.03.2015.
 * Describe : 第三方实现滚动监听
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public interface OnScrollChangedListener {

    /**
     * This method will be invoked when scroll state
     * of {@link ru.noties.scrollable.ScrollableLayout} has changed.
     * @see ru.noties.scrollable.ScrollableLayout#setOnScrollChangedListener(OnScrollChangedListener)
     * @param y current scroll y
     * @param oldY previous scroll y
     * @param maxY maximum scroll y (helpful for calculating scroll ratio for e.g. for alpha to be applied)
     */
    void onScrollChanged(int y, int oldY, int maxY);
}
