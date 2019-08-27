package com.fellowcode.animewatcher.Anime;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class AnimeItemDecoration extends RecyclerView.ItemDecoration {

    private int offset = 0;

    public AnimeItemDecoration(int offset){
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state){
        super.getItemOffsets(outRect, view, parent, state);
        int top, right, bottom, left, pos;
        top = right = bottom = left = offset;
        pos = parent.getChildAdapterPosition(view);
        if (pos%2!=0)
            left /= 2;
        else
            right /= 2;

        if (pos>1)
            top /= 2;
        if (pos < parent.getItemDecorationCount() - 2)
            bottom /= 2;

        outRect.set(left, top, right, bottom);
    }
}
