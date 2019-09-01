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

import java.util.BitSet;

public class WebActivity extends AppCompatActivity {

    WebView webView;

    String requestType;

    Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        new NavButtons(this);

        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        requestType = getIntent().getStringExtra("requestType");


        if (requestType!=null){
            if (requestType.equals("login")) {
                webView.setWebViewClient(new WebViewClient() {
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        Log.d("webView", "onPageFinished: " + url);
                        if (url.equals("https://smotret-anime-365.ru/users/profile"))
                            onLoginSuccess();
                    }
                });
                webView.loadUrl("https://smotret-anime-365.ru/users/login");
            }
            else if (requestType.equals("shikiOAuth2")){
                webView.setWebViewClient(new WebViewClient() {
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        Log.d("webView", "onPageFinished: " + url);
                        if (url.contains(Link.shikiUrl + "oauth/authorize/")) {
                            authShiki(url);
                        }
                    }

                });
                String url = Api.getAuthURI();
                Log.d("link", url);
                webView.loadUrl(url);
            }
        }
    }

    void onLoginSuccess(){
        Toast.makeText(this, R.string.auth_success, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    void authShiki(String url){
        Log.d("function", "authShiki");
        String[] tmp = url.split("/");
        String authCode = tmp[tmp.length-1];
        Api api = new Api(this);

        api.authInShiki(authCode, new Api.Auth() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, R.string.auth_shiki_success, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(String response) {

            }
        });
    }

}
