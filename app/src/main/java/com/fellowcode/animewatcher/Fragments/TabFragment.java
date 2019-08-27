package com.fellowcode.animewatcher.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.PagerAdapter;

public class TabFragment extends Fragment {

    public ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_tabanimes, container, false);
        // Получаем ViewPager и устанавливаем в него адаптер
        viewPager = view.findViewById(R.id.viewpager);
        // Передаём ViewPager в TabLayout
        tabLayout = view.findViewById(R.id.sliding_tabs);
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), getContext()));
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
