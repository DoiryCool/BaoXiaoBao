package com.doiry.baoxiaobao.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class sheetListView extends ListView {
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }

    public sheetListView(Context context) {
        super(context);
    }

    public sheetListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public sheetListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
