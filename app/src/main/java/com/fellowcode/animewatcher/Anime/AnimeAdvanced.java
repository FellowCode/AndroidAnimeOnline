package com.fellowcode.animewatcher.Anime;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.RelativeLayout;

import com.fellowcode.animewatcher.Api.Link;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AnimeAdvanced extends Anime implements Serializable {

    public String season;
    public String aired_on;
    public String released_on;
    public String next_episode_at;
    public int numberOfEpisodes;
    public int countViews;
    public String rating;
    public String studioName;

    public String description;
    public String descriptionHtml;

    public ArrayList<Episode> episodes = new ArrayList<>();
    ArrayList<Genre> genres = new ArrayList<>();
    public ArrayList<AnimeCharacter> characters = new ArrayList<>();
    public ArrayList<Relation> relations = new ArrayList<>();

    public AnimeAdvanced(JSONObject anime) throws JSONException {
        ParseSmAnime(anime);
    }

    public AnimeAdvanced(Anime anime) {
        updateAnimeValues(anime);
    }

    public void updateAnimeValues(Anime anime){
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
        posterUrl = anime.posterUrl;
        rateStatus = anime.rateStatus;
    }

    @Override
    public void ParseSmAnime(JSONObject anime) throws JSONException {
        super.ParseSmAnime(anime);
        season = anime.getString("season");
        numberOfEpisodes = anime.getInt("numberOfEpisodes");
        countViews = anime.getInt("countViews");
        JSONArray episodArray = anime.getJSONArray("episodes");
        for (int i = 0; i < episodArray.length(); i++) {
            JSONObject episode = episodArray.getJSONObject(i);
            episodes.add(new Episode(episode));
        }
        JSONArray gen = anime.getJSONArray("genres");
        for (int i=0; i<gen.length(); i++){
            JSONObject g = gen.getJSONObject(i);
            Genre genre = new Genre(g.getInt("id"), g.getString("title"));
            genres.add(genre);
        }

    }

    public void ParseShiki(JSONObject anime) throws JSONException{
        descriptionHtml = anime.getString("description_html");
        rating = anime.getString("rating");
        aired_on = reFormatDate(anime.getString("aired_on"));
        released_on = reFormatDate(anime.getString("released_on"));
        next_episode_at = reFormatDate(anime.getString("next_episode_at").split("T")[0]);
        studioName = anime.getJSONArray("studios").getJSONObject(0).getString("name");
    }

    public void ParseShikiCharacters(JSONArray characters) throws JSONException{
        for (int i=characters.length()-1; i>=0; i--){
            JSONObject obj = characters.getJSONObject(i);
            if (!obj.isNull("character")) {
                JSONObject character = obj.getJSONObject("character");
                AnimeCharacter ch = new AnimeCharacter();
                ch.id = character.getInt("id");
                ch.name = character.getString("name");
                ch.russian = character.getString("russian");
                ch.image = Link.shikiUrl + character.getJSONObject("image").getString("preview");
                this.characters.add(ch);
            }
        }
    }
    public void ParseShikiRelations(JSONArray relations) throws JSONException{
        this.relations.clear();
        for (int i=0; i<relations.length(); i++){
            JSONObject obj = relations.getJSONObject(i);
            if (!obj.isNull("anime")) {
                Relation relation = new Relation();
                relation.rel_eng = obj.getString("relation");
                relation.rel_rus = obj.getString("relation_russian");
                relation.anime = new Anime();
                relation.anime.ParseShiki(obj.getJSONObject("anime"));
                this.relations.add(relation);
            }
        }
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

    @SuppressLint("DefaultLocale")
    public String getFullType(){
        int epCount = episodes.size();
        if (epCount < numberOfEpisodes)
            return String.format("(%d из %d)", epCount, numberOfEpisodes);
        if (numberOfEpisodes != 0)
            return String.format("(%d эп.)", numberOfEpisodes);
        return "";
    }

    @SuppressLint("SimpleDateFormat")
    private String reFormatDate(String date){
        try {
            Date tmp_date = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            return new SimpleDateFormat("dd.MM.yyyy").format(tmp_date);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public Episode getEpisodeById(int id){
        for (int i=0; i<episodes.size(); i++){
            if (episodes.get(i).id == id)
                return episodes.get(i);
        }
        return null;
    }

    public Episode getEpisodeByInt(String number){
        for (int i=0; i<episodes.size(); i++){
            if (episodes.get(i).episodeInt.equals(number))
                return episodes.get(i);
        }
        return null;
    }

    public int getEpisodeIndexByInt(String number){
        for (int i=0; i<episodes.size(); i++){
            if (episodes.get(i).episodeInt.equals(number))
                return i;
        }
        return -1;
    }

}
