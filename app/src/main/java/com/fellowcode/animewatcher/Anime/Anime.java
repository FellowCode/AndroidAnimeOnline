package com.fellowcode.animewatcher.Anime;

import com.fellowcode.animewatcher.Api.Link;

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
    public String posterUrl;

    public String rateStatus;

    public Anime(JSONObject anime) throws JSONException {
        ParseSmAnime(anime);
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

    public String getTitle(String lang) {
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

    public void ParseSmAnime(JSONObject anime) throws JSONException {
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
        posterUrl = anime.getString("posterUrl");
    }

    public void ParseShiki(JSONObject anime) throws JSONException {
        shikiId = anime.getInt("id");
        romaji = anime.getString("name");
        russian = anime.getString("russian");
        posterUrlSmall = Link.shikiUrl + anime.getJSONObject("image").getString("preview");
        posterUrl = Link.shikiUrl + anime.getJSONObject("image").getString("original");
        type = anime.getString("kind");
        if (anime.getString("status").equals("ongoing"))
            isAiring = 1;
        else
            isAiring = 0;
        if (!anime.isNull("aired_on"))
            year = Integer.valueOf(anime.getString("aired_on").split("-")[0]);
    }


    protected Anime get() {
        return this;
    }
}
