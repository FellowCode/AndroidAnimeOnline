package com.fellowcode.animewatcher.Activities;

import android.os.Bundle;
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
import com.fellowcode.animewatcher.Utils.NavButtons;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    View listEmpty;
    View progressBar;

    Toolbar toolbar;

    AnimeList animeList;

    Api api;

    SearchView searchView;
    MenuItem searchItem;

    boolean newSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listEmpty = findViewById(R.id.list_empty);

        NavButtons navBtns = new NavButtons(this);
        navBtns.select(NavButtons.SEARCH); // this func disabled onclick
        navBtns.setOnClick(NavButtons.SEARCH, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchItem.isActionViewExpanded()) {
                    searchItem.expandActionView();
                    searchView.requestFocusFromTouch();
                } else {
                    searchView.clearFocus();
                }
            }
        });

        api = new Api(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new AnimeItemDecoration(25));

        animeList = new AnimeList(this, api, recyclerView);

        if (getIntent().getStringExtra("query") != null)
            doSearch(getIntent().getStringExtra("query"));
        else
            newSearch = true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        searchItem = menu.findItem(R.id.action_search);

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

        if (newSearch) {
            searchItem.expandActionView();
            searchView.requestFocusFromTouch();
        }


        return super.onCreateOptionsMenu(menu);
    }

    void doSearch(final String query){
        animeList.clear();
        progressBar.setVisibility(View.VISIBLE);
        animeList.setRequest(new AnimeListRequest() {
            @Override
            public Link getUrl() {
                Log.d("request", "ReqSearch");
                return new Link().animes().addField("query", query).offset(animeList.size());
            }

            @Override
            public void onResponse(String response) {

                Log.d("response", response);
                if (animeList.size() == 0)
                    listEmpty.setVisibility(View.VISIBLE);
                else
                    listEmpty.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).loadAnimes();
        Log.d("test", "search");
        setTitle(String.format("%s: %s", getString(R.string.search), query));
    }

}
