package com.fellowcode.animewatcher.Utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ViewUtils {

    public static ImageView getChildImageView(View viewGroup){
        for(int index = 0; index<((ViewGroup)viewGroup).getChildCount(); ++index) {
            View nextChild = ((ViewGroup)viewGroup).getChildAt(index);
            try{
                ImageView image = (ImageView)nextChild;
                return image;
            } catch (Exception e){}
        }
        return null;
    }
}
