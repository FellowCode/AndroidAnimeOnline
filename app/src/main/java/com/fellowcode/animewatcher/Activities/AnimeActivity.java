package com.fellowcode.animewatcher.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fellowcode.animewatcher.Anime.Anime;
import com.fellowcode.animewatcher.Anime.AnimeAdvanced;
import com.fellowcode.animewatcher.Anime.AnimeRatings;
import com.fellowcode.animewatcher.Anime.CharacterAdapter;
import com.fellowcode.animewatcher.Anime.Favorites;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.NavButtons;
import com.fellowcode.animewatcher.Utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class AnimeActivity extends AppCompatActivity {

    Api api;

    AnimeAdvanced anime;

    ImageView poster;

    TextView title, title_romaji, genres, type, score, rating, description, next_episode, aired_on, released_on, studio;
    LinearLayout nextep_layout, released_layout;

    View loader;

    RecyclerView charactersView;

    View myScore;
    EditText myScoreEdit;

    ImageView favoriteBtn;
    View watchBtn;

    Favorites favorites;

    View progressBar;
    ArrayList<Boolean> requests = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        poster = findViewById(R.id.poster);
        title = findViewById(R.id.title);
        title_romaji = findViewById(R.id.title_romaji);
        genres = findViewById(R.id.genres);
        type = findViewById(R.id.type);
        score = findViewById(R.id.score);
        rating = findViewById(R.id.rating);
        description = findViewById(R.id.description);
        next_episode = findViewById(R.id.next_episode);
        aired_on = findViewById(R.id.aired_on);
        released_on = findViewById(R.id.released_on);
        nextep_layout = findViewById(R.id.nextep_layout);
        released_layout = findViewById(R.id.released_layout);
        studio = findViewById(R.id.studio);
        loader = findViewById(R.id.loader);
        myScore = findViewById(R.id.my_score);
        myScoreEdit = findViewById(R.id.my_score_edit);
        favoriteBtn = findViewById(R.id.favorite_btn);
        watchBtn = findViewById(R.id.watch_btn);
        progressBar = findViewById(R.id.progressBar);

        new NavButtons(this);

        favorites = new Favorites(this);

        charactersView = findViewById(R.id.charactersView);
        charactersView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        api = new Api(this);

        Anime a = (Anime) getIntent().getSerializableExtra("anime");
        anime = new AnimeAdvanced(a);

        if (favorites.checkIn(anime))
            favoriteBtn.setImageResource(R.drawable.ic_star_fill);

        getAnimeRequest(anime.shikiId);
        getAnimeFromShikiReq(anime.shikiId);
        getAnimeCharacters(anime.shikiId);
        UpdateFields();
        UpdateFiledsShiki();

        if (api.isShikiAuthenticated()){
            myScore.setVisibility(View.VISIBLE);
            getUserRateForAnime(anime.shikiId);
        }
    }

    void getAnimeRequest(int shikiId) {
        showProgressBar();
        Log.d("request", "getAnime");
        Response.Listener<String> respListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    anime.ParseSmAnime(new JSONObject(response).getJSONArray("data").getJSONObject(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressBar();
                UpdateFields();
            }
        };
        Link link = new Link().animeByShikiId(shikiId);
        Log.d("link", link.get());
        api.Request(link.get(), respListener);
    }

    void getAnimeFromShikiReq(int id) {
        showProgressBar();
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
                hideProgressBar();
                UpdateFiledsShiki();
            }
        };
        Link link = new Link().shiki().anime(id);
        Log.d("link", link.get());
        api.Request(link.get(), respListener);
    }

    void getUserRateForAnime(int shikiId){
        showProgressBar();
        Link link = new Link().shiki().userRate(shikiId);
        Log.d("request", "getUserRateForAnime");
        Log.d("link", link.get());
        api.ShikiProtectReq(link.get(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try{
                    JSONObject data = new JSONArray(response).getJSONObject(0);
                    int score = data.getInt("score");
                    Log.d("response", String.valueOf(score));
                    if (score > 0)
                        myScoreEdit.setText(String.valueOf(score));
                } catch (JSONException e){
                    e.printStackTrace();
                }
                hideProgressBar();
            }
        });

    }

    void getAnimeCharacters(int id){
        showProgressBar();
        Log.d("request", "getAnimeCharacters");
        Response.Listener<String> respListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    anime.ParseShikiCharacters(new JSONArray(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressBar();
                CharacterAdapter adapter = new CharacterAdapter(anime.characters);
                charactersView.setAdapter(adapter);
            }
        };
        Link link = new Link().shiki().roles(id);
        Log.d("link", link.get());
        api.Request(link.get(), respListener);
    }


    void UpdateFields() {
        Glide.with(this)
                .load(anime.posterUrl)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loader.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(poster);

        title.setText(anime.russian);
        title_romaji.setText(anime.romaji);
        genres.setText(HtmlCompat.fromHtml(
                String.format("<b>%s: </b>%s", getString(R.string.genres), anime.getGenres()), HtmlCompat.FROM_HTML_MODE_LEGACY));
        if (!anime.type.equals("movie"))
            type.setText(anime.typeTitle + " " + anime.getFullType());
        else
            type.setText(anime.typeTitle);
        score.setText(anime.myAnimeListScore);
        if (anime.episodes.size()>0)
            watchBtn.setVisibility(View.VISIBLE);
    }

    void UpdateFiledsShiki() {
        rating.setText(AnimeRatings.getTitle(anime.rating));
        aired_on.setText(anime.aired_on);

        if (anime.next_episode_at != null) {
            next_episode.setText(anime.next_episode_at);
            nextep_layout.setVisibility(View.VISIBLE);
        }else
            nextep_layout.setVisibility(View.GONE);

        if (anime.released_on != null) {
            released_on.setText(anime.released_on);
            released_layout.setVisibility(View.VISIBLE);
        }else
            released_layout.setVisibility(View.GONE);

        studio.setText(anime.studioName);
        if (anime.descriptionHtml != null)
            description.setText(HtmlCompat.fromHtml(anime.descriptionHtml, HtmlCompat.FROM_HTML_MODE_COMPACT));
    }

    public void favoriteBtnClick(View v){
        if (favorites.checkIn(anime)) {
            favorites.remove(anime);
            favoriteBtn.setImageResource(R.drawable.ic_star);
        } else {
            favorites.add(anime);
            favoriteBtn.setImageResource(R.drawable.ic_star_fill);
        }
    }

    public void watchAnime(View v){
        Intent intent = new Intent(this, WatchActivity.class);
        intent.putExtra("anime", anime);
        startActivity(intent);
    }

    void showProgressBar(){
        requests.add(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideProgressBar(){
        requests.remove(0);
        if(requests.size() == 0)
            progressBar.setVisibility(View.INVISIBLE);
    }
}
