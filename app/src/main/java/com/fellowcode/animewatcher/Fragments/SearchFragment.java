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

import com.fellowcode.animewatcher.Activities.MainActivity;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Anime.AnimeItemDecoration;
import com.fellowcode.animewatcher.Anime.AnimeList;
import com.fellowcode.animewatcher.Anime.AnimeListRequest;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;

public class SearchFragment extends Fragment {

    int ANIMES_LIMIT = 20;

    AnimeList animeList;

    MainActivity mainActivity;

    Api api;

    RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        mainActivity = (MainActivity)getActivity();
        assert mainActivity != null;
        api = mainActivity.api;

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new AnimeItemDecoration(15));
        recyclerScrollSetup();
        animeList = new AnimeList().setContext(getContext()).setApi(api).setRecyclerView(recyclerView);

        return view;
    }


    public void SetupSearchList(final String query){
        animeList.clear();
        animeList.setRequest(new AnimeListRequest() {
            @Override
            public Link getUrl() {
                Log.d("request", "ReqSearch");
                return new Link().animes(animeList.size()).addField("query", query);
            }

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
            }
        }).loadAnimes();
    }
    void recyclerScrollSetup(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = ((GridLayoutManager)recyclerView.getLayoutManager());
                assert layoutManager != null;
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition > (animeList.size() - ANIMES_LIMIT * 0.5))
                    animeList.loadAnimes();
            }
        });
    }
}
