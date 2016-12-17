package com.jju.yuxin.supercoder.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.supercoder.view
 * Created by yuxin.
 * Created time 2016/12/16 0016 下午 5:06.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */

public class YVideoView extends VideoView {
    public YVideoView(Context context) {
        super(context);
    }

    public YVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //我们重新计算高度
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
