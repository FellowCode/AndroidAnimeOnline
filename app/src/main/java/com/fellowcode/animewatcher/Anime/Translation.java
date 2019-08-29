package com.fellowcode.animewatcher.Anime;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Translation implements Serializable {
    int id;
    int priority;
    int countViews;
    int height;
    String qualityType;
    String type;
    String typeKind;
    String typeLang;
    String authorsSummary;
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
        embedUrl = translation.getString("embedUrl");
    }

    public String getSummary(){
        return String.format("%s - %s (%sp)", authorsSummary, qualityType, height);
    }
}