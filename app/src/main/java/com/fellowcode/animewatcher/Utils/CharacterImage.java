package com.fellowcode.animewatcher.Utils;

import android.content.Context;
import android.util.AttributeSet;

public class CharacterImage extends android.support.v7.widget.AppCompatImageView {
    public CharacterImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public CharacterImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CharacterImage(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight();

        int width = Math.round(height * .71428f);
        setMeasuredDimension(width, height);
    }
}
