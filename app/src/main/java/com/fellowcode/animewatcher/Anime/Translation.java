package com.fellowcode.animewatcher.Anime;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Translation implements Serializable {
    int id;
    int priority;
    int countViews;
    public int height;
    public String qualityType;
    String type;
    String typeKind;
    String typeLang;
    public String authorsSummary;
    public String embedUrl;

    public Translation(JSONObject translation) throws JSONException {
        Parse(translation);
    }

    void Parse(JSONObject translation) throws JSONException{
        id = translation.getInt("id");
        priority = translation.getInt("priority");
        countViews = translation.getInt("countViews");
        height = translation.getInt("height");
        qualityType = translation.getString("qualityType");
        type = translation.getString("type");
        typeKind = translation.getString("typeKind");
        typeLang = translation.getString("typeLang");
        authorsSummary = translation.getString("authorsSummary");
        if (authorsSummary.length() == 0)
            authorsSummary = "Неизвестный";
        embedUrl = translation.getString("embedUrl");
    }

    public String getSummary(){
        if (height > 0)
            return String.format("%s - %s (%sp)", authorsSummary, qualityType.toUpperCase(), height);
        else
            return String.format("%s - %s", authorsSummary, qualityType.toUpperCase());
    }
}
