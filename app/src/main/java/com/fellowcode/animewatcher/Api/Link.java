package com.fellowcode.animewatcher.Api;

import java.io.Serializable;
import java.util.ArrayList;

public class Link implements Serializable {
    class Field implements Serializable{
        String key;
        String value;

        Field(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String get() {
            return String.format("%s=%s", key, value);
        }
    }

    private String link = "https://smotret-anime-365.ru/";
    public static String shikiUrl = "https://shikimori.one/";
    private String api = "api/";
    private String url;

    private boolean animes = false;

    public boolean isShiki = false;


    private ArrayList<Field> fields;
    private ArrayList<Field> filterFields;

    public Link() {
        fields = new ArrayList<>();
        filterFields = new ArrayList<>();
    }

    public Link shiki(){
        isShiki = true;
        link = shikiUrl;
        return this;
    }

    public Link animes() {
        if (isShiki)
            url = "animes";
        else {
            url = "series";
            addField("fields", "id,myAnimeListId,isActive,isAiring,myAnimeListScore,year,type,typeTitle,titles,posterUrlSmall,posterUrl");
            addField("isHentai", 0);
        }
        addField("limit", 20);
        return this;
    }

    public Link offset(int offset){
        if (isShiki){
            addUniqueField("page", offset/20+1);
        } else
            addUniqueField("offset", offset);
        return this;
    }

    public Link anime(int id){
        if (isShiki)
            url = "animes/"+id;
        else
            url = "series/"+id;
        return this;
    }

    public Link whoami(){
        url = "users/whoami";
        return this;
    }

    public Link userRates(int userId){
        url = "users/"+userId+"/anime_rates?limit=10000&page=1";
        return this;
    }

    public Link animeByShikiId(int shikiId){
        url = "series";
        addField("myAnimeListId", shikiId);
        return this;
    }

    public Link userRate(int shikiId){
        api = "api/v2/";
        url = "user_rates";
        addField("target_id", shikiId);
        addField("target_type", "Anime");
        return this;
    }

    public Link episode(int id){
        url = "episodes/" + id;
        return this;
    }

    public Link roles(int shikiId){
        anime(shikiId);
        url += "/roles";
        return this;
    }

    public Link addField(String key, String value) {
        if (value != null)
            fields.add(new Field(key, value));
        return this;
    }

    public Link addField(String key, int value) {
        fields.add(new Field(key, String.valueOf(value)));
        return this;
    }

    public Link addField(String key, ArrayList<Integer> values){
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<values.size();i++){
            builder.append(values.get(i));
            if (i<values.size()-1)
                builder.append(',');
        }
        addField(key, builder.toString());
        return this;
    }

    public Link addUniqueField(String key, String value){
        for (int i=0; i<fields.size(); i++){
            if (fields.get(i).key.equals(key)){
                fields.get(i).value = value;
                return this;
            }
        }
        return addField(key, value);
    }

    public Link addUniqueField(String key, int value){
        return addUniqueField(key, String.valueOf(value));
    }

    public Link addFilterField(String key, String value){
        if (value != null)
            filterFields.add(new Field(key, value));
        return this;
    }

    String getFilterParams(){
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < filterFields.size(); i++) {
            params.append(filterFields.get(i).get());
            if (i < filterFields.size() - 1)
                params.append(";");
        }
        return params.toString();
    }

    public String get() {
        if (filterFields.size() > 0) {
            addField("chips", getFilterParams());
            filterFields.clear();
        }

        StringBuilder url = new StringBuilder(link + api + this.url);
        if (fields.size() > 0)
            url.append("?");
        for (int i = 0; i < fields.size(); i++) {
            url.append(fields.get(i).get());
            if (i < fields.size() - 1)
                url.append("&");
        }
        //Log.d("link", url.toString());
        return url.toString();
    }
}
