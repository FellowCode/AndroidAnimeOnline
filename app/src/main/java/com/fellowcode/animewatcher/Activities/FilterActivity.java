package com.fellowcode.animewatcher.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.Fragments.FilteredAnimesFragment;
import com.fellowcode.animewatcher.Fragments.FiltersFragment;
import com.fellowcode.animewatcher.R;

public class FilterActivity extends AppCompatActivity {

    FiltersFragment filtersFragment = new FiltersFragment();
    FilteredAnimesFragment filteredAnimesFragment = new FilteredAnimesFragment();

    boolean filtersListIsOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.filter);

        OpenFiltersList();
    }


    void OpenFiltersList(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_filter, filtersFragment).commit();
        filtersListIsOpen = true;
    }

    public void doFilter(Link link){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_filter, filteredAnimesFragment).commit();
        getSupportFragmentManager().executePendingTransactions();
        filteredAnimesFragment.SetupFilter(link);
        filtersListIsOpen = false;
    }

    public void filterBtnClick(View v){
        if (!filtersListIsOpen)
            OpenFiltersList();
        else
            filtersFragment.doFilter();
    }

}
