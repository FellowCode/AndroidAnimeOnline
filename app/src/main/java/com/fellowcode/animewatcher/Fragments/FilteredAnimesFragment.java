package com.fellowcode.animewatcher.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Anime.AnimeItemDecoration;
import com.fellowcode.animewatcher.Anime.AnimeList;
import com.fellowcode.animewatcher.Anime.AnimeListRequest;

public class FilteredAnimesFragment extends Fragment {

    int ANIMES_LIMIT = 20;

    RecyclerView recyclerView;

    AnimeList animeList;

    Api api;

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        api = new Api(getContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new AnimeItemDecoration(15));
        animeList = new AnimeList().setContext(getContext()).setApi(api).setRecyclerView(recyclerView);

        return view;
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
