package com.fellowcode.animewatcher.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageFixed extends android.support.v7.widget.AppCompatImageView {
    public ImageFixed(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public ImageFixed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageFixed(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();

        //force a 16:9 aspect ratio
        int height = Math.round(width / .71428f);
        setMeasuredDimension(width, height);
    }
}
