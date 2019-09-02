package com.fellowcode.animewatcher.Utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fellowcode.animewatcher.Fragments.MainPageFragment;
import com.fellowcode.animewatcher.Fragments.TranslationsPageFragment;
import com.fellowcode.animewatcher.R;

public class TranslationsPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 3;
    private Context context;
    private int[] tabTitles = new int[]{R.string.voice, R.string.sub, R.string.raw};


    public TranslationsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override public int getCount() {
        return PAGE_COUNT;
    }

    @Override public Fragment getItem(int position) {
        return TranslationsPageFragment.newInstance(position);
    }
    @Override public CharSequence getPageTitle(int position) {
        // генерируем заголовок в зависимости от позиции
        return context.getString(tabTitles[position]);
    }
}
