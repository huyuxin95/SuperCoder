package com.jju.yuxin.supercoder.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.supercoder.view
 * Created by yuxin.
 * Created time 2016/12/16 0016 下午 1:27.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */

public class YListView extends ListView {
    public YListView(Context context) {
        super(context);
    }

    public YListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 只重写该方法，达到使ListView适应ScrollView的效果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);

    }

}
