package com.fellowcode.animewatcher.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.fellowcode.animewatcher.Anime.AnimeItemDecoration;
import com.fellowcode.animewatcher.Anime.AnimeList;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.User.Rate;
import com.fellowcode.animewatcher.Utils.NavButtons;

import java.util.ArrayList;

public class UserAnimeListActivity extends AppCompatActivity {

    Toolbar toolbar;

    View listEmpty;
    View progressBar;

    Api api;
    RecyclerView recyclerView;
    AnimeList animeList;
    Context context = this;

    ArrayList<Integer> animeIds;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_anime_list);

        listEmpty = findViewById(R.id.list_empty);
        progressBar = findViewById(R.id.loader);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new NavButtons(this);

        recyclerView = findViewById(R.id.recyclerView);


        api = new Api(context);

        String status = getIntent().getStringExtra("status");
        setTitle(Rate.getTitle(status));

        animeIds = getIntent().getIntegerArrayListExtra("animeIds");

        animeList = new AnimeList(context, api, recyclerView);


        setupList();
    }

    void setupList(){
        animeList.clear();
        if(animeIds.size()>0) {
            progressBar.setVisibility(View.VISIBLE);
            listEmpty.setVisibility(View.INVISIBLE);
            animeList.setRequest(new AnimeList.Request() {
                @Override
                public Link getUrl() {
                    return new Link().shiki()
                            .animes()
                            .addField("ids", animeIds)
                            .offset(animeList.size());
                }

                @Override
                public void onResponse(String response) {
                    Log.d("response", response);
                    progressBar.setVisibility(View.INVISIBLE);

                }
            }).loadAnimes();
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            listEmpty.setVisibility(View.VISIBLE);
        }
    }
}
