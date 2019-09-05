package com.fellowcode.animewatcher.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fellowcode.animewatcher.Activities.FavoritesActivity;
import com.fellowcode.animewatcher.Activities.FiltersListActivity;
import com.fellowcode.animewatcher.Activities.MainActivity;
import com.fellowcode.animewatcher.Activities.ProfileActivity;
import com.fellowcode.animewatcher.Activities.SearchActivity;
import com.fellowcode.animewatcher.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NavButtons {

    LinearLayout homeBtn, profileBtn, favoritesBtn, searchBtn, filterBtn;

    AppCompatActivity activity;

    public static int HOME = 0, PROFILE = 1, FAVORITES = 2, SEARCH = 3, FILTER = 4;

    Map<Integer, LinearLayout> btns = new HashMap<>();

    public NavButtons(final AppCompatActivity activity){
        this.activity = activity;
        homeBtn = activity.findViewById(R.id.home_btn);
        profileBtn = activity.findViewById(R.id.profile_btn);
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

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                activity.startActivity(intent);
            }
        });

        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FavoritesActivity.class);
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
        btns.put(PROFILE, profileBtn);
        btns.put(FAVORITES, favoritesBtn);
        btns.put(SEARCH, searchBtn);
        btns.put(FILTER, filterBtn);
    }

    public NavButtons select(int btn){

        setColor(R.color.selectIcon, btns.get(btn));
        return this;
    }

    public NavButtons clearClick(int btn){
        setOnClick(btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return this;
    }


    private NavButtons setColor(int color_res, View btn){
        ImageView icon = ViewUtils.getChildImageView(btn);
        if (icon != null)
            icon.setColorFilter(ContextCompat.getColor(activity, color_res));
        return this;
    }

    public NavButtons setOnClick(int btn, View.OnClickListener listener){
        Objects.requireNonNull(btns.get(btn)).setOnClickListener(listener);
        return this;
    }

}
