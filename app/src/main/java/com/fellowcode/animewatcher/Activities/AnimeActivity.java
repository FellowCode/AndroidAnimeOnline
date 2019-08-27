package com.fellowcode.animewatcher.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;

public class AnimeActivity extends AppCompatActivity {

    Api api;

    AnimeAdvanced anime;

    ImageView poster;

    TextView title, title_romaji, genres, type, season, score, rating, description, next_episode, aired_on, released_on;
    LinearLayout nextep_layout, released_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        next_episode = findViewById(R.id.next_episode);
        aired_on = findViewById(R.id.aired_on);
        released_on = findViewById(R.id.released_on);
        nextep_layout = findViewById(R.id.nextep_layout);
        released_layout = findViewById(R.id.released_layout);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        api = new Api(this);

        Anime a = (Anime) getIntent().getSerializableExtra("anime");
        anime = new AnimeAdvanced(a);
        getAnimeRequest(anime.id);
        getAnimeFromShikiReq(anime.shikiId);
        UpdateFields();
        UpdateFiledsShiki();
    }

    void getAnimeRequest(int id) {
        Log.d("request", "getAnime");
        Response.Listener<String> respListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    anime.Parse(new JSONObject(response).getJSONObject("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UpdateFields();
            }
        };
        Link link = new Link().anime(id);
        api.Request(link.get(), respListener);
    }

    void getAnimeFromShikiReq(int id) {
        Log.d("request", "getAnimeFromShiki");
        Response.Listener<String> respListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    anime.ParseShiki(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UpdateFiledsShiki();
            }
        };
        Link link = new Link().shiki().anime(id);
        api.Request(link.get(), respListener);
    }

    void UpdateFields() {
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

    void UpdateFiledsShiki() {
        rating.setText(anime.rating);
        aired_on.setText(anime.aired_on);

        if (anime.next_episode_at != null) {
            next_episode.setText(anime.next_episode_at.split("T")[0]);
            nextep_layout.setVisibility(View.VISIBLE);
        }else
            nextep_layout.setVisibility(View.GONE);

        if (anime.released_on != null) {
            released_on.setText(anime.released_on);
            released_layout.setVisibility(View.VISIBLE);
        }else
            released_layout.setVisibility(View.GONE);
    }
}
