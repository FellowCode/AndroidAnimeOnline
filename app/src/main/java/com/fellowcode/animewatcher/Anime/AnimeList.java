package com.fellowcode.animewatcher.Anime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Response;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.Utils.Serialize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class AnimeList implements Serializable {
    private ArrayList<Anime> animes = new ArrayList<>();
    private AnimeAdapter adapter;
    private RecyclerView recyclerView;
    public boolean currReq = false;
    private AnimeListRequest request;

    private Api api;
    private Context context;

    boolean endOfList = false;

    private int ANIME_LIMIT = 20;

    public AnimeList() {
        adapter = new AnimeAdapter(animes);
    }

    public AnimeList(Context context, Api api, RecyclerView recyclerView){
        adapter = new AnimeAdapter(animes);
        setContext(context);
        setApi(api);
        setRecyclerView(recyclerView);
    }

    public AnimeList setApi(Api api){
        this.api = api;
        return this;
    }

    public AnimeList setContext(Context context){
        this.context = context;
        return this;
    }

    public AnimeList setRecyclerView(RecyclerView recycler){
        recyclerView = recycler;
        recyclerView.setAdapter(adapter);
        recyclerScrollSetup();
        return this;
    }

    public AnimeList setRequest(AnimeListRequest request) {
        this.request = request;
        return this;
    }
    public AnimeList clear(){
        endOfList = false;
        animes.clear();
        return this;
    }

    private void Request() {
        currReq = true;
        Link link = request.getUrl();
        Log.d("link", link.get());
        api.Request(link.get(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                animes.addAll(ParseAnimes(response));
                adapter.notifyDataSetChanged();
                request.onResponse(response);
                currReq = false;
            }
        });
    }

    private void RequestShiki(){

    }

    public void loadAnimes() {
        if (!currReq && !endOfList)
            Request();
    }

    private ArrayList<Anime> ParseAnimes(String response) {
        try {
            JSONArray array = new JSONObject(response).getJSONArray("data");
            ArrayList<Anime> animeList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Anime anime = new Anime(obj);
                animeList.add(anime);
            }
            if (array.length() < ANIME_LIMIT)
                endOfList = true;
            return animeList;
        } catch (JSONException e) {
            e.printStackTrace();
            endOfList = true;
            return new ArrayList<>();
        }
    }

    public int size() {
        return animes.size();
    }

    public void Save(String key) {
        new Serialize<Anime>(context, key).wrList(animes);
    }

    public void Load(String key) {
        animes = new Serialize<Anime>(context, key).rdList();
        if (animes == null) {
            animes = new ArrayList<>();

        }
        adapter = new AnimeAdapter(animes);
        recyclerView.setAdapter(adapter);
    }

    void recyclerScrollSetup(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = ((GridLayoutManager)recyclerView.getLayoutManager());
                assert layoutManager != null;
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition > (size() - ANIME_LIMIT * 0.5))
                    loadAnimes();
            }
        });
    }
}
