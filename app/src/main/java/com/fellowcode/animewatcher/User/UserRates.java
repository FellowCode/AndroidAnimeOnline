package com.fellowcode.animewatcher.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserRates {

    ArrayList<Rate> rates = new ArrayList<>();

    public UserRates(JSONArray rates) throws JSONException{
        Parse(rates);
    }

    void Parse(JSONArray rates) throws JSONException{
        for (int i=0;i<rates.length();i++)
            this.rates.add(new Rate(rates.getJSONObject(i)));
    }
}
