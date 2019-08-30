package com.fellowcode.animewatcher.Anime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Episode implements Serializable {

    public int id;
    public int countViews;
    public String episodeFull;
    public String episodeInt;
    public String episodeTitle;
    String type;

    ArrayList<Translation> translations;

    public Episode(JSONObject episode) throws JSONException{
        Parse(episode);
    }

    public void Parse(JSONObject episode) throws JSONException {
      id = episode.getInt("id");
      countViews = episode.getInt("countViews");
      episodeFull = episode.getString("episodeFull");
      episodeInt = episode.getString("episodeInt");
      episodeTitle = episode.getString("episodeTitle");
      type = episode.getString("episodeType");
    }

    public void ParseTranslations(JSONArray array) throws JSONException{
        translations = new ArrayList<>();
        for(int i=0; i<array.length(); i++){
            translations.add(new Translation(array.getJSONObject(i)));
        }
    }

    private ArrayList<Translation> getTranslationsByType(String type){
        ArrayList<Translation> t = new ArrayList<>();
        for (int i=0; i<translations.size(); i++){
            if (translations.get(i).type.equals(type))
                t.add(translations.get(i));
        }
        return t;
    }

    public ArrayList<Translation> getVoiceRuTranslations(){
        return getTranslationsByType("voiceRu");
    }
    public ArrayList<Translation> getSubRuTranslations(){
        return getTranslationsByType("subRu");
    }
    public ArrayList<Translation> getRawTranslations(){
        return getTranslationsByType("raw");
    }
}
