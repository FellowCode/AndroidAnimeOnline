package com.fellowcode.animewatcher.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.User.UserRates;
import com.fellowcode.animewatcher.User.UserShiki;
import com.fellowcode.animewatcher.Utils.NavButtons;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    View shikiProfile, authBtn, exitBtn, authSmAnime, exitSmAnime;
    ImageView userAvatar;
    TextView nickname, plannedCount, watchingCount, completedCount, droppedCount, holdOnCount;

    UserShiki userShiki;

    Api api;

    UserRates userRates;

    Context context = this;

    boolean shikiAuth = false;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        shikiProfile = findViewById(R.id.shikiProfile);
        authBtn = findViewById(R.id.shikiAuth);
        exitBtn = findViewById(R.id.shikiExit);
        userAvatar = findViewById(R.id.userAvatar);
        nickname = findViewById(R.id.nickname);
        plannedCount = findViewById(R.id.plannedCount);
        watchingCount = findViewById(R.id.watchingCount);
        completedCount = findViewById(R.id.completedCount);
        droppedCount = findViewById(R.id.droppedCount);
        holdOnCount = findViewById(R.id.holdOnCount);
        authSmAnime = findViewById(R.id.smAnimeAuth);
        exitSmAnime = findViewById(R.id.smAnimeExit);


        api = new Api(this);

        userShiki = new UserShiki(this);
        if (userShiki.isAuthenticated()){
            authBtn.setVisibility(View.GONE);
            exitBtn.setVisibility(View.VISIBLE);
            shikiProfile.setVisibility(View.VISIBLE);
            Glide.with(this).load(userShiki.imageUrl).centerCrop().into(userAvatar);
            nickname.setText(userShiki.nickname);

            api.getUserRates(new UserRates.Response() {
                @Override
                public void onResponse() {
                    userRates = new UserRates(context);
                    userRates.sort();
                    plannedCount.setText(String.valueOf(userRates.plannedIds.size()));
                    watchingCount.setText(String.valueOf(userRates.watchingIds.size()));
                    completedCount.setText(String.valueOf(userRates.completedIds.size()));
                    droppedCount.setText(String.valueOf(userRates.droppedIds.size()));
                    holdOnCount.setText(String.valueOf(userRates.onHoldIds.size()));

                }
            });
        }

        NavButtons navButtons = new NavButtons(this);
        navButtons.select(NavButtons.PROFILE);
        navButtons.clearClick(NavButtons.PROFILE);
    }

    public void plannedClick(View v){
        openAnimeList("planned", userRates.plannedIds);
    }

    public void watchingClick(View v){
        openAnimeList("watching", userRates.watchingIds);
    }

    public void completedClick(View v){
        openAnimeList("completed", userRates.completedIds);
    }

    public void droppedClick(View v){
        openAnimeList("dropped", userRates.droppedIds);
    }

    public void onHoldClick(View v){
        openAnimeList("on_hold", userRates.onHoldIds);
    }

    public void authShikiClick(View v){
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("requestType", "shikiOAuth2");
        startActivity(intent);
    }

    public void exitShikiClick(View v){
        UserShiki userShiki = new UserShiki(this);
        userShiki.accessToken = null;
        userShiki.refreshToken = null;
        userShiki.save(this);
        userRates.rates = null;
        userRates.save(this);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        shikiAuth = false;

    }

    void openAnimeList(String status, ArrayList<Integer> ids){
        if (ids != null && ids.size() > 0) {
            Intent intent = new Intent(this, UserAnimeListActivity.class);
            intent.putExtra("status", status);
            intent.putExtra("animeIds", ids);
            startActivity(intent);
        }
    }

    public void smAnimeAuthClick(View v){
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("requestType", "smAnimeAuth");
        startActivity(intent);
    }

    public void smAnimeExitClick(View v){
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("requestType", "smAnimeExit");
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("test", "onResume");

        userShiki = new UserShiki(this);
        if (userShiki.isAuthenticated()){
            authBtn.setVisibility(View.GONE);
            exitBtn.setVisibility(View.VISIBLE);
            shikiProfile.setVisibility(View.VISIBLE);
            Glide.with(this).load(userShiki.imageUrl).centerCrop().into(userAvatar);
            nickname.setText(userShiki.nickname);

            api.getUserRates(new UserRates.Response() {
                @Override
                public void onResponse() {
                    userRates = new UserRates(context);
                    userRates.sort();
                    plannedCount.setText(String.valueOf(userRates.plannedIds.size()));
                    watchingCount.setText(String.valueOf(userRates.watchingIds.size()));
                    completedCount.setText(String.valueOf(userRates.completedIds.size()));
                    droppedCount.setText(String.valueOf(userRates.droppedIds.size()));
                    holdOnCount.setText(String.valueOf(userRates.onHoldIds.size()));

                }
            });
            shikiAuth = true;
        }
        SharedPreferences auth = getSharedPreferences("auth", Context.MODE_PRIVATE);
        if (auth.getBoolean("authSmAnime", false)){
            authSmAnime.setVisibility(View.GONE);
            exitSmAnime.setVisibility(View.VISIBLE);
        }
    }
}
