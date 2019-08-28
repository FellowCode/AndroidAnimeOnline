package com.fellowcode.animewatcher.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.fellowcode.animewatcher.Anime.AnimeItemDecoration;
import com.fellowcode.animewatcher.Anime.AnimeList;
import com.fellowcode.animewatcher.Anime.AnimeListRequest;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.R;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    Toolbar toolbar;

    AnimeList animeList;

    Api api;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        api = new Api(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new AnimeItemDecoration(25));

        animeList = new AnimeList(this, api, recyclerView);

        doSearch(getIntent().getStringExtra("query"));
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
                searchItem.collapseActionView();
                doSearch(query);
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

    void doSearch(final String query){
        animeList.clear();
        animeList.setRequest(new AnimeListRequest() {
            @Override
            public Link getUrl() {
                Log.d("request", "ReqSearch");
                return new Link().animes().addField("query", query).offset(animeList.size());
            }

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
            }
        }).loadAnimes();
        Log.d("test", "search");
        setTitle(String.format("%s: %s", getString(R.string.search), query));
    }

    public void filterBtnClick(View v){
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }

}
