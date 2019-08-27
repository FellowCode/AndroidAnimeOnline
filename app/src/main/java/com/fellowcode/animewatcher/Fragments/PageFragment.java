package com.fellowcode.animewatcher.Fragments;

import android.content.Context;
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
import com.fellowcode.animewatcher.Utils.Page;

import java.util.Objects;

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    AnimeList animeList;

    private int ANIMES_ON_LIMIT = 20;

    RecyclerView recyclerView;
    Api api;
    Context context;

    public static PageFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
        api = ((MainActivity) Objects.requireNonNull(getActivity())).api;
        context = getContext();
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new AnimeItemDecoration(25));
        recyclerScrollSetup();

        animeList = new AnimeList().setContext(context).setApi(api).setRecyclerView(recyclerView);
        if (mPage == Page.ONGOINGS) {
            animeList.Load("ongoings");
            SetupOngoingsList();
        } else if (mPage == Page.ALL_ANIMES) {
            animeList.Load("allAnimes");
            SetupAllAnimesList();
        }
        if (animeList.size() == 0)
            animeList.loadAnimes();

        return view;
    }

    void recyclerScrollSetup(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = ((GridLayoutManager)recyclerView.getLayoutManager());
                assert layoutManager != null;
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition > (animeList.size() - ANIMES_ON_LIMIT * 0.5))
                    animeList.loadAnimes();
            }
        });
    }
    void SetupOngoingsList(){
        animeList.setRequest(new AnimeListRequest() {
            @Override
            public Link getUrl() {
                Log.d("request", "ReqOngoingsList");
                return new Link().animes(animeList.size()).addField("isAiring", 1);
            }

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                animeList.Save("ongoings");
            }
        });
    }


    void SetupAllAnimesList(){
        animeList.setRequest(new AnimeListRequest() {
            @Override
            public Link getUrl() {
                Log.d("request", "ReqAllAnimes");
                return new Link().animes(animeList.size());
            }

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                animeList.Save("allAnimes");
            }
        });
    }

}
