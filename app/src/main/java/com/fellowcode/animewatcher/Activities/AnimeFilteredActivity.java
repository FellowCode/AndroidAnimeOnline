package com.fellowcode.animewatcher.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.fellowcode.animewatcher.Anime.AnimeItemDecoration;
import com.fellowcode.animewatcher.Anime.AnimeList;
import com.fellowcode.animewatcher.Anime.AnimeListRequest;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.NavButtons;

public class AnimeFilteredActivity extends AppCompatActivity {

    Toolbar toolbar;

    RecyclerView recyclerView;

    Api api;
    AnimeList animeList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_anime);

        api = new Api(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.filter);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new AnimeItemDecoration(15));

        animeList = new AnimeList(this, api, recyclerView);

        NavButtons navButtons = new NavButtons(this);
        navButtons.select(NavButtons.FILTER);

        SetupFilter((Link)getIntent().getSerializableExtra("filterLink"));
    }

    public void SetupFilter(final Link link){
        animeList.clear();
        link.animes();
        animeList.setRequest(new AnimeListRequest() {
            @Override
            public Link getUrl() {
                Log.d("request", "ReqFilter");
                return link.offset(animeList.size());
            }

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
            }
        }).loadAnimes();
    }
}
