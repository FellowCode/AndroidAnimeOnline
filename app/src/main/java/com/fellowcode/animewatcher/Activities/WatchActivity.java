package com.fellowcode.animewatcher.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.fellowcode.animewatcher.Anime.AnimeAdvanced;
import com.fellowcode.animewatcher.Anime.Episode;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.Fragments.TranslationsPageFragment;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.NavButtons;
import com.fellowcode.animewatcher.Utils.TranslationsPagerAdapter;

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

    WebView webView;
    TextView videoNotify;
    View videoProgressBar;

    public Api api;

    public ViewPager viewPager;
    TabLayout tabLayout;

    public AnimeAdvanced anime;

    public Episode currentEpisode;
    int episodeIndex = 0;
    EditText episodeEdit;

    public ArrayList<TranslationsPageFragment> tabs = new ArrayList<>();

    String embedUrlForParse;


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
        videoNotify = findViewById(R.id.video_notify);
        videoProgressBar = findViewById(R.id.video_progress);

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

        currentEpisode = anime.episodes.get(episodeIndex);
        loadTranslations(currentEpisode.id);
        episodeEdit.setText(currentEpisode.episodeInt);

        login();
    }

    public void login(){
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://smotret-anime-365.ru/users/login");
        webView.setVisibility(View.VISIBLE);
    }

    public void setupVideo(String embedUrl) {
        Log.d("request", "openVideo: " + embedUrl);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //webSettings.setUserAgentString("Anime online");
        webView.setVisibility(View.INVISIBLE);
        videoNotify.setVisibility(View.INVISIBLE);
        videoProgressBar.setVisibility(View.VISIBLE);
        webView.loadUrl(embedUrl);

        ParseEmbed(embedUrl);
    }

    public void ParseEmbed(String embedUrl){
        embedUrlForParse = embedUrl;
        ParseTask mt = new ParseTask();
        mt.execute();
    }

    public void onParsed(String videoSources){
        //Log.d("response", "video: "+videoSources);
    }

    class ParseTask extends AsyncTask<Void, Void, Void> {

        String videoSources;

        @Override
        protected Void doInBackground(Void... params) {
            final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
            final String LOGIN_FORM_URL = "https://smotret-anime-365.ru/users/login?dynpage=1";
            final String LOGIN_ACTION_URL = "https://smotret-anime-365.ru/users/login";
            final String USERNAME = "sergo79f1@gmail.com";
            final String PASSWORD = "Sergo7997_f1";

            Document doc = null;//Здесь хранится будет разобранный html документ
            try {
                Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL)
                        .method(Connection.Method.GET)
                        .userAgent(USER_AGENT)
                        .execute();
                Document loginDoc = loginForm.parse();
                HashMap<String, String> cookies = new HashMap<>(loginForm.cookies());
                for (Map.Entry<String, String> cookie : cookies.entrySet()) {
                    Log.d("response", "cookie: "+cookie.getKey() + " : " + cookie.getValue());
                }
                String csrf = loginDoc.select("input[name=csrf]").first().attr("value");
                HashMap<String, String> formData = new HashMap<>();
                formData.put("yt0", "");
                formData.put("dynpage", "1");
                formData.put("login", USERNAME);
                formData.put("password", PASSWORD);
                formData.put("csrf", csrf);

                Log.d("response", csrf);

                Connection.Response homePage = Jsoup.connect(LOGIN_ACTION_URL)
                        .cookies(loginForm.cookies())
                        .data(formData)
                        .method(Connection.Method.POST)
                        .userAgent(USER_AGENT)
                        .followRedirects(true)
                        .execute();

                for (Map.Entry<String, String> cookie : homePage.cookies().entrySet()) {
                    Log.d("response", "cookie2: "+cookie.getKey() + " : " + cookie.getValue());
                }
                Log.d("response", csrf);

                videoSources = homePage.parse().body().toString();

                cookies = new HashMap<>(homePage.cookies());

                Connection.Response videoPage = Jsoup.connect(embedUrlForParse)
                        .method(Connection.Method.GET)
                        .userAgent(USER_AGENT)
                        .execute();

                //videoSources = videoPage.parse().body().toString();

            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            onParsed(videoSources);
        }
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

    public void changeEpisode(){
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

}
