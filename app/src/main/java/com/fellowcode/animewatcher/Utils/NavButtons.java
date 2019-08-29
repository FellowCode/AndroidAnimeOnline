package com.fellowcode.animewatcher.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fellowcode.animewatcher.Activities.FiltersListActivity;
import com.fellowcode.animewatcher.Activities.MainActivity;
import com.fellowcode.animewatcher.Activities.SearchActivity;
import com.fellowcode.animewatcher.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NavButtons {

    LinearLayout homeBtn, favoritesBtn, searchBtn, filterBtn;

    AppCompatActivity activity;

    public static int HOME = 0, FAVORITES = 1, SEARCH = 2, FILTER = 3;

    Map<Integer, LinearLayout> btns = new HashMap<>();

    public NavButtons(final AppCompatActivity activity){
        this.activity = activity;
        homeBtn = activity.findViewById(R.id.home_btn);
        favoritesBtn = activity.findViewById(R.id.favorites_btn);
        searchBtn = activity.findViewById(R.id.search_btn);
        filterBtn = activity.findViewById(R.id.filter_btn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SearchActivity.class);
                activity.startActivity(intent);
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FiltersListActivity.class);
                activity.startActivity(intent);
            }
        });

        btns.put(HOME, homeBtn);
        btns.put(FAVORITES, favoritesBtn);
        btns.put(SEARCH, searchBtn);
        btns.put(FILTER, filterBtn);
    }

    public void select(int btn){
        setColor(R.color.selectIcon, btns.get(btn));
    }


    private void setColor(int color_res, View btn){
        ImageView icon = ViewUtils.getChildImageView(btn);
        if (icon != null)
            icon.setColorFilter(ContextCompat.getColor(activity, color_res));
    }

    public void setOnClick(int btn, View.OnClickListener listener){
        Objects.requireNonNull(btns.get(btn)).setOnClickListener(listener);
    }

}
