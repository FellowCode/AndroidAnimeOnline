package com.fellowcode.animewatcher.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rate implements Serializable {

    public int id;
    public int score;
    public String status;
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

    public static ArrayList<String> getStatusTitles(){

        ArrayList<String> statusTitles = new ArrayList<>();
        statusTitles.add("Добавить в список");
        statusTitles.add("Запланировано");
        statusTitles.add("Смотрю");
        statusTitles.add("Пересматриваю");
        statusTitles.add("Просмотрено");
        statusTitles.add("Отложено");
        statusTitles.add("Брошено");
        statusTitles.add("Удалить из спика");
        return statusTitles;
    }

    public static ArrayList<String> getStatuses(){
        ArrayList<String> statuses = new ArrayList<>();
        statuses.add("add");
        statuses.add("planned");
        statuses.add("watching");
        statuses.add("rewatching");
        statuses.add("completed");
        statuses.add("on_hold");
        statuses.add("dropped");
        statuses.add("delete");
        return statuses;
    }

    public static int findStatus(String status){
        ArrayList<String> statuses = getStatuses();
        for (int i=0;i<statuses.size(); i++){
            if (statuses.get(i).equals(status))
                return i;
        }
        return -1;
    }
}
