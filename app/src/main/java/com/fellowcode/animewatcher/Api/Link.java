package com.fellowcode.animewatcher.Api;

import java.util.ArrayList;

public class Link {
    class Field {
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
    private String api = "api/";
    private String url;

    private boolean animes = false;

    private boolean isShiki = false;


    private ArrayList<Field> fields;
    private ArrayList<Field> filterFields;

    public Link() {
        fields = new ArrayList<>();
        filterFields = new ArrayList<>();
    }

    public Link shiki(){
        isShiki = true;
        link = "https://shikimori.one/";
        return this;
    }

    public Link animes(int offset) {
        url = "series";
        if (!animes) {
            addField("fields", "id,myAnimeListId,isActive,isAiring,myAnimeListScore,year,type,typeTitle,titles,posterUrlSmall");
            addField("limit", 20);
            addField("isHentai", 0);
        }
        animes = true;
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

    public Link addField(String key, String value) {
        if (value != null)
            fields.add(new Field(key, value));
        return this;
    }

    public Link addField(String key, int value) {
        fields.add(new Field(key, String.valueOf(value)));
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
