package com.fellowcode.animewatcher.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.fellowcode.animewatcher.Fragments.SearchFragment;
import com.fellowcode.animewatcher.Fragments.TabFragment;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.R;
import com.mancj.materialsearchbar.MaterialSearchBar;

public class MainActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private MaterialSearchBar searchBar;

    boolean isSearch = false;

    TabFragment tabFragment = new TabFragment();
    SearchFragment searchFragment = new SearchFragment();

    public Api api;

    LinearLayout profileBtn, favoritesBtn, searchBtn, filterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.searchBar);
        searchBar.setPlaceHolder(getString(R.string.anime_list));
        searchBar.inflateMenu(R.menu.main);
        searchBar.setOnSearchActionListener(this);

        api = new Api(this);

        profileBtn = findViewById(R.id.profile_btn);
        favoritesBtn = findViewById(R.id.favorites_btn);
        searchBtn = findViewById(R.id.search_btn);
        filterBtn = findViewById(R.id.filter_btn);

        OpenTabAnimes();
    }

    /* Fragments */

    void OpenTabAnimes(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main, tabFragment).commit();
    }

    /* Search */

    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        if (text.length() > 0) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_main, searchFragment).commit();
            getSupportFragmentManager().executePendingTransactions();
            searchFragment.SetupSearchList(text.toString());
            searchBar.setPlaceHolder(getString(R.string.search) + ": " + text);
            isSearch = true;
        }
        searchBar.disableSearch();
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                OpenTabAnimes();
                break;
        }
    }

    @Override
    public void onBackPressed(){
        if (searchBar.isSearchEnabled())
            searchBar.disableSearch();
        else if (isSearch){
            isSearch = false;
            searchBar.setPlaceHolder(getString(R.string.anime_list));
            OpenTabAnimes();
        } else
            super.onBackPressed();
    }

    public void searchBtnClick(View v){
        if (searchBar.isSearchEnabled())
            onSearchConfirmed(searchBar.getText());
        else
            searchBar.enableSearch();
    }

    public void filterBtnClick(View v){
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }


}
