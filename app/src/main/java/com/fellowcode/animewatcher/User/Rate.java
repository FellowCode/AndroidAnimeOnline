package com.fellowcode.animewatcher.User;

import org.json.JSONException;
import org.json.JSONObject;

public class Rate{
    int id;
    int score;
    String status;
    int episodes;
    int rewatches;

    public Rate(JSONObject rate) throws JSONException {
        Parse(rate);
    }

    void Parse(JSONObject rate) throws JSONException {
        id = rate.getInt("id");
        score = rate.getInt("score");
        status = rate.getString("status");
        episodes = rate.getInt("episodes");
        rewatches = rate.getInt("rewatches");
    }
}
