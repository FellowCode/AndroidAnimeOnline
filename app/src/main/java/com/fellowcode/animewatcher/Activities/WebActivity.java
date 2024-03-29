package com.fellowcode.animewatcher.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.NavButtons;

public class WebActivity extends AppCompatActivity {

    WebView webView;

    String requestType;

    Context context = this;

    View progressbar, smAnimeIsAuthBtn;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        new NavButtons(this);

        webView = findViewById(R.id.webView);
        progressbar = findViewById(R.id.progressBar);
        smAnimeIsAuthBtn = findViewById(R.id.smAnimeIsAuthBtn);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        requestType = getIntent().getStringExtra("requestType");


        if (requestType!=null){
            if (requestType.equals("smAnimeAuth")) {
                loginSmAnime();
            } else if (requestType.equals("smAnimeExit")){
                progressbar.setVisibility(View.VISIBLE);
                webView.setWebViewClient(new WebViewClient() {
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        Log.d("webView", "url:"+url);
                        if (url.equals("https://smotret-anime-365.ru/"))
                            onExitSmAnime();
                    }
                    public void onPageFinished(WebView view, String url){
                        progressbar.setVisibility(View.INVISIBLE);
                    }
                });
                webView.loadUrl("https://smotret-anime-365.ru/users/logout");
            }
            else if (requestType.equals("shikiOAuth2")){
                progressbar.setVisibility(View.VISIBLE);
                webView.setWebViewClient(new WebViewClient() {
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        if (url.contains(Link.shikiUrl + "oauth/authorize/")) {
                            authShiki(url);
                        }
                    }
                    public void onPageFinished(WebView view, String url){
                        progressbar.setVisibility(View.INVISIBLE);
                    }

                });
                String url = Api.getAuthURI();
                Log.d("link", url);
                webView.loadUrl(url);
            }
        }
    }

    public void setSmAnimeIsAuthBtnClick(View v){
        loginSmAnime();
    }

    void loginSmAnime(){
        smAnimeIsAuthBtn.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressbar.setVisibility(View.VISIBLE);
                Log.d("webView", "url:"+url);
                if (url.equals("https://smotret-anime-365.ru/")
                        || url.equals("https://smotret-anime-365.ru")
                        || url.equals("https://smotret-anime-365.ru/users/profile"))
                    onLoginSmAnime();
            }
            public void onPageFinished(WebView view, String url){
                progressbar.setVisibility(View.INVISIBLE);
            }
        });
        webView.loadUrl("https://smotret-anime-365.ru/users/login");
    }

    void onLoginSmAnime(){
        SharedPreferences auth = getSharedPreferences("auth", Context.MODE_PRIVATE);
        auth.edit().putBoolean("authSmAnime", true).apply();
        Toast.makeText(this, R.string.auth_success, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    void onExitSmAnime(){
        SharedPreferences auth = getSharedPreferences("auth", Context.MODE_PRIVATE);
        auth.edit().putBoolean("authSmAnime", false).apply();
        Toast.makeText(this, R.string.exit_success, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    void authShiki(String url){
        Log.d("function", "authShiki");
        String[] tmp = url.split("/");
        String authCode = tmp[tmp.length-1];
        Api api = new Api(this);
        webView.setVisibility(View.INVISIBLE);
        api.authInShiki(authCode, new Api.Auth() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.auth_shiki_success, Toast.LENGTH_LONG).show();
                onBackPressed();
            }

            @Override
            public void onError(String response) {

            }
        });
    }

}
