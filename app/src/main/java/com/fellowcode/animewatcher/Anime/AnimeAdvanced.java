package com.fellowcode.animewatcher.Anime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class AnimeAdvanced extends Anime implements Serializable {

    class Episode implements Serializable{
        int id;
        int countViews;
        String episodeFull;
        String episodeInt;
        String episodeTitle;
        String type;
    }

    String posterUrl;
    String season;
    int countViews;

    String description;

    ArrayList<Episode> episodes = new ArrayList<>();

    @Override
    void Parse(JSONObject anime) throws JSONException{
        super.Parse(anime);
        posterUrl = anime.getString("posterUrl");
        season = anime.getString("season");
        countViews = anime.getInt("countViews");
        JSONArray descArray = anime.getJSONArray("descriptions");
        for (int i=0; i<descArray.length(); i++){
            JSONObject desc = descArray.getJSONObject(i);
            if (desc.getString("source").equals("shikimori.org") || i == descArray.length()-1){
                description = desc.getString("value");
                break;
            }
        }
        JSONArray episodArray = anime.getJSONArray("episodes");
        for (int i=0; i<episodArray.length(); i++){
            JSONObject ep = episodArray.getJSONObject(i);
            Episode episode = new Episode();
            episode.id = ep.getInt("id");
            episode.countViews = ep.getInt("countViews");
            episode.episodeFull = ep.getString("episodeFull");
            episode.episodeInt = ep.getString("episodeInt");
            episode.episodeTitle = ep.getString("episodeTitle");
            episode.type = ep.getString("episodeType");
        }
    }

}
