package com.fellowcode.animewatcher.User;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.Serialize;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class UserRates {

    public interface Response {
        void onResponse();
    }



    public ArrayList<Rate> rates;

    public ArrayList<Integer> plannedIds, watchingIds, completedIds, droppedIds, onHoldIds;


    public UserRates(Context context) {
        load(context);
    }

    public UserRates(JSONArray rates) throws JSONException {
        Parse(rates);
    }

    void Parse(JSONArray rates) throws JSONException {
        this.rates = new ArrayList<>();
        for (int i = 0; i < rates.length(); i++)
            this.rates.add(new Rate(rates.getJSONObject(i)));
    }

    void load(Context context) {
        ArrayList<Rate> rates = Serialize.read(context, "userRates");
        if (rates != null)
            this.rates = rates;

    }

    public void save(Context context) {
        Serialize.write(context, "userRates", rates);
    }

    public static int getIcon(@NonNull String status) {
        if (status.equals("planned"))
            return R.drawable.planned;
        if (status.equals("watching"))
            return R.drawable.watching;
        if (status.equals("rewatching"))
            return R.drawable.watching;
        if (status.equals("completed"))
            return R.drawable.completed;
        if (status.equals("on_hold"))
            return R.drawable.on_hold;
        if (status.equals("dropped"))
            return R.drawable.dropped;
        return 0;
    }

    public void sort(){
        plannedIds = new ArrayList<>();
        watchingIds = new ArrayList<>();
        completedIds = new ArrayList<>();
        droppedIds = new ArrayList<>();
        onHoldIds = new ArrayList<>();
        for (int i=0; i<rates.size(); i++){
            if (rates.get(i).status.equals("planned")){
                plannedIds.add(rates.get(i).animeId);
            } else if (rates.get(i).status.equals("watching")
                    || rates.get(i).status.equals("rewatching")){
                watchingIds.add(rates.get(i).animeId);
            } else if (rates.get(i).status.equals("completed")){
                completedIds.add(rates.get(i).animeId);
            } else if (rates.get(i).status.equals("dropped")){
                droppedIds.add(rates.get(i).animeId);
            } else if (rates.get(i).status.equals("on_hold")){
                onHoldIds.add(rates.get(i).animeId);
            }
        }
    }
}
