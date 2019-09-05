package com.fellowcode.animewatcher.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.User.UserRates;
import com.fellowcode.animewatcher.User.UserShiki;
import com.fellowcode.animewatcher.Utils.NavButtons;

import org.json.JSONArray;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    View shikiProfile, authBtn, exitBtn;
    ImageView userAvatar;
    TextView nickname, plannedCount, watchingCount, completedCount, droppedCount, holdOnCount;

    UserShiki userShiki;

    Api api;

    UserRates userRates;

    Context context = this;

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
                    holdOnCount.setText(String.valueOf(userRates.holdOnIds.size()));

                }
            });
        }

        NavButtons navButtons = new NavButtons(this);
        navButtons.select(NavButtons.PROFILE);
        navButtons.clearClick(NavButtons.PROFILE);
    }

    public void plannedClick(View v){

    }

    public void watchingClick(View v){

    }

    public void completedClick(View v){

    }

    public void droppedClick(View v){

    }

    public void holdOnClick(View v){

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

    }
}
