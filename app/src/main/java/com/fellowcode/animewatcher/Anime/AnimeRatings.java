package com.fellowcode.animewatcher.Anime;

import java.util.HashMap;
import java.util.Map;

public class AnimeRatings {

    static public String getTitle(String rating){
        Map<String, String> ratings = new HashMap<>();
        ratings.put("r", "R-17");
        ratings.put("r_plus", "R+");
        ratings.put("pg_13", "PG-13");
        ratings.put("pg", "PG");
        return ratings.get(rating);
    }

}
