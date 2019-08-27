package com.fellowcode.animewatcher.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fellowcode.animewatcher.Anime.AnimeAdvanced;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.R;

public class AnimeActivity extends AppCompatActivity {

    Api api;

    AnimeAdvanced anime;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        api = new Api(this);
    }

    void getAnimeRequest(){

    }
}
