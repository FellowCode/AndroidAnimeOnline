package com.fellowcode.animewatcher.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.PagerAdapter;
import com.mancj.materialsearchbar.MaterialSearchBar;

public class MainActivity extends AppCompatActivity {

    private MaterialSearchBar searchBar;

    boolean isSearch = false;

    public Api api;

    LinearLayout profileBtn, favoritesBtn, searchBtn, filterBtn;

    Toolbar toolbar;

    public ViewPager viewPager;
    TabLayout tabLayout;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("test", "MainActivity");

        api = new Api(this);

        profileBtn = findViewById(R.id.profile_btn);
        favoritesBtn = findViewById(R.id.favorites_btn);
        searchBtn = findViewById(R.id.search_btn);
        filterBtn = findViewById(R.id.filter_btn);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);


        // Получаем ViewPager и устанавливаем в него адаптер
        viewPager = findViewById(R.id.viewpager);
        // Передаём ViewPager в TabLayout
        tabLayout = findViewById(R.id.sliding_tabs);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                /*if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }*/
                Search(query);
                searchItem.collapseActionView();
                searchView.setQuery("", false);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    void Search(String query){
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
        Log.d("test", "aasd");
    }

    public void searchBtnClick(View v){
        Log.d("test", "searchBtnClick");
        searchView.setIconified(!searchView.isIconified());
        Search("aa");
    }

    public void filterBtnClick(View v){
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }


}
