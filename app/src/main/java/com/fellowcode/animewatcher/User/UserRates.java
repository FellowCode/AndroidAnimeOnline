package com.fellowcode.animewatcher.User;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.Serialize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserRates {

    public interface Response {
        void onResponse();
    }

    public ArrayList<Rate> rates = new ArrayList<>();


    public UserRates(Context context) {
        load(context);
    }

    public UserRates(JSONArray rates) throws JSONException {
        Parse(rates);
    }

    void Parse(JSONArray rates) throws JSONException {
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
}
