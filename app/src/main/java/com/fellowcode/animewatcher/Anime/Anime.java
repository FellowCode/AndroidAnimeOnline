package com.fellowcode.animewatcher.Anime;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Anime implements Serializable {
    //"id, isActive, isAiring, myAnimeListScore, year, typeTitle, titles, posterUrlSmall"
    public int id;
    public int shikiId;
    public int isActive;
    public int isAiring;
    public String myAnimeListScore;
    public int year;
    public String type;
    public String typeTitle;
    public String russian;
    public String english;
    public String romaji;
    public String posterUrlSmall;

    public Anime(JSONObject anime) throws JSONException {
        Parse(anime);
    }

    public Anime() {
    }

    public Anime setScore(String score) {
        /*float sc = Float.valueOf(score);
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        myAnimeListScore = df.format(sc);
        Log.d("test", String.valueOf(myAnimeListScore.length()));*/
        myAnimeListScore = score;
        if (myAnimeListScore.length() == 1)
            myAnimeListScore += ".0";
        if (myAnimeListScore.length() == 3)
            myAnimeListScore += "0";
        return this;
    }

    String getTitle(String lang) {
        if (lang.equals("ru"))
            return russian;
        if (lang.equals("en"))
            return english;
        if (lang.equals("romaji"))
            return romaji;
        return null;
    }

    void setTitles(JSONObject titles) throws JSONException {
        if (titles.has("ru"))
            russian = titles.getString("ru");
        if (titles.has("en"))
            english = titles.getString("en");
        if (titles.has("romaji"))
            romaji = titles.getString("romaji");
    }

    public void Parse(JSONObject anime) throws JSONException {
        id = anime.getInt("id");
        shikiId = anime.getInt("myAnimeListId");
        isActive = anime.getInt("isActive");
        isAiring = anime.getInt("isAiring");
        setScore(anime.getString("myAnimeListScore"));
        year = anime.getInt("year");
        type = anime.getString("type");
        typeTitle = anime.getString("typeTitle");
        setTitles(anime.getJSONObject("titles"));
        posterUrlSmall = anime.getString("posterUrlSmall");
    }

    protected Anime get() {
        return this;
    }
}
