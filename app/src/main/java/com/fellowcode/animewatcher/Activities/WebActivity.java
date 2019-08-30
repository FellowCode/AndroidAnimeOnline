package com.fellowcode.animewatcher.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.NavButtons;

public class WebActivity extends AppCompatActivity {

    WebView webView;

    String requestType;

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


        if (requestType!=null && requestType.equals("login")){
            webView.setWebViewClient(new WebViewClient(){
                public void onPageFinished(WebView view, String url) {
                    Log.d("webView", "onPageFinished: "+url);
                    if (url.equals("https://smotret-anime-365.ru/users/profile"))
                        onLoginSuccess();
                }
            });
            webView.loadUrl("https://smotret-anime-365.ru/users/login");
        }
    }

    void onLoginSuccess(){
        Toast.makeText(this, R.string.auth_success, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

}
