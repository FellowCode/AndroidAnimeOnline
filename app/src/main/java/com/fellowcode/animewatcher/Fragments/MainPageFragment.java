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
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.User.UserRates;
import com.fellowcode.animewatcher.Utils.Page;

public class MainPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    AnimeList animeList;

    RecyclerView recyclerView;
    Api api;
    Context context;
    MainActivity mainActivity;

    public static MainPageFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MainPageFragment fragment = new MainPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
        mainActivity = (MainActivity)getActivity();
        assert mainActivity != null;
        api = mainActivity.api;
        context = getContext();
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.template_recycler, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mainActivity, 2));
        recyclerView.addItemDecoration(new AnimeItemDecoration(25));

        animeList = new AnimeList(context, api, recyclerView);
        if (mPage == Page.ONGOINGS) {
            //animeList.Load("ongoings");
            SetupOngoingsList();
        } else if (mPage == Page.ALL_ANIMES) {
            //animeList.Load("allAnimes");
            SetupAllAnimesList();
        }
        if (animeList.size() == 0)
            animeList.loadAnimes();

        mainActivity.animeLists.add(animeList);

        return view;
    }
    void SetupOngoingsList(){
        animeList.setRequest(new AnimeList.Request() {
            @Override
            public Link getUrl() {
                Log.d("request", "ReqOngoingsList");
                return new Link().animes().addField("isAiring", 1).offset(animeList.size());
            }

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                animeList.Save("ongoings");
            }
        });
    }


    void SetupAllAnimesList(){
        animeList.setRequest(new AnimeList.Request() {
            @Override
            public Link getUrl() {
                Log.d("request", "ReqAllAnimes");
                return new Link().animes().offset(animeList.size());
            }

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                animeList.Save("allAnimes");
            }
        });
    }

    public void setUserRates(UserRates userRates){
        animeList.setUserRates(userRates);
    }

}
