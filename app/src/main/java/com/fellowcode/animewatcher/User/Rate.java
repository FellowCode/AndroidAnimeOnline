package com.fellowcode.animewatcher.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Rate implements Serializable {
    public int id;
    public int score;
    public String status = "none";
    public int episodes;
    public int rewatches;
    public int animeId;

    public Rate(JSONObject rate) throws JSONException {
        Parse(rate);
    }

    void Parse(JSONObject rate) throws JSONException {
        id = rate.getInt("id");
        score = rate.getInt("score");
        status = rate.getString("status");
        episodes = rate.getInt("episodes");
        rewatches = rate.getInt("rewatches");
        animeId = rate.getJSONObject("anime").getInt("id");
    }
}
