package com.fellowcode.animewatcher.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.android.volley.Response;
import com.fellowcode.animewatcher.Anime.AnimeAdvanced;
import com.fellowcode.animewatcher.Anime.Episode;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.NavButtons;
import com.fellowcode.animewatcher.Utils.TranslationsPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WatchActivity extends AppCompatActivity {

    WebView webView;

    public Api api;

    public ViewPager viewPager;
    TabLayout tabLayout;

    public AnimeAdvanced anime;

    public Episode currentEpisode;
    int episodeIndex = 0;
    EditText episodeEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        NavButtons navButtons = new NavButtons(this);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                webView.setVisibility(View.VISIBLE);
            }
        });
        episodeEdit = findViewById(R.id.episode);

        // Получаем ViewPager и устанавливаем в него адаптер
        viewPager = findViewById(R.id.viewpager);
        // Передаём ViewPager в TabLayout
        tabLayout = findViewById(R.id.sliding_tabs);



        api = new Api(this);

        anime = (AnimeAdvanced)getIntent().getSerializableExtra("anime");

        currentEpisode = anime.episodes.get(episodeIndex);
        loadTranslations(currentEpisode.id);
        episodeEdit.setText(currentEpisode.episodeInt);
    }

    public void setupVideo(String embedUrl) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //webSettings.setUserAgentString("Anime online");
        webView.setVisibility(View.INVISIBLE);
        webView.loadUrl(embedUrl);
    }

    void loadTranslations(final int episodeId) {
        Log.d("request", "loadTranslations");
        api.Request(new Link().episode(episodeId).get(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONArray translations = new JSONObject(response).getJSONObject("data").getJSONArray("translations");
                            anime.getEpisodeById(episodeId).ParseTranslations(translations);
                            setupTabLayout();
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    void setupTabLayout(){
        viewPager.setAdapter(new TranslationsPagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
    }

    public void decEpisodeClick(View v){
        if (episodeIndex >0) {
            episodeIndex--;
            currentEpisode = anime.episodes.get(episodeIndex);
            changeEpisode();
        }
    }

    public void incEpisodeClick(View v){
        if (episodeIndex < anime.episodes.size()) {
            episodeIndex++;
            currentEpisode = anime.episodes.get(episodeIndex);
            changeEpisode();
        }
    }

    public void changeEpisode(){
        episodeEdit.setText(currentEpisode.episodeInt);
        loadTranslations(currentEpisode.id);
        webView.setVisibility(View.INVISIBLE);
    }

}
