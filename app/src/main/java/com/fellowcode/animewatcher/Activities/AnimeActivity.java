package com.fellowcode.animewatcher.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.fellowcode.animewatcher.Anime.Anime;
import com.fellowcode.animewatcher.Anime.AnimeAdvanced;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SecureCacheResponse;

public class AnimeActivity extends AppCompatActivity {

    Api api;

    AnimeAdvanced anime;

    ImageView poster;

    TextView title, title_romaji, genres, type, season, score, rating, description;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        poster = findViewById(R.id.poster);
        title = findViewById(R.id.title);
        title_romaji = findViewById(R.id.title_romaji);
        genres = findViewById(R.id.genres);
        type = findViewById(R.id.type);
        season = findViewById(R.id.season);
        score = findViewById(R.id.score);
        rating = findViewById(R.id.rating);
        description = findViewById(R.id.description);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        api = new Api(this);

        Anime a = (Anime)getIntent().getSerializableExtra("anime");
        anime = new AnimeAdvanced(a);
        getAnimeRequest(anime.id);
        getAnimeFromShikiReq(anime.shikiId);
        UpdateFields();
    }

    void getAnimeRequest(int id){
        Log.d("request", "getAnime");
        Response.Listener<String> respListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    anime.Parse(new JSONObject(response).getJSONObject("data"));
                } catch (JSONException e){
                    e.printStackTrace();
                }
                UpdateFields();
            }
        };
        Link link = new Link().anime(id);
        api.Request(link.get(), respListener);
    }

    void getAnimeFromShikiReq(int id){
        Log.d("request", "getAnimeFromShiki");
        Response.Listener<String> respListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    anime.ParseShiki(new JSONObject(response));
                } catch (JSONException e){
                    e.printStackTrace();
                }
                UpdateFiledsShiki();
            }
        };
        Link link = new Link().shiki().anime(id);
        api.Request(link.get(), respListener);
    }

    void UpdateFields(){
        if (anime.posterUrl == null)
            Glide.with(this).load(anime.posterUrlSmall).centerCrop().into(poster);
        Glide.with(this).load(anime.posterUrl).centerCrop().into(poster);
        title.setText(anime.russian);
        title_romaji.setText(anime.romaji);
        genres.setText(Html.fromHtml(
                String.format("<b>%s: </b>%s", getString(R.string.genres), anime.getGenres())));
        if (!anime.type.equals("movie"))
            type.setText(anime.typeTitle + " " + anime.getFullType());
        else
            type.setText(anime.typeTitle);
        season.setText(anime.season);
        score.setText(anime.myAnimeListScore);
        description.setText(anime.description);
    }

    void UpdateFiledsShiki(){
        rating.setText(anime.rating);
    }
}
