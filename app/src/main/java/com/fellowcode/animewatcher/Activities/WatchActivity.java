package com.fellowcode.animewatcher.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.fellowcode.animewatcher.Anime.AnimeAdvanced;
import com.fellowcode.animewatcher.Anime.Episode;
import com.fellowcode.animewatcher.Anime.Favorites;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.Fragments.TranslationsPageFragment;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.NavButtons;
import com.fellowcode.animewatcher.Utils.Serialize;
import com.fellowcode.animewatcher.Utils.TranslationsPagerAdapter;
import com.fellowcode.animewatcher.Utils.VideoEnabledWebChromeClient;
import com.fellowcode.animewatcher.Utils.VideoEnabledWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WatchActivity extends AppCompatActivity {

    VideoEnabledWebView webView;
    VideoEnabledWebChromeClient webChromeClient;
    WebView webCheckLogin;
    View authBtn;
    TextView videoNotify;
    View videoProgressBar;
    View episodeControls;

    public Api api;

    public ViewPager viewPager;
    TabLayout tabLayout;

    public AnimeAdvanced anime;

    public Episode currentEpisode;
    int episodeIndex = 0;
    EditText episodeEdit;

    public ArrayList<TranslationsPageFragment> tabs = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        authBtn = findViewById(R.id.auth_btn);

        webCheckLogin = findViewById(R.id.webCheckLogin);
        webCheckLogin.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                Log.d("test", url);
                if (!url.equals("https://smotret-anime-365.ru/users/profile"))
                    authBtn.setVisibility(View.VISIBLE);
            }
        });
        webCheckLogin.loadUrl("https://smotret-anime-365.ru/users/login");

        NavButtons navButtons = new NavButtons(this);

        episodeControls = findViewById(R.id.episodeControls);


        setupWebView();

        videoNotify = findViewById(R.id.video_notify);
        videoProgressBar = findViewById(R.id.video_progress);

        //Setup episode controls
        episodeEdit = findViewById(R.id.episode);
        episodeEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    episodeEdit.clearFocus();
                    Episode ep = anime.getEpisodeByInt(v.getText().toString());
                    if (ep != null) {
                        currentEpisode = ep;
                        changeEpisode();
                    } else {
                        episodeEdit.setText(currentEpisode.episodeInt);
                        Toast.makeText(getApplicationContext(), R.string.episode_not_found, Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });

        // Получаем ViewPager и устанавливаем в него адаптер
        viewPager = findViewById(R.id.viewpager);
        // Передаём ViewPager в TabLayout
        tabLayout = findViewById(R.id.sliding_tabs);



        api = new Api(this);

        anime = (AnimeAdvanced)getIntent().getSerializableExtra("anime");

        if (anime.type.equals("movie"))
            episodeControls.setVisibility(View.GONE);

        currentEpisode = anime.episodes.get(episodeIndex);
        loadTranslations(currentEpisode.id);
        episodeEdit.setText(currentEpisode.episodeInt);

    }

    void setupWebView(){
        webView = findViewById(R.id.webView);
        View nonVideoLayout = findViewById(R.id.nonVideoLayout);
        ViewGroup videoLayout = findViewById(R.id.videoLayout);
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, webView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                // Your code...
            }
        };

        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
        {
            @Override
            public void toggledFullscreen(boolean fullscreen)
            {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen)
                {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                else
                {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

            }
        });
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                if (!url.equals("about:blank"))
                    webView.setVisibility(View.VISIBLE);
            }
        });
    }


    public void onAuthBtnClick(View v){
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("requestType", "login");
        startActivity(intent);
    }

    public void setupVideo(String embedUrl) {
        Log.d("request", "openVideo: " + embedUrl);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

        //webSettings.setUserAgentString("Anime online");
        webView.setVisibility(View.INVISIBLE);
        videoNotify.setVisibility(View.INVISIBLE);
        videoProgressBar.setVisibility(View.VISIBLE);
        webView.loadUrl(embedUrl);
    }


    void loadTranslations(final int episodeId) {
        Log.d("request", "loadTranslations");
        Link link = new Link().episode(episodeId);
        Log.d("link", link.get());
        api.Request(link.get(),
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
        tabs.clear();
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

    public void episodeWatchedClick(View v){

    }

    public void changeEpisode(){
        for(int i=0;i<tabs.size();i++)
            tabs.get(i).hideList();

        episodeEdit.setText(currentEpisode.episodeInt);
        loadTranslations(currentEpisode.id);
        webView.setVisibility(View.INVISIBLE);
        videoProgressBar.setVisibility(View.INVISIBLE);
        videoNotify.setVisibility(View.VISIBLE);
        webView.loadUrl("about:blank");
    }

    public void clearSelectTranslation(){
        for (int i=0;i<tabs.size(); i++){
            tabs.get(i).clearSelect();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        /*if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onBackPressed()
    {
        // Notify the VideoEnabledWebChromeClient, and handle it ourselves if it doesn't handle it
        if (!webChromeClient.onBackPressed())
        {
            if (webView.canGoBack())
            {
                webView.goBack();
            }
            else
            {
                // Standard back button implementation (for example this could close the app)
                super.onBackPressed();
            }
        }
    }


}
