package com.fellowcode.animewatcher.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.NavButtons;

public class SettingsActivity extends AppCompatActivity {

    View authShikiBtn;

    Api api;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        new NavButtons(this);

        authShikiBtn = findViewById(R.id.auth_shiki_btn);

        api = new Api(this);

        if (api.isShikiAuthenticated())
            authShikiBtn.setVisibility(View.GONE);

    }

    public void authShikiClick(View v){
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("requestType", "shikiOAuth2");
        startActivity(intent);
    }
}
