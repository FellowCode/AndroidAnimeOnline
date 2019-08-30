package com.fellowcode.animewatcher.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

public class VideoWebView extends WebView {
    public VideoWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public VideoWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoWebView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();


        //force a 16:9 aspect ratio
        int height = Math.round(width / 1.77778f) + 100;
        setMeasuredDimension(width, height);
    }
}
