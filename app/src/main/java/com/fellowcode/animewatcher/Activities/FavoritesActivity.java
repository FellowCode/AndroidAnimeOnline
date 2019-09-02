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
import com.fellowcode.animewatcher.Anime.Favorites;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.NavButtons;

public class FavoritesActivity extends AppCompatActivity {

    MenuItem searchItem;
    SearchView searchView;

    RecyclerView recyclerView;

    View listEmpty;
    View progressBar;

    Favorites favorites;

    Toolbar toolbar;

    Api api;
    AnimeList animeList;

    NavButtons navButtons;

    @Override
    public void onCreate(Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.activity_favorites);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.favorites);

        listEmpty = findViewById(R.id.list_empty);
        progressBar = findViewById(R.id.loader);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new AnimeItemDecoration(25));

        favorites = new Favorites(this);

        api = new Api(this);
        animeList = new AnimeList(this, api, recyclerView);

        setupFavorites();

        navButtons = new NavButtons(this);
        navButtons.select(NavButtons.FAVORITES);
        navButtons.clearClick(NavButtons.FAVORITES);
        navButtons.setOnClick(NavButtons.SEARCH, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchItem.isActionViewExpanded()){
                    searchView.setQuery("", false);
                    searchItem.collapseActionView();
                    searchView.clearFocus();
                } else {
                    searchItem.expandActionView();
                    searchView.requestFocusFromTouch();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchItem.collapseActionView();
                doSearch(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    void doSearch(final String query){
        if(favorites.getShikiIdsList().size()>0) {
            navButtons.setOnClick(NavButtons.FAVORITES, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animeList.clearSearch();
                    setTitle(R.string.favorites);
                }
            });

            animeList.search(query);

            if (animeList.size() == 0)
                listEmpty.setVisibility(View.VISIBLE);

            setTitle(String.format("%s: %s", getString(R.string.favorites), query));
        } else {
            listEmpty.setVisibility(View.VISIBLE);
            animeList.clearSearch();
        }
        if (query.replace(" ", "").length()==0)
            animeList.clearSearch();
    }

    void setupFavorites(){
        animeList.clear();
        if(favorites.getShikiIdsList().size()>0) {
            progressBar.setVisibility(View.VISIBLE);
            listEmpty.setVisibility(View.INVISIBLE);
            animeList.setRequest(new AnimeList.Request() {
                @Override
                public Link getUrl() {
                    Log.d("request", "ReqFavorites");
                    return new Link().shiki()
                            .animes()
                            .addField("ids", favorites.getShikiIdsList())
                            .offset(animeList.size());
                }

                @Override
                public void onResponse(String response) {
                    Log.d("response", response);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }).loadAnimes();
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            listEmpty.setVisibility(View.VISIBLE);
        }
        setTitle(R.string.favorites);
    }
}
