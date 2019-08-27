package com.fellowcode.animewatcher.Anime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class AnimeAdvanced extends Anime implements Serializable {

    class Episode implements Serializable {
        int id;
        int countViews;
        String episodeFull;
        String episodeInt;
        String episodeTitle;
        String type;
    }

    public String posterUrl;
    public String season;
    public String aired_on;
    public String released_on;
    public String next_episode_at;
    public int numberOfEpisodes;
    public int countViews;
    public String rating;

    public String description;

    ArrayList<Episode> episodes = new ArrayList<>();
    ArrayList<Genre> genres = new ArrayList<>();

    public AnimeAdvanced(JSONObject anime) throws JSONException {
        Parse(anime);
    }

    public AnimeAdvanced(Anime anime) {
        id = anime.id;
        shikiId = anime.shikiId;
        isActive = anime.isActive;
        isAiring = anime.isAiring;
        myAnimeListScore = anime.myAnimeListScore;
        year = anime.year;
        type = anime.type;
        typeTitle = anime.typeTitle;
        russian = anime.russian;
        english = anime.english;
        romaji = anime.romaji;

    }

    @Override
    public void Parse(JSONObject anime) throws JSONException {
        super.Parse(anime);
        posterUrl = anime.getString("posterUrl");
        season = anime.getString("season");
        numberOfEpisodes = anime.getInt("numberOfEpisodes");
        countViews = anime.getInt("countViews");
        JSONArray descArray = anime.getJSONArray("descriptions");
        for (int i = 0; i < descArray.length(); i++) {
            JSONObject desc = descArray.getJSONObject(i);
            if (desc.getString("source").equals("shikimori.org") || i == descArray.length() - 1) {
                description = desc.getString("value");
                break;
            }
        }
        JSONArray episodArray = anime.getJSONArray("episodes");
        for (int i = 0; i < episodArray.length(); i++) {
            JSONObject ep = episodArray.getJSONObject(i);
            Episode episode = new Episode();
            episode.id = ep.getInt("id");
            episode.countViews = ep.getInt("countViews");
            episode.episodeFull = ep.getString("episodeFull");
            episode.episodeInt = ep.getString("episodeInt");
            episode.episodeTitle = ep.getString("episodeTitle");
            episode.type = ep.getString("episodeType");
            episodes.add(episode);
        }
        JSONArray gen = anime.getJSONArray("genres");
        for (int i=0; i<gen.length(); i++){
            JSONObject g = gen.getJSONObject(i);
            Genre genre = new Genre(g.getInt("id"), g.getString("title"));
            genres.add(genre);
        }
    }

    public void ParseShiki(JSONObject anime) throws JSONException{
        rating = anime.getString("rating");
        aired_on = anime.getString("aired_on");
        if (aired_on.equals("null"))
            aired_on = null;
        released_on = anime.getString("released_on");
        if (released_on.equals("null"))
            released_on = null;
        next_episode_at = anime.getString("next_episode_at");
        if (next_episode_at.equals("null"))
            next_episode_at = null;
    }

    public String getGenres(){
        StringBuilder g = new StringBuilder();
        for (int i=0; i<genres.size(); i++){
            g.append(genres.get(i).title);
            if (i < genres.size()-1)
                g.append(", ");
        }
        return g.toString();
    }

    public String getFullType(){
        int epCount = episodes.size();
        if (epCount < numberOfEpisodes)
            return String.format("(%d из %d)", epCount, numberOfEpisodes);
        if (numberOfEpisodes != 0)
            return String.format("(%d эп.)", numberOfEpisodes);
        return "";
    }

}
