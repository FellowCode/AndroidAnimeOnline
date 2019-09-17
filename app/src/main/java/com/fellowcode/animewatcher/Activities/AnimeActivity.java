package com.fellowcode.animewatcher.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fellowcode.animewatcher.Adapters.RelationAdapter;
import com.fellowcode.animewatcher.Anime.Anime;
import com.fellowcode.animewatcher.Anime.AnimeAdvanced;
import com.fellowcode.animewatcher.Anime.AnimeRatings;
import com.fellowcode.animewatcher.Adapters.CharacterAdapter;
import com.fellowcode.animewatcher.Anime.Favorites;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.User.Rate;
import com.fellowcode.animewatcher.User.UserRates;
import com.fellowcode.animewatcher.User.UserShiki;
import com.fellowcode.animewatcher.Utils.NavButtons;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnimeActivity extends AppCompatActivity {

    Api api;

    AnimeAdvanced anime;

    ImageView poster;

    TextView title, title_romaji, genres, type, score, rating, description, next_episode, aired_on, released_on, studio;
    LinearLayout nextep_layout, released_layout;

    View loader;

    RecyclerView relationsView, charactersView;

    View userListParams;

    ImageView favoriteBtn;
    View watchBtn;

    Favorites favorites;

    View progressBar;
    ArrayList<Boolean> requests = new ArrayList<>();

    MaterialSpinner addInListSpinner;

    Rate rate;

    Context context = this;

    RatingBar ratingBar;
    TextView ratingText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        poster = findViewById(R.id.poster);
        title = findViewById(R.id.title);
        title_romaji = findViewById(R.id.title_romaji);
        genres = findViewById(R.id.genres);
        type = findViewById(R.id.qualityType);
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
        userListParams = findViewById(R.id.userListParams);
        favoriteBtn = findViewById(R.id.favorite_btn);
        watchBtn = findViewById(R.id.watch_btn);
        progressBar = findViewById(R.id.progressBar);
        addInListSpinner = findViewById(R.id.addInList);
        ratingBar = findViewById(R.id.ratingIndicator);
        ratingText = findViewById(R.id.ratingView);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d("test", "rating:"+rating);
                int r = (int)(rating*2);
                ratingText.setText(String.valueOf(r));
                try {
                    JSONObject json = new JSONObject();
                    JSONObject userRate = new JSONObject().put("score", r);
                    json.put("user_rate", userRate);
                    if (rate != null)
                        editUserRate(userRate);
                    else
                        createUserRate(userRate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        new NavButtons(this);

        favorites = new Favorites(this);

        charactersView = findViewById(R.id.charactersView);
        charactersView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        relationsView = findViewById(R.id.relationsView);
        relationsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        api = new Api(this);

        if (getIntent().hasExtra("anime")) {
            Anime a = (Anime) getIntent().getSerializableExtra("anime");
            anime = new AnimeAdvanced(a);
        }
        if (getIntent().hasExtra("animeAdvanced")) {
            anime = (AnimeAdvanced) getIntent().getSerializableExtra("animeAdvanced");
        }


        if (favorites.checkIn(anime))
            favoriteBtn.setImageResource(R.drawable.ic_star_fill);

        getAnimeRequest(anime.shikiId);
        getAnimeFromShikiReq(anime.shikiId);
        getAnimeCharacters(anime.shikiId);
        getAnimeRelations(anime.shikiId);
        UpdateFields();
        UpdateFiledsShiki();

        if (api.isShikiAuthenticated()) {
            userListParams.setVisibility(View.VISIBLE);
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

    void getUserRateForAnime(int shikiId) {
        showProgressBar();
        Link link = new Link().shiki().userRate(shikiId, new UserShiki(this).id);
        Log.d("request", "getUserRateForAnime");
        Log.d("link", link.get());
        api.ReqShikiProtect(link.get(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    JSONObject data = new JSONArray(response).getJSONObject(0);
                    rate = new Rate(data);
                    if (rate.score > 0) {
                        ratingBar.setRating((float)(rate.score/2.0));
                        ratingText.setText(String.valueOf(rate.score));
                    }
                    int selectIndex = Rate.findStatus(rate.status);
                    setupSpinner(selectIndex);

                } catch (JSONException e) {
                    e.printStackTrace();
                    setupSpinner(0);
                }
                hideProgressBar();
            }
        });

    }

    void getAnimeCharacters(int id) {
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

    void getAnimeRelations(int id){
        showProgressBar();
        Log.d("request", "getAnimeRelations");
        Response.Listener<String> respListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    anime.ParseShikiRelations(new JSONArray(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressBar();
                RelationAdapter adapter = new RelationAdapter(anime.relations);
                relationsView.setAdapter(adapter);
                if (anime.relations.size() == 0)
                    relationsView.setVisibility(View.GONE);
            }
        };
        Link link = new Link().shiki().related(id);
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
        if (anime.episodes.size() > 0)
            watchBtn.setVisibility(View.VISIBLE);

        if (anime.rateStatus != null){
            setupSpinner(Rate.findStatus(anime.rateStatus));
        }
    }

    void UpdateFiledsShiki() {
        rating.setText(AnimeRatings.getTitle(anime.rating));
        aired_on.setText(anime.aired_on);

        if (anime.next_episode_at != null) {
            next_episode.setText(anime.next_episode_at);
            nextep_layout.setVisibility(View.VISIBLE);
        } else
            nextep_layout.setVisibility(View.GONE);

        if (anime.released_on != null) {
            released_on.setText(anime.released_on);
            released_layout.setVisibility(View.VISIBLE);
        } else
            released_layout.setVisibility(View.GONE);

        studio.setText(anime.studioName);
        if (anime.descriptionHtml != null)
            description.setText(HtmlCompat.fromHtml(anime.descriptionHtml, HtmlCompat.FROM_HTML_MODE_COMPACT));
    }

    public void favoriteBtnClick(View v) {
        if (favorites.checkIn(anime)) {
            favorites.remove(anime);
            favoriteBtn.setImageResource(R.drawable.ic_star);
            Toast.makeText(this, R.string.added_in_favorites, Toast.LENGTH_SHORT).show();
        } else {
            favorites.add(anime);
            favoriteBtn.setImageResource(R.drawable.ic_star_fill);
            Toast.makeText(this, R.string.remove_from_favorites, Toast.LENGTH_SHORT).show();
        }
    }

    public void watchAnime(View v) {
        Intent intent = new Intent(this, WatchActivity.class);
        intent.putExtra("anime", anime);
        if (rate != null)
            intent.putExtra("rate", rate);
        startActivity(intent);
    }

    void showProgressBar() {
        requests.add(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideProgressBar() {
        requests.remove(0);
        if (requests.size() == 0)
            progressBar.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("DefaultLocale")
    void setupSpinner(int selectIndex) {
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_rate_status, Rate.getStatusTitles());
        ArrayList<String> items = Rate.getStatusTitles();
        if (rate == null){
            items.remove(items.size()-1);
        }else if (selectIndex == Rate.getStatuses().indexOf("watching") || selectIndex == Rate.getStatuses().indexOf("dropped")){
            items.set(selectIndex, Rate.getStatusTitles().get(selectIndex)+String.format(" (%d эп.)", rate.episodes));
        }
        addInListSpinner.setItems(items);
        addInListSpinner.setOnItemSelectedListener(
                new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        if (position != 0) {
                            if (Rate.getStatuses().get(position).equals("delete"))
                                deleteUserRate();
                            else {
                                try {
                                    JSONObject userRate = new JSONObject().put("status", Rate.getStatuses().get(position));
                                    if (rate != null) {
                                        editUserRate(userRate);
                                    } else {
                                        createUserRate(userRate);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
        addInListSpinner.setSelectedIndex(selectIndex);
    }

    void editUserRate(JSONObject userRate) {
        Log.d("request", "editUserRate");
        Link link = new Link().shiki().editUserRate(rate.id);
        Log.d("link", link.get());
        api.jsonReqShikiProtect(Request.Method.PUT, link.get(), userRate, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", "userRate: " + response.toString());
            }
        });

    }

    void createUserRate(JSONObject userRate) throws JSONException{
        Log.d("request", "createUserRate");
        userRate.put("user_id", new UserShiki(this).id);
        userRate.put("target_id", anime.shikiId);
        userRate.put("target_type", "Anime");
        Link link = new Link().shiki().createUserRate();
        Log.d("link", link.get());
        api.jsonReqShikiProtect(Request.Method.POST, link.get(), userRate, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", "userRate: " + response.toString());
                try {
                    rate = new Rate(response);
                    new UserRates(context).add(rate).save(context);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    void deleteUserRate(){
        Log.d("request", "createUserRate");
        if (rate != null) {
            Link link = new Link().shiki().editUserRate(rate.id);
            Log.d("link", link.get());
            api.jsonReqShikiProtect(Request.Method.DELETE, link.get(), null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null || error.networkResponse == null) {
                        Log.d("response", "deleteRate");
                        new UserRates(context).remove(rate).save(context);
                        rate = null;
                        setupSpinner(0);
                    }
                }
            });
        }
    }

}
